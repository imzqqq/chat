from chat.config._base import Config
from chat.types import JsonDict


class ExperimentalConfig(Config):
    """Config section for enabling experimental features"""

    section = "experimental"

    def read_config(self, config: JsonDict, **kwargs):
        experimental = config.get("experimental_features") or {}

        # MSC2858 (multiple SSO identity providers)
        self.msc2858_enabled: bool = experimental.get("msc2858_enabled", False)

        # MSC3026 (busy presence state)
        self.msc3026_enabled: bool = experimental.get("msc3026_enabled", False)

        # MSC2716 (backfill existing history)
        self.msc2716_enabled: bool = experimental.get("msc2716_enabled", False)

        # MSC2285 (hidden read receipts)
        self.msc2285_enabled: bool = experimental.get("msc2285_enabled", False)

        # MSC3244 (room version capabilities)
        self.msc3244_enabled: bool = experimental.get("msc3244_enabled", False)

        # MSC3283 (set displayname, avatar_url and change 3pid capabilities)
        self.msc3283_enabled: bool = experimental.get("msc3283_enabled", False)

        # MSC3266 (room summary api)
        self.msc3266_enabled: bool = experimental.get("msc3266_enabled", False)
