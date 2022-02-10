from typing import TYPE_CHECKING, Mapping

from twisted.web.resource import Resource

from chat.rest.chat.client.new_user_consent import NewUserConsentResource
from chat.rest.chat.client.pick_idp import PickIdpResource
from chat.rest.chat.client.pick_username import pick_username_resource
from chat.rest.chat.client.sso_register import SsoRegisterResource

if TYPE_CHECKING:
    from chat.server import HomeServer


def build_synapse_client_resource_tree(hs: "HomeServer") -> Mapping[str, Resource]:
    """Builds a resource tree to include chat-specific client resources

    These are resources which should be loaded on all workers which expose a C-S API:
    ie, the main process, and any generic workers so configured.

    Returns:
         map from path to Resource.
    """
    resources = {
        # SSO bits. These are always loaded, whether or not SSO login is actually
        # enabled (they just won't work very well if it's not)
        "/_chat/client/pick_idp": PickIdpResource(hs),
        "/_chat/client/pick_username": pick_username_resource(hs),
        "/_chat/client/new_user_consent": NewUserConsentResource(hs),
        "/_chat/client/sso_register": SsoRegisterResource(hs),
    }

    # provider-specific SSO bits. Only load these if they are enabled, since they
    # rely on optional dependencies.
    if hs.config.oidc_enabled:
        from chat.rest.chat.client.oidc import OIDCResource

        resources["/_chat/client/oidc"] = OIDCResource(hs)

    if hs.config.saml2_enabled:
        from chat.rest.chat.client.saml2 import SAML2Resource

        res = SAML2Resource(hs)
        resources["/_chat/client/saml2"] = res

        # This is also mounted under '/chat' for backwards-compatibility.
        # To be removed in Chat v1.32.0.
        resources["/chat/saml2"] = res

    return resources


__all__ = ["build_synapse_client_resource_tree"]
