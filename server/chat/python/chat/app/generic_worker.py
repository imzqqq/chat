import logging
import sys
from typing import Dict, Optional

from twisted.internet import address
from twisted.web.resource import IResource
from twisted.web.server import Request

import chat
import chat.events
from chat.api.errors import HttpResponseException, RequestSendFailed, SynapseError
from chat.api.urls import (
    CLIENT_API_PREFIX,
    FEDERATION_PREFIX,
    LEGACY_MEDIA_PREFIX,
    MEDIA_PREFIX,
    SERVER_KEY_V2_PREFIX,
)
from chat.app import _base
from chat.app._base import (
    handle_startup_exception,
    max_request_body_size,
    redirect_stdio_to_logs,
    register_start,
)
from chat.config._base import ConfigError
from chat.config.homeserver import HomeServerConfig
from chat.config.logger import setup_logging
from chat.config.server import ListenerConfig
from chat.federation.transport.server import TransportLayerServer
from chat.http.server import JsonResource, OptionsResource
from chat.http.servlet import RestServlet, parse_json_object_from_request
from chat.http.site import SynapseSite
from chat.logging.context import LoggingContext
from chat.metrics import METRICS_PREFIX, MetricsResource, RegistryProxy
from chat.replication.http import REPLICATION_PREFIX, ReplicationRestResource
from chat.replication.slave.storage._base import BaseSlavedStore
from chat.replication.slave.storage.account_data import SlavedAccountDataStore
from chat.replication.slave.storage.appservice import SlavedApplicationServiceStore
from chat.replication.slave.storage.client_ips import SlavedClientIpStore
from chat.replication.slave.storage.deviceinbox import SlavedDeviceInboxStore
from chat.replication.slave.storage.devices import SlavedDeviceStore
from chat.replication.slave.storage.directory import DirectoryStore
from chat.replication.slave.storage.events import SlavedEventStore
from chat.replication.slave.storage.filtering import SlavedFilteringStore
from chat.replication.slave.storage.groups import SlavedGroupServerStore
from chat.replication.slave.storage.keys import SlavedKeyStore
from chat.replication.slave.storage.profile import SlavedProfileStore
from chat.replication.slave.storage.push_rule import SlavedPushRuleStore
from chat.replication.slave.storage.pushers import SlavedPusherStore
from chat.replication.slave.storage.receipts import SlavedReceiptsStore
from chat.replication.slave.storage.registration import SlavedRegistrationStore
from chat.rest.admin import register_servlets_for_media_repo
from chat.rest.client import (
    account_data,
    events,
    groups,
    login,
    presence,
    read_marker,
    receipts,
    room,
    room_keys,
    sync,
    tags,
    user_directory,
)
from chat.rest.client._base import client_patterns
from chat.rest.client.account import ThreepidRestServlet
from chat.rest.client.account_data import AccountDataServlet, RoomAccountDataServlet
from chat.rest.client.devices import DevicesRestServlet
from chat.rest.client.initial_sync import InitialSyncRestServlet
from chat.rest.client.keys import (
    KeyChangesServlet,
    KeyQueryServlet,
    OneTimeKeyServlet,
)
from chat.rest.client.profile import (
    ProfileAvatarURLRestServlet,
    ProfileDisplaynameRestServlet,
    ProfileRestServlet,
)
from chat.rest.client.push_rule import PushRuleRestServlet
from chat.rest.client.register import RegisterRestServlet
from chat.rest.client.sendtodevice import SendToDeviceRestServlet
from chat.rest.client.versions import VersionsRestServlet
from chat.rest.client.voip import VoipRestServlet
from chat.rest.health import HealthResource
from chat.rest.key.v2 import KeyApiV2Resource
from chat.rest.chat.client import build_synapse_client_resource_tree
from chat.server import HomeServer
from chat.storage.databases.main.censor_events import CensorEventsStore
from chat.storage.databases.main.client_ips import ClientIpWorkerStore
from chat.storage.databases.main.e2e_room_keys import EndToEndRoomKeyStore
from chat.storage.databases.main.lock import LockStore
from chat.storage.databases.main.media_repository import MediaRepositoryStore
from chat.storage.databases.main.metrics import ServerMetricsStore
from chat.storage.databases.main.monthly_active_users import (
    MonthlyActiveUsersWorkerStore,
)
from chat.storage.databases.main.presence import PresenceStore
from chat.storage.databases.main.room import RoomWorkerStore
from chat.storage.databases.main.search import SearchStore
from chat.storage.databases.main.stats import StatsStore
from chat.storage.databases.main.transactions import TransactionWorkerStore
from chat.storage.databases.main.ui_auth import UIAuthWorkerStore
from chat.storage.databases.main.user_directory import UserDirectoryStore
from chat.util.httpresourcetree import create_resource_tree
from chat.util.versionstring import get_version_string

logger = logging.getLogger("chat.app.generic_worker")


class KeyUploadServlet(RestServlet):
    """An implementation of the `KeyUploadServlet` that responds to read only
    requests, but otherwise proxies through to the master instance.
    """

    PATTERNS = client_patterns("/keys/upload(/(?P<device_id>[^/]+))?$")

    def __init__(self, hs):
        """
        Args:
            hs (chat.server.HomeServer): server
        """
        super().__init__()
        self.auth = hs.get_auth()
        self.store = hs.get_datastore()
        self.http_client = hs.get_simple_http_client()
        self.main_uri = hs.config.worker_main_http_uri

    async def on_POST(self, request: Request, device_id: Optional[str]):
        requester = await self.auth.get_user_by_req(request, allow_guest=True)
        user_id = requester.user.to_string()
        body = parse_json_object_from_request(request)

        if device_id is not None:
            # passing the device_id here is deprecated; however, we allow it
            # for now for compatibility with older clients.
            if requester.device_id is not None and device_id != requester.device_id:
                logger.warning(
                    "Client uploading keys for a different device "
                    "(logged in as %s, uploading for %s)",
                    requester.device_id,
                    device_id,
                )
        else:
            device_id = requester.device_id

        if device_id is None:
            raise SynapseError(
                400, "To upload keys, you must pass device_id when authenticating"
            )

        if body:
            # They're actually trying to upload something, proxy to main chat.

            # Proxy headers from the original request, such as the auth headers
            # (in case the access token is there) and the original IP /
            # User-Agent of the request.
            headers = {
                header: request.requestHeaders.getRawHeaders(header, [])
                for header in (b"Authorization", b"User-Agent")
            }
            # Add the previous hop to the X-Forwarded-For header.
            x_forwarded_for = request.requestHeaders.getRawHeaders(
                b"X-Forwarded-For", []
            )
            # we use request.client here, since we want the previous hop, not the
            # original client (as returned by request.getClientAddress()).
            if isinstance(request.client, (address.IPv4Address, address.IPv6Address)):
                previous_host = request.client.host.encode("ascii")
                # If the header exists, add to the comma-separated list of the first
                # instance of the header. Otherwise, generate a new header.
                if x_forwarded_for:
                    x_forwarded_for = [
                        x_forwarded_for[0] + b", " + previous_host
                    ] + x_forwarded_for[1:]
                else:
                    x_forwarded_for = [previous_host]
            headers[b"X-Forwarded-For"] = x_forwarded_for

            # Replicate the original X-Forwarded-Proto header. Note that
            # XForwardedForRequest overrides isSecure() to give us the original protocol
            # used by the client, as opposed to the protocol used by our upstream proxy
            # - which is what we want here.
            headers[b"X-Forwarded-Proto"] = [
                b"https" if request.isSecure() else b"http"
            ]

            try:
                result = await self.http_client.post_json_get_json(
                    self.main_uri + request.uri.decode("ascii"), body, headers=headers
                )
            except HttpResponseException as e:
                raise e.to_synapse_error() from e
            except RequestSendFailed as e:
                raise SynapseError(502, "Failed to talk to master") from e

            return 200, result
        else:
            # Just interested in counts.
            result = await self.store.count_e2e_one_time_keys(user_id, device_id)
            return 200, {"one_time_key_counts": result}


class GenericWorkerSlavedStore(
    # FIXME(#3714): We need to add UserDirectoryStore as we write directly
    # rather than going via the correct worker.
    UserDirectoryStore,
    StatsStore,
    UIAuthWorkerStore,
    EndToEndRoomKeyStore,
    PresenceStore,
    SlavedDeviceInboxStore,
    SlavedDeviceStore,
    SlavedReceiptsStore,
    SlavedPushRuleStore,
    SlavedGroupServerStore,
    SlavedAccountDataStore,
    SlavedPusherStore,
    CensorEventsStore,
    ClientIpWorkerStore,
    SlavedEventStore,
    SlavedKeyStore,
    RoomWorkerStore,
    DirectoryStore,
    SlavedApplicationServiceStore,
    SlavedRegistrationStore,
    SlavedProfileStore,
    SlavedClientIpStore,
    SlavedFilteringStore,
    MonthlyActiveUsersWorkerStore,
    MediaRepositoryStore,
    ServerMetricsStore,
    SearchStore,
    TransactionWorkerStore,
    LockStore,
    BaseSlavedStore,
):
    pass


class GenericWorkerServer(HomeServer):
    DATASTORE_CLASS = GenericWorkerSlavedStore

    def _listen_http(self, listener_config: ListenerConfig):
        port = listener_config.port
        bind_addresses = listener_config.bind_addresses

        assert listener_config.http_options is not None

        site_tag = listener_config.http_options.tag
        if site_tag is None:
            site_tag = port

        # We always include a health resource.
        resources: Dict[str, IResource] = {"/health": HealthResource()}

        for res in listener_config.http_options.resources:
            for name in res.names:
                if name == "metrics":
                    resources[METRICS_PREFIX] = MetricsResource(RegistryProxy)
                elif name == "client":
                    resource = JsonResource(self, canonical_json=False)

                    RegisterRestServlet(self).register(resource)
                    login.register_servlets(self, resource)
                    ThreepidRestServlet(self).register(resource)
                    DevicesRestServlet(self).register(resource)
                    KeyQueryServlet(self).register(resource)
                    OneTimeKeyServlet(self).register(resource)
                    KeyChangesServlet(self).register(resource)
                    VoipRestServlet(self).register(resource)
                    PushRuleRestServlet(self).register(resource)
                    VersionsRestServlet(self).register(resource)

                    ProfileAvatarURLRestServlet(self).register(resource)
                    ProfileDisplaynameRestServlet(self).register(resource)
                    ProfileRestServlet(self).register(resource)
                    KeyUploadServlet(self).register(resource)
                    AccountDataServlet(self).register(resource)
                    RoomAccountDataServlet(self).register(resource)

                    sync.register_servlets(self, resource)
                    events.register_servlets(self, resource)
                    room.register_servlets(self, resource, True)
                    room.register_deprecated_servlets(self, resource)
                    InitialSyncRestServlet(self).register(resource)
                    room_keys.register_servlets(self, resource)
                    tags.register_servlets(self, resource)
                    account_data.register_servlets(self, resource)
                    receipts.register_servlets(self, resource)
                    read_marker.register_servlets(self, resource)

                    SendToDeviceRestServlet(self).register(resource)

                    user_directory.register_servlets(self, resource)

                    presence.register_servlets(self, resource)

                    groups.register_servlets(self, resource)

                    resources.update({CLIENT_API_PREFIX: resource})

                    resources.update(build_synapse_client_resource_tree(self))
                elif name == "federation":
                    resources.update({FEDERATION_PREFIX: TransportLayerServer(self)})
                elif name == "media":
                    if self.config.can_load_media_repo:
                        media_repo = self.get_media_repository_resource()

                        # We need to serve the admin servlets for media on the
                        # worker.
                        admin_resource = JsonResource(self, canonical_json=False)
                        register_servlets_for_media_repo(self, admin_resource)

                        resources.update(
                            {
                                MEDIA_PREFIX: media_repo,
                                LEGACY_MEDIA_PREFIX: media_repo,
                                "/_chat/admin": admin_resource,
                            }
                        )
                    else:
                        logger.warning(
                            "A 'media' listener is configured but the media"
                            " repository is disabled. Ignoring."
                        )

                if name == "openid" and "federation" not in res.names:
                    # Only load the openid resource separately if federation resource
                    # is not specified since federation resource includes openid
                    # resource.
                    resources.update(
                        {
                            FEDERATION_PREFIX: TransportLayerServer(
                                self, servlet_groups=["openid"]
                            )
                        }
                    )

                if name in ["keys", "federation"]:
                    resources[SERVER_KEY_V2_PREFIX] = KeyApiV2Resource(self)

                if name == "replication":
                    resources[REPLICATION_PREFIX] = ReplicationRestResource(self)

        # Attach additional resources registered by modules.
        resources.update(self._module_web_resources)
        self._module_web_resources_consumed = True

        root_resource = create_resource_tree(resources, OptionsResource())

        _base.listen_tcp(
            bind_addresses,
            port,
            SynapseSite(
                "chat.access.http.%s" % (site_tag,),
                site_tag,
                listener_config,
                root_resource,
                self.version_string,
                max_request_body_size=max_request_body_size(self.config),
                reactor=self.get_reactor(),
            ),
            reactor=self.get_reactor(),
        )

        ### MARK
        logger.info("Chat server worker now listening on port %d", port)
        ###

    def start_listening(self):
        for listener in self.config.worker_listeners:
            if listener.type == "http":
                self._listen_http(listener)
            elif listener.type == "manhole":
                _base.listen_manhole(
                    listener.bind_addresses, listener.port, manhole_globals={"hs": self}
                )
            elif listener.type == "metrics":
                if not self.config.enable_metrics:
                    logger.warning(
                        "Metrics listener configured, but "
                        "enable_metrics is not True!"
                    )
                else:
                    _base.listen_metrics(listener.bind_addresses, listener.port)
            else:
                logger.warning("Unsupported listener type: %s", listener.type)

        self.get_tcp_replication().start_replication(self)


def start(config_options):
    try:
        ### MARK - TODO:
        config = HomeServerConfig.load_config("Chat worker", config_options)
        ###
    except ConfigError as e:
        sys.stderr.write("\n" + str(e) + "\n")
        sys.exit(1)

    # For backwards compatibility let any of the old app names.
    assert config.worker_app in (
        "chat.app.appservice",
        "chat.app.client_reader",
        "chat.app.event_creator",
        "chat.app.federation_reader",
        "chat.app.federation_sender",
        "chat.app.frontend_proxy",
        "chat.app.generic_worker",
        "chat.app.media_repository",
        "chat.app.pusher",
        "chat.app.synchrotron",
        "chat.app.user_dir",
    )

    if config.worker_app == "chat.app.appservice":
        if config.appservice.notify_appservices:
            sys.stderr.write(
                "\nThe appservices must be disabled in the main chat process"
                "\nbefore they can be run in a separate worker."
                "\nPlease add ``notify_appservices: false`` to the main config"
                "\n"
            )
            sys.exit(1)

        # Force the appservice to start since they will be disabled in the main config
        config.appservice.notify_appservices = True
    else:
        # For other worker types we force this to off.
        config.appservice.notify_appservices = False

    if config.worker_app == "chat.app.user_dir":
        if config.server.update_user_directory:
            sys.stderr.write(
                "\nThe update_user_directory must be disabled in the main chat process"
                "\nbefore they can be run in a separate worker."
                "\nPlease add ``update_user_directory: false`` to the main config"
                "\n"
            )
            sys.exit(1)

        # Force the pushers to start since they will be disabled in the main config
        config.server.update_user_directory = True
    else:
        # For other worker types we force this to off.
        config.server.update_user_directory = False

    chat.events.USE_FROZEN_DICTS = config.use_frozen_dicts
    chat.util.caches.TRACK_MEMORY_USAGE = config.caches.track_memory_usage

    if config.server.gc_seconds:
        chat.metrics.MIN_TIME_BETWEEN_GCS = config.server.gc_seconds

    ### MARK - TODO:
    hs = GenericWorkerServer(
        config.server_name,
        config=config,
        version_string="Chat/" + get_version_string(chat),
    )
    ###

    setup_logging(hs, config, use_worker_options=True)

    try:
        hs.setup()

        # Ensure the replication streamer is always started in case we write to any
        # streams. Will no-op if no streams can be written to by this worker.
        hs.get_replication_streamer()
    except Exception as e:
        handle_startup_exception(e)

    register_start(_base.start, hs)

    # redirect stdio to the logs, if configured.
    if not hs.config.no_redirect_stdio:
        redirect_stdio_to_logs()

    _base.start_worker_reactor("chat-generic-worker", config)


if __name__ == "__main__":
    with LoggingContext("main"):
        start(sys.argv[1:])
