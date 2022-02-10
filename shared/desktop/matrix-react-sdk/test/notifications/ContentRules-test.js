const notifications = require('../../src/notifications');

const ContentRules = notifications.ContentRules;
const PushRuleVectorState = notifications.PushRuleVectorState;

const NORMAL_RULE = {
    actions: [
        "notify",
        { set_tweak: "highlight", value: false },
    ],
    enabled: true,
    pattern: "vdh2",
    rule_id: "vdh2",
};

const LOUD_RULE = {
    actions: [
        "notify",
        { set_tweak: "highlight" },
        { set_tweak: "sound", value: "default" },
    ],
    enabled: true,
    pattern: "vdh2",
    rule_id: "vdh2",
};

const USERNAME_RULE = {
    actions: [
        "notify",
        { set_tweak: "sound", value: "default" },
        { set_tweak: "highlight" },
    ],
    default: true,
    enabled: true,
    pattern: "richvdh",
    rule_id: ".m.rule.contains_user_name",
};

describe("ContentRules", function() {
    describe("parseContentRules", function() {
        it("should handle there being no keyword rules", function() {
            const rules = { 'global': { 'content': [
                USERNAME_RULE,
            ] } };
            const parsed = ContentRules.parseContentRules(rules);
            expect(parsed.rules).toEqual([]);
            expect(parsed.vectorState).toEqual(PushRuleVectorState.ON);
            expect(parsed.externalRules).toEqual([]);
        });

        it("should parse regular keyword notifications", function() {
            const rules = { 'global': { 'content': [
                NORMAL_RULE,
                USERNAME_RULE,
            ] } };

            const parsed = ContentRules.parseContentRules(rules);
            expect(parsed.rules.length).toEqual(1);
            expect(parsed.rules[0]).toEqual(NORMAL_RULE);
            expect(parsed.vectorState).toEqual(PushRuleVectorState.ON);
            expect(parsed.externalRules).toEqual([]);
        });

        it("should parse loud keyword notifications", function() {
            const rules = { 'global': { 'content': [
                LOUD_RULE,
                USERNAME_RULE,
            ] } };

            const parsed = ContentRules.parseContentRules(rules);
            expect(parsed.rules.length).toEqual(1);
            expect(parsed.rules[0]).toEqual(LOUD_RULE);
            expect(parsed.vectorState).toEqual(PushRuleVectorState.LOUD);
            expect(parsed.externalRules).toEqual([]);
        });

        it("should parse mixed keyword notifications", function() {
            const rules = { 'global': { 'content': [
                LOUD_RULE,
                NORMAL_RULE,
                USERNAME_RULE,
            ] } };

            const parsed = ContentRules.parseContentRules(rules);
            expect(parsed.rules.length).toEqual(1);
            expect(parsed.rules[0]).toEqual(LOUD_RULE);
            expect(parsed.vectorState).toEqual(PushRuleVectorState.LOUD);
            expect(parsed.externalRules.length).toEqual(1);
            expect(parsed.externalRules[0]).toEqual(NORMAL_RULE);
        });
    });
});
