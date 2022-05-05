---
id: release-3.1
title: Tube v3.1 is out!
date: March 24, 2021
---


<p>Hi everybody,</p>

<p>
  After releasing Tube v3 in early January, it's time for a new main version with plenty of improvements and new
  features. Here are some explanations on what this v3.1 brings us.
</p>

<h4>Better transcoding features</h4>

<p>Transcoding? It's the process of converting an audio or video file from one encoding format to another so
  media files can be visible on different platforms and devices. On Tube, we use the FFmpeg program to transcode the
  videos you upload. You probably have noticed the warning message when you post a video saying "This video is being
  transcoded, it may not work properly yet". In fact, until transcoding is complete, your video may not be seen from all
  your devices.</p>

<p>
  Until now, transcoding rules were the same for every Tube instance: you couldn't modify them. From this v3.1,
  <strong>you can create transcoding profiles by installing plugins on instances</strong>. Instance administrators can
  select a transcoding profile adapted to their needs after installing a plugin. We hope many of them will create such
  plugins to customize their FFmpeg settings.
</p>

<p>
  For example, you can now create a specific transcoding profile that highlights live videos (over other videos) in
  terms of bandwidth. In addition, the administrator of a Tube instance specialized in broadcasting musical contents
  will be happy to know he can create a high quality audio profile.
</p>

<p>
  This v3.1 also changes the way transcoding tasks are managed. Before, and for each instance, transcoding occured
  chronologically as users were uploading videos. Thus when a video maker was uploading several videos in a row, he
  prevented video transcoding from other users of the instance. Therefore we have <strong>modified the priority
    management system for these transcoding jobs</strong> so that when a user upload several files at the same time, the
  transcoding of some of his videos is de-prioritized (they are on standby) if another user upload a file. In other
  words, administrators can give an higher priority to optimize jobs and can decrease priority of transcoding jobs
  depending on the amount of videos uploaded by the user in the last 7 days. This prevents one single user from blocking
  other people's contents' uplaods: it's more fair. Besides, administrators can now see the progress of video
  transcoding in the list of on-going jobs on their instance.
</p>

<figure>
  <img loading="lazy" src="/img/news/release-3.1/en/jobs.png" alt="">
</figure>

<p>
  Finally, instance administrators can now <strong>choose how many simultaneous transcoding jobs</strong> they support.
  Of course, transcoding several videos at once requires an important computing power. If you want to allow the
  transcoding of more than one video at once on your instance, make sure you have the proper hardware.
</p>

<h4>More and more pleasant interfaces</h4>

<p>
  As we know that Tube interface is not always easy to understand, we still improve it so that everyone feels
  comfortable using this tool.
</p>

<p>
  The disappearance of the "most liked" category, in the left menu, is the most visible change. In return, <strong>we
    added to the "trending" category 3 options for sorting videos</strong>:
</p>

<ul>
  <li><em>hot</em>: a selection of recent videos with the most interactions</li>
  <li><em>views</em>: videos with the most views in the last 24 hours</li>
  <li><em>likes</em>: the most liked videos</li>
</ul>

<figure>
  <img loading="lazy" src="/img/news/release-3.1/en/trending.png" alt="">
</figure>

<p>
  We have modified some elements in the Administration menu (available for instance administrators only). For example in
  the "users" tab, the "Create user" button is now on the left side to be more visible. Also administrators can now
  customize the value of video quota (total and daily) of each user.
</p>

<figure>
  <img loading="lazy" src="/img/news/release-3.1/en/quota.png" alt="">
</figure>

<h4>And also:</h4>

<p>
  You can now easily <strong>subscribe to an account hosted on a different instance from the one you have registered
    on</strong> (remote account) by clicking on the "subscribe" button under a video and then by entering your Tube
  ID (username@domain).
</p>

<figure>
  <img loading="lazy" src="/img/news/release-3.1/en/subscribe.png" alt="">
</figure>

<p>
  Instance administrators can now <strong>choose how many simultaneous import jobs</strong> they support (whether these
  imports are realized through a URL or a torrent). This prevents big instances from having long waiting lists.
</p>

<p>
  Finally, we have also implemented a system of async torrent creation on video upload in order to fix some upload
  errors bugs.
</p>

<p>
  We have made many other improvements in this new version. You can read the whole list (in English) on <a
    target="_blank"
    href="https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md">https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md</a>.
</p>

<p>
  <translate>Thanks to all Tube contributors!</translate>
  <br />
  Framasoft
</p>
