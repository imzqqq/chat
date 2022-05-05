const join = require('../usecases/join');
const sendMessage = require('../usecases/send-message');
const { receiveMessage } = require('../usecases/timeline');
const { createRoom } = require('../usecases/create-room');
const { changeRoomSettings } = require('../usecases/room-settings');

module.exports = async function roomDirectoryScenarios(alice, bob) {
    console.log(" creating a public room and join through directory:");
    const room = 'test';
    await createRoom(alice, room);
    await changeRoomSettings(alice, { directory: true, visibility: "public", alias: "#test" });
    await join(bob, room); //looks up room in directory
    const bobMessage = "hi Alice!";
    await sendMessage(bob, bobMessage);
    await receiveMessage(alice, { sender: "bob", body: bobMessage });
    const aliceMessage = "hi Bob, welcome!";
    await sendMessage(alice, aliceMessage);
    await receiveMessage(bob, { sender: "alice", body: aliceMessage });
};
