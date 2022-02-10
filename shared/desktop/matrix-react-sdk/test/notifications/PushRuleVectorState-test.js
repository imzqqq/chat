const notifications = require('../../src/notifications');

const prvs = notifications.PushRuleVectorState;

describe("PushRuleVectorState", function() {
    describe("contentRuleVectorStateKind", function() {
        it("should understand normal notifications", function() {
            const rule = {
                actions: [
                    "notify",
                ],
            };

            expect(prvs.contentRuleVectorStateKind(rule)).
                toEqual(prvs.ON);
        });

        it("should handle loud notifications", function() {
            const rule = {
                actions: [
                    "notify",
                    { set_tweak: "highlight", value: true },
                    { set_tweak: "sound", value: "default" },
                ],
            };

            expect(prvs.contentRuleVectorStateKind(rule)).
                toEqual(prvs.LOUD);
        });

        it("should understand missing highlight.value", function() {
            const rule = {
                actions: [
                    "notify",
                    { set_tweak: "highlight" },
                    { set_tweak: "sound", value: "default" },
                ],
            };

            expect(prvs.contentRuleVectorStateKind(rule)).
                toEqual(prvs.LOUD);
        });
    });
});
