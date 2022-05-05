const assert = require('assert');

async function openSettings(session, section) {
    const menuButton = await session.query(".mx_UserMenu");
    await menuButton.click();
    const settingsItem = await session.query(".mx_UserMenu_iconSettings");
    await settingsItem.click();
    if (section) {
        const sectionButton = await session.query(
            `.mx_UserSettingsDialog .mx_TabbedView_tabLabels .mx_UserSettingsDialog_${section}Icon`);
        await sectionButton.click();
    }
}

module.exports.enableLazyLoading = async function(session) {
    session.log.step(`enables lazy loading of members in the lab settings`);
    const settingsButton = await session.query('.mx_BottomLeftMenu_settings');
    await settingsButton.click();
    const llCheckbox = await session.query("#feature_lazyloading");
    await llCheckbox.click();
    await session.waitForReload();
    const closeButton = await session.query(".mx_RoomHeader_cancelButton");
    await closeButton.click();
    session.log.done();
};

module.exports.getE2EDeviceFromSettings = async function(session) {
    session.log.step(`gets e2e device/key from settings`);
    await openSettings(session, "security");
    const deviceAndKey = await session.queryAll(".mx_SettingsTab_section .mx_SecurityUserSettingsTab_deviceInfo code");
    assert.equal(deviceAndKey.length, 2);
    const id = await (await deviceAndKey[0].getProperty("innerText")).jsonValue();
    const key = await (await deviceAndKey[1].getProperty("innerText")).jsonValue();
    const closeButton = await session.query(".mx_UserSettingsDialog .mx_Dialog_cancelButton");
    await closeButton.click();
    session.log.done();
    return { id, key };
};
