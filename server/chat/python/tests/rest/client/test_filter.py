from twisted.internet import defer

from chat.api.errors import Codes
from chat.rest.client import filter

from tests import unittest

PATH_PREFIX = "/chat/client/v2_alpha"


class FilterTestCase(unittest.HomeserverTestCase):

    user_id = "@apple:test"
    hijack_auth = True
    EXAMPLE_FILTER = {"room": {"timeline": {"types": ["m.room.message"]}}}
    EXAMPLE_FILTER_JSON = b'{"room": {"timeline": {"types": ["m.room.message"]}}}'
    servlets = [filter.register_servlets]

    def prepare(self, reactor, clock, hs):
        self.filtering = hs.get_filtering()
        self.store = hs.get_datastore()

    def test_add_filter(self):
        channel = self.make_request(
            "POST",
            "/chat/client/r0/user/%s/filter" % (self.user_id),
            self.EXAMPLE_FILTER_JSON,
        )

        self.assertEqual(channel.result["code"], b"200")
        self.assertEqual(channel.json_body, {"filter_id": "0"})
        filter = self.store.get_user_filter(user_localpart="apple", filter_id=0)
        self.pump()
        self.assertEquals(filter.result, self.EXAMPLE_FILTER)

    def test_add_filter_for_other_user(self):
        channel = self.make_request(
            "POST",
            "/chat/client/r0/user/%s/filter" % ("@watermelon:test"),
            self.EXAMPLE_FILTER_JSON,
        )

        self.assertEqual(channel.result["code"], b"403")
        self.assertEquals(channel.json_body["errcode"], Codes.FORBIDDEN)

    def test_add_filter_non_local_user(self):
        _is_mine = self.hs.is_mine
        self.hs.is_mine = lambda target_user: False
        channel = self.make_request(
            "POST",
            "/chat/client/r0/user/%s/filter" % (self.user_id),
            self.EXAMPLE_FILTER_JSON,
        )

        self.hs.is_mine = _is_mine
        self.assertEqual(channel.result["code"], b"403")
        self.assertEquals(channel.json_body["errcode"], Codes.FORBIDDEN)

    def test_get_filter(self):
        filter_id = defer.ensureDeferred(
            self.filtering.add_user_filter(
                user_localpart="apple", user_filter=self.EXAMPLE_FILTER
            )
        )
        self.reactor.advance(1)
        filter_id = filter_id.result
        channel = self.make_request(
            "GET", "/chat/client/r0/user/%s/filter/%s" % (self.user_id, filter_id)
        )

        self.assertEqual(channel.result["code"], b"200")
        self.assertEquals(channel.json_body, self.EXAMPLE_FILTER)

    def test_get_filter_non_existant(self):
        channel = self.make_request(
            "GET", "/chat/client/r0/user/%s/filter/12382148321" % (self.user_id)
        )

        self.assertEqual(channel.result["code"], b"404")
        self.assertEquals(channel.json_body["errcode"], Codes.NOT_FOUND)

    # Currently invalid params do not have an appropriate errcode
    # in errors.py
    def test_get_filter_invalid_id(self):
        channel = self.make_request(
            "GET", "/chat/client/r0/user/%s/filter/foobar" % (self.user_id)
        )

        self.assertEqual(channel.result["code"], b"400")

    # No ID also returns an invalid_id error
    def test_get_filter_no_id(self):
        channel = self.make_request(
            "GET", "/chat/client/r0/user/%s/filter/" % (self.user_id)
        )

        self.assertEqual(channel.result["code"], b"400")
