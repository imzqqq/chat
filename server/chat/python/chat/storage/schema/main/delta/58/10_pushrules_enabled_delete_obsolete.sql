/**
  Delete stuck 'enabled' bits that correspond to deleted or non-existent push rules.
  We ignore rules that are server-default rules because they are not defined
  in the `push_rules` table.
**/

DELETE FROM push_rules_enable WHERE
  rule_id NOT LIKE 'global/%/.m.rule.%'
  AND NOT EXISTS (
    SELECT 1 FROM push_rules
    WHERE push_rules.user_name = push_rules_enable.user_name
      AND push_rules.rule_id = push_rules_enable.rule_id
  );
