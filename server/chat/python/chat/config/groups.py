from ._base import Config


class GroupsConfig(Config):
    section = "groups"

    def read_config(self, config, **kwargs):
        self.enable_group_creation = config.get("enable_group_creation", False)
        self.group_creation_prefix = config.get("group_creation_prefix", "")

    def generate_config_section(self, **kwargs):
        return """\
        # Uncomment to allow non-server-admin users to create groups on this server
        #
        #enable_group_creation: true

        # If enabled, non server admins can only create groups with local parts
        # starting with this prefix
        #
        #group_creation_prefix: "unofficial_"
        """
