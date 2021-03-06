# User Authentication

Chat server supports multiple methods of authenticating users, either out-of-the-box or through custom pluggable
authentication modules.

Included in Chat server is support for authenticating users via:

* A username and password.
* An email address and password.
* Single Sign-On through the SAML, Open ID Connect or CAS protocols.
* JSON Web Tokens.
* An administrator's shared secret.

Chat server can additionally be extended to support custom authentication schemes through optional "password auth provider"
modules.