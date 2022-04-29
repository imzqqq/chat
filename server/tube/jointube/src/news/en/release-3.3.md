---
id: release-3.3
title: Tube v3.3 is out!
date: July 20, 2021
---

Hi everybody,

With this v3.3, we offer you new features to further customise your instances, playlists' addition to the search, smaller URLs and many other things...

#### Customise your instance's homepage

The great novelty of this 3.3 version is the ability to create a custom homepage for each Tube instance. This will allow instance administrators to indicate more clearly what their instance is, what content is available, how to subscribe or propose content selections (non-exhaustive list). The system setup is flexible enough to allow everyone to publish what they want.

![img](/img/news/release-3.3/en/EN-homepage-900px.png "the homepage created on our test instance")

To customise your homepage, once you are logged in, simply go to the *Administration* menu, *Configuration* section, *Homepage* tab. There, an empty block allows you to add the elements you want, in Markdown or HTML format. Many elements are available via custom HTML tags created for the occasion. To discover them, do not hesitate to consult [our documentation](https://tube.docs.imzqqq.top/api-custom-client-markup).

![img](/img/news/release-3.3/en/EN-admin-homepage.png "code corresponding to the homepage displayed above")

This will allow you to display:
  * a custom button
  * an embed player for videos or playlists
  * a video, playlist or channel miniature
  * an automatically updated list of videos (with ability to filter by language, category...)

You can also use containers to display elements (videos, channels, accounts, playlists) in a column or in a row and thus offer editorialized selections in an attractive layout.

In order to allow as many people as possible to see your homepage, make sure you set it as the default page. To do this, go to the *Administration* menu, *Configuration* section, *Basic* tab and select "Home" in *Landing page* field.

These customisation options are now also available in the instance description page (*About* menu).

#### Ability to search for playlists

Whether browsing Tube or using [Sepia Search engine](https://sepiasearch.org/), playlists are now displayed in the search results.

![img](/img/news/release-3.3/en/EN-playlists-dans-SepiaSearch.png)

#### Smaller public URLs

We have been asked a lot about this feature, so we have set up a system to shorten some public URLs. Indeed, the identifiers were a bit long.

From now on, the unique identifier of the video https://peertube2.cpy.re/videos/watch/d10c66b2-8fb2-4fa6-a6e2-bdcb3dab79d2 (36 characters) is shortened to https://peertube2.cpy.re/w/rPdWN4SKgiKWa7LWX3ooMq (22 characters).

And the playlist URL: https://peertube2.cpy.re/videos/watch/playlist/8524e54a-67da-4f78-a296-de844d224952 becomes https://peertube2.cpy.re/w/p/hrAdcvjkMMkHJ28upnoN21.

As you can notice, we have also shortened our URLs syntax: we use `/w/` instead of `/videos/watch/` and `/w/p/` instead of `/videos/watch/playlist/`. And we have extended this syntax to accounts and channels: `/a/` instead of `/accounts/` and `/c/` instead of `/video-channels/`.

Of course, the old URLs are still supported.

#### An adapted interface for RTL (right to left) languages

Tube now supports RTL layout if you set Tube interface to one of the right to left languages. The menu moves to the right and thumbnails are right justified.

![img](/img/news/release-3.3/en/FR-RTL-arabe.png)


#### And also:

We have made some changes to improve Tube performance. Retrieving information from a video is 2 times faster and we have optimized queries within the federation. We are currently trying to identify performance issues that large Tube instances (with many users, videos, viewers or federated with many instances) may hit. If you see any scalability issues with your instance, don't hesitate to explain it to us on [our forum](https://framacolibri.org/c/tube/38).

We also made several changes to Tube code so the plugins can add, update or remove links from the left menu. We hope to see new plugins released soon.

We have made many other improvements in this new version. You can read the whole list on https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

Thanks to all Tube contributors!
Framasoft
