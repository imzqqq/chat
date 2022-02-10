const assert = require('assert');

async function assertNoToasts(session) {
    try {
        await session.query('.mx_Toast_toast', 1000, true);
    } catch (e) {
        const h2Element = await session.query('.mx_Toast_title h2', 1000);
        const toastTitle = await session.innerText(h2Element);
        throw new Error(`"${toastTitle}" toast found when none expected`);
    }
}

async function assertToast(session, expectedTitle) {
    const h2Element = await session.query('.mx_Toast_title h2');
    const toastTitle = await session.innerText(h2Element);
    assert.equal(toastTitle, expectedTitle);
}

async function acceptToast(session, expectedTitle) {
    await assertToast(session, expectedTitle);
    const btn = await session.query('.mx_Toast_buttons .mx_AccessibleButton_kind_primary');
    await btn.click();
}

async function rejectToast(session, expectedTitle) {
    await assertToast(session, expectedTitle);
    const btn = await session.query('.mx_Toast_buttons .mx_AccessibleButton_kind_danger_outline');
    await btn.click();
}

module.exports = { assertNoToasts, assertToast, acceptToast, rejectToast };
