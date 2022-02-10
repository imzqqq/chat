/* delete room keys that belong to deleted room key version, or to room key
 * versions that don't exist (anymore)
 */
DELETE FROM e2e_room_keys
WHERE version NOT IN (
  SELECT version
  FROM e2e_room_keys_versions
  WHERE e2e_room_keys.user_id = e2e_room_keys_versions.user_id
  AND e2e_room_keys_versions.deleted = 0
);
