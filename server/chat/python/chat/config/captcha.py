from ._base import Config


class CaptchaConfig(Config):
    section = "captcha"

    def read_config(self, config, **kwargs):
        self.recaptcha_private_key = config.get("recaptcha_private_key")
        self.recaptcha_public_key = config.get("recaptcha_public_key")
        self.enable_registration_captcha = config.get(
            "enable_registration_captcha", False
        )
        self.recaptcha_siteverify_api = config.get(
            "recaptcha_siteverify_api",
            "https://www.recaptcha.net/recaptcha/api/siteverify",
        )
        self.recaptcha_template = self.read_template("recaptcha.html")

    def generate_config_section(self, **kwargs):
        return """\
        ## Captcha ##
        # See docs/CAPTCHA_SETUP.md for full details of configuring this.

        # This homeserver's ReCAPTCHA public key. Must be specified if
        # enable_registration_captcha is enabled.
        #
        #recaptcha_public_key: "YOUR_PUBLIC_KEY"

        # This homeserver's ReCAPTCHA private key. Must be specified if
        # enable_registration_captcha is enabled.
        #
        #recaptcha_private_key: "YOUR_PRIVATE_KEY"

        # Uncomment to enable ReCaptcha checks when registering, preventing signup
        # unless a captcha is answered. Requires a valid ReCaptcha
        # public/private key. Defaults to 'false'.
        #
        #enable_registration_captcha: true

        # The API endpoint to use for verifying m.login.recaptcha responses.
        # Defaults to "https://www.recaptcha.net/recaptcha/api/siteverify".
        #
        #recaptcha_siteverify_api: "https://my.recaptcha.site"
        """
