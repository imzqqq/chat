const { openRoomDirectory } = require('./create-room');
const { measureStart, measureStop } = require('../util');

module.exports = async function join(session, roomName) {
    session.log.step(`joins room "${roomName}"`);
    await measureStart(session, "mx_JoinRoom");
    await openRoomDirectory(session);
    const roomInput = await session.query('.mx_DirectorySearchBox input');
    await session.replaceInputText(roomInput, roomName);

    const joinFirstLink = await session.query('.mx_RoomDirectory_table .mx_RoomDirectory_join .mx_AccessibleButton');
    await joinFirstLink.click();
    await session.query('.mx_MessageComposer');
    await measureStop(session, "mx_JoinRoom");
    session.log.done();
};
