const { createSpace, inviteSpace } = require("../usecases/create-space");

module.exports = async function spacesScenarios(alice, bob) {
    console.log(" creating a space for spaces scenarios:");

    await alice.delay(1000); // wait for dialogs to close
    await setupSpaceUsingAliceAndInviteBob(alice, bob);
};

const space = "Test Space";

async function setupSpaceUsingAliceAndInviteBob(alice, bob) {
    await createSpace(alice, space);
    await inviteSpace(alice, space, "@bob:localhost");
    await bob.query(`.mx_SpaceButton[aria-label="${space}"]`); // assert invite received
}
