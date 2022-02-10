import attr

from chat.python_dependencies import DependencyException, check_requirements

from ._base import Config, ConfigError


@attr.s
class MetricsFlags:
    known_servers = attr.ib(default=False, validator=attr.validators.instance_of(bool))

    @classmethod
    def all_off(cls):
        """
        Instantiate the flags with all options set to off.
        """
        return cls(**{x.name: False for x in attr.fields(cls)})


class MetricsConfig(Config):
    section = "metrics"

    def read_config(self, config, **kwargs):
        self.enable_metrics = config.get("enable_metrics", False)
        self.report_stats = config.get("report_stats", None)
        self.report_stats_endpoint = config.get(
            "report_stats_endpoint", "https://matrix.org/report-usage-stats/push"
        )
        self.metrics_port = config.get("metrics_port")
        self.metrics_bind_host = config.get("metrics_bind_host", "127.0.0.1")

        if self.enable_metrics:
            _metrics_config = config.get("metrics_flags") or {}
            self.metrics_flags = MetricsFlags(**_metrics_config)
        else:
            self.metrics_flags = MetricsFlags.all_off()

        self.sentry_enabled = "sentry" in config
        if self.sentry_enabled:
            try:
                check_requirements("sentry")
            except DependencyException as e:
                raise ConfigError(
                    e.message  # noqa: B306, DependencyException.message is a property
                )

            self.sentry_dsn = config["sentry"].get("dsn")
            if not self.sentry_dsn:
                raise ConfigError(
                    "sentry.dsn field is required when sentry integration is enabled"
                )

    def generate_config_section(self, report_stats=None, **kwargs):
        res = """\
        ## Metrics ###

        # Enable collection and rendering of performance metrics
        #
        #enable_metrics: false

        # Enable sentry integration
        # NOTE: While attempts are made to ensure that the logs don't contain
        # any sensitive information, this cannot be guaranteed. By enabling
        # this option the sentry server may therefore receive sensitive
        # information, and it in turn may then diseminate sensitive information
        # through insecure notification channels if so configured.
        #
        #sentry:
        #    dsn: "..."

        # Flags to enable Prometheus metrics which are not suitable to be
        # enabled by default, either for performance reasons or limited use.
        #
        metrics_flags:
            # Publish synapse_federation_known_servers, a gauge of the number of
            # servers this homeserver knows about, including itself. May cause
            # performance problems on large homeservers.
            #
            #known_servers: true

        # Whether or not to report anonymized homeserver usage statistics.
        #
        """

        if report_stats is None:
            res += "#report_stats: true|false\n"
        else:
            res += "report_stats: %s\n" % ("true" if report_stats else "false")

        res += """
        # The endpoint to report the anonymized homeserver usage statistics to.
        # Defaults to https://matrix.org/report-usage-stats/push
        #
        #report_stats_endpoint: https://example.com/report-usage-stats/push
        """
        return res
