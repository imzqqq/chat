const { measureStart, measureStop } = require('../util');

async function openRoomDirectory(session) {
    const roomDirectoryButton = await session.query('.mx_LeftPanel_exploreButton');
    await roomDirectoryButton.click();
}

async function findSublist(session, name) {
    return await session.query(`.mx_RoomSublist[aria-label="${name}" i]`);
}

async function createRoom(session, roomName, encrypted=false) {
    session.log.step(`creates room "${roomName}"`);

    const roomsSublist = await findSublist(session, "rooms");
    const addRoomButton = await roomsSublist.$(".mx_RoomSublist_auxButton");
    await addRoomButton.click();

    const createRoomButton = await session.query('.mx_AccessibleButton[aria-label="Create new room"]');
    await createRoomButton.click();

    const roomNameInput = await session.query('.mx_CreateRoomDialog_name input');
    await session.replaceInputText(roomNameInput, roomName);

    if (!encrypted) {
        const encryptionToggle = await session.query('.mx_CreateRoomDialog_e2eSwitch .mx_ToggleSwitch');
        await encryptionToggle.click();
    }

    const createButton = await session.query('.mx_Dialog_primary');
    await createButton.click();

    await session.query('.mx_MessageComposer');
    session.log.done();
}

async function createDm(session, invitees) {
    session.log.step(`creates DM with ${JSON.stringify(invitees)}`);

    await measureStart(session, "mx_CreateDM");

    const dmsSublist = await findSublist(session, "people");
    const startChatButton = await dmsSublist.$(".mx_RoomSublist_auxButton");
    await startChatButton.click();

    const inviteesEditor = await session.query('.mx_InviteDialog_editor input');
    for (const target of invitees) {
        await session.replaceInputText(inviteesEditor, target);
        await session.delay(1000); // give it a moment to figure out a suggestion
        // find the suggestion and accept it
        const suggestions = await session.queryAll('.mx_InviteDialog_roomTile_userId');
        const suggestionTexts = await Promise.all(suggestions.map(s => session.innerText(s)));
        const suggestionIndex = suggestionTexts.indexOf(target);
        if (suggestionIndex === -1) {
            throw new Error(`failed to find a suggestion in the DM dialog to invite ${target} with`);
        }
        await suggestions[suggestionIndex].click();
    }

    // press the go button and hope for the best
    const goButton = await session.query('.mx_InviteDialog_goButton');
    await goButton.click();

    await session.query('.mx_MessageComposer');
    session.log.done();

    await measureStop(session, "mx_CreateDM");
}

module.exports = { openRoomDirectory, findSublist, createRoom, createDm };
