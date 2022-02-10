 /* for some reason, we have accumulated duplicate entries in
  * device_lists_outbound_pokes, which makes prune_outbound_device_list_pokes less
  * efficient.
  */

INSERT INTO background_updates (ordering, update_name, progress_json)
    VALUES (5800, 'remove_dup_outbound_pokes', '{}');
