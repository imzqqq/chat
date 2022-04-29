from typing import Any, List

from chat.util.module_loader import load_module

from ._base import Config

LDAP_PROVIDER = "ldap_auth_provider.LdapAuthProvider"


class PasswordAuthProviderConfig(Config):
    section = "authproviders"

    def read_config(self, config, **kwargs):
        self.password_providers: List[Any] = []
        providers = []

        # We want to be backwards compatible with the old `ldap_config`
        # param.
        ldap_config = config.get("ldap_config", {})
        if ldap_config.get("enabled", False):
            providers.append({"module": LDAP_PROVIDER, "config": ldap_config})

        providers.extend(config.get("password_providers") or [])
        for i, provider in enumerate(providers):
            mod_name = provider["module"]

            # This is for backwards compat when the ldap auth provider resided
            # in this package.
            if mod_name == "chat.util.ldap_auth_provider.LdapAuthProvider":
                mod_name = LDAP_PROVIDER

            (provider_class, provider_config) = load_module(
                {"module": mod_name, "config": provider["config"]},
                ("password_providers", "<item %i>" % i),
            )

            self.password_providers.append((provider_class, provider_config))

    def generate_config_section(self, **kwargs):
        return """\
        # Password providers allow homeserver administrators to integrate
        # their Chat server installation with existing authentication methods
        # ex. LDAP, external tokens, etc.
        #
        # For more information and known implementations, please see
        # https://chat.docs.imzqqq.top/password_auth_providers.html
        #
        # Note: instances wishing to use SAML or CAS authentication should
        # instead use the `saml2_config` or `cas_config` options,
        # respectively.
        #
        password_providers:
        #    # Example config for an LDAP auth provider
        #    - module: "ldap_auth_provider.LdapAuthProvider"
        #      config:
        #        enabled: true
        #        uri: "ldap://ldap.example.com:389"
        #        start_tls: true
        #        base: "ou=users,dc=example,dc=com"
        #        attributes:
        #           uid: "cn"
        #           mail: "email"
        #           name: "givenName"
        #        #bind_dn:
        #        #bind_password:
        #        #filter: "(objectClass=posixAccount)"
        """
