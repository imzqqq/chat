from ._base import Config


class UserDirectoryConfig(Config):
    """User Directory Configuration
    Configuration for the behaviour of the /user_directory API
    """

    section = "userdirectory"

    def read_config(self, config, **kwargs):
        user_directory_config = config.get("user_directory") or {}
        self.user_directory_search_enabled = user_directory_config.get("enabled", True)
        self.user_directory_search_all_users = user_directory_config.get(
            "search_all_users", False
        )
        self.user_directory_search_prefer_local_users = user_directory_config.get(
            "prefer_local_users", False
        )

    def generate_config_section(self, config_dir_path, server_name, **kwargs):
        ### MARK - TODO
        return """\
        # User Directory configuration
        #
        user_directory:
            # Defines whether users can search the user directory. If false then
            # empty responses are returned to all queries. Defaults to true.
            #
            # Uncomment to disable the user directory.
            #
            #enabled: false

            # Defines whether to search all users visible to your HS when searching
            # the user directory, rather than limiting to users visible in public
            # rooms. Defaults to false.
            #
            # If you set it true, you'll have to rebuild the user_directory search
            # indexes, see:
            # https://chat.docs.imzqqq.top/user_directory.html
            #
            # Uncomment to return search results containing all known users, even if that
            # user does not share a room with the requester.
            #
            #search_all_users: true

            # Defines whether to prefer local users in search query results.
            # If True, local users are more likely to appear above remote users
            # when searching the user directory. Defaults to false.
            #
            # Uncomment to prefer local over remote users in user directory search
            # results.
            #
            #prefer_local_users: true
        """
