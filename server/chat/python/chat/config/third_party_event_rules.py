from chat.util.module_loader import load_module

from ._base import Config


class ThirdPartyRulesConfig(Config):
    section = "thirdpartyrules"

    def read_config(self, config, **kwargs):
        self.third_party_event_rules = None

        provider = config.get("third_party_event_rules", None)
        if provider is not None:
            self.third_party_event_rules = load_module(
                provider, ("third_party_event_rules",)
            )
