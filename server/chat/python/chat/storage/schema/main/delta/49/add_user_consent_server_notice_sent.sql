/* record whether we have sent a server notice about consenting to the
 * privacy policy. Specifically records the version of the policy we sent
 * a message about.
 */
ALTER TABLE users ADD COLUMN consent_server_notice_sent TEXT;
