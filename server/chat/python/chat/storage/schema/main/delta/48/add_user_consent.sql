/* record the version of the privacy policy the user has consented to
 */
ALTER TABLE users ADD COLUMN consent_version TEXT;
