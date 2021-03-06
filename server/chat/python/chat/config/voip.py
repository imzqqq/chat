from ._base import Config


class VoipConfig(Config):
    section = "voip"

    def read_config(self, config, **kwargs):
        self.turn_uris = config.get("turn_uris", [])
        self.turn_shared_secret = config.get("turn_shared_secret")
        self.turn_username = config.get("turn_username")
        self.turn_password = config.get("turn_password")
        self.turn_user_lifetime = self.parse_duration(
            config.get("turn_user_lifetime", "1h")
        )
        self.turn_allow_guests = config.get("turn_allow_guests", True)

    def generate_config_section(self, **kwargs):
        return """\
        ## TURN ##

        # The public URIs of the TURN server to give to clients
        #
        #turn_uris: []

        # The shared secret used to compute passwords for the TURN server
        #
        #turn_shared_secret: "YOUR_SHARED_SECRET"

        # The Username and password if the TURN server needs them and
        # does not use a token
        #
        #turn_username: "TURNSERVER_USERNAME"
        #turn_password: "TURNSERVER_PASSWORD"

        # How long generated TURN credentials last
        #
        #turn_user_lifetime: 1h

        # Whether guests should be allowed to use the TURN server.
        # This defaults to True, otherwise VoIP will be unreliable for guests.
        # However, it does introduce a slight security risk as it allows users to
        # connect to arbitrary endpoints without having first signed up for a
        # valid account (e.g. by passing a CAPTCHA).
        #
        #turn_allow_guests: true
        """
