from typing import Any, List

from chat.config.sso import SsoAttributeRequirement

from ._base import Config, ConfigError
from ._util import validate_config


class CasConfig(Config):
    """Cas Configuration

    cas_server_url: URL of CAS server
    """

    section = "cas"

    def read_config(self, config, **kwargs):
        cas_config = config.get("cas_config", None)
        self.cas_enabled = cas_config and cas_config.get("enabled", True)

        if self.cas_enabled:
            self.cas_server_url = cas_config["server_url"]

            # The public baseurl is required because it is used by the redirect
            # template.
            public_baseurl = self.public_baseurl
            if not public_baseurl:
                raise ConfigError("cas_config requires a public_baseurl to be set")

            # TODO Update this to a _chat URL.
            self.cas_service_url = public_baseurl + "chat/client/r0/login/cas/ticket"
            self.cas_displayname_attribute = cas_config.get("displayname_attribute")
            required_attributes = cas_config.get("required_attributes") or {}
            self.cas_required_attributes = _parsed_required_attributes_def(
                required_attributes
            )

        else:
            self.cas_server_url = None
            self.cas_service_url = None
            self.cas_displayname_attribute = None
            self.cas_required_attributes = []

    def generate_config_section(self, config_dir_path, server_name, **kwargs):
        return """\
        # Enable Central Authentication Service (CAS) for registration and login.
        #
        cas_config:
          # Uncomment the following to enable authorization against a CAS server.
          # Defaults to false.
          #
          #enabled: true

          # The URL of the CAS authorization endpoint.
          #
          #server_url: "https://cas-server.com"

          # The attribute of the CAS response to use as the display name.
          #
          # If unset, no displayname will be set.
          #
          #displayname_attribute: name

          # It is possible to configure Chat server to only allow logins if CAS attributes
          # match particular values. All of the keys in the mapping below must exist
          # and the values must match the given value. Alternately if the given value
          # is None then any value is allowed (the attribute just must exist).
          # All of the listed attributes must match for the login to be permitted.
          #
          #required_attributes:
          #  userGroup: "staff"
          #  department: None
        """


# CAS uses a legacy required attributes mapping, not the one provided by
# SsoAttributeRequirement.
REQUIRED_ATTRIBUTES_SCHEMA = {
    "type": "object",
    "additionalProperties": {"anyOf": [{"type": "string"}, {"type": "null"}]},
}


def _parsed_required_attributes_def(
    required_attributes: Any,
) -> List[SsoAttributeRequirement]:
    validate_config(
        REQUIRED_ATTRIBUTES_SCHEMA,
        required_attributes,
        config_path=("cas_config", "required_attributes"),
    )
    return [SsoAttributeRequirement(k, v) for k, v in required_attributes.items()]
