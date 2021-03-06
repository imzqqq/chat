---
title: Admin::Account
description: Admin-level information about a given account.
menu:
  docs:
    parent: entities
---

## Example

```javascript
{}
```

## Attributes

### `id` {#id}

**Description:** The ID of the account in the database.\
**Type:** String \(cast from an integer, but not guaranteed to be a number\)\
**Version history:** Added in 2.9.1

### `username` {#username}

**Description:** The username of the account.\
**Type:** String\
**Version history:** Added in 2.9.1

### `domain` {#domain}

**Description:** The domain of the account.\
**Type:** String\
**Version history:** Added in 2.9.1

### `created_at` {#created_at}

**Description:** When the account was first discovered.\
**Type:** String \(ISO 8601 Datetime\)\
**Version history:** Added in 2.9.1

### `email` {#email}

**Description:** The email address associated with the account.\
**Type:** String\
**Version history:** Added in 2.9.1

### `ip` {#ip}

**Description:** The IP address last used to login to this account.\
**Type:** String\
**Version history:** Added in 2.9.1

### `locale` {#locale}

**Description:** The locale of the account.\
**Type:** String \(ISO 639 Part 1 two-letter language code\)\
**Version history:** Added in 2.9.1

### `invite_request` {#invite_request}

**Description:** Invite request text ???\
**Type:** String\
**Version history:** Added in 2.9.1

## State attributes

### `role` {#role}

**Description:** The current role of the account.\
**Type:** String \(Enumerable oneOf\)\
**Version history:** Added in 2.9.1

### `confirmed` {#confirmed}

**Description:** Whether the account has confirmed their email address.\
**Type:** Boolean\
**Version history:** Added in 2.9.1

### `approved` {#approved}

**Description:** Whether the account is currently approved.\
**Type:** Boolean\
**Version history:** Added in 2.9.1

### `disabled` {#disabled}

**Description:** Whether the account is currently disabled.\
**Type:** Boolean\
**Version history:** Added in 2.9.1

### `silenced` {#silenced}

**Description:** Whether the account is currently silenced.
**Type:** Boolean\
**Version history:** Added in 2.9.1

### `suspended` {#suspended}

**Description:** Whether the account is currently suspended.\
**Type:** Boolean\
**Version history:** Added in 2.9.1

### `account` {#account}

**Description:** User-level information about the account.\
**Type:** Account\
**Version history:** Added in 2.9.1

## Nullable attributes

### `created_by_application_id` {#created_by_application_id}

**Description:** The ID of the application that created this account.\
**Type:** String \(cast from an integer, but not guaranteed to be a number\)\
**Version history:** Added in 2.9.1

### `invited_by_account_id` {#invited_by_account_id}

**Description:** The ID of the account that invited this user\
**Type:** String \(cast from an integer, but not guaranteed to be a number\)\
**Version history:** Added in 2.9.1

## See also

{{< page-ref page="methods/admin.md" >}}

{{< caption-link url="https://github.com/tootsuite/flow/blob/master/app/serializers/rest/admin/account_serializer.rb" caption="app/serializers/rest/admin/account\_serializer.rb" >}}



