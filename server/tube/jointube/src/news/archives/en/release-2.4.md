---
id: release-2-4-0
title: Tube 2.4 is out!
date: September 08, 2020
---

Hi everybody,

In mid-july, we released Tube 2.3 and now here is the 2.4 version. This latest release implements features we've already told you about in the last news as we still follow our [roadmap](https://joinpeertube.org/roadmap). Let us present you in details our latest innovations!

#### More efficient moderation tools

It was already possible to report videos, and now you can also report accounts and comments. It's very simple to inform moderators of an instance about a problematic account. Go to the account page, click on the 3 horizontal dots on the right of the profile name, and select _Report this account_. A window will appear in which you can indicate the issue encountered and/or describe it. In the same way, it is now easy to report a comment: click on the _Options_ menu under the comment you wish to report and select _Report this comment_.

![](/img/news/release-2.4/en/report-account.png)

Reports are also sent to moderators of the instance where the reported item (video, account or comment) is hosted. They are displayed in the _Administration_ menu - _Moderation_ tab - _Reports_ page. The new video player on this page makes it easier to accept or reject a report. Moderators can also send messages to local accounts (registered on the same instance) that made reports: they just have to go to the column _Messages_ and click on the little bubble.

![](/img/news/release-2.4/en/reporter-messages.png)

Users who report an item are notified when their report status changed (accepted/rejected) and when they get a message from moderators. They can see the list of reports they made in _Account Settings_ - _Misc_ tab - _My Abuse Reports_ page. In this page, you can send a message to your instance moderators by clicking on the bubble-shaped icon in the _Messages_ column.

![](/img/news/release-2.4/en/moderation-team-messages.png)

#### Playlist system improved

If it was easy to embed a Tube video on a website or to share it on social media, it wasn't possible to embed playlists. So we worked on their integration on third party websites. It's now very easy to share playlists with the embed code:

![](/img/news/release-2.4/en/share-playlist.png)

Tube's playlists already allow you to list just one clip of a video (and not the whole video), but they did not allow you to include several clips of one video in the same playlist. It is now possible and really simple: go on the page of the video, click on the _Save_ button under the video, tick a playlist and then click on the <em>+</em> icon next to the playlist name. An additional field will allow you to enter different _time-codes_ you wish to add. Playlists can be very useful remix tools, e.g. for educational purposes.

![](/img/news/release-2.4/en/save-to-playlist.png)

#### An annotation Tube plugin

This [annotation Tube plugin](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-video-annotation) allows you to display information in the player at a given time of a video. To do so, once the plugin is installed on the instance, you just have to go on the uploaded video information page, open the _Plugin settings_ tab and then add your annotations and their time code. The placement of the annotations is in the player top right corner by default, but you can choose other locations. The annotations format must respect specific rules:

<ul>
  <li><em>start</em>: when to show the annotation</li>
  <li><em>stop</em>: when to hide the annotation</li>
  <li><em>options</em>: set options for your annotation (e.g. position)</li>
  <li><em>HTML</em>: content of your annotation (in html)</li>
</ul>

![](/img/news/release-2.4/en/annotation-format.png)

This may give for example:

![](/img/news/release-2.4/en/player-annotations.png)

#### More pleasant and accessible interfaces

We made many improvements to the Tube interface in this new release. We would like to take this opportunity to thanks two contributors, [@Kimsible](https://github.com/kimsible) and [@Rigelk](https://github.com/rigelk), who developed/improved the following components.

The comment window composer has been improved: there is now a small button indicating that you can use the Markdown format. A delete/rewrite feature on comments with no replies, that we already know from other software in the fediverse like Mastodon, has also been implemented.

The administration menu of an instance is clearer. When one of the tabs of this menu bar is open, the other tabs are grayed out, which helps you to find your way around more easily. In the _Users_ tab, the users' table layout has been modified: the action button is now on the left, to facilitate management on mobile devices, the video quota is now displayed as a progress bar and the role of each user is immediately recognizable with the use of different colors.

<figure>
  <img loading="lazy" src="/img/news/release-2.4/en/before-user-table.png" alt="">
  <figcaption>Before</figcaption>
</figure>

<figure>
  <img loading="lazy" src="/img/news/release-2.4/en/after-user-table.png" alt="">
  <figcaption>After</figcaption>
</figure>

We have also improved the video edition form (that you can access when you upload or update a video). The menu to select the channel now displays the channel icon and the language selector displays the language currently set by the instance on top of the list. Finally, the privacy selector provides a clearer description of the available choices.

![](/img/news/release-2.4/en/select.png)

And also:

We have also improved Tube performances: loading an instance's interface for the first time is now faster. We also have fixed a few bugs on the player that were reported to us.

This new release includes many other improvements. You can see the complete list on https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

In other news, we are going to change the moderation policy of the public instances index we maintain on https://instances.joinpeertube.org/instances. The new moderation terms are stated in the header and will take effect on Monday September, 21.

Thanks to all Tube contributors!
Framasoft
