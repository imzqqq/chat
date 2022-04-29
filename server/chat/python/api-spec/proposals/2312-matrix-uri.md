# URI scheme for Chat

This is a proposal of a URI scheme to identify Chat resources in a wide
range of applications (web, desktop, or mobile) both throughout Chat software
and (especially) outside it. It supersedes
[MSC455](https://github.com/matrix-org/matrix-doc/issues/455) in order
to continue the discussion in the modern GFM style.

While Chat has its own resource naming system that allows it to identify
resources without resolving them, there is a common need to provide URIs
to Chat resources (e.g., rooms, users, PDUs) that could be transferred
outside of Chat and then resolved in a uniform way - matching URLs
in World Wide Web.

Specific use cases include:
1. Representation: as a Chat user I want to refer to Chat entities
   in the same way as for web pages, so that others could unambiguously identify
   the resource, regardless of the context or used medium to identify it to them
   (within or outside Chat, e.g., in a web page or an email message).
1. Inbound integration: as an author of Chat software, I want to have a way
   to invoke my software from the operating environment to resolve a Chat URI
   passed from another program. This is a case of, e.g.,
   opening a Chat client by clicking on a link from an email message.
1. Outbound integration: as an author of Chat software, I want to have a way
   to export identifiers of Chat resources to non-Chat environment
   so that they could be resolved in another time-place in a uniform way.
   An example of this case is the "Share via…" action in a mobile Chat client.

Chat identifiers as defined by the current specification have a form distinct
enough from other identifiers to mostly fulfil the representation use case.
Since they are not URIs, they can not cover the two integration use cases.
https://to.chat.imzqqq.top somehow compensates for this; however:
* it requires a web browser to run JavaScript code that resolves identifiers
  (basically limiting first-class support to browser-based clients), and
* it relies on to.chat.imzqqq.top as an intermediary that provides that JavaScript code.

To cover the use cases above, the following scheme is proposed for Chat URIs
(`[]` enclose optional parts, `{}` enclose variables):
```text
matrix:[//{authority}/]{type}/{id without sigil}[/{type}/{id without sigil}...][?{query}][#{fragment}]
```
with `{type}` defining the resource type (such as `r`, `u` or `roomid` - see
the "Path" section in the proposal) and `{query}` containing additional hints
or request details on the Chat entity (see "Query" in the proposal).
`{authority}` and `{fragment}` parts are reserved for future use; this proposal
does not define them and implementations SHOULD ignore them for now.

This MSC does not introduce new Chat entities, nor API endpoints -
it merely defines a mapping between URIs with the scheme name `matrix:`
and Chat identifiers, as well as operations on them. The MSC should be
sufficient to produce an implementation that would convert Chat URIs to
a series of [CS API](https://chat.api-spec.imzqqq.top/client_server/r0.6.1) calls,
entirely on the client side. It is recognised, however, that most of
the URI processing logic can and should (eventually) be on the server side
in order to facilitate adoption of Chat URIs; further MSCs are needed
to define details for that, as well as to extend the mapping to more resources
(including those without equivalent Chat identifiers, such as room state or
user profile data).

The Chat identifier (or identifiers) can be reconstructed from
`{id without sigil}` by prepending a sigil character corresponding to `{type}`.
To support a hierarchy of Chat resources, more `/{type}/{id without sigil}`
pairs can be appended, identifying resources within other resources.
As of now, there's only one such case, with exactly one additional pair -
pointing to an event in a room.

Examples:
* Room `#someroom:example.org`:
  `matrix:r/someroom:example.org`
* User `@me:example.org`:
  `matrix:u/me:example.org`
* Event in a room:
  `matrix:r/someroom:example.org/e/Arbitrary_Event_Id`
* [A commit like this](https://github.com/her001/steamlug.org/commit/2bd69441e1cf21f626e699f0957193f45a1d560f)
  could make use of a Chat URI in the form of
  `<a href="{Chat URI}">{Chat identifier}</a>`.


## Proposal

### Definitions

Further text uses the following terms:
- Chat identifier - one of identifiers defined by the current
[Chat Specification](https://chat.api-spec.imzqqq.top/appendices.html#identifier-grammar),
- Chat URI - a uniform resource identifier proposed hereby, following
  the RFC-compliant URI format.
- MUST/SHOULD/MAY etc. follow the conventions of
  [RFC 2119](https://www.ietf.org/rfc/rfc2119.txt).


### Requirements

The following considerations drive the requirements for Chat URIs:
1. Follow existing standards and practices.
1. Endorse the principle of the least surprise.
1. Humans first, machines second.
1. Cover as many entities as practical.
1. URIs are expected to be extremely portable and stable;
   you cannot rewrite them once they are released to the world.
1. Ease of implementation, allowing reuse of existing codes.

The following requirements resulted from these drivers:
1. Chat URI MUST comply with
   [RFC 3986](https://tools.ietf.org/html/rfc3986) and
   [RFC 7595](https://tools.ietf.org/html/rfc7595).
1. By definition, Chat URI MUST unambiguously identify a resource
   in a Chat network, across servers and types of resources.
   This means, in particular, that two Chat identifiers distinct by
   [Chat Specification](https://chat.api-spec.imzqqq.top/appendices.html#identifier-grammar)
   MUST NOT have Chat URIs that are equal in
   [RFC 3986](https://tools.ietf.org/html/rfc3986) sense
   (but two distinct Chat URIs MAY map to the same Chat identifier).
1. References to the following entities MUST be supported:
   1. User IDs (`@user:example.org`)
   1. Room IDs (`!roomid:example.org`)
   1. Room aliases (`#roomalias:example.org`)
   1. Event IDs (`$arbitrary_eventid_with_or_without_serverpart`)
1. The mapping MUST take into account that some identifiers
   (e.g. aliases) can have non-ASCII characters - reusing
   [RFC 3987](https://tools.ietf.org/html/rfc3987) is RECOMMENDED,
   but an alternative encoding can be used if there are reasons for that.
1. The mapping between Chat identifiers and Chat URIs MUST
   be extensible (without invalidating previous URIs) to:
   1. new classes of identifiers (there MUST be a meta-rule to produce
      a new mapping for IDs following the `&somethingnew:example.org`
      pattern assumed for Chat identifiers);
   1. new ways to navigate to and interact with objects in Chat
      (e.g., we might eventually want to have a mapping for
      room-specific user profiles).
1. The mapping MUST support decentralised as well as centralised IDs.
   This basically means that the URI scheme MUST have provisions
   for mapping of identifiers with `:<serverpart>` but it MUST NOT require
   `:<serverpart>` to be there.
1. Chat URI SHOULD allow encoding of action requests such as joining a room.
1. Chat URI SHOULD have a human-readable, if not necessarily
   human-friendly, representation - to allow visual sanity-checks.
   In particular, characters escaping/encoding should be reduced
   to bare minimum in that representation. As food for thought, see
   [Wikipedia: Clean URL, aka SEF URL](https://en.wikipedia.org/wiki/Clean_URL) and
   [a use case from RFC 3986](https://tools.ietf.org/html/rfc3986#section-1.2.1).
1. It SHOULD be easy to parse Chat URI in popular programming
   languages: e.g., one should be able to use `parseUri()`
   to dissect a Chat URI into components in JavaScript.
1. The mapping SHOULD be consistent across different classes of
   Chat identifiers.
1. The mapping SHOULD support linking to unfederated servers/networks
   (see also
   [matrix-doc#2309](https://github.com/matrix-org/matrix-doc/issues/2309)
   that calls for such linking).

The syntax and mapping discussed below meet all these requirements except
the last one that will be addressed separately.
Further extensions MUST NOT reduce the supported set of requirements.


### Syntax and high-level processing

The proposed generic Chat URI syntax is a subset of the generic
URI syntax
[defined by RFC 3986](https://tools.ietf.org/html/rfc3986#section-3):
```text
MatrixURI = "matrix:" hier-part [ "?" query ] [ "#" fragment ]
hier-part = [ "//" authority "/" ] path
```
As mentioned above, this MSC assumes client-side URI processing
(i.e. mapping to Chat identifiers and CS API requests).
However, even when URI processing is shifted to the server side
the client will still have to parse the URI at least to remove
the authority and fragment parts (if either exists)
before sending the request to the server (more on that below).

#### Scheme name
The proposed scheme name is `matrix`.
[RFC 7595](https://tools.ietf.org/html/rfc7595) states:

    if there’s one-to-one correspondence between a service name and
    a scheme name then the scheme name should be the same as
    the service name.

Other considered options were `mx` and `web+matrix`;
[comments to MSC455](https://github.com/matrix-org/matrix-doc/issues/455)
mention two scheme names proposed and one more has been mentioned
in `#matrix-core:chat.imzqqq.top`.

The scheme name is a definitive indication of a Chat URI and MUST NOT
be omitted. As can be seen below, Chat URI rely heavily on [relative
references](https://tools.ietf.org/html/rfc3986#section-4.2) and
omitting the scheme name makes them indistinguishable from a local path
that might have nothing to do with Chat. Clients MUST NOT try to
parse pieces like `r/MyRoom:example.org` as Chat URIs; instead,
users should be encouraged to use Chat identifiers for in-text references
(`#MyRoom:example.org`) and client applications SHOULD turn them into
hyperlinks to Chat URIs.

#### Authority

Basing on
[the definition in RFC 3986](https://tools.ietf.org/html/rfc3986#section-3.2),
this MSC restricts the authority part to never have a userinfo component,
partially to prevent confusion concerned with the `@` character that has its
own meaning in Chat, but also because this component has historically been
a popular target of abuse.
```text
authority = host [ ":" port ]
```
Further definition of syntax or semantics for the authority part is left for
future MSCs. Clients MUST parse the authority part as per RFC 3986 (i.e.
the presence of an authority part MUST NOT break URI parsing) but SHOULD NOT
use data from the authority part other than for experiments or research.

The authority part may eventually be used to indicate access to a Chat
resource (such as a room or a user) specifically through a given entity.
See "Ideas for further evolution".

#### Path
This MSC restricts
[the very wide definition of path in RFC 3986](https://tools.ietf.org/html/rfc3986#section-3.3),
to a simple pattern that allows to easily reconstruct a Chat identifier or
a chain of identifiers and also to locate a certain sub-resource in the scope
of a given Chat entity:
```text
path = entity-descriptor ["/" entity-descriptor]
entity-descriptor = nonid-segment / type-qualifier id-without-sigil
nonid-segment = segment-nz ; as defined in RFC 3986, see also below
type-qualifier = segment-nz "/" ; as defined in RFC 3986, see also below
id-without-sigil = string ; as defined in Chat identifier spec, see below
```
The path component consists of 1 or more descriptors separated by a slash
(`/`) character. This is a generic pattern intended for reusing in future
extensions.

This MSC only proposes mappings along `type-qualifier id-without-sigil` syntax;
`nonid-segment` is unused and reserved for future use.
For the sake of integrity future `nonid-segment` extensions must follow
[the ABNF for `segment-nz` as defined in RFC 3986](https://tools.ietf.org/html/rfc3986#appendix-A).

This MSC defines the following `type` specifiers: `u` (user id, sigil `@`),
`r` (room alias, sigil `#`), `roomid` (room id, sigil `!`),  and
`e` (event id, sigil `$`). This MSC does not define a type specifier for sigil `+`
([groups](https://github.com/matrix-org/matrix-doc/issues/1513) aka communities
or, in the more recent incarnation,
[spaces](https://github.com/matrix-org/matrix-doc/pull/1772)); a separate MSC
can introduce the specifier, along with the parsing/construction logic and
relevant CS API invocations, following the framework of this proposal.

The following type specifiers proposed in earlier editions of this MSC and
already in use in several implementations, are deprecated: `user`, `room`, and
`event`. Client applications MAY parse these specifiers as if they were
`u`, `r`, and `e` respectively; they MUST NOT emit URIs with the deprecated
specifiers. The rationale behind the switch is laid out in "Alternatives".

As of this MSC, `u`, `r`, and `roomid` can only be at the top
level. The type `e` (event) can only be used on the 2nd level and only under
`r` or `roomid`; this is driven by the current shape of Client-Server API
that does not provide a non-deprecated way to retrieve an event without knowing
the room (see [MSC2695](https://github.com/matrix-org/matrix-doc/pull/2695) and
[MSC2779](https://github.com/matrix-org/matrix-doc/issues/2779) that may
change this).

Further MSCs may introduce navigation to more top-level as well as
non-top-level objects; see "Ideas for further evolution" to get inspired. These
new proposals SHOULD follow the generic grammar laid out above, adding new
`type` and `nonid-segment` specifiers and/or allowing them in other levels,
rather than introduce a new grammar. It is recommended to only use abbreviated
single-letter specifiers if they are expected to be user visible and convenient
for type-in; if a URI for a given resource type is usually generated
(e.g. because the corresponding identifier is not human-friendly), it's
RECOMMENDED to use full (though short) words to avoid ambiguity and confusion.

`id-without-sigil` is defined as the `string` part of Chat
[Common identifier format](https://chat.api-spec.imzqqq.top/appendices#common-identifier-format)
with percent-encoded characters that are NEITHER unreserved, sub-delimiters, `:` nor `@`,
[as per RFC 3986 rule for pchar](https://tools.ietf.org/html/rfc3986#appendix-A).
This notably exempts `:` from percent-encoding but includes `/`.

See the rationale behind dropping sigils and the respective up/downsides in
"Discussion points and tradeoffs" as well as "Alternatives" below.

#### Query

Chat URI can optionally have
[the query part](https://tools.ietf.org/html/rfc3986#section-3.4).
This MSC defines the general form for the query and two "standard" query items;
further MSCs may add to this as long as RFC 3986 is followed.
```text
query = query-element *( "&" query-item )
query-item = action / routing / custom-query-item
action = "action=" ( "join" / "chat" )
routing = "via=” authority
custom-query-item = custom-item-name "=" custom-item-value
custom-item-name = 1*unreserved ; reverse-DNS name; see below
custom-item-value = ; see below
```

The `action` query item is used in contexts where, on top of identifying
the Chat entity, a certain action is requested on it. This proposal
describes two possible actions:
* `action=join` is only valid in a URI resolving to a Chat room;
  applications MUST ignore it if found in other contexts and MUST NOT generate
  it for other Chat resources. This action means that a client application
  SHOULD attempt to join the room specified by the URI path using the standard
  CS API means.
* `action=chat` is only valid in a URI resolving to a Chat user;
  applications MUST ignore it if found in other contexts and MUST NOT generate
  it for other Chat resources. This action means that a client application
  SHOULD open a direct chat window with the user specified by the URI path;
  clients supporting
  [canonical direct chats](https://github.com/matrix-org/matrix-doc/pull/2199)
  SHOULD open the canonical direct chat.

For both actions, where applicable, client applications SHOULD ask for user
confirmation or at least notify the user before joining or creating a new room.
Conversely, no additional confirmation/notification is necessary when
the action leads to opening a room the user is already a member of.

It is worth reiterating on the (blurry) distinction between URIs with `action`
and those without:
- a URI with no `action` simply _identifies_ the resource; if the context
  implies an operation, it is usually focused on the retrieval of the resource,
  in line with RFC 3986 (see also the next paragraph);
- a URI with `action` in the query means that a client application should (but
  is not obliged to) perform that action, with precautions as described above.

In some cases a client application may have no meaningful way to immediately
perform the default operation suggested by this MSC (see below); e.g.,
the client may be unable to display a room before joining it, while the URI
doesn't have `action=join`. In these cases client applications are free to do
what's best for user experience (e.g., suggest joining the room), even if that
means performing an action on a URI with no `action` in the query.

The routing query (`via=`) indicates servers that are likely involved in
the room (see also
[the feature of to.chat.imzqqq.top](https://chat.api-spec.imzqqq.top/appendices#routing)).
In the meantime, it is proposed that this routing query be used not only with
room ids in a public federation but also when a URI refers to a resource in
a non-public Chat network (see the question about closed federations in
"Discussion points and tradeoffs"). Note that `authority` in the definition
above is only a part of the _query parameter_ grammar; it is not proposed here
to generate or interpret the _authority part_ of the URI.

Clients MAY introduce and recognise custom query items, according to
the following rules:
- the name of a custom item MUST follow the reverse-DNS (aka "Java package")
  naming convention, as per
  [MSC2758](https://github.com/matrix-org/matrix-doc/pull/2758) - e.g.,
  a custom action item for Element clients would be named `io.element.action`,
  for Quaternion - `com.github.quaternion.action`, etc.
- the value of the item can be any content but its representation in the URI
  MUST follow the general RFC requirements for the query part; on top of that,
  if the raw value contains `&` it MUST be percent-encoded.
- clients SHOULD respect standard query items over their own ones; e.g.,
  if a URI contains both `action` and the custom client action, the standard
  action should be respected as much as possible. Client authors SHOULD strive
  for consistent experience across their and 3rd party clients, anticipating
  that the same user may happen to have both their client and a 3rd party one.

Client authors are strongly encouraged to standardise custom query elements
that gain adoption by submitting an MSC defining them in a way compatible
across the client ecosystem.


### Recommended implementation

#### URI parsing algorithm

The reference algorithm of parsing a Chat URI follows. Note that, although
clients are encouraged to use lower-case strings in their URIs, all string
comparisons are case-INsensitive.

1. Parse the URI into main components (`scheme name`, `authority`, `path`,
   `query`, and `fragment`), decoding special or international characters
   as directed by [RFC 3986](https://tools.ietf.org/html/rfc3986) and
   (for IRIs) [RFC 3987](https://tools.ietf.org/html/rfc3987). Authors are
   strongly RECOMMENDED that they find an existing implementation of that step
   for their language and SDK, rather than implement it from scratch based
   on RFCs.

1. Check that `scheme name` is exactly `matrix`, case-insensitive. If
   the scheme name doesn't match, exit parsing: this is not a Chat URI.

1. Split the `path` into segments separated by `/` character; several
   subsequent `/` characters delimit empty segments, as advised by RFC 3986.

1. Check that the URI contains either 2 or 4 segments; if it's not the case,
   fail parsing; the Chat URI is invalid.

1. To construct the top-level (primary) Chat identifier:

   a. Pick the leftmost segment of `path` until `/` (path segment) and match
      it against the following list to produce `sigil-1`:
      - `u` (or, optionally, `user` - see "Path") -> `@`
      - `r` (or, optionally, `room`) -> `#`
      - `roomid` -> `!`
      - any other string, including an empty one -> fail parsing:
        the Chat URI is invalid.

   b. Pick the next (2nd) leftmost path segment:
      - if the segment is empty, fail parsing;
      - otherwise, percent-decode the segment (unless the initial URI parse
        has already done that) and make `mxid-1` by prepending `sigil-1`.

1. If `sigil-1` is `!` or `#` and the URI path has exactly 4 segments,
   it may be possible to construct the 2nd-level Chat identifier to
   point to an event inside the room identified by `mxid-1`:

   a. Pick the next (3rd) path segment:
      - if the segment is exactly `e` (or, optionally, `event`), proceed;
      - otherwise, including the case of an empty segment (trailing `/`, e.g.),
        fail parsing.

   b. Pick the next (4th) leftmost path segment:
      - if the segment is empty, fail parsing;
      - otherwise, percent-decode the segment (unless the initial URI parse
        has already done that) and make `mxid-2` by prepending `$`.

1. Split the `query` into items separated by `&` character; several subsequent
   `&` characters delimit empty items, ignored by this algorithm.

   a. If `query` contains one or more items starting with `via=`: for each item, treat
      the rest of the item as a percent-encoded homeserver name to be used in
      [routing](https://chat.api-spec.imzqqq.top/appendices#routing).

   b. If `query` contains one or more items starting with `action=`: treat
      _the last_ such item as an instruction, as this proposal defines in [query](#query).

Clients MUST implement proper percent-decoding of the identifiers; there's no
liberty similar to that of to.chat.imzqqq.top.

#### Operations on Chat URIs

The main purpose of a Chat URI is accessing the resource specified by the
identifier. This MSC defines the "default" operation
([in the sense of RFC 7595](https://tools.ietf.org/html/rfc7595#section-3.4))
that a client application SHOULD perform when the user activates
(e.g. clicks on) a URI; further MSCs may introduce additional operations
enabled either by passing an `action` value in the query part, or by other
means.

The classes of URIs and corresponding default operations (along with relevant
CS API calls) are collected below. The table assumes that the operations are
performed on behalf (using the access token) of the user `@me:example.org`:

| URI class/example | Interactive operation | Non-interactive operation / Involved CS API |
| ----------------- | --------------------- | --------------------------------------------- |
| User Id (no `action` in URI):<br/>`matrix:u/her:example.org` | _Outside the room context_: show user profile<br/>_Inside the room context:_ mention the user in the current room (client-local operation) | No default non-interactive operation<br/>`GET /profile/@her:example.org/display_name`<br/>`GET /profile/@her:example.org/avatar_url` |
| User Id (`action=chat`):<br/>`matrix:u/her:example.org?action=chat` | 1. Confirm with the local user if needed (see "Query")<br/>2. Open the room as defined in the next column | If [canonical direct chats](https://github.com/matrix-org/matrix-doc/pull/2199) are supported: `GET /chat/client/r0/user/@me:example.org/dm?involves=@her:example.org`<br/>Without canonical direct chats:<br/>1. `GET /user/@me:example.org/account_data/m.direct`<br/>2. Find the room id for `@her:example.org` in the event content<br/>3. if found, return this room id; if not, `POST /createRoom` with `"is_direct": true` and return id of the created room |
| Room (no `action` in URI):<br/>`matrix:roomid/rid:example.org`<br/>`matrix:r/us:example.org` | Attempt to "open" (usually: display the timeline at the latest or last remembered position) the room | No default non-interactive operation<br/>API: Find the respective room in the local `/sync` cache or<br/>`GET /rooms/!rid:example.org/...`<br/> |
| Room (`action=join`):<br/>`matrix:roomid/rid:example.org?action=join&via=example2.org`<br/>`matrix:r/us:example.org?action=join` | 1. Confirm with the local user if needed (see "Query")<br/>2. Attempt to join the room | `POST /join/!rid:example.org?server_name=example2.org`<br/>`POST /join/#us:example.org` |
| Event:<br/>`matrix:r/us:example.org/e/lol823y4bcp3qo4`<br/>`matrix:roomid/rid:example.org/event/lol823y4bcp3qo4?via=example2.org` | 1. For room aliases, resolve an alias to a room id (see the next column)<br/>2. Attempt to retrieve (see the next column) and display the event;<br/>3. If the event could not be retrieved due to access denial and the current user is not a member of the room, the client MAY offer the user to join the room and try to open the event again | Non-interactive operation: return event or event content, depending on context<br/>API: find the event in the local `/sync` cache or<br/>`GET /directory/room/%23us:example.org` (to resolve alias to id)<br/>`GET /rooms/!rid:example.org/event/lol823y4bcp3qo4?server_name=example2.org`<br/> |


#### URI construction algorithm

The following algorithm assumes a Chat identifier that follows
the high-level grammar described in the specification. Clients MUST ensure
compliance of identifiers passed to this algorithm.

For room and user identifiers (including room aliases):
1. Remove the sigil character from the identifier and match it against
   the following list to produce `prefix-1`:
   - `@` -> `u/`
   - `#` -> `r/`
   - `!` -> `roomid/`
2. Build the Chat URI as a concatenation of:
   - literal `matrix:`;
   - `prefix-1`;
   - the remainder of identifier (`id without sigil`), percent-encoded as per
     [RFC 3986](https://tools.ietf.org/html/rfc3986).

For event identifiers (assuming they need the room context, see
[MSC2695](https://github.com/matrix-org/matrix-doc/pull/2695) and
[MSC2779](https://github.com/matrix-org/matrix-doc/issues/2779) that
may change this):
1. Take the event's room id or canonical alias and build a Chat URI for them
   as described above.
2. Append to the result of previous step:
   - literal `e/`;
   - the event id after removing the sigil (`$`) and percent-encoding.

Clients MUST implement proper percent-encoding of the identifiers; there's no
liberty similar to that of to.chat.imzqqq.top.


## Discussion and non-normative statements

### Ideas for further evolution

This MSC is obviously just the first step, keeping the door open for
extensions. Here are a few ideas:

* Add new actions; e.g. leaving a room (`action=leave`).

* Add specifying a segment of the room timeline (`from=$evtid1&to=$evtid2`).

* Unlock bare event ids (`matrix:e/$event_id`) - subject to change in
  other areas of the specification.

* Bring tangible semantics to the authority part. The main purpose of
  the authority part,
  [as per RFC 3986](https://tools.ietf.org/html/rfc3986#section-3.2),
  is to identify the entity governing the namespace for the rest of the URI.
  The current MSC rules out the userinfo component but leaves it to a separate
  MSC to define semantics of the remaining`host[:port]` piece.

  Importantly, future MSCs are advised against using the authority part for
  _routing over federation_ (the case for `via=` query items), as it would be
  against the spirit of RFC 3986. The authority part can be used in cases when
  a given Chat entity is only available from certain servers (the case of
  closed federations or non-federating servers).

  While being a part of the original proposal in an attempt to address
  [the respective case](https://github.com/matrix-org/matrix-doc/issues/2309),
  the definition of the authority semantics has been dropped as a result of
  [the subsequent discussion](https://github.com/matrix-org/matrix-doc/pull/2312#discussion_r348960282).
  A further MSC may approach the same case (and/or others) and define the
  meaning of the authority part (either on the client- or even on
  the server-side - provided that using Chat URIs on the server-side brings
  some other value along the way). This might not necessarily be actual DNS
  hostnames even - one (quite far-fetched for now) idea to entertain might be
  introducing some decentralised system of "network names" in order to equalise
  "public" and "non-public" federations.

  Along the same lines, if providing any part of user credentials via
  the authority part is found to be of considerable value in some case,
  a separate MSC could both reinstate it in the grammar and define how
  to construct, parse, and use it - provided that the same MSC addresses
  the security concerns associated with such URIs.

* One could conceive a URI mapping of avatars in the form of
  `matrix:u/uid:chat.imzqqq.top/avatar/room:chat.imzqqq.top`
  (a user’s avatar for a given room).

* As described in "Alternatives", a synonymous system can be introduced that
  uses Chat identifiers with sigils by adding another path prefix (e.g.,
  `matrix:id/%23matrix:chat.imzqqq.top`). However, such MSC would have to address
  the concerns of possible confusion arising from having two similar but
  distinct notations.

* Interoperability of Chat URIs with
  [Linked Data](https://en.wikipedia.org/wiki/Linked_data).


### Past discussion points and tradeoffs

The below documents the discussion and outcomes in various prior forums;
further discussion should happen in GitHub comments.
1. _Why no double-slashes in a typical URI?_
   Because `//` is used to mark the beginning of an authority
   part. RFC 3986 explicitly forbids to start the path component with
   `//` if the URI doesn't have an authority component. In other words,
   `//` implies a centre of authority, and the (public) Chat
   federation is not supposed to have one; hence no `//` in most URIs.
1. ~~_Why do type specifiers use singular rather than plural
   as is common in RESTful APIs?_~~
   This is no more relevant with single-letter type specifiers. The answer
   below is provided for history only.
   Unlike in actual RESTful APIs, this MSC does not see `rooms/` or
   `users/` as collections to browse. The type specifier completes
   the id specification in the URI, defining a very specific and
   easy to parse syntax for that. Future MSCs may certainly add
   collection URIs, but it is recommended to use more distinct naming
   for such collections. In particular, `rooms/` is ambiguous, as
   different sets of rooms are available to any user at any time
   (e.g., all rooms known to the user; or all routable rooms; or
   public rooms known to the user's homeserver).
1. _Should we advise using the query part for collections then?_
   Not in this MSC but that can be considered in the future.
1. _Why can't event URIs use the fragment part for the event ID?_
   Because fragment is a part processed exclusively by the client
   in order to navigate within a larger document, and room cannot
   be considered a "document". Each event can be retrieved from the server
   individually, so each event can be viewed as a self-contained document.
   When/if URI processing is shifted to the server-side, servers are not even
   going to receive fragments (as per RFC 3986), which is why usage of
   fragments to remove the need for percent-encoding in other identifiers
   would lead to URIs that cannot be resolved on servers. Effectively, all
   clients would have to implement full URI processing with no chance
   to offload that to the server. For that reason fragments, if/when ever
   employed in Chat, only should be used to pinpoint a position within events
   and for similar strictly client-side operations.
1. _How does this MSC work with closed federations?_ ~~If you need to
   communicate a URI to the bigger world where you cannot expect
   the consumer to know in advance which federation they should use -
   supply any server of the closed federation in the authority part.
   Users inside the closed federation can omit the authority part if
   they know the URI is not going to be used outside this federation.
   Clients can facilitate that by having an option to always add or omit
   the authority part in generated URIs for a given user account.~~
   As of now, use `via=` in order to point to a homeserver in the closed
   federation. The authority part may eventually be used for that (or for some
   other case - see the previous section).


### Alternatives

#### Using full words for all types

During its draft state, this MSC was proposing type specifiers using full words
(`user`, `room`, `event` etc.), arguing that abbreviations can be introduced
separately as synonyms. Full words have several shortcomings pointed out in
discussions across the whole period of preparation, namely:
- The singular vs. plural choice (see also "Past discussion points")
- Using English words raises a question about eventual support of localised
  URI variants (`matrix:benutzer/...`, `matrix:usuario/...` etc.) catering to
  international audience, that would add complication to the Chat technology.
- Abbreviated forms are popularised by Reddit and make URIs shorter which is
  crucial for the outbound integration case (see the introduction).

Meanwhile, using `u`/`r`/`e` for users, rooms and events has the following
advantages:
1. there's a strong Reddit legacy, with users across the world quite familiar
   with the abbreviated forms (and `r/` coincidentally standing for sub-Reddits
   links to which have basically the same place in the Reddit ecosystem as
   Chat room aliases have in the Chat ecosystem);
2. to.chat.imzqqq.top links to users and room aliases are heavily used throughout Chat,
   specifically in end-user-facing contexts (see also use cases in the
   introductory section of this MSC);
3. the singular vs. plural (`room` or `rooms`?) confusion is avoided;
4. it's shorter, which is crucial for typing the URI in an external medium.

The rationale behind not abbreviating `roomid/` is a better distinction between
room aliases and room ids; also, since room ids are almost never typed in
manually, the advantages (3) and (4) above don't hold.

For these reasons, it was decided in the end to use the single-letter style
for types most used in the outbound integration case. It's still possible to
reinstate full words as synonyms some time down the road, with the caveat that
a canonicalisation service from homeservers may be needed to avoid having
to enable synonyms at each client individually.

#### URNs

The discussion in
[MSC455](https://github.com/matrix-org/matrix-doc/issues/455)
mentions an option to standardise URNs rather than URLs/URIs,
with the list of resolvers being user-specific. While a URN namespace
such as `urn:matrix:`, along with a URN scheme, might be deemed useful
once we shift to (even) more decentralised structure of the network,
`urn:` URIs must be managed entities (see
[RFC 8141](https://tools.ietf.org/html/rfc8141)) which is not always
the case in Chat (consider room aliases, e.g.).

With that said, a URN-styled (`matrix:room:example.org:roomalias`)
option was considered. However, Chat already uses colon (`:`) as
a delimiter of id parts and, as can be seen above, reversing the parts
to meet the URN's hierarchical order would look confusing for Chat
users (as in example above - is `room` a part of the identifier or
the type signifier?).

#### "Full REST"

Yet another alternative considered was to go "full REST" and structure
URLs in a more traditional way with serverparts coming first, followed
by type grouping (sic - not specifiers), and then by localparts,
i.e. `matrix://example.org/rooms/roomalias`. This is even more difficult
to comprehend for a Chat user than the previous alternative and besides it
conflates the notion of an authority server with that of a namespace
discriminator: clients would not connect to `example.org` to resolve the alias
above, they would still connect to their own homeserver.

#### Minimal syntax

One early proposal was to simply prepend `matrix:` to a Chat identifier
(without encoding it), assuming that it will only be processed on the client
side. The massive downside of this option is that such strings are not actual
URIs even though they look like ones: most URI parsers won't handle them
correctly. As laid out in the beginning of this proposal, Chat URIs are
not striving to preempt Chat identifiers; instead of trying to produce
an equally readable string, one should just use identifiers where they work.
Why Chat identifiers look the way they look is way out of the MSC scope
to discuss here.

#### Minimal syntax based on the path component and percent-encoding

A simple modification of the previous option is much more viable:
proper percent-encoding of the Chat identifier allows to use it as
a URI path part. A single identifier packed in a URI could look like
`matrix:/encoded_id_with_sigil`; an event-in-a-room URI would be something
like `matrix:/roomid_or_alias/$event_id` (NB: RFC 3986 doesn't require `$`
to be encoded). This is considerably more concise and encoding is only
needed for `#`.

Quite unfortunately, `#` is one of the two sigils in Chat most relevant
to integration cases. The other one is `@`; it doesn't need encoding except
in the authority part - which is why the form above uses a leading `/` that
puts the identifier in the path part instead of what parsers treat as
the authority part. `#` has to be encoded wherever it appears, making a URI
for Chat HQ, the first chat room many new users join, look like
`matrix:/%23matrix:chat.imzqqq.top`. Beyond first-time usage, this generally impacts
[the "napkin" case](https://tools.ietf.org/html/rfc3986#section-1.2.1) from
RFC 3986 that the Requirements section of this MSC mentions. Until we have
applications generally recognising Chat identifiers in the same way e-mail
addresses are recognised without prefixing `mailto:`, we should live with
the fact that people will have to produce Chat URIs by hand in various
instances, from pen-and-paper to other instant messengers.

Putting the whole id to the URI fragment (`matrix:#id_with_sigil` or,
following on the `to.chat.imzqqq.top` tradition, `matrix:#/id_with_sigil` for
readability) allows using `#` without encoding on many URI parsers. It is
still not fully RFC-compliant and rules out using URIs by homeservers
(see also "Past discussion points" on using fragments to address events).

Regardless of the placement (the fragment or the path), one more consideration
is that the character space for sigils is extremely limited and
Chat identifiers are generally less expressive than full-blown URI paths.
Not that Chat showed a tendency to produce many classes of objects that would
warrant a dedicated sigil but that cannot be ruled out. Rather than rely
on the institute of sigils, this proposal gives an alternative more
extensible syntax that can be used for more advanced cases - as a uniform way
to represent arbitrary sub-objects (with or without Chat identifier) such as
user profiles, or a notifications feed for the room - and also, if ever needed,
as an escape hatch to a bigger namespace if we hit shortage of sigils.

The current proposal is also flexible enough to incorporate the minimal
syntax of this option as an alternative to its own notation - e.g., a further
MSC could enable `matrix:id/%23matrix:chat.imzqqq.top` as a synonym for
`matrix:room/matrix:chat.imzqqq.top`.


## Potential issues

Despite the limited functionality of URIs as proposed in this MSC,
Chat authors are advised to use tools that would process URIs just
like an HTTP(S) URI instead of making home-baked parsers/emitters.
Even with that in mind, not all tools normalise and sanitise all cases
in a fully RFC-compliant way. This MSC tries to keep the required
transformations to the minimum and will likely not bring much grief even
with naive implementations; however, as functionality of Chat URI grows,
the number of corner cases will increase.


## Security/privacy considerations

This MSC mostly builds on RFC 3986 but tries to reduce the scope
as much as possible. Notably, it avoids introducing complex traversable
structures and further restricts the URI grammar to the necessary subset.
In particular, dot path segments (`.` and `..`), while potentially useful
when URIs become richer, would come too much ahead of time for now. Care
is taken to not make essential parts of the URI omittable to avoid
even accidental misrepresentation of a local resource for a remote one
in Chat and vice versa.

As mentioned in the authority part section, the MSC intentionally doesn't
support conveying any kind of user information in URIs.

The MSC strives to not be prescriptive in treating URIs except the `action`
query parameter. Actions without user confirmation may lead to unintended
leaks of certain metadata and/or changes in the account state with respect
to Chat. To reiterate, clients SHOULD ask for a user consent if/when they
can unless applying the action doesn't lead to sending persistent (message
or state) events on user's behalf.


## Conclusion

A dedicated URI scheme is well overdue for Chat. Many other networks
already have got one for themselves, benefiting both in terms of
branding (compare `matrix:r/weruletheworld:example.org` vs.
`#weruletheworld:example.org` from the standpoint of someone who
hasn't been to Chat) and interoperability (`to.chat.imzqqq.top` requires
opening a browser while clicking a `tg:` link dumped to the terminal
application will open the correct application for Telegram without
user intervention or can even offer to install one, if needed).
The proposed syntax makes conversion between Chat URIs
and Chat identifiers as easy as a bunch of string comparisons or
regular expressions; so even though client-side processing of URIs
might not be optimal longer-term, it's a very simple and quick way
that allows plenty of experimentation early on.
