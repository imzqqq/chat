from typing import Any, Dict, List, Tuple

from chat.config._base import Config, ConfigError
from chat.util.module_loader import load_module


class ModulesConfig(Config):
    section = "modules"

    def read_config(self, config: dict, **kwargs):
        self.loaded_modules: List[Tuple[Any, Dict]] = []

        configured_modules = config.get("modules") or []
        for i, module in enumerate(configured_modules):
            config_path = ("modules", "<item %i>" % i)
            if not isinstance(module, dict):
                raise ConfigError("expected a mapping", config_path)

            self.loaded_modules.append(load_module(module, config_path))

    def generate_config_section(self, **kwargs):
        return """
            ## Modules ##

            # Server admins can expand Chat server's functionality with external modules.
            #
            # See https://chat.docs.dingshunyu.top/modules.html for more
            # documentation on how to configure or create custom modules for Chat server.
            #
            modules:
                # - module: my_super_module.MySuperClass
                #   config:
                #       do_thing: true
                # - module: my_other_super_module.SomeClass
                #   config: {}
            """
