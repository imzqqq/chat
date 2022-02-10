const { acceptToast } = require("./toasts");

async function setupSecureBackup(session) {
    session.log.step("sets up Secure Backup");

    await acceptToast(session, "Set up Secure Backup");

    // Continue with the default (generate a security key)
    const xsignContButton = await session.query('.mx_CreateSecretStorageDialog .mx_Dialog_buttons .mx_Dialog_primary');
    await xsignContButton.click();

    //ignore the recovery key
    //TODO: It's probably important for the tests to know the recovery key
    const copyButton = await session.query('.mx_CreateSecretStorageDialog_recoveryKeyButtons_copyBtn');
    await copyButton.click();

    //acknowledge that we copied the recovery key to a safe place
    const copyContinueButton = await session.query(
        '.mx_CreateSecretStorageDialog .mx_Dialog_buttons .mx_Dialog_primary',
    );
    await copyContinueButton.click();

    session.log.done();
}

module.exports = { setupSecureBackup };
