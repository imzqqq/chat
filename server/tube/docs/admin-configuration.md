# Configuration

Admins can configure their instance directly in the web interface using in `Administration` -> `Configuration`.

All the fields from this web interface will override your `production.yaml` configuration keys.

## VOD transcoding

Enabling transcoding ensures that your users will have a video playback that works. It's highly recommended to enable this.

### WebTorrent transcoding or HLS transcoding

We have two different ways to stream the video in the Tube player: using [WebTorrent](https://webtorrent.io/) or [HLS with P2P](https://en.wikipedia.org/wiki/HTTP_Live_Streaming).

At the beginning of Tube, we only supported WebTorrent streaming. Due to several limitations of the library leading to many bugs, we had to add HLS (with P2P) support.
Unfortunately, we can't use the same video file for WebTorrent and HLS: we need to transcode 2 different versions of the file (a fragmented mp4 for HLS, and a raw mp4 for WebTorrent).

So if you enable WebTorrent **and** HLS, the storage will be multiplied by 2.

We recommand you to enable HLS (and disable WebTorrent if you don't want to store 2 different versions of the same video resolution) because video playback is really better:

 * There is a buffer system so your web browser won't download the entire video (better bandwidth management)
 * We don't have to store video chunks in the web browser IndexedDB (so the video is not stored on the watcher disk, which was a little bit hacky/dirty)
 * Video resolution change is smoother (because we don't have to change the source of the `<video>` tag)
 * Start-up of videos is faster (especially long videos)
 * We use a widely used HLS library ([hls.js](https://github.com/video-dev/hls.js/)) and a clean P2P WebRTC loader ([p2p-media-loader](https://github.com/novage/p2p-media-loader)) resulting in less bugs and an easier support

The main drawback is that this HLS with P2P player is not compatible with WebTorrent, so you won't be able to help the swarm using your BitTorrent client.
This is a disadvantage that we accept, because we realized that BitTorrent client that helped to seed Tube files were not really used.
Moreover, the [Tube redundancy system](https://tube.docs.dingshunyu.top/admin-following-instances?id=instances-redundancy) can be another way to help the origin instance to seed the video.

![](/assets/transcoding-hls-webtorrent.png)

### Resolutions

Tube can transcode the uploaded video in multiple resolutions. It allows to users that does not have a high speed Internet connection to watch the video in low quality. Keep in mind that a transcoding job takes a lot of CPU, requires time and create an additional video file stored on your disk storage.

![](/assets/transcoding-resolutions.png)

### Type of files users can upload

If you enable transcoding, you can also allow additional files formats lile `.mkv`, `.avi` and/or allow audio file uploads (Tube will create a video from them).

![](/assets/transcoding-additional-extensions.png)


## Live streaming

You can enable live streaming on your instance in `Administration > Configuration > Live Streaming`. In this section, you can choose to:

 * Allow your users to automatically save a replay of their live. Enabling VOD transcoding is required to enable this setting
 * Limit parallel lives per account/on your instance
 * Set a max duration for lives
 * Enable live transcoding. This setting is different than VOD transcoding, because Tube will transcode the live in real time.
 It means that if your instance is streaming 10 lives, Tube will run 10 FFmpeg processes (high CPU/RAM usage).
 So we recommend you to make some tests to see how many parallel lives your instance can handle before increasing limits/enabling transcoding resolutions

Bear in mind that enabling live streaming will make your server listen on port `1935/TCP`, which is required for incoming RTMP by your streamers.
The listening port can be changed in the Tube configuration file.

## Search

### Global search

You can enable global search, to use an external index (https://framagit.org/framasoft/tube/search-index).
This way, you give the ability to your users to get results from instances that may not be federated with yours.

To enable global search, your need to specify the `Search index URL` of the remote search index.
Framasoft provides a search index (indexing videos from https://instances.joinpeertube.org): https://search.joinpeertube.org/.
This index is not moderated, so we strongly recommend that you create your own moderated index.


### URI search

Users can use a video URI or channel URI/handle search to fetch a specific remote video/channel, that may not be federated with your instance.
Since this feature adds ability to your users to *escape* from your federation policy, you can disable it using
`Allow users to do remote URI/handle search` or `Allow anonymous to do remote URI/handle search` checkboxes.
