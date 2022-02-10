const { findSublist } = require("./create-room");

module.exports = async function acceptInvite(session, name) {
    session.log.step(`accepts "${name}" invite`);
    const inviteSublist = await findSublist(session, "invites");
    const invitesHandles = await inviteSublist.$$(".mx_RoomTile_name");
    const invitesWithText = await Promise.all(invitesHandles.map(async (inviteHandle) => {
        const text = await session.innerText(inviteHandle);
        return { inviteHandle, text };
    }));
    const inviteHandle = invitesWithText.find(({ inviteHandle, text }) => {
        return text.trim() === name;
    }).inviteHandle;

    await inviteHandle.click();

    const acceptInvitationLink = await session.query(".mx_RoomPreviewBar_Invite .mx_AccessibleButton_kind_primary");
    await acceptInvitationLink.click();

    session.log.done();
};
