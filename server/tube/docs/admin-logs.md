# Logs

Admins can use Tube logs to understand what happens on their instances. There are two types of logs.

## Standard logs

Logs HTTP requests, and some information depending on the log level defined in the configuration.
Can be read in your logs directory or in the web interface `Administration > System > Logs`.

## Audit logs

**Available in Tube 2.1**

Logs resources creation/update/deletion with the user who did the action.

Resources can be:
 * Videos
 * Video imports
 * Comments
 * Video channels
 * Video abuses
 * Custom config

Example:

```
audit[13/12/2019 à 15:31:31]By chocobozzz ->videos -> update

{
  "user": "chocobozzz",
  "domain": "videos",
  "action": "update",
  "video-tags": [],
  "video-uuid": "1974ddde-3d83-4cfa-af68-5ab77f588c9d",
  "video-id": 89655,
  "video-createdAt": "2019-12-13T14:31:26.525Z",
  "video-updatedAt": "2019-12-13T14:31:27.175Z",
  "video-publishedAt": "2019-12-13T14:31:27.175Z",
  "video-description": null,
  "video-duration": 2,
  "video-isLocal": true,
  "video-name": "60fps_small",
  "video-thumbnailPath": "/static/thumbnails/1974ddde-3d83-4cfa-af68-5ab77f588c9d.jpg",
  "video-previewPath": "/static/previews/1974ddde-3d83-4cfa-af68-5ab77f588c9d.jpg",
  "video-nsfw": false,
  "video-waitTranscoding": true,
  "video-account-id": 37855,
  "video-account-name": "chocobozzz",
  "video-channel-id": 5187,
  "video-channel-name": "chocobozzz_channel",
  "video-support": null,
  "video-commentsEnabled": true,
  "video-downloadEnabled": true,
  "new-video-updatedAt": "2019-12-13T14:31:31.538Z",
  "new-video-name": "test video"
}
```

Can be read in your logs directory or in the web interface `Administration > System > Logs`.
