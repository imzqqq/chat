from ._base import Config


class AuthConfig(Config):
    """Password and login configuration"""

    section = "auth"

    def read_config(self, config, **kwargs):
        password_config = config.get("password_config", {})
        if password_config is None:
            password_config = {}

        self.password_enabled = password_config.get("enabled", True)
        self.password_localdb_enabled = password_config.get("localdb_enabled", True)
        self.password_pepper = password_config.get("pepper", "")

        # Password policy
        self.password_policy = password_config.get("policy") or {}
        self.password_policy_enabled = self.password_policy.get("enabled", False)

        # User-interactive authentication
        ui_auth = config.get("ui_auth") or {}
        self.ui_auth_session_timeout = self.parse_duration(
            ui_auth.get("session_timeout", 0)
        )

    def generate_config_section(self, config_dir_path, server_name, **kwargs):
        return """\
        password_config:
           # Uncomment to disable password login
           #
           #enabled: false

           # Uncomment to disable authentication against the local password
           # database. This is ignored if `enabled` is false, and is only useful
           # if you have other password_providers.
           #
           #localdb_enabled: false

           # Uncomment and change to a secret random string for extra security.
           # DO NOT CHANGE THIS AFTER INITIAL SETUP!
           #
           #pepper: "EVEN_MORE_SECRET"

           # Define and enforce a password policy. Each parameter is optional.
           # This is an implementation of MSC2000.
           #
           policy:
              # Whether to enforce the password policy.
              # Defaults to 'false'.
              #
              #enabled: true

              # Minimum accepted length for a password.
              # Defaults to 0.
              #
              #minimum_length: 15

              # Whether a password must contain at least one digit.
              # Defaults to 'false'.
              #
              #require_digit: true

              # Whether a password must contain at least one symbol.
              # A symbol is any character that's not a number or a letter.
              # Defaults to 'false'.
              #
              #require_symbol: true

              # Whether a password must contain at least one lowercase letter.
              # Defaults to 'false'.
              #
              #require_lowercase: true

              # Whether a password must contain at least one lowercase letter.
              # Defaults to 'false'.
              #
              #require_uppercase: true

        ui_auth:
            # The amount of time to allow a user-interactive authentication session
            # to be active.
            #
            # This defaults to 0, meaning the user is queried for their credentials
            # before every action, but this can be overridden to allow a single
            # validation to be re-used.  This weakens the protections afforded by
            # the user-interactive authentication process, by allowing for multiple
            # (and potentially different) operations to use the same validation session.
            #
            # This is ignored for potentially "dangerous" operations (including
            # deactivating an account, modifying an account password, and
            # adding a 3PID).
            #
            # Uncomment below to allow for credential validation to last for 15
            # seconds.
            #
            #session_timeout: "15s"
        """
