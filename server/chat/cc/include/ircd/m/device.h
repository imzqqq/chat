// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

#pragma once
#define HAVE_IRCD_M_DEVICE_H

namespace ircd::m
{
	struct device;
	struct device_keys;
	struct device_list_update;
	struct cross_signing_key;
	struct signing_key_update;
}

struct ircd::m::cross_signing_key
:json::tuple
<
	/// Required. The ID of the user the key belongs to.
	json::property<name::user_id, json::string>,

	/// Required. What the key is used for.
	/// One of: ["master", "self_signing", "user_signing"]
	json::property<name::usage, json::string>,

	/// Required. The public key. The object must have exactly one property,
	/// whose name is in the form <algorithm>:<unpadded_base64_public_key>,
	/// and whose value is the unpadded base64 public key.
	json::property<name::keys, json::object>,

	/// Signatures of the key, calculated using the process described at
	/// Signing JSON. Optional for the master key. Other keys must be signed
	/// by the user's master key.
	json::property<name::signatures, json::object>
>
{
	using super_type::tuple;
	using super_type::operator=;
};

/// An EDU that lets servers push details to each other when one of their
/// users updates their cross-signing keys.
struct ircd::m::signing_key_update
:json::tuple
<
	/// Required. The user ID whose cross-signing keys have changed.
	json::property<name::user_id, json::string>,

	/// Cross signing key
	json::property<name::master_key, json::object>,

	/// Cross signing key
	json::property<name::self_signing_key, json::object>
>
{
	using super_type::tuple;
	using super_type::operator=;
};

struct ircd::m::device_keys
:json::tuple
<
	/// Required. The ID of the user the device belongs to. Must match the
	/// user ID used when logging in.
	json::property<name::user_id, json::string>,

	/// Required. The ID of the device these keys belong to. Must match the
	/// device ID used when logging in.
	json::property<name::device_id, json::string>,

	/// Required. The encryption algorithms supported by this device.
	json::property<name::algorithms, json::array>,

	/// Required. Public identity keys. The names of the properties should
	/// be in the format <algorithm>:<device_id>. The keys themselves should
	/// be encoded as specified by the key algorithm.
	json::property<name::keys, json::object>,

	/// Required. Signatures for the device key object. A map from user ID, to
	/// a map from <algorithm>:<device_id> to the signature. The signature is
	/// calculated using the process described at Signing JSON.
	json::property<name::signatures, json::object>,

	/// Additional data added to the device key information by intermediate
	/// servers, and not covered by the signatures.
	json::property<name::unsigned_, json::object>
>
{
	using super_type::tuple;
	using super_type::operator=;
};

struct ircd::m::device_list_update
:json::tuple
<
	/// Required. The user ID who owns this device.
	json::property<name::user_id, json::string>,

	/// Required. The ID of the device whose details are changing.
	json::property<name::device_id, json::string>,

	/// The public human-readable name of this device. Will be absent if
	/// the device has no name.
	json::property<name::device_display_name, json::string>,

	/// Required. An ID sent by the server for this update, unique
	/// for a given user_id. Used to identify any gaps in the sequence
	/// of m.device_list_update EDUs broadcast by a server.
	json::property<name::stream_id, long>,

	/// The stream_ids of any prior m.device_list_update EDUs sent for this
	/// user which have not been referred to already in an EDU's prev_id
	/// field. If the receiving server does not recognise any of the prev_ids,
	/// it means an EDU has been lost and the server should query a snapshot
	/// of the device list via /user/keys/query in order to correctly interpret
	/// future m.device_list_update EDUs. May be missing or empty for the
	/// first EDU in a sequence.
	json::property<name::prev_id, json::array>,

	/// True if the server is announcing that this device has been deleted.
	json::property<name::deleted, bool>,

	/// The updated identity keys (if any) for this device. May be absent
	/// if the device has no E2E keys defined.
	json::property<name::keys, json::object>
>
{
	using super_type::tuple;
	using super_type::operator=;
};

struct ircd::m::device
:json::tuple
<
	///	(c2s / s2s) Required. The device ID.
	json::property<name::device_id, json::string>,

	/// (c2s) Display name set by the user for this device. Absent if no
	/// name has been set.
	json::property<name::display_name, json::string>,

	/// (c2s) The IP address where this device was last seen. (May be a few
	/// minutes out of date, for efficiency reasons).
	json::property<name::last_seen_ip, json::string>,

	/// (c2s) The timestamp (in milliseconds since the unix epoch) when this
	/// devices was last seen. (May be a few minutes out of date, for
	/// efficiency reasons).
	json::property<name::last_seen_ts, time_t>,

	/// (s2s) Required. Identity keys for the device.
	json::property<name::keys, json::object>,

	/// (s2s) Optional display name for the device.
	json::property<name::device_display_name, json::string>
>
{
	using id = m::id::device;

	using super_type::tuple;
	using super_type::operator=;
};
