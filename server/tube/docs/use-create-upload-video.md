# Publish a video or a live

## Upload a video

To publish a video you have to click the <i data-feather="upload-cloud"></i>**Publish** button in top right corner. Once clicked you have 3 ways to upload a video:

  1. [by selecting a file on your device](use-create-upload-video?id=upload-a-file);
  1. [by importing an online video by its url](use-create-upload-video?id=import-with-url);
  1. [by importing an online video by its URI (torrent)](use-create-upload-video?id=import-with-torrent).

### Upload a file

Once you have to click the <i data-feather="upload-cloud"></i>**Publish** button in top right corner:

  1. select which channel you want to upload your video (can be done/change after upload);
  1. select privacy settings for this video (can be done/change after upload);
  1. click **Select file to upload** button.

While the video is uploading you can set [some details](use-create-upload-video?id=video-fields).

### Import with URL

If the administrator of your instance enabled this option, you can import any URL [supported by youtube-dl](https://ytdl-org.github.io/youtube-dl/supportedsites.html) or URL that points to a raw MP4 file. To do so, you have to:

  1. click the <i data-feather="upload-cloud"></i>**Publish** button in top right corner;
  1. click **Import with URL** tab;
  1. paste your video's url into **URL** field;
  1. select which channel you want to upload your video (can be done/change after upload);
  1. select privacy settings for this video (can be done/change after upload);
  1. click **Import** button.

!> You should make sure you have diffusion rights over the content it points to, otherwise it could cause legal trouble to yourself and your instance.

While the video is uploading you can set [some details](use-create-upload-video?id=video-fields).

### Import with torrent

If the administrator of your instance enabled this option, you can import any torrent file that points to a mp4 file. To do so, you have to:

  1. click the <i data-feather="upload-cloud"></i>**Publish** button in top right corner;
  1. click **Import with torrent** tab;
  1. select a `.torrents` file on your commputer or paste magnet URI of a video;
  1. select which channel you want to upload your video (can be done/change after upload);
  1. select privacy settings for this video (can be done/change after upload);
  1. click **Import** button.

!> You should make sure you have diffusion rights over the content it points to, otherwise it could cause legal trouble to yourself and your instance.

While the video is uploading you can set [some details](use-create-upload-video?id=video-fields).

## Publish a live (in Tube >= v3)

If the administrator of your instance enabled this option, you can create a live using Tube and a streaming software (for example [OBS](https://obsproject.com/)). To do so, you have to:

  1. click the <i data-feather="upload-cloud"></i>**Publish** button in top right corner;
  1. click **Go live** tab;
  1. select which channel you want to publish your live;
  1. select privacy settings for this live;
  1. click **Go Live** button.

![user upload basic info - image](/assets/go-live-UI.png)

In the publication form, you have a **Live settings** tab that allows you to:

 * See the **RTMP URL** to put in your streaming software
 * See the live **stream key** associated to this live, to put in your streaming software. It is a private key, allowing anyone to stream a video in this live so don't share it with anyone!
 * Choose to automatically publish **a replay** of your live if the administrator enabled this option: when your live will end, Tube will create a video replay behind the same URL of your live
 * Enable the **permanent live** mode. In this mode, you cannot save a replay of your live but you can stream multiple times in the live video: the live URL won't change between two live events, and a stream disconnection won't end the live

Now you're ready to go live! 

### Live examples
#### With OBS

In this example we'll use [OBS software](https://obsproject.com/) to send a stream to Tube, but you can use
any streaming software that can stream videos using the [RTMP protocol](https://en.wikipedia.org/wiki/Real-Time_Messaging_Protocol).

 1. Open OBS on your computer
 1. Click on **Settings** (or: **File** in top bar > **Settings**)
 1. Click on **Streams** tab
 1. Choose **Custom** service
 1. Fill **Server** input using the Tube RTMP URL
 1. Fill **Stream key** input using your live stream key
 1. Do not use **authentication** and click on **OK**
 1. Stream whatever your want and click on **Start streaming**. After some time, you'll be able to see your live in the Tube interface.

![obs settings image](/assets/live-obs-settings.png)

#### With Jitsi Meet

If you want to broadcast a webinar or a conference very easily, you can also use [Jitsi Meet](https://meet.jit.si) for recording the event, and rely on Tube for streaming it live. It is very useful if you have an expected audience too big for Jitsi to handle, but actually only a few people talking.

1. Create a chat room on a Jitsi server
1. Click on the **menu** (3-points icon at the bottom-right of the screen)
1. Click on **Start live stream**
1. You will be asked a Youtube live stream key. Actually, there is a way to make it work with Tube : simply paste the **Tube RTMP URL** followed by the **Tube live stream key** (you may have to add a slash in-between).
1. Click **Start live stream**, you may heard "The stream has started" and see your live on Tube after some time.

![Jitsi Start live stream menu](/assets/jitsi-start-live-stream.png)
![Jitsi window where inserting stream key](/assets/jitsi-insert-stream-key.png)

## Video fields

### Basic info

  * **Title**: the name of the video (something more catchy than `myvideo.mp4` for example :wink: );
  * **Tags**: tags can be used to suggest revelant recommandations. 5 tags maximum. You have to press enter to add one;
  * **Description**: text you want to display above your video (supports markdown) - you can see how it looks above;
  * **Channel**: in which channel you want to add your video;
  * **Category**: what kind of content is your video (Activism? How to? Music?);
  * **Licence**:
    * Attribution,
    * Attribution - Share Alike,
    * Attribution - No Derivatives,
    * Attribution - Non Commercial,
    * Attribution - Non Commercial - Share Alike,
    * Attribution - Non Commercial - No Derivatives,
    * Public Domain Dedication;
  * **Language**: what is the main language in the video;
  * **Privacy**: public, internal, unlisted, private. [See what it means](use-create-upload-video?id=video-confidentiality-options-what-do-they-mean);
  * **Contains sensitive content**? Some instances do not list videos containing mature or explicit content by default.

![user upload basic info - image](/assets/user-upload-video-basic-info.png)

### Captions

This tab allows you to add subtitles to your video. To add one, you have to:

  1. go to **Captions** tab;
  1. click <i data-feather="plus-circle"></i> **Add another caption** button;
  1. select the caption language in the list;
  1. click **Select the caption file** button;
  1. browse into your computer file to select your `.vtt` or `.srt` file;
  1. click **Add this caption** button;
  1. click **<i data-feather="check-circle"></i> Update** button.

Your caption is now available by clicking <i data-feather="settings"></i> > **Subtitles/CC** and selecting the language.

To delete a caption you have to:

  1. click **Delete** button in front of the language you want to delete;
  1. click **<i data-feather="check-circle"></i> Update** button.

### Advanced settings

This tab allows you:

  * to edit your preview image;
  * to add a short text to tell people how they can support you (membership platform...);
  * change the original publication date;
  * disable/enable video comments;
  * disable/enable download of your video.

?> Click **<i data-feather="check-circle"></i> Update** button to save your new settings.

### Video confidentiality options: what do they mean?

  * **Public**: your video is public. Everyone can see it (by using search engine, link or embed);
  * **Internal**: only authenticated user having an account on your instance can see your video. Users searching from another instance or having the link without being authentifaced can't see it;
  * **Unlisted**: only people with the private link can see this video; the video is not visible without its link (can't be found by searching);
  * **Private**: only you can see the video.
