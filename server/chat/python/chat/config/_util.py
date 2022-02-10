from typing import Any, Iterable

import jsonschema

from chat.config._base import ConfigError
from chat.types import JsonDict


def validate_config(
    json_schema: JsonDict, config: Any, config_path: Iterable[str]
) -> None:
    """Validates a config setting against a JsonSchema definition

    This can be used to validate a section of the config file against a schema
    definition. If the validation fails, a ConfigError is raised with a textual
    description of the problem.

    Args:
        json_schema: the schema to validate against
        config: the configuration value to be validated
        config_path: the path within the config file. This will be used as a basis
           for the error message.
    """
    try:
        jsonschema.validate(config, json_schema)
    except jsonschema.ValidationError as e:
        raise json_error_to_config_error(e, config_path)


def json_error_to_config_error(
    e: jsonschema.ValidationError, config_path: Iterable[str]
) -> ConfigError:
    """Converts a json validation error to a user-readable ConfigError

    Args:
        e: the exception to be converted
        config_path: the path within the config file. This will be used as a basis
           for the error message.

    Returns:
        a ConfigError
    """
    # copy `config_path` before modifying it.
    path = list(config_path)
    for p in list(e.absolute_path):
        if isinstance(p, int):
            path.append("<item %i>" % p)
        else:
            path.append(str(p))
    return ConfigError(e.message, path)
