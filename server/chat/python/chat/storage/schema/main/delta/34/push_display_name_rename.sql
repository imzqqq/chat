DELETE FROM push_rules WHERE rule_id = 'global/override/.m.rule.contains_display_name';
UPDATE push_rules SET rule_id = 'global/override/.m.rule.contains_display_name' WHERE rule_id = 'global/underride/.m.rule.contains_display_name';

DELETE FROM push_rules_enable WHERE rule_id = 'global/override/.m.rule.contains_display_name';
UPDATE push_rules_enable SET rule_id = 'global/override/.m.rule.contains_display_name' WHERE rule_id = 'global/underride/.m.rule.contains_display_name';
