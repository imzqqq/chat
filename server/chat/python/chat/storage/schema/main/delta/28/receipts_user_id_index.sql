/** Using CREATE INDEX directly is deprecated in favour of using background
 * update see chat/storage/schema/delta/33/access_tokens_device_index.sql
 * and chat/storage/registration.py for an example using
 * "access_tokens_device_index" **/
CREATE INDEX receipts_linearized_user ON receipts_linearized(
    user_id
);
