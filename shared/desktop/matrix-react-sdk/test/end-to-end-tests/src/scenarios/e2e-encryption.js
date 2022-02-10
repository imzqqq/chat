const sendMessage = require('../usecases/send-message');
const acceptInvite = require('../usecases/accept-invite');
const { receiveMessage } = require('../usecases/timeline');
const { createDm } = require('../usecases/create-room');
const { checkRoomSettings } = require('../usecases/room-settings');
const { startSasVerification, acceptSasVerification } = require('../usecases/verify');
const { setupSecureBackup } = require('../usecases/security');
const assert = require('assert');
const { measureStart, measureStop } = require('../util');

module.exports = async function e2eEncryptionScenarios(alice, bob) {
    console.log(" creating an e2e encrypted DM and join through invite:");
    await createDm(bob, ['@alice:localhost']);
    await checkRoomSettings(bob, { encryption: true }); // for sanity, should be e2e-by-default
    await acceptInvite(alice, 'bob');
    // do sas verifcation
    bob.log.step(`starts SAS verification with ${alice.username}`);
    await measureStart(bob, "mx_VerifyE2EEUser");
    const bobSasPromise = startSasVerification(bob, alice.username);
    const aliceSasPromise = acceptSasVerification(alice, bob.username);
    // wait in parallel, so they don't deadlock on each other
    // the logs get a bit messy here, but that's fine enough for debugging (hopefully)
    const [bobSas, aliceSas] = await Promise.all([bobSasPromise, aliceSasPromise]);
    assert.deepEqual(bobSas, aliceSas);
    await measureStop(bob, "mx_VerifyE2EEUser");
    bob.log.done(`done (match for ${bobSas.join(", ")})`);
    const aliceMessage = "Guess what I just heard?!";
    await sendMessage(alice, aliceMessage);
    await receiveMessage(bob, { sender: "alice", body: aliceMessage, encrypted: true });
    const bobMessage = "You've got to tell me!";
    await sendMessage(bob, bobMessage);
    await receiveMessage(alice, { sender: "bob", body: bobMessage, encrypted: true });
    await setupSecureBackup(alice);
};
