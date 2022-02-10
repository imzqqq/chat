from ._base import ConfigError, find_config_files

# export ConfigError and find_config_files if somebody does
# import *
# this is largely a fudge to stop PEP8 moaning about the import
__all__ = ["ConfigError", "find_config_files"]
