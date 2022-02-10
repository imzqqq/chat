const assert = require('assert');

async function assertDialog(session, expectedTitle) {
    const titleElement = await session.query(".mx_Dialog .mx_Dialog_title");
    const dialogHeader = await session.innerText(titleElement);
    assert.equal(dialogHeader, expectedTitle);
}

async function acceptDialog(session, expectedTitle) {
    const foundDialog = await acceptDialogMaybe(session, expectedTitle);
    if (!foundDialog) {
        throw new Error("could not find a dialog");
    }
}

async function acceptDialogMaybe(session, expectedTitle) {
    let primaryButton = null;
    try {
        primaryButton = await session.query(".mx_Dialog .mx_Dialog_primary");
    } catch (err) {
        return false;
    }
    if (expectedTitle) {
        await assertDialog(session, expectedTitle);
    }
    await primaryButton.click();
    return true;
}

module.exports = {
    assertDialog,
    acceptDialog,
    acceptDialogMaybe,
};
