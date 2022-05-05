async function openSpaceCreateMenu(session) {
    const spaceCreateButton = await session.query('.mx_SpaceButton_new');
    await spaceCreateButton.click();
}

async function createSpace(session, name, isPublic = false) {
    session.log.step(`creates space "${name}"`);

    await openSpaceCreateMenu(session);
    const className = isPublic ? ".mx_SpaceCreateMenuType_public" : ".mx_SpaceCreateMenuType_private";
    const visibilityButton = await session.query(className);
    await visibilityButton.click();

    const nameInput = await session.query('input[name="spaceName"]');
    await session.replaceInputText(nameInput, name);

    await session.delay(100);

    const createButton = await session.query('.mx_SpaceCreateMenu_wrapper .mx_AccessibleButton_kind_primary');
    await createButton.click();

    if (!isPublic) {
        const justMeButton = await session.query('.mx_SpaceRoomView_privateScope_justMeButton');
        await justMeButton.click();
        const continueButton = await session.query('.mx_AddExistingToSpace_footer .mx_AccessibleButton_kind_primary');
        await continueButton.click();
    } else {
        for (let i = 0; i < 2; i++) {
            const continueButton = await session.query('.mx_SpaceRoomView_buttons .mx_AccessibleButton_kind_primary');
            await continueButton.click();
        }
    }

    session.log.done();
}

async function inviteSpace(session, spaceName, userId) {
    session.log.step(`invites "${userId}" to space "${spaceName}"`);

    const spaceButton = await session.query(`.mx_SpaceButton[aria-label="${spaceName}"]`);
    await spaceButton.click({
        button: 'right',
    });

    const inviteButton = await session.query('[aria-label="Invite people"]');
    await inviteButton.click();

    try {
        const button = await session.query('.mx_SpacePublicShare_inviteButton');
        await button.click();
    } catch (e) {
        // ignore
    }

    const inviteTextArea = await session.query(".mx_InviteDialog_editor input");
    await inviteTextArea.type(userId);
    const selectUserItem = await session.query(".mx_InviteDialog_roomTile");
    await selectUserItem.click();
    const confirmButton = await session.query(".mx_InviteDialog_goButton");
    await confirmButton.click();
    session.log.done();
}

module.exports = { openSpaceCreateMenu, createSpace, inviteSpace };
