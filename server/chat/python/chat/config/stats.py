import logging

from ._base import Config

ROOM_STATS_DISABLED_WARN = """\
WARNING: room/user statistics have been disabled via the stats.enabled
configuration setting. This means that certain features (such as the room
directory) will not operate correctly. Future versions of Chat server may ignore
this setting.

To fix this warning, remove the stats.enabled setting from your configuration
file.
--------------------------------------------------------------------------------"""

logger = logging.getLogger(__name__)


class StatsConfig(Config):
    """Stats Configuration
    Configuration for the behaviour of Chat server's stats engine
    """

    section = "stats"

    def read_config(self, config, **kwargs):
        self.stats_enabled = True
        stats_config = config.get("stats", None)
        if stats_config:
            self.stats_enabled = stats_config.get("enabled", self.stats_enabled)
        if not self.stats_enabled:
            logger.warning(ROOM_STATS_DISABLED_WARN)

    def generate_config_section(self, config_dir_path, server_name, **kwargs):
        ### MARK -TODO
        return """\
        # Settings for local room and user statistics collection. See
        # https://chat.docs.dingshunyu.top/room_and_user_statistics.html.
        #
        stats:
          # Uncomment the following to disable room and user statistics. Note that doing
          # so may cause certain features (such as the room directory) not to work
          # correctly.
          #
          #enabled: false
        """
