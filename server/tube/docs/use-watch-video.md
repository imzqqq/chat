# Watch, share, download a video

## Watching a video

![image player interface](/assets/video-player-watch.png)

The player interface is composed of:

  1. <i data-feather="play"></i> and <i data-feather="pause"></i> button to play or pause the video
  1. <i data-feather="skip-forward"></i> button to go to the next video
  1. time indicator: elapsed time/total
  1. P2P statistics (if enabled; if not you see **HTTP** instead)
  1. <i data-feather="volume-2"></i> or <i data-feather="volume-x"></i> to mute/unmute soundtrack
  1. volume controls: you can use your keyboard arrow to increase or decrese volume when you first click the icon
  1. <i data-feather="settings"></i> settings: to change the video quality, speed and subtitles
  1. theater or normal mode to enlarge the display
  1. full screen mode

## Sharing a video

To share a video, you first have to go to the video page you want to share. If you are the publisher, you can list all your videos via <i data-feather="more-vertical"></i>__My Videos__.

Once on the video page, you just have to click the <i data-feather="share-2"></i>__SHARE__ button, and are presented with a few options:

?> The small icons at the end of the line allow you to copy the whole URL at once.

### URL

An URL to the video (i.e.: `https://framatube.org/videos/watch/9db9f3f1-9b54-44ed-9e91-461d262d2205`). This address can be sent to your contact the way you want; they will be able to access the video directly.

![Modal presenting options to share a video](/assets/video-share-modal.png)

### QR code

A QR code to facilitate sharing.

![Modal QR cdoe img](/assets/video-share-modal-qr-code.png)

### OEmbed

An embed code that allows you to insert a video player in your website.

![Modal embed img](/assets/video-share-modal-embed.png)


When a user shares a Tube link, the application exposes information using the [OEmbed](https://oembed.com/) format allowing external websites to automatically embed the video (i.e inject the Tube player). Some platforms do this with any external link (Twitter, Mastodon...) but some other platforms may need additional configuration.

If your application supports OEmbed (for example [Moodle](https://moodle.org/) with the [Oembed Filter plugin](https://moodle.org/plugins/filter_oembed)), you could add the following code snippet in the OEmbed administration configuration to automatically embed Tube videos:

```
[
  {
    "schemes": ["https:\/\/instance.tube\/videos\/watch\/*"],
    "url":"https:\/\/instance.tube\/services\/oembed",
    "discovery":true
  }
]
```

### More options

For each option, you can:

  * set a time to start by clicking **Start at** and change the timestamp
  * if there are subtitles, choose to display one by default by clicking **Auto select subtitle**

You also have the possibility to customize a little more by clicking **More customization** button:

![More customization modal image](/assets/video-share-modal-more.png)

  * **Start at**: choose the timestamp you want to start the video;
  * **Stop at**: choose the timestamp you want to stop the video;
  * **Autoplay**: click if you want to start the video automatically;
  * **Muted**: click if you want the video to be played without sound (can be undone by user during watching);
  * **Loop**: click if you want the video to be repeated;
  * **Display video title** (only for **Embed**): unclick if you don't want to display the title of the video;
  * **Display privacy warning** (only for **Embed**): unclick if you don't want to display **Watching this video may reveal your IP address to others.** warning message;
  * **Display player controls** (only for **Embed**): unclick if you don't want to display play/pause etc. buttons.

## Download a video

You can download videos directly from the web interface of Tube, by clicking the <i data-feather="download"></i> **Download** button below the video:

![Modal presenting options to download a video](/assets/video-share-download.png)

A popup should appear with default values: direct download of the best quality video. You just have to click **Download** button to actually download the video on your computer.

By clicking the <i data-feather="chevron-down"></i> **Advanced** button, you can find detailled informations about the video like its format, video and audio stream.

![advanced download popup](/assets/use-download-modal-advanced.gif)

You also have the possibility to download the video using torrent:

?> **Direct Download**, which does what it says: your web browser downloads the video from the origin server of the video. <br />
**Torrent (.torrent file)**, where you need a WebTorrent compatible client to download the video not only from the origin server but also from other peers downloading the video

?> Depending on the instance, you can download the video in different formats. However, please make sure you are granted a licence compatible with the planned usage of the video beforehand.

## Settings for disconnected users

As disconnected user you can cutomize your interface too. To do so you have to click **Settings** on the left menu.

### Display settings

  * **Default policy on videos containing sensitive content:**
    * Do not list
    * Blur thumbnails
    * Display

    ?> With Do not list or Blur thumbnails, a confirmation will be requested to watch the video.
  * **Only display videos in the following languages/subtitles**: select which language you want to display in Recently added, Trending, Local, Most liked and Search pages.

### Video settings

  * **Help share videos being played**: the sharing system implies that some technical information about your system (such as a public IP address) can be sent to other peers, but greatly helps to reduce server load;
  * **Automatically play videos**: when on a video page, directly start playing the video;
  * **Automatically start playing the next video**: When a video ends, follow up with the next suggested video.

### Interface settings

To change the instance theme.

## Keyboard Shortcuts

You can use shortcuts to do some actions. To display the help menu, you can either click your avatar and click **<i data-feather="command"></i> Keyboard shortcuts** or click **Keyboard shortcuts** at the end of left hand menu if you're not connected to your account.
You also can use `?`.

  * `?`	Show / hide this help menu
  * `esc`	Hide this help menu
  * `/s`	Focus the search bar
  * `b`	Toggle the left menu
  * `g o`	Go to the discover videos page
  * `g t`	Go to the trending videos page
  * `g r`	Go to the recently added videos page
  * `g l`	Go to the local videos page
  * `g u`	Go to the videos upload page
  * `f`	Enter/exit fullscreen (requires player focus)
  * `space`	Play/Pause the video (requires player focus)
  * `m`	Mute/unmute the video (requires player focus)
  * `0-9`	Skip to a percentage of the video: 0 is 0% and 9 is 90% (requires player focus)
  * `↑`	Increase the volume (requires player focus)
  * `↓`	Decrease the volume (requires player focus)
  * `→`	Seek the video forward (requires player focus)
  * `←`	Seek the video backward (requires player focus)
  * `>`	Increase playback rate (requires player focus)
  * `<`	Decrease playback rate (requires player focus)
  * `.`	Navigate in the video frame by frame (requires player focus)
