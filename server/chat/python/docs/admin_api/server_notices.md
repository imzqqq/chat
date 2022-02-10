# Server Notices

The API to send notices is as follows:

```bash
POST /_chat/admin/v1/send_server_notice
```

or:

```bash
PUT /_chat/admin/v1/send_server_notice/{txnId}
```

You will need to authenticate with an access token for an admin user.

When using the `PUT` form, retransmissions with the same transaction ID will be
ignored in the same way as with `PUT
/chat/client/r0/rooms/{roomId}/send/{eventType}/{txnId}`.

The request body should look something like the following:

```json
{
    "user_id": "@target_user:server_name",
    "content": {
        "msgtype": "m.text",
        "body": "This is my message"
    }
}
```

You can optionally include the following additional parameters:

* `type`: the type of event. Defaults to `m.room.message`.
* `state_key`: Setting this will result in a state event being sent.

Once the notice has been sent, the API will return the following response:

```json
{
    "event_id": "<event_id>"
}
```

Note that server notices must be enabled in `homeserver.yaml` before this API
can be used. See [the server notices documentation](../server_notices.md) for more information.
