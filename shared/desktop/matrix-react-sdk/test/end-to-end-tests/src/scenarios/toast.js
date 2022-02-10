const { assertNoToasts, acceptToast, rejectToast } = require("../usecases/toasts");

module.exports = async function toastScenarios(alice, bob) {
    console.log(" checking and clearing toasts:");

    alice.log.startGroup(`clears toasts`);
    alice.log.step(`reject desktop notifications toast`);
    await rejectToast(alice, "Notifications");
    alice.log.done();

    alice.log.step(`accepts analytics toast`);
    await acceptToast(alice, "Help us improve Chat");
    alice.log.done();

    alice.log.step(`checks no remaining toasts`);
    await assertNoToasts(alice);
    alice.log.done();
    alice.log.endGroup();

    bob.log.startGroup(`clears toasts`);
    bob.log.step(`reject desktop notifications toast`);
    await rejectToast(bob, "Notifications");
    bob.log.done();

    bob.log.step(`reject analytics toast`);
    await rejectToast(bob, "Help us improve Chat");
    bob.log.done();

    bob.log.step(`checks no remaining toasts`);
    await assertNoToasts(bob);
    bob.log.done();
    bob.log.endGroup();
};
