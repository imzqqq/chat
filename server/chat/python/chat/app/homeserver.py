### MARK
""" Chat server start at here! Hello World~
"""
###

import logging
import os
import sys
from typing import Iterator

from twisted.internet import reactor
from twisted.web.resource import EncodingResourceWrapper, IResource
from twisted.web.server import GzipEncoderFactory
from twisted.web.static import File

import chat
import chat.config.logger
from chat import events
from chat.api.urls import (
    FEDERATION_PREFIX,
    LEGACY_MEDIA_PREFIX,
    MEDIA_PREFIX,
    SERVER_KEY_V2_PREFIX,
    STATIC_PREFIX,
    WEB_CLIENT_PREFIX,
)
from chat.app import _base
from chat.app._base import (
    handle_startup_exception,
    listen_ssl,
    listen_tcp,
    max_request_body_size,
    redirect_stdio_to_logs,
    register_start,
)
from chat.config._base import ConfigError
from chat.config.emailconfig import ThreepidBehaviour
from chat.config.homeserver import HomeServerConfig
from chat.config.server import ListenerConfig
from chat.federation.transport.server import TransportLayerServer
from chat.http.additional_resource import AdditionalResource
from chat.http.server import (
    OptionsResource,
    RootOptionsRedirectResource,
    RootRedirect,
    StaticResource,
)
from chat.http.site import SynapseSite
from chat.logging.context import LoggingContext
from chat.metrics import METRICS_PREFIX, MetricsResource, RegistryProxy
from chat.python_dependencies import check_requirements
from chat.replication.http import REPLICATION_PREFIX, ReplicationRestResource
from chat.replication.tcp.resource import ReplicationStreamProtocolFactory
from chat.rest import ClientRestResource
from chat.rest.admin import AdminRestResource
from chat.rest.health import HealthResource
from chat.rest.key.v2 import KeyApiV2Resource
from chat.rest.chat.client import build_synapse_client_resource_tree
from chat.rest.well_known import WellKnownResource
from chat.server import HomeServer
from chat.storage import DataStore
from chat.util.httpresourcetree import create_resource_tree
from chat.util.module_loader import load_module
from chat.util.versionstring import get_version_string

logger = logging.getLogger("chat.app.homeserver")


def gz_wrap(r):
    return EncodingResourceWrapper(r, [GzipEncoderFactory()])


class SynapseHomeServer(HomeServer):
    DATASTORE_CLASS = DataStore

    def _listener_http(self, config: HomeServerConfig, listener_config: ListenerConfig):
        port = listener_config.port
        bind_addresses = listener_config.bind_addresses
        tls = listener_config.tls
        site_tag = listener_config.http_options.tag
        if site_tag is None:
            site_tag = str(port)

        # We always include a health resource.
        resources = {"/health": HealthResource()}

        for res in listener_config.http_options.resources:
            for name in res.names:
                if name == "openid" and "federation" in res.names:
                    # Skip loading openid resource if federation is defined
                    # since federation resource will include openid
                    continue
                resources.update(self._configure_named_resource(name, res.compress))

        additional_resources = listener_config.http_options.additional_resources
        logger.debug("Configuring additional resources: %r", additional_resources)
        module_api = self.get_module_api()
        for path, resmodule in additional_resources.items():
            handler_cls, config = load_module(
                resmodule,
                ("listeners", site_tag, "additional_resources", "<%s>" % (path,)),
            )
            handler = handler_cls(config, module_api)
            if IResource.providedBy(handler):
                resource = handler
            elif hasattr(handler, "handle_request"):
                resource = AdditionalResource(self, handler.handle_request)
            else:
                raise ConfigError(
                    "additional_resource %s does not implement a known interface"
                    % (resmodule["module"],)
                )
            resources[path] = resource

        # Attach additional resources registered by modules.
        resources.update(self._module_web_resources)
        self._module_web_resources_consumed = True

        # try to find something useful to redirect '/' to
        if WEB_CLIENT_PREFIX in resources:
            root_resource = RootOptionsRedirectResource(WEB_CLIENT_PREFIX)
        elif STATIC_PREFIX in resources:
            root_resource = RootOptionsRedirectResource(STATIC_PREFIX)
        else:
            root_resource = OptionsResource()

        site = SynapseSite(
            "chat.access.%s.%s" % ("https" if tls else "http", site_tag),
            site_tag,
            listener_config,
            create_resource_tree(resources, root_resource),
            self.version_string,
            max_request_body_size=max_request_body_size(self.config),
            reactor=self.get_reactor(),
        )

        if tls:
            ports = listen_ssl(
                bind_addresses,
                port,
                site,
                self.tls_server_context_factory,
                reactor=self.get_reactor(),
            )
            logger.info("Chat server now listening on TCP port %d (TLS)", port)

        else:
            ports = listen_tcp(
                bind_addresses,
                port,
                site,
                reactor=self.get_reactor(),
            )
            logger.info("Chat server now listening on TCP port %d", port)

        return ports

    def _configure_named_resource(self, name, compress=False):
        """Build a resource map for a named resource

        Args:
            name (str): named resource: one of "client", "federation", etc
            compress (bool): whether to enable gzip compression for this
                resource

        Returns:
            dict[str, Resource]: map from path to HTTP resource
        """
        resources = {}
        if name == "client":
            client_resource = ClientRestResource(self)
            if compress:
                client_resource = gz_wrap(client_resource)

            ### MARK
            resources.update(
                {
                    "/chat/client/api/v1": client_resource,
                    "/chat/client/r0": client_resource,
                    "/chat/client/unstable": client_resource,
                    "/chat/client/v2_alpha": client_resource,
                    "/chat/client/versions": client_resource,
                    "/.well-known/matrix/client": WellKnownResource(self),
                    "/_chat/admin": AdminRestResource(self),
                    **build_synapse_client_resource_tree(self),
                }
            )
            ###

            if self.config.threepid_behaviour_email == ThreepidBehaviour.LOCAL:
                from chat.rest.chat.client.password_reset import (
                    PasswordResetSubmitTokenResource,
                )

                ### MARK
                resources[
                    "/_chat/client/password_reset/email/submit_token"
                ] = PasswordResetSubmitTokenResource(self)
                ###

        if name == "consent":
            from chat.rest.consent.consent_resource import ConsentResource

            consent_resource = ConsentResource(self)
            if compress:
                consent_resource = gz_wrap(consent_resource)
            ### MARK
            resources.update({"/chat/consent": consent_resource})
            ###

        if name == "federation":
            resources.update({FEDERATION_PREFIX: TransportLayerServer(self)})

        if name == "openid":
            resources.update(
                {
                    FEDERATION_PREFIX: TransportLayerServer(
                        self, servlet_groups=["openid"]
                    )
                }
            )

        if name in ["static", "client"]:
            resources.update(
                {
                    STATIC_PREFIX: StaticResource(
                        os.path.join(os.path.dirname(chat.__file__), "static")
                    )
                }
            )

        if name in ["media", "federation", "client"]:
            if self.config.enable_media_repo:
                media_repo = self.get_media_repository_resource()
                resources.update(
                    {MEDIA_PREFIX: media_repo, LEGACY_MEDIA_PREFIX: media_repo}
                )
            elif name == "media":
                raise ConfigError(
                    "'media' resource conflicts with enable_media_repo=False"
                )

        if name in ["keys", "federation"]:
            resources[SERVER_KEY_V2_PREFIX] = KeyApiV2Resource(self)

        if name == "webclient":
            webclient_loc = self.config.web_client_location

            if webclient_loc is None:
                logger.warning(
                    "Not enabling webclient resource, as web_client_location is unset."
                )
            elif webclient_loc.startswith("http://") or webclient_loc.startswith(
                "https://"
            ):
                resources[WEB_CLIENT_PREFIX] = RootRedirect(webclient_loc)
            else:
                logger.warning(
                    "Running webclient on the same domain is not recommended: "
                    "https://github.com/matrix-org/chat#security-note - "
                    "after you move webclient to different host you can set "
                    "web_client_location to its full URL to enable redirection."
                )
                # GZip is disabled here due to
                # https://twistedmatrix.com/trac/ticket/7678
                resources[WEB_CLIENT_PREFIX] = File(webclient_loc)

        if name == "metrics" and self.config.enable_metrics:
            resources[METRICS_PREFIX] = MetricsResource(RegistryProxy)

        if name == "replication":
            resources[REPLICATION_PREFIX] = ReplicationRestResource(self)

        return resources

    def start_listening(self):
        if self.config.redis_enabled:
            # If redis is enabled we connect via the replication command handler
            # in the same way as the workers (since we're effectively a client
            # rather than a server).
            self.get_tcp_replication().start_replication(self)

        for listener in self.config.server.listeners:
            if listener.type == "http":
                self._listening_services.extend(
                    self._listener_http(self.config, listener)
                )
            elif listener.type == "manhole":
                _base.listen_manhole(
                    listener.bind_addresses, listener.port, manhole_globals={"hs": self}
                )
            elif listener.type == "replication":
                services = listen_tcp(
                    listener.bind_addresses,
                    listener.port,
                    ReplicationStreamProtocolFactory(self),
                )
                for s in services:
                    reactor.addSystemEventTrigger("before", "shutdown", s.stopListening)
            elif listener.type == "metrics":
                if not self.config.enable_metrics:
                    logger.warning(
                        "Metrics listener configured, but "
                        "enable_metrics is not True!"
                    )
                else:
                    _base.listen_metrics(listener.bind_addresses, listener.port)
            else:
                # this shouldn't happen, as the listener type should have been checked
                # during parsing
                logger.warning("Unrecognized listener type: %s", listener.type)


def setup(config_options):
    """
    Args:
        config_options_options: The options passed to Chat server. Usually
            `sys.argv[1:]`.

    Returns:
        HomeServer
    """
    try:
        ### MARK - TODO:
        config = HomeServerConfig.load_or_generate_config(
            "Chat Homeserver", config_options
        )
        ###
    except ConfigError as e:
        sys.stderr.write("\n")
        for f in format_config_error(e):
            sys.stderr.write(f)
        sys.stderr.write("\n")
        sys.exit(1)

    if not config:
        # If a config isn't returned, and an exception isn't raised, we're just
        # generating config files and shouldn't try to continue.
        sys.exit(0)

    events.USE_FROZEN_DICTS = config.use_frozen_dicts
    chat.util.caches.TRACK_MEMORY_USAGE = config.caches.track_memory_usage

    if config.server.gc_seconds:
        chat.metrics.MIN_TIME_BETWEEN_GCS = config.server.gc_seconds

    hs = SynapseHomeServer(
        config.server_name,
        config=config,
        ### MARK - TODO
        version_string="Chat/" + get_version_string(chat),
        ###
    )

    chat.config.logger.setup_logging(hs, config, use_worker_options=False)

    logger.info("Setting up server")

    try:
        hs.setup()
    except Exception as e:
        handle_startup_exception(e)

    async def start():
        # Load the OIDC provider metadatas, if OIDC is enabled.
        if hs.config.oidc_enabled:
            oidc = hs.get_oidc_handler()
            # Loading the provider metadata also ensures the provider config is valid.
            await oidc.load_metadata()

        await _base.start(hs)

        hs.get_datastore().db_pool.updates.start_doing_background_updates()

    register_start(start)

    return hs


def format_config_error(e: ConfigError) -> Iterator[str]:
    """
    Formats a config error neatly

    The idea is to format the immediate error, plus the "causes" of those errors,
    hopefully in a way that makes sense to the user. For example:

        Error in configuration at 'oidc_config.user_mapping_provider.config.display_name_template':
          Failed to parse config for module 'JinjaOidcMappingProvider':
            invalid jinja template:
              unexpected end of template, expected 'end of print statement'.

    Args:
        e: the error to be formatted

    Returns: An iterator which yields string fragments to be formatted
    """
    yield "Error in configuration"

    if e.path:
        yield " at '%s'" % (".".join(e.path),)

    yield ":\n  %s" % (e.msg,)

    e = e.__cause__
    indent = 1
    while e:
        indent += 1
        yield ":\n%s%s" % ("  " * indent, str(e))
        e = e.__cause__


def run(hs):
    PROFILE_SYNAPSE = False
    if PROFILE_SYNAPSE:

        def profile(func):
            from cProfile import Profile
            from threading import current_thread

            def profiled(*args, **kargs):
                profile = Profile()
                profile.enable()
                func(*args, **kargs)
                profile.disable()
                ident = current_thread().ident
                profile.dump_stats(
                    "/tmp/%s.%s.%i.pstat" % (hs.hostname, func.__name__, ident)
                )

            return profiled

        from twisted.python.threadpool import ThreadPool

        ThreadPool._worker = profile(ThreadPool._worker)
        reactor.run = profile(reactor.run)

    _base.start_reactor(
        "chat-homeserver",
        soft_file_limit=hs.config.soft_file_limit,
        gc_thresholds=hs.config.gc_thresholds,
        pid_file=hs.config.pid_file,
        daemonize=hs.config.daemonize,
        print_pidfile=hs.config.print_pidfile,
        logger=logger,
    )


def main():
    with LoggingContext("main"):
        # check base requirements
        check_requirements()
        hs = setup(sys.argv[1:])

        # redirect stdio to the logs, if configured.
        if not hs.config.no_redirect_stdio:
            redirect_stdio_to_logs()

        run(hs)


if __name__ == "__main__":
    main()
