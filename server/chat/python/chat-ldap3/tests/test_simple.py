# -*- coding: utf-8 -*-

from twisted.trial import unittest
from twisted.internet import defer

from mock import Mock

from . import (
    create_ldap_server,
    create_auth_provider,
    get_qualified_user_id,
    make_awaitable,
)

import logging
logging.basicConfig()


class LdapSimpleTestCase(unittest.TestCase):
    @defer.inlineCallbacks
    def setUp(self):
        self.ldap_server = yield defer.ensureDeferred(create_ldap_server())
        account_handler = Mock(spec_set=["check_user_exists", "get_qualified_user_id"])
        account_handler.check_user_exists.return_value = make_awaitable(True)
        account_handler.get_qualified_user_id = get_qualified_user_id

        self.auth_provider = create_auth_provider(
            self.ldap_server, account_handler,
            config={
                "enabled": True,
                "uri": "ldap://localhost:%d" % self.ldap_server.listener.getHost().port,
                "base": "ou=people,dc=example,dc=org",
                "attributes": {
                    "uid": "cn",
                    "name": "gn",
                    "mail": "mail",
                },
            },
        )

    def tearDown(self):
        self.ldap_server.close()

    @defer.inlineCallbacks
    def test_unknown_user(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "non_existent",
            'm.login.password',
            {"password": "password"}
        ))
        self.assertFalse(result)

    @defer.inlineCallbacks
    def test_incorrect_pwd(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "bob",
            'm.login.password',
            {"password": "wrong_password"}
        ))
        self.assertFalse(result)

    @defer.inlineCallbacks
    def test_correct_pwd(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "bob",
            'm.login.password',
            {"password": "secret"}
        ))
        self.assertEqual(result, "@bob:test")

    @defer.inlineCallbacks
    def test_no_pwd(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "bob",
            'm.login.password',
            {"password": ""}
        ))
        self.assertFalse(result)


class LdapSearchTestCase(unittest.TestCase):
    @defer.inlineCallbacks
    def setUp(self):
        self.ldap_server = yield defer.ensureDeferred(create_ldap_server())
        account_handler = Mock(spec_set=["check_user_exists", "get_qualified_user_id"])
        account_handler.check_user_exists.return_value = make_awaitable(True)
        account_handler.get_qualified_user_id = get_qualified_user_id

        self.auth_provider = create_auth_provider(
            self.ldap_server, account_handler,
            config={
                "enabled": True,
                "uri": "ldap://localhost:%d" % self.ldap_server.listener.getHost().port,
                "base": "ou=people,dc=example,dc=org",
                "bind_dn": "cn=jsmith,ou=people,dc=example,dc=org",
                "bind_password": "eekretsay",
                "attributes": {
                    "uid": "cn",
                    "name": "gn",
                    "mail": "mail",
                },
            },
        )

    def tearDown(self):
        self.ldap_server.close()

    @defer.inlineCallbacks
    def test_correct_pwd_search_mode(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "bob",
            'm.login.password',
            {"password": "secret"}
        ))
        self.assertEqual(result, "@bob:test")

    @defer.inlineCallbacks
    def test_incorrect_pwd_search_mode(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "bob",
            'm.login.password',
            {"password": "wrong_password"}
        ))
        self.assertFalse(result)

    @defer.inlineCallbacks
    def test_unknown_user_search_mode(self):
        result = yield defer.ensureDeferred(self.auth_provider.check_auth(
            "foobar",
            'm.login.password',
            {"password": "some_password"}
        ))
        self.assertFalse(result)
