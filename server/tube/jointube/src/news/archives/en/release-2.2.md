---
id: release-2-2-0
title: 'Tube 2.2 is out!'
date: 'June 03, 2020'
mastodon: 'https://framapiaf.org/@tube/104279652112851748'
twitter: 'https://twitter.com/joinpeertube/status/1268132526226378752'
---

Hi everybody,

After showcasing our roadmap for Tube V3, we are happy to announce that version 2.2 is out. Let's look around and see what it brings us...

#### Many improvements interface-wise

Version 2.2 includes many improvements to make Tube's interfaces more pleasant to use. The most visible improvement is definitely the __search bar__. Located in the top right, this search bar now offers indications to make an efficient search. For instance, you can search for a channel with its name but also by using the @channel_id@domain form.

![](/img/news/release-2.2/en/search.png)

When you want to download a video on Tube (to do this, you just need to click on the three horizontal dots  located in the menu under the video, and select Download), a window now shows detailed information about the file. This new feature is only active for videos uploaded after the release of version 2.2.

![](/img/news/release-2.2/en/download.png)

Users who aren't logged in now have a __Settings button in the left menu__ that will let them customize how they use Tube:

- using P2P or not
- displaying sensitive video thumbnails or not
- filtering videos based on language
- choosing an interface theme
- activating automatic video playback or not

![](/img/news/release-2.2/en/settings.png)

Another very convenient improvement: you can now drag and drop to upload a video file. No need to click "Select file" anymore, you only need to use your mouse, touchpad or fingers to <strong><em>drag and drop</em></strong> your video file from your hard drive.

__Video imports via URL have been improved__: you can now import subtitles and even get the video license and language. Quite useful when you want to duplicate a video from another Tube instance while keeping all the metadata.

![](/img/news/release-2.2/en/import-url.jpg)

We already offered a markdown editor to, for instance, __format text in the <em>Description</em> field__ when you upload a video. We have improved upon this editor to make it more clear, and we added a fullscreen mode.

![](/img/news/release-2.2/en/description.jpg)

Tube also allows you to __import audio files__. Quite a nice feature to share musical arrangements or podcasts without having to make a clip. When you upload the audio file, it is even possible to add an illustrative picture that will be fused with the file. However, be careful because the picture you choose will be definitive and you will not be able to change it.

As the administrator of an instance, you will have acces to a __new interface to manage duplicated videos via the redundancy system__. You can now see a list of the videos from your instance that have duplicated onto other instances. But most importantly, we now show you a list of videos you have duplicated and we show you how much space they take with graphs (aren't our pie charts pretty?).

![](/img/news/release-2.2/en/redundancies.jpg)

__The video abuses management interface__ has also been improved: we added research filters, quick actions you can take regarding videos and accounts, video thumbnails in the chart, quick access to embed, etc.

![](/img/news/release-2.2/en/moderation.png)

The majority of these improvements have come to be thanks to external contribution from @rigelk et @kimsible. Big thanks to the two of them!

#### New plugins for varied experiences

In this new version, we have made it possible for plugins to __define external authentication methods__ and developed 3 authentication plugins:

<ul>
  <li>
    <a href="https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-ldap" target="_blank">LDAP</a>
  </li>

  <li>
    <a href="https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-openid-connect" target="_blank">OpenID</a>
  </li>

  <li>
    <a href="https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-saml2" target="_blank">SAMLv2</a>
  </li>
</ul>

Thus, it is now possible to authenticate users via an external server (OpenID or SAMLv2 compatible) or via an LDAP directory. This work was made possible thanks to funding from the _"Direction du Numérique pour l'Éducation du Ministère de l'Éducation et de la Jeunesse" (France)_.

We also added __some <em>hooks</em> (entry points towards actions lists) to the plugins system__, which will allow developers to create new plugins dedicated to moderation:

- deleting a video
- validating a URL/torrent import
- ability to hide/unhide an instance or an account
- blacklisting or unblacklisting a video

From these additions a plugin was born, still in testing to this day: [tube-plugin-auto-mute](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-mute),which allows you to automatically hide accounts and instances depending on a public list. If you want other admins to be able to use your public list of accounts and instances to hide, do not hesitate to make a pull request on the plugin's README.

#### And also:

This v2 adds __HTML support (on top of text mode) in emails__ sent by Tube, making them less austere and avoiding bugs where links would get shortened.

![](/img/news/release-2.2/en/mail.jpg)

The admin of a Tube instance can choose to auto follow other instances. Before, the majority of admins used this feature to automatically follow all the instances in the public index (which causes moderation issues). It is now possible for anyone to share a list of instances (on github, gitlab, pastebin, etc.) so that the admins of a Tube instance can use that list's web address to make their instance automatically follow the instances of that shared list. This lets users create auto follow lists within small groups.

And finally, we improved the embed API, allowing websites that embed a Tube video to have better control over the player:knowing the video's length, knowing when playback is over, exporting video subtitles, indicating a specific subtitle to activate. More info on https://tube.docs.imzqqq.top/#/api-embed-player.

This new release includes many other improvements. You can see the complete list (in English) on https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

Thanks to all those who contribute to Tube!
Framasoft.
