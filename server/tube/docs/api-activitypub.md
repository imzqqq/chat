# ActivityPub

Each Tube instance is able to fetch video from other compatible servers it follows, in a process known as “federation”.
Federation is implemented using the ActivityPub protocol, in order to leverage existing tools and be
compatible with other services such as Mastodon, Pleroma and [many more](https://fediverse.network/).

Federation in Tube is twofold: videos metadata are shared as activities for inter-server communication
in what amounts to sharing parts of one's database, and user interaction via comments which are compatible
with the kind of activity textual platforms like Mastodon use.

## Supported Activities

- [Create](#create)
- [Update](#update)
- [Delete](#delete)
- [Follow](#follow)
- [Accept](#accept)
- [Announce](#announce)
- [Undo](#undo)
- [Like](#like)
- [Dislike](#dislike)
- [Reject](#reject)
- [Flag](#flag)
- [View](#view)

## Supported Objects

- [Video](#video)
- [CacheFile](#cache-file)
- [Note](#note)
- [Playlist](#playlist)
- [PlaylistElement](#playlist-element)
- Actor
- [Note](#note) (comment)

### Follow

Follow is an activity standardized in the ActivityPub specification (see [Follow Activity](https://www.w3.org/TR/activitypub/#follow-activity-inbox)).
The Follow activity is used to subscribe to the activities of another actor (a server
subscribing to another server's videos, a user subscribing to another user's videos).

##### Supported on

- Actor (`tube` actor, account actor or channel actor)

### Accept

##### Supported on

- Follow

### Reject

Reject is an activity standardized in the ActivityPub specification (see [Reject Activity](https://www.w3.org/TR/activitypub/#reject-activity-inbox)).

##### Supported on

- Follow

### Undo

Undo is an activity standardized in the ActivityPub specification (see [Undo Activity](https://www.w3.org/TR/activitypub/#undo-activity-inbox)).
The Undo activity is used to undo a previous activity.

##### Supported on

- [Follow](#follow)
- [Like](#like)
- [Dislike](#dislike)
- [Create](#create)
  - [CacheFile](#cachefile)
- [Announce](#announce)

### Like

Like is an activity standardized in the ActivityPub specification (see [Like Activity](https://www.w3.org/TR/activitypub/#like-activity-inbox)).
##### Supported on

- [Video](#video)


### Dislike

Dislike is an activity standardized in the ActivityStream specification (see [Dislike Activity](https://www.w3.org/TR/activitystreams-vocabulary/#dfn-dislike)).

##### Supported on

- [Video](#video)

### Update

Update is an activity standardized in the ActivityPub specification (see [Update Activity](https://www.w3.org/TR/activitypub/#update-activity-inbox)).
The Update activity is used when updating an already existing object.

##### Supported on

- [Video](#video)
- [Playlist](#playlist)
- [CacheFile](#cachefile)
- Actor

### Create

Create is an activity standardized in the ActivityPub specification (see [Create Activity](https://www.w3.org/TR/activitypub/#create-activity-inbox)).
The Create activity is used when posting a new object. This has the side effect
that the `object` embedded within the Activity (in the object property) is created.

##### Supported on

- [Video](#video)
- [Playlist](#playlist)
- [CacheFile](#cachefile)
- [Note](#note) (a comment)

#### Example

```json
{
  "@context": [
    "https://www.w3.org/ns/activitystreams",
    "https://w3id.org/security/v1",
    {}
  ],
  "to": ["https://peertube2.cpy.re/accounts/root/activity"],
  "type": "Create",
  "actor": "https://peertube2.cpy.re/accounts/root",
  "object": {}
}
```

### Delete

Delete is an activity standardized in the ActivityPub specification (see [Delete Activity](https://www.w3.org/TR/activitypub/#delete-activity-outbox)).

##### Supported on

- [Video](#video)
- Actor
- [Playlist](#playlist)
- [Note](#note) (a comment)


### Announce

Announce is an activity standardized in the ActivityPub specification (see [Announce Activity](https://www.w3.org/TR/activitypub/#announce-activity-inbox)).

##### Supported on

- [Video](#video)

#### Example

```json
{
  "type": "Announce",
  "id": "https://peertube2.cpy.re/videos/watch/997111d4-e8d8-4f45-99d3-857905785d05/announces/1",
  "actor": "https://peertube2.cpy.re/accounts/root",
  "object": "https://peertube2.cpy.re/videos/watch/997111d4-e8d8-4f45-99d3-857905785d05",
  "to": [
    "https://www.w3.org/ns/activitystreams#Public",
    "https://peertube2.cpy.re/accounts/root/followers",
    "https://peertube2.cpy.re/video-channels/root_channel/followers"
  ],
  "cc": []
}
```

### Flag

Flag is an activity standardized in the ActivityStream specification (see [Flag Activity](https://www.w3.org/TR/activitystreams-vocabulary/#dfn-flag)).

##### Supported on

- [Video](#video)
- Account actor
- [Note](#note) (comment)

### View

View is an activity standardized in the ActivityStream specification (see [View Activity](https://www.w3.org/TR/activitystreams-vocabulary/#dfn-view)). A Tube instance sends a `View` activity every time a user viewed a video. If a user is watching a live video, a `View` is sent periodically.

##### Supported on

- [Video](#video)

## Supported Objects

### Video

!> **Note:** this object extends the ActivityPub specification, and therefore some properties are not part of it.

#### Structure

The model structure definition lies in [shared/models/activitypub/objects/video-torrent-object.ts](https://github.com/Chocobozzz/Tube/blob/develop/shared/models/activitypub/objects/video-torrent-object.ts).

[video torrent object](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/activitypub/objects/video-torrent-object.ts ':include :type=code')


#### Example

A `Video` object could be complex depending on transcoding settings. Here is an example with some comments:

```JSON
{
  "type": "Video",
  "id": "https://peertube2.cpy.re/videos/watch/...",
  "name": "HLS test 1",
  "duration": "PT730S",
  "uuid": "969bf103-7818-43b5-94a0-...",
  "tag": [
    {
      "type": "Hashtag",
      "name": "tagexample"
    }
  ],
  "views": 35,
  "sensitive": false,
  "waitTranscoding": true,
  "isLiveBroadcast": false,
  // If this is a live, tell if the user will save a replay or not
  "liveSaveReplay": null,
  // If this is a live, tell if this is a permanent live or not
  "permanentLive": null,
  // See the REST API documentation: https://tube.docs.imzqqq.top/api-rest-reference.html#operation/getVideo
  "state": 1,
  "commentsEnabled": true,
  "downloadEnabled": true,
  "published": "2019-04-17T08:55:11.871Z",
  "originallyPublishedAt": null,
  "updated": "2020-12-15T11:01:02.477Z",
  "mediaType": "text/markdown",
  "content": "The story of programming prodigy and information activist Aaron Swartz, who took his own life at the age of 26.",
  // How to support the uploaded/content creator
  "support": "Pay me a coffee when you see me",
  "subtitleLanguage": [
    {
      "identifier": "ca",
      "name": "Catalan",
      "url": "https://peertube2.cpy.re/lazy-static/video-captions/...-ca.vtt"
    }
  ],
  "category": {
    "identifier": "2", // Internal Tube ID
    "name": "Films"
  },
  "licence": {
    "identifier": "5", // Internal Tube ID
    "name": "Attribution - Non Commercial - Share Alike"
  },
  // Optional
  "language": {
    "identifier": "en",
    "name": "English"
  },
  "icon": [
    {
      "type": "Image",
      "url": "https://peertube2.cpy.re/static/thumbnails/....jpg",
      "mediaType": "image/jpeg",
      "width": 200,
      "height": 110
    },
    {
      "type": "Image",
      "url": "https://peertube2.cpy.re/lazy-static/previews/....jpg",
      "mediaType": "image/jpeg",
      "width": 560,
      "height": 315
    }
  ],
  "url": [
    // Webpage
    {
      "type": "Link",
      "mediaType": "text/html",
      "href": "https://peertube2.cpy.re/videos/watch/969bf103-7818-43b5-94a0-de159e13de50"
    },

    // Raw URL to webtorrent compatible mp4 file (only if WebTorrent transcoding is enabled)
    {
      "type": "Link",
      "mediaType": "video/mp4",
      "href": "https://peertube2.cpy.re/static/webseed/969bf103-7818-43b5-94a0-de159e13de50-536.mp4",
      "height": 536,
      "size": 135239407,
      "fps": 24
    },
    // URL to fetch video file metadata (only if WebTorrent transcoding is enabled)
    {
      "type": "Link",
      "rel": [
        "metadata",
        "video/mp4"
      ],
      "mediaType": "application/json",
      "href": "https://peertube2.cpy.re/api/v1/videos/969bf103-7818-43b5-94a0-de159e13de50/metadata/1642209",
      "height": 536,
      "fps": 24
    },
    // Torrent file URL (only if WebTorrent transcoding is enabled)
    {
      "type": "Link",
      "mediaType": "application/x-bittorrent",
      "href": "https://peertube2.cpy.re/static/torrents/969bf103-7818-43b5-94a0-de159e13de50-536.torrent",
      "height": 536
    },
    // Magnet URI (only if WebTorrent transcoding is enabled)
    {
      "type": "Link",
      "mediaType": "application/x-bittorrent;x-scheme-handler/magnet",
      "href": "magnet:?xs=https%3A%2F%2Fpeertube2.cpy.re%2Fstatic%2Ftorrents%2F969bf103-7818-43b5-94a0-de159e13de50-536.torrent&xt=urn:btih:673aaa764ad4ba61aa5b50306a5fd77fdbd4e78e&dn=HLS+test+1&tr=wss%3A%2F%2Fpeertube2.cpy.re%3A443%2Ftracker%2Fsocket&tr=https%3A%2F%2Fpeertube2.cpy.re%2Ftracker%2Fannounce&ws=https%3A%2F%2Fpeertube2.cpy.re%2Fstatic%2Fwebseed%2F969bf103-7818-43b5-94a0-de159e13de50-536.mp4",
      "height": 536
    },

    // HLS playlist URL (only if HLS transcoding is enabled)
    {
      "type": "Link",
      "mediaType": "application/x-mpegURL",
      "href": "https://peertube2.cpy.re/static/streaming-playlists/hls/969bf103-7818-43b5-94a0-de159e13de50/master.m3u8",
      "tag": [
        // Infohashes for p2p-media-loader of every resolution
        {
          "type": "Infohash",
          "name": "d7844378e5a6b9af2d45267c0e413688e7839918"
        },
        // URL to a JSON that contains the playlist's segments sha sum
        {
          "type": "Link",
          "name": "sha256",
          "mediaType": "application/json",
          "href": "https://peertube2.cpy.re/static/streaming-playlists/hls/969bf103-7818-43b5-94a0-de159e13de50/segments-sha256.json"
        },
        // Raw URL to the fragmented mp4 file used by the HLS playlist
        {
          "type": "Link",
          "mediaType": "video/mp4",
          "href": "https://peertube2.cpy.re/static/streaming-playlists/hls/969bf103-7818-43b5-94a0-de159e13de50/969bf103-7818-43b5-94a0-de159e13de50-536-fragmented.mp4",
          "height": 536,
          "size": 135108145,
          "fps": 24
        },
        // URL to fetch video file metadata
        {
          "type": "Link",
          "rel": [
            "metadata",
            "video/mp4"
          ],
          "mediaType": "application/json",
          "href": "https://peertube2.cpy.re/api/v1/videos/969bf103-7818-43b5-94a0-de159e13de50/metadata/3649370",
          "height": 536,
          "fps": 24
        },
        // Fragmented mp4 file torrent URL
        {
          "type": "Link",
          "mediaType": "application/x-bittorrent",
          "href": "https://peertube2.cpy.re/static/torrents/969bf103-7818-43b5-94a0-de159e13de50-536-hls.torrent",
          "height": 536
        },
        // Fragmented mp4 file magnet URI
        {
          "type": "Link",
          "mediaType": "application/x-bittorrent;x-scheme-handler/magnet",
          "href": "magnet:?xs=https%3A%2F%2Fpeertube2.cpy.re%2Fstatic%2Ftorrents%2F969bf103-7818-43b5-94a0-de159e13de50-536-hls.torrent&xt=urn:btih:e629524f99aa27bc2b42d05b22ac318cc22cfcf7&dn=HLS+test+1&tr=wss%3A%2F%2Fpeertube2.cpy.re%3A443%2Ftracker%2Fsocket&tr=https%3A%2F%2Fpeertube2.cpy.re%2Ftracker%2Fannounce&ws=https%3A%2F%2Fpeertube2.cpy.re%2Fstatic%2Fstreaming-playlists%2Fhls%2F969bf103-7818-43b5-94a0-de159e13de50%2F969bf103-7818-43b5-94a0-de159e13de50-536-fragmented.mp4",
          "height": 536
        }
      ]
    }
  ],
  "likes": "https://peertube2.cpy.re/videos/watch/969bf103-7818-43b5-94a0-de159e13de50/likes",
  "dislikes": "https://peertube2.cpy.re/videos/watch/969bf103-7818-43b5-94a0-de159e13de50/dislikes",
  "shares": "https://peertube2.cpy.re/videos/watch/969bf103-7818-43b5-94a0-de159e13de50/announces",
  "comments": "https://peertube2.cpy.re/videos/watch/969bf103-7818-43b5-94a0-de159e13de50/comments",
  "attributedTo": [
    // The account
    {
      "type": "Person",
      "id": "https://peertube2.cpy.re/accounts/root"
    },
    // The channel
    {
      "type": "Group",
      "id": "https://peertube2.cpy.re/video-channels/a75cbdf4-acf2-45c0-a491-e9b28939f8db"
    }
  ],
  "to": [],
  "cc": [],
  "@context": [
    "https://www.w3.org/ns/activitystreams",
    "https://w3id.org/security/v1",
    ...
  ]
```



### Playlist

!> **Note:** this object is not standard to the ActivityPub specification.

The model structure definition lies in [shared/models/activitypub/objects/playlist-object.ts](https://github.com/Chocobozzz/Tube/blob/develop/shared/models/activitypub/objects/playlist-object.ts).

[playlist object](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/activitypub/objects/playlist-object.ts ':include :type=code')


### CacheFile

!> **Note:** this object is not standard to the ActivityPub specification.

The object is used to represent a cached file. It is usually sent by third-party servers to the origin server hosting a video
file (a resolution from a `Video`), to notify it that they have put up a copy of that file. The origin server should
then add the server emitting the `CacheFile` to the list of [WebSeeds](http://bittorrent.org/beps/bep_0019.html) for that file.

#### Structure

The model structure definition lies in [shared/models/activitypub/objects/cache-file-object.ts](https://github.com/Chocobozzz/Tube/blob/develop/shared/models/activitypub/objects/cache-file-object.ts).

[cache file object](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/activitypub/objects/cache-file-object.ts ':include :type=code')

### Note

A `Note` is usually a comment made to a video. Since most ActivityPub textual platforms use the `Note` object for their
messages, most of them can interact in the same way with Tube videos, making them able to comment Tube videos
directly! A `Note` is emitted along the Video publication object: the former is used to notify textual platforms of
the Fediverse, the latter to notify the Vidiverse.

#### Structure

The model structure definition lies in [shared/models/activitypub/objects/video-comment-object.ts](https://github.com/Chocobozzz/Tube/blob/develop/shared/models/activitypub/objects/video-comment-object.ts).

[video comment object](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/activitypub/objects/video-comment-object.ts ':include :type=code')

### Playlist

!> **Note:** this object is not standard to the ActivityPub specification.

The object is used to represent a video playlist. It extends [OrderedCollectionPage](https://www.w3.org/TR/activitystreams-vocabulary/#dfn-orderedcollectionpage) and items are [PlaylistElement](#playlist-element).

#### Structure

The model structure definition lies in [shared/models/activitypub/objects/playlist-object.ts](https://github.com/Chocobozzz/Tube/blob/develop/shared/models/activitypub/objects/playlist-object.ts).

[cache file object](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/activitypub/objects/playlist-object.ts ':include :type=code')

### Playlist Element

!> **Note:** this object is not standard to the ActivityPub specification.

The object is used to represent a video playlist element inside the `Playlist` object.

#### Structure

The model structure definition lies in [shared/models/activitypub/objects/playlist-element-object.ts](https://github.com/Chocobozzz/Tube/blob/develop/shared/models/activitypub/objects/playlist-element-object.ts).

[cache file object](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/activitypub/objects/playlist-element-object.ts ':include :type=code')
