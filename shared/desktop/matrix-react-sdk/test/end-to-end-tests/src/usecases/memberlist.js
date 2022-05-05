const assert = require('assert');
const { openRoomSummaryCard } = require("./rightpanel");

async function openMemberInfo(session, name) {
    const membersAndNames = await getMembersInMemberlist(session);
    const matchingLabel = membersAndNames.filter((m) => {
        return m.displayName === name;
    }).map((m) => m.label)[0];
    await matchingLabel.click();
}

module.exports.openMemberInfo = openMemberInfo;

module.exports.verifyDeviceForUser = async function(session, name, expectedDevice) {
    session.log.step(`verifies e2e device for ${name}`);
    const membersAndNames = await getMembersInMemberlist(session);
    const matchingLabel = membersAndNames.filter((m) => {
        return m.displayName === name;
    }).map((m) => m.label)[0];
    await matchingLabel.click();
    // click verify in member info
    const firstVerifyButton = await session.query(".mx_MemberDeviceInfo_verify");
    await firstVerifyButton.click();
    // expect "Verify device" dialog and click "Begin Verification"
    const dialogHeader = await session.innerText(await session.query(".mx_Dialog .mx_Dialog_title"));
    assert(dialogHeader, "Verify device");
    const beginVerificationButton = await session.query(".mx_Dialog .mx_Dialog_primary");
    await beginVerificationButton.click();
    // get emoji SAS labels
    const sasLabelElements = await session.queryAll(
        ".mx_VerificationShowSas .mx_VerificationShowSas_emojiSas .mx_VerificationShowSas_emojiSas_label");
    const sasLabels = await Promise.all(sasLabelElements.map(e => session.innerText(e)));
    console.log("my sas labels", sasLabels);

    const dialogCodeFields = await session.queryAll(".mx_QuestionDialog code");
    assert.equal(dialogCodeFields.length, 2);
    const deviceId = await session.innerText(dialogCodeFields[0]);
    const deviceKey = await session.innerText(dialogCodeFields[1]);
    assert.equal(expectedDevice.id, deviceId);
    assert.equal(expectedDevice.key, deviceKey);
    const confirmButton = await session.query(".mx_Dialog_primary");
    await confirmButton.click();
    const closeMemberInfo = await session.query(".mx_MemberInfo_cancel");
    await closeMemberInfo.click();
    session.log.done();
};

async function getMembersInMemberlist(session) {
    await openRoomSummaryCard(session);
    const memberPanelButton = await session.query(".mx_RoomSummaryCard_icon_people");
    // We are back at the room summary card
    await memberPanelButton.click();

    const memberNameElements = await session.queryAll(".mx_MemberList .mx_EntityTile_name");
    return Promise.all(memberNameElements.map(async (el) => {
        return { label: el, displayName: await session.innerText(el) };
    }));
}

module.exports.getMembersInMemberlist = getMembersInMemberlist;
