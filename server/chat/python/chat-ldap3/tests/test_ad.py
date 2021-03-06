# -*- coding: utf-8 -*-

from ldaptor import interfaces
from ldaptor.protocols import pureldap
from ldaptor.protocols.ldap import ldaperrors
from ldaptor.protocols.ldap.ldapserver import LDAPServer

from twisted.internet.defer import ensureDeferred
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


class _ActiveDirectoryLDAPServer(LDAPServer):
    """Extends LDAPServer to return AD-specific attributes

    Includes `rootDomainNamingContext` in bind responses.
    """
    def getRootDSE(self, request, reply):
        root = interfaces.IConnectedLDAPEntry(self.factory)
        reply(pureldap.LDAPSearchResultEntry(
            objectName='',
            attributes=[('supportedLDAPVersion', ['3']),
                        ('namingContexts', [root.dn.getText()]),
                        ('supportedExtension', [
                            pureldap.LDAPPasswordModifyRequest.oid, ]),
                        ('rootDomainNamingContext', ['DC=example,DC=org']), ], ))
        return pureldap.LDAPSearchResultDone(
            resultCode=ldaperrors.Success.resultCode)


class AbstractLdapActiveDirectoryTestCase():
    @defer.inlineCallbacks
    def setUp(self):
        self.ldap_server = yield ensureDeferred(
            create_ldap_server(_ActiveDirectoryLDAPServer)
        )
        account_handler = Mock(spec_set=["check_user_exists", "get_qualified_user_id"])
        account_handler.check_user_exists.return_value = make_awaitable(True)
        account_handler.get_qualified_user_id = get_qualified_user_id

        self.auth_provider = create_auth_provider(
            self.ldap_server, account_handler,
            config=self.getConfig(),
        )

    def getConfig(self):
        return self.getDefaultConfig()

    def getDefaultConfig(self):
        return {
            "enabled": True,
            "active_directory": True,
            "uri": "ldap://localhost:%d" % self.ldap_server.listener.getHost().port,
            "base": "dc=example,dc=org",
            "bind_dn": "cn=jsmith,ou=people,dc=example,dc=org",
            "bind_password": "eekretsay",
            "attributes": {
                "uid": "userPrincipalName",
                "name": "gn",
                "mail": "mail",
            },
        }

    def tearDown(self):
        self.ldap_server.close()


class LdapActiveDirectoryTestCase(AbstractLdapActiveDirectoryTestCase, unittest.TestCase):
    @defer.inlineCallbacks
    def test_correct_pwd(self):
        result = yield ensureDeferred(self.auth_provider.check_auth(
            "main.example.org\\mainuser",
            'm.login.password',
            {"password": "abracadabra"}
        ))
        self.assertEqual(result, "@mainuser/main.example.org:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "main\\mainuser",
            'm.login.password',
            {"password": "abracadabra"}
        ))
        self.assertEqual(result, "@mainuser/main.example.org:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary.example.org\\nonmainuser",
            'm.login.password',
            {"password": "simsalabim"}
        ))
        self.assertEqual(result, "@nonmainuser/subsidiary.example.org:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary\\nonmainuser",
            'm.login.password',
            {"password": "simsalabim"}
        ))
        self.assertEqual(result, "@nonmainuser/subsidiary.example.org:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary.example.org\\mainuser",
            'm.login.password',
            {"password": "changeit"}
        ))
        self.assertEqual(result, "@mainuser/subsidiary.example.org:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary\\mainuser",
            'm.login.password',
            {"password": "changeit"}
        ))
        self.assertEqual(result, "@mainuser/subsidiary.example.org:test")

    @defer.inlineCallbacks
    def test_single_email(self):
        result = yield ensureDeferred(self.auth_provider.check_3pid_auth(
            "email",
            "mainuser@main.example.org",
            "abracadabra",
        ))
        self.assertFalse(result)

    @defer.inlineCallbacks
    def test_incorrect_pwd(self):
        result = yield ensureDeferred(self.auth_provider.check_auth(
            "main.example.org\\mainuser",
            'm.login.password',
            {"password": "bruteforce"}
        ))
        self.assertFalse(result)

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary.example.org\\mainuser",
            'm.login.password',
            {"password": "abracadabra"}
        ))
        self.assertFalse(result)

    @defer.inlineCallbacks
    def test_email_login(self):
        result = yield ensureDeferred(self.auth_provider.check_3pid_auth(
            "email",
            "uniqueuser@main.example.org",
            "nothing",
        ))
        self.assertEqual(result, "@uniqueuser/main.example.org:test")


class LdapActiveDirectoryDefaultDomainTestCase(
    AbstractLdapActiveDirectoryTestCase,
    unittest.TestCase
):
    def getConfig(self):
        return dict(
            self.getDefaultConfig(),
            default_domain="main.example.org"
        )

    @defer.inlineCallbacks
    def test_correct_pwd(self):
        result = yield ensureDeferred(self.auth_provider.check_auth(
            "mainuser",
            'm.login.password',
            {"password": "abracadabra"}
        ))
        self.assertEqual(result, "@mainuser:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "main.example.org\\mainuser",
            'm.login.password',
            {"password": "abracadabra"}
        ))
        self.assertEqual(result, "@mainuser:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary.example.org\\nonmainuser",
            'm.login.password',
            {"password": "simsalabim"}
        ))
        self.assertEqual(result, "@nonmainuser/subsidiary.example.org:test")

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary.example.org\\mainuser",
            'm.login.password',
            {"password": "changeit"}
        ))
        self.assertEqual(result, "@mainuser/subsidiary.example.org:test")

    @defer.inlineCallbacks
    def test_incorrect_pwd(self):
        result = yield ensureDeferred(self.auth_provider.check_auth(
            "mainuser",
            'm.login.password',
            {"password": "changeit"}
        ))
        self.assertFalse(result)

        result = yield ensureDeferred(self.auth_provider.check_auth(
            "subsidiary.example.org\\mainuser",
            'm.login.password',
            {"password": "abracadabra"}
        ))
        self.assertFalse(result)

    @defer.inlineCallbacks
    def test_email_login(self):
        result = yield ensureDeferred(self.auth_provider.check_3pid_auth(
            "email",
            "uniqueuser@main.example.org",
            "nothing",
        ))
        self.assertEqual(result, "@uniqueuser:test")
