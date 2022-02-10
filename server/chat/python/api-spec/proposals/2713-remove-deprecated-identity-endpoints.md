# MSC2713: Remove deprecated Identity Service endpoints

Implementations will have had plenty of time to adopt the new v2 API for Identity Servers, so
we should clean out the old endpoints.

All deprecated endpoints in the r0.3.0 Identity Service API specification are to be removed.

For completeness, this includes:

* `GET /chat/identity/api/v1`
* `GET /chat/identity/api/v1/pubkey/{keyId}`
* `GET /chat/identity/api/v1/pubkey/isvalid`
* `GET /chat/identity/api/v1/pubkey/ephemeral/isvalid`
* `GET /chat/identity/api/v1/lookup`
* `POST /chat/identity/api/v1/bulk_lookup`
* `POST /chat/identity/api/v1/validate/email/requestToken`
* `POST /chat/identity/api/v1/validate/email/submitToken`
* `GET /chat/identity/api/v1/validate/email/submitToken`
* `POST /chat/identity/api/v1/validate/msisdn/requestToken`
* `POST /chat/identity/api/v1/validate/msisdn/submitToken`
* `GET /chat/identity/api/v1/validate/msisdn/submitToken`
* `GET /chat/identity/api/v1/3pid/getValidated3pid`
* `POST /chat/identity/api/v1/3pid/bind`
* `POST /chat/identity/api/v1/3pid/unbind`
* `POST /chat/identity/api/v1/store-invite`
* `POST /chat/identity/api/v1/sign-ed25519`
