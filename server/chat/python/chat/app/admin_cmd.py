import argparse
import json
import logging
import os
import sys
import tempfile

from twisted.internet import defer, task

import chat
from chat.app import _base
from chat.config._base import ConfigError
from chat.config.homeserver import HomeServerConfig
from chat.config.logger import setup_logging
from chat.handlers.admin import ExfiltrationWriter
from chat.replication.slave.storage._base import BaseSlavedStore
from chat.replication.slave.storage.account_data import SlavedAccountDataStore
from chat.replication.slave.storage.appservice import SlavedApplicationServiceStore
from chat.replication.slave.storage.client_ips import SlavedClientIpStore
from chat.replication.slave.storage.deviceinbox import SlavedDeviceInboxStore
from chat.replication.slave.storage.devices import SlavedDeviceStore
from chat.replication.slave.storage.events import SlavedEventStore
from chat.replication.slave.storage.filtering import SlavedFilteringStore
from chat.replication.slave.storage.groups import SlavedGroupServerStore
from chat.replication.slave.storage.push_rule import SlavedPushRuleStore
from chat.replication.slave.storage.receipts import SlavedReceiptsStore
from chat.replication.slave.storage.registration import SlavedRegistrationStore
from chat.server import HomeServer
from chat.util.logcontext import LoggingContext
from chat.util.versionstring import get_version_string

logger = logging.getLogger("chat.app.admin_cmd")


class AdminCmdSlavedStore(
    SlavedReceiptsStore,
    SlavedAccountDataStore,
    SlavedApplicationServiceStore,
    SlavedRegistrationStore,
    SlavedFilteringStore,
    SlavedGroupServerStore,
    SlavedDeviceInboxStore,
    SlavedDeviceStore,
    SlavedPushRuleStore,
    SlavedEventStore,
    SlavedClientIpStore,
    BaseSlavedStore,
):
    pass


class AdminCmdServer(HomeServer):
    DATASTORE_CLASS = AdminCmdSlavedStore


async def export_data_command(hs, args):
    """Export data for a user.

    Args:
        hs (HomeServer)
        args (argparse.Namespace)
    """

    user_id = args.user_id
    directory = args.output_directory

    res = await hs.get_admin_handler().export_user_data(
        user_id, FileExfiltrationWriter(user_id, directory=directory)
    )
    print(res)


class FileExfiltrationWriter(ExfiltrationWriter):
    """An ExfiltrationWriter that writes the users data to a directory.
    Returns the directory location on completion.

    Note: This writes to disk on the main reactor thread.

    Args:
        user_id (str): The user whose data is being exfiltrated.
        directory (str|None): The directory to write the data to, if None then
            will write to a temporary directory.
    """

    def __init__(self, user_id, directory=None):
        self.user_id = user_id

        if directory:
            self.base_directory = directory
        else:
            self.base_directory = tempfile.mkdtemp(
                prefix="chat-exfiltrate__%s__" % (user_id,)
            )

        os.makedirs(self.base_directory, exist_ok=True)
        if list(os.listdir(self.base_directory)):
            raise Exception("Directory must be empty")

    def write_events(self, room_id, events):
        room_directory = os.path.join(self.base_directory, "rooms", room_id)
        os.makedirs(room_directory, exist_ok=True)
        events_file = os.path.join(room_directory, "events")

        with open(events_file, "a") as f:
            for event in events:
                print(json.dumps(event.get_pdu_json()), file=f)

    def write_state(self, room_id, event_id, state):
        room_directory = os.path.join(self.base_directory, "rooms", room_id)
        state_directory = os.path.join(room_directory, "state")
        os.makedirs(state_directory, exist_ok=True)

        event_file = os.path.join(state_directory, event_id)

        with open(event_file, "a") as f:
            for event in state.values():
                print(json.dumps(event.get_pdu_json()), file=f)

    def write_invite(self, room_id, event, state):
        self.write_events(room_id, [event])

        # We write the invite state somewhere else as they aren't full events
        # and are only a subset of the state at the event.
        room_directory = os.path.join(self.base_directory, "rooms", room_id)
        os.makedirs(room_directory, exist_ok=True)

        invite_state = os.path.join(room_directory, "invite_state")

        with open(invite_state, "a") as f:
            for event in state.values():
                print(json.dumps(event), file=f)

    def finished(self):
        return self.base_directory


def start(config_options):
    parser = argparse.ArgumentParser(description="Chat Admin Command")
    HomeServerConfig.add_arguments_to_parser(parser)

    subparser = parser.add_subparsers(
        title="Admin Commands",
        required=True,
        dest="command",
        metavar="<admin_command>",
        help="The admin command to perform.",
    )
    export_data_parser = subparser.add_parser(
        "export-data", help="Export all data for a user"
    )
    export_data_parser.add_argument("user_id", help="User to extra data from")
    export_data_parser.add_argument(
        "--output-directory",
        action="store",
        metavar="DIRECTORY",
        required=False,
        help="The directory to store the exported data in. Must be empty. Defaults"
        " to creating a temp directory.",
    )
    export_data_parser.set_defaults(func=export_data_command)

    try:
        config, args = HomeServerConfig.load_config_with_parser(parser, config_options)
    except ConfigError as e:
        sys.stderr.write("\n" + str(e) + "\n")
        sys.exit(1)

    if config.worker_app is not None:
        assert config.worker_app == "chat.app.admin_cmd"

    # Update the config with some basic overrides so that don't have to specify
    # a full worker config.
    config.worker_app = "chat.app.admin_cmd"

    if (
        not config.worker_daemonize
        and not config.worker_log_file
        and not config.worker_log_config
    ):
        # Since we're meant to be run as a "command" let's not redirect stdio
        # unless we've actually set log config.
        config.no_redirect_stdio = True

    # Explicitly disable background processes
    config.update_user_directory = False
    config.run_background_tasks = False
    config.start_pushers = False
    config.pusher_shard_config.instances = []
    config.send_federation = False
    config.federation_shard_config.instances = []

    chat.events.USE_FROZEN_DICTS = config.use_frozen_dicts

    ss = AdminCmdServer(
        config.server_name,
        config=config,
        version_string="Chat/" + get_version_string(chat),
    )

    setup_logging(ss, config, use_worker_options=True)

    ss.setup()

    # We use task.react as the basic run command as it correctly handles tearing
    # down the reactor when the deferreds resolve and setting the return value.
    # We also make sure that `_base.start` gets run before we actually run the
    # command.

    async def run():
        with LoggingContext("command"):
            _base.start(ss)
            await args.func(ss, args)

    _base.start_worker_reactor(
        "chat-admin-cmd",
        config,
        run_command=lambda: task.react(lambda _reactor: defer.ensureDeferred(run())),
    )


if __name__ == "__main__":
    with LoggingContext("main"):
        start(sys.argv[1:])
