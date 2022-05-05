module.exports = async function invite(session, userId) {
    session.log.step(`invites "${userId}" to room`);
    await session.delay(1000);
    const memberPanelButton = await session.query(".mx_RightPanel_membersButton");
    try {
        await session.query(".mx_RightPanel_headerButton_highlight", 500);
        // Right panel is open - toggle it to ensure it's the member list
        // Sometimes our tests have this opened to MemberInfo
        await memberPanelButton.click();
        await memberPanelButton.click();
    } catch (e) {
        // Member list is closed - open it
        await memberPanelButton.click();
    }
    const inviteButton = await session.query(".mx_MemberList_invite");
    await inviteButton.click();
    const inviteTextArea = await session.query(".mx_InviteDialog_editor input");
    await inviteTextArea.type(userId);
    const selectUserItem = await session.query(".mx_InviteDialog_roomTile");
    await selectUserItem.click();
    const confirmButton = await session.query(".mx_InviteDialog_goButton");
    await confirmButton.click();
    session.log.done();
};
