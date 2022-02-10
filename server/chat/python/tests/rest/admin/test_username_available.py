import chat.rest.admin
from chat.api.errors import Codes, SynapseError
from chat.rest.client import login

from tests import unittest


class UsernameAvailableTestCase(unittest.HomeserverTestCase):
    servlets = [
        chat.rest.admin.register_servlets,
        login.register_servlets,
    ]
    url = "/_chat/admin/v1/username_available"

    def prepare(self, reactor, clock, hs):
        self.register_user("admin", "pass", admin=True)
        self.admin_user_tok = self.login("admin", "pass")

        async def check_username(username):
            if username == "allowed":
                return True
            raise SynapseError(400, "User ID already taken.", errcode=Codes.USER_IN_USE)

        handler = self.hs.get_registration_handler()
        handler.check_username = check_username

    def test_username_available(self):
        """
        The endpoint should return a 200 response if the username does not exist
        """

        url = "%s?username=%s" % (self.url, "allowed")
        channel = self.make_request("GET", url, None, self.admin_user_tok)

        self.assertEqual(200, int(channel.result["code"]), msg=channel.result["body"])
        self.assertTrue(channel.json_body["available"])

    def test_username_unavailable(self):
        """
        The endpoint should return a 200 response if the username does not exist
        """

        url = "%s?username=%s" % (self.url, "disallowed")
        channel = self.make_request("GET", url, None, self.admin_user_tok)

        self.assertEqual(400, int(channel.result["code"]), msg=channel.result["body"])
        self.assertEqual(channel.json_body["errcode"], "M_USER_IN_USE")
        self.assertEqual(channel.json_body["error"], "User ID already taken.")
