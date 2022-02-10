"""
### MARK - imqzzZ, TODO: What is the difference between /chat and /_chat?
"""

from chat.http.server import JsonResource
from chat.rest import admin
from chat.rest.client import (
    account,
    account_data,
    account_validity,
    auth,
    capabilities,
    devices,
    directory,
    events,
    filter,
    groups,
    initial_sync,
    keys,
    knock,
    login as v1_login,
    logout,
    notifications,
    openid,
    password_policy,
    presence,
    profile,
    push_rule,
    pusher,
    read_marker,
    receipts,
    register,
    relations,
    report_event,
    room,
    room_batch,
    room_keys,
    room_upgrade_rest_servlet,
    sendtodevice,
    shared_rooms,
    sync,
    tags,
    thirdparty,
    tokenrefresh,
    user_directory,
    versions,
    voip,
)


class ClientRestResource(JsonResource):
    """Chat Client API REST resource.

    This gets mounted at various points under /chat/client, including:
       * /chat/client/r0
       * /chat/client/api/v1
       * /chat/client/unstable
       * etc
    """

    def __init__(self, hs):
        JsonResource.__init__(self, hs, canonical_json=False)
        self.register_servlets(self, hs)

    @staticmethod
    def register_servlets(client_resource, hs):
        versions.register_servlets(hs, client_resource)

        # Deprecated in r0
        initial_sync.register_servlets(hs, client_resource)
        room.register_deprecated_servlets(hs, client_resource)

        # Partially deprecated in r0
        events.register_servlets(hs, client_resource)

        room.register_servlets(hs, client_resource)
        v1_login.register_servlets(hs, client_resource)
        profile.register_servlets(hs, client_resource)
        presence.register_servlets(hs, client_resource)
        directory.register_servlets(hs, client_resource)
        voip.register_servlets(hs, client_resource)
        pusher.register_servlets(hs, client_resource)
        push_rule.register_servlets(hs, client_resource)
        logout.register_servlets(hs, client_resource)
        sync.register_servlets(hs, client_resource)
        filter.register_servlets(hs, client_resource)
        account.register_servlets(hs, client_resource)
        register.register_servlets(hs, client_resource)
        auth.register_servlets(hs, client_resource)
        receipts.register_servlets(hs, client_resource)
        read_marker.register_servlets(hs, client_resource)
        room_keys.register_servlets(hs, client_resource)
        keys.register_servlets(hs, client_resource)
        tokenrefresh.register_servlets(hs, client_resource)
        tags.register_servlets(hs, client_resource)
        account_data.register_servlets(hs, client_resource)
        report_event.register_servlets(hs, client_resource)
        openid.register_servlets(hs, client_resource)
        notifications.register_servlets(hs, client_resource)
        devices.register_servlets(hs, client_resource)
        thirdparty.register_servlets(hs, client_resource)
        sendtodevice.register_servlets(hs, client_resource)
        user_directory.register_servlets(hs, client_resource)
        groups.register_servlets(hs, client_resource)
        room_upgrade_rest_servlet.register_servlets(hs, client_resource)
        room_batch.register_servlets(hs, client_resource)
        capabilities.register_servlets(hs, client_resource)
        account_validity.register_servlets(hs, client_resource)
        relations.register_servlets(hs, client_resource)
        password_policy.register_servlets(hs, client_resource)
        knock.register_servlets(hs, client_resource)

        # moving to /_chat/admin
        admin.register_servlets_for_client_rest_resource(hs, client_resource)

        # unstable
        shared_rooms.register_servlets(hs, client_resource)
