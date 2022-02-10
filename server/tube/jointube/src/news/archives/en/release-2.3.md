---
id: release-2-3-0
title: Tube 2.3 is out!
date: July 24, 2020
mastodon: https://framapiaf.org/@tube/104584600044284729
twitter: https://twitter.com/joinpeertube/status/1287648749850955776
---

Hi everybody,

In early june, we released Tube 2.2 and less than two months later we are releasing this 2.3 version. We are proud to  move forward so fast on Tube development! As we continue to follow our [roadmap](https://joinpeertube.org/en_US/roadmap), this release incorporates the features we told you about in the latest news. Let's look around and see what it brings us...

#### Global video search is now available

Roadmap step 1, the video search on the entire vidiverse is now accessible to everyone. By creating this index engine (a tool to index all videos and channels of predefined Tube instances), we are now allowing instances administrators to set the search bar of their instance to search on the entire (or a portion of) the vidiverse.

For more details about this feature, please read our explanations on https://joinpeertube.org/news#roadmap-v3-part-1-find-videos-global-search.

![](/img/news/release-2.3/en/global-search.png)

#### Information banners on instances

Broadcast message system that allows instances administrators to display information to people who visit it is now active. This is a handy feature to indicate that your instance will be in maintenance on a certain day and that the service might be disrupted... or anything else!

We give you the possibility to display 3 different types of messages:

- <i>info</i>: blue text on light blue background
- <i>warning</i>: brown text on light yellow background
- <i>error</i>: red text on light red background

![](/img/news/release-2.3/en/banner-information.png)

![](/img/news/release-2.3/en/banner-warning.png)

![](/img/news/release-2.3/en/banner-error.png)

#### Many improvements on accessibility

Caroline Chuong ([@Pandoraaa](https://github.com/Pandoraaa)), consultant at [Octo Technology](https://www.octo.com/) has proposed several contributions to improve Tube accessibility. These contributions are essential to bring Tube's interfaces accessible to everyone. And as we are aware not being accessibility experts, we are really delighted that contributors give us a hand on this aspect. A big thank to Caroline for her contributions.

#### New features on content moderation

As indicated in our roadmap, we have spent time improving and adding moderation tools. Tube 2.3 includes the ability for instances administrators to delete all comments from a fediverse account with a single click. It is also possible to mute an account directly from a video thumbnail.

In terms of interface, the video report window has been greatly improved by @rigelk. As a reminder, the video reporting feature is accessible if you have an account and are logged in. It is therefore only possible to report videos that you see from the instance where you are registered: either because this video is hosted on your instance, or because your instance is federated to the instance where the video was uploaded.

This new video report window now includes a checklist of possible reasons for the report and offers you a free writing field to specify your report if necessary.

<figure>
  <img loading="lazy" src="/img/news/release-2.3/en/report-2.2.png" alt="">
  <figcaption>Tube 2.2 report window</figcaption>
</figure>

<figure>
  <img loading="lazy" src="/img/news/release-2.3/en/report-2.3.png" alt="">
  <figcaption>Tube 2.3 report window</figcaption>
</figure>

#### Plugins to block or mute instances

The [auto-mute plugin](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-mute) allows to automatically hide accounts and instances from a public list while the [videos-auto-block plugin](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-block-videos) can automatically block videos from a public list.

We haven't yet identified any Tube instance administrator who use these plugins and generate a public list. But if there is, please let us know on our [forum](https://framacolibri.org/t/lists-for-plugin-auto-block-videos-plugin-auto-mute/).

#### And also:

Tube is now available in 2 new languages: Vietnamese and Kabyle!

This new release includes many other improvements. You can see the complete list on https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

Thanks to all Tube contributors!
Framasoft
