const assert = require('assert');

module.exports = async function sendMessage(session, message) {
    session.log.step(`writes "${message}" in room`);
    // this selector needs to be the element that has contenteditable=true,
    // not any if its parents, otherwise it behaves flaky at best.
    const composer = await session.query('.mx_SendMessageComposer');
    // sometimes the focus that type() does internally doesn't seem to work
    // and calling click before seems to fix it 🤷
    await composer.click();
    await composer.type(message);
    const text = await session.innerText(composer);
    assert.equal(text.trim(), message.trim());
    await composer.press("Enter");
    // wait for the message to appear sent
    await session.query(".mx_EventTile_last:not(.mx_EventTile_sending)");
    session.log.done();
};
