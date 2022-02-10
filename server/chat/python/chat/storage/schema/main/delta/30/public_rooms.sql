/* This release removes the restriction that published rooms must have an alias,
 * so we go back and ensure the only 'public' rooms are ones with an alias.
 * We use (1 = 0) and (1 = 1) so that it works in both postgres and sqlite
 */
UPDATE rooms SET is_public = (1 = 0) WHERE is_public = (1 = 1) AND room_id not in (
    SELECT room_id FROM room_aliases
);
