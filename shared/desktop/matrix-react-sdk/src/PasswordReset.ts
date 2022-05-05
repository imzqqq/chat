import { createClient, IRequestTokenResponse, MatrixClient } from 'matrix-js-sdk/src/matrix';
import { _t } from './languageHandler';

/**
 * Allows a user to reset their password on a homeserver.
 *
 * This involves getting an email token from the identity server to "prove" that
 * the client owns the given email address, which is then passed to the password
 * API on the homeserver in question with the new password.
 */
export default class PasswordReset {
    private client: MatrixClient;
    private clientSecret: string;
    private identityServerDomain: string;
    private password: string;
    private sessionId: string;

    /**
     * Configure the endpoints for password resetting.
     * @param {string} homeserverUrl The URL to the HS which has the account to reset.
     * @param {string} identityUrl The URL to the IS which has linked the email -> mxid mapping.
     */
    constructor(homeserverUrl: string, identityUrl: string) {
        this.client = createClient({
            baseUrl: homeserverUrl,
            idBaseUrl: identityUrl,
        });
        this.clientSecret = this.client.generateClientSecret();
        this.identityServerDomain = identityUrl ? identityUrl.split("://")[1] : null;
    }

    /**
     * Attempt to reset the user's password. This will trigger a side-effect of
     * sending an email to the provided email address.
     * @param {string} emailAddress The email address
     * @param {string} newPassword The new password for the account.
     * @return {Promise} Resolves when the email has been sent. Then call checkEmailLinkClicked().
     */
    public resetPassword(emailAddress: string, newPassword: string): Promise<IRequestTokenResponse> {
        this.password = newPassword;
        return this.client.requestPasswordEmailToken(emailAddress, this.clientSecret, 1).then((res) => {
            this.sessionId = res.sid;
            return res;
        }, function(err) {
            if (err.errcode === 'M_THREEPID_NOT_FOUND') {
                err.message = _t('This email address was not found');
            } else if (err.httpStatus) {
                err.message = err.message + ` (Status ${err.httpStatus})`;
            }
            throw err;
        });
    }

    /**
     * Checks if the email link has been clicked by attempting to change the password
     * for the mxid linked to the email.
     * @return {Promise} Resolves if the password was reset. Rejects with an object
     * with a "message" property which contains a human-readable message detailing why
     * the reset failed, e.g. "There is no mapped matrix user ID for the given email address".
     */
    public async checkEmailLinkClicked(): Promise<void> {
        const creds = {
            sid: this.sessionId,
            client_secret: this.clientSecret,
        };

        try {
            await this.client.setPassword({
                // Note: Though this sounds like a login type for identity servers only, it
                // has a dual purpose of being used for homeservers too.
                type: "m.login.email.identity",
                // TODO: Remove `threepid_creds` once servers support proper UIA
                // See https://github.com/matrix-org/synapse/issues/5665
                // See https://github.com/matrix-org/matrix-doc/issues/2220
                threepid_creds: creds,
                threepidCreds: creds,
            }, this.password);
        } catch (err) {
            if (err.httpStatus === 401) {
                err.message = _t('Failed to verify email address: make sure you clicked the link in the email');
            } else if (err.httpStatus === 404) {
                err.message =
                    _t('Your email address does not appear to be associated with a Chat ID on this Homeserver.');
            } else if (err.httpStatus) {
                err.message += ` (Status ${err.httpStatus})`;
            }
            throw err;
        }
    }
}

