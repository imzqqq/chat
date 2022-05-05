---
id: release-3.0rc
title: The Release Candidate for Tube v3 is live!
date: December 16, 2020
---

<p>Hi everybody,</p>

<p>
  Tube v3 is almost complete, and we have just published <a
    href="https://github.com/Chocobozzz/Tube/releases/tag/v3.0.0-rc.1">a RC (release candidate)</a> for admins who
  like to try it and give us feedback so we can publish a beautiful v3 in early January.
</p>

<h4>A barebone yet functioning p2p live streaming</h4>

<p>
  The big feature of this v3 will be live streaming, and we're proud to say that it works fine! 🎉🎉🎉
</p>

<p>
  We've had lots of tests and feedback from pioneers (shout out to <a
    href="https://framacolibri.org/t/fonctionnalite-live-retour-dutilisation/10070">Le Canard Réfractaire</a> for their
  help), and we are now confident that a Tube p2p live stream can scale up to hundreds of simultaneous users (but
  not thousands - not yet).
</p>

<p>
  In the different tests we've had, we have managed to keep lag between 30s to 1mn. To our knowledge, peer-to-peer live
  broadcasting will induce an incompressible lag between the streamer and the audience. Ultimately, this lag will depend
  on the server charge (how many live streams are happening at the same time) and bandwidth.
</p>

<p>
  We encourage admins to enable live transcoding for live streaming. Even though it will take processing power and
  induce some lag, it is really essential to facilitate the experience both for streamers (who can use basic OBS
  settings) and for viewers (who can watch in their preferred video resolution).
</p>

<p>
  With this v3, admins will have the option to enable live streaming, for which they can set a maximum number of ongoing
  and awaiting live streams (per user and for the whole instance). They will also be able to set up a maximum duration
  limit for live streams.
</p>

<figure>
  <img loading="lazy" src="/img/news/release-3.0rc/en/2020-05-21_Peertube-Livestream_by-David-Revoy_hires.jpg"
    alt="">
</figure>

<h4>Two ways to set up your live</h4>

<p>
  As you can see <a href="https://tube.docs.imzqqq.top/use-create-upload-video?id=publish-a-live-in-tube-gt-v3">in
    our <i>how to go live</i> documentation</a>, Tube streamers will need a broadcasting software (we recommend the
  Free-Libre software <a href="https://obsproject.com">OBS</a>), and use the live RTMP key.
</p>

<p>
  Setting up a new livestream is like uploading a new video. The default setting will get you one Tube URL, one
  video container (with description and thumbnail and tags...), and one RTMP Key for each of your livestreams. This
  setting is useful if you want to host multiple lives simultaneously on you channel. When your live is finished, it
  will be replaced by the replay (if both the instance admin and the content creator have activated this setting).
</p>

<p>
  Streamers will also have the option to enable "permanent live". It will work more like Twitch does: your permanent
  live URL and video container will correspond to a single RTMP key. You can go live and stop it whenever you want, the
  live will be broadcast on the same URL. This setting does not allow saving a video for replay, though.
</p>

<h4>There is more than live in life </h4>

<p>
  This v3 comes packed with many changes and improvements.
</p>

<p>
  The sidebar menu has been completely reworked, thanks to the UX design work we did with Marie Cécile Paccard. It is
  now way easyier to interact with you profile or to distinguish pages displaying what's in your Library from what's on
  your instance federation bubble.
</p>

<p>
  Notifications have been improved: now, when an account has been muted (either by a user or an admin) the notification
  of their actions are deleted, which comes really handy when someone is having an activity peak and you d'ont want to
  clean your notifications one by one ;).
</p>

<p>
  Administrators and moderators have, once again, usefull new tools in this update. There's a new page to facilitate
  comment moderation, batch actions, the option to delete all comments of an account, or to see unlisted videos uploaded
  by an account on the instance you moderate.
</p>

<figure>
  <img loading="lazy" src="/img/news/release-3.0rc/en/tube-v3rc.jpg" alt=>
</figure>

<h4>Warning: fresh paint</h4>

<p>
  There is still a lot to tell about this v3, and people who contributed to make it happen. That's a good thing, because
  we will talk about it in January, for the v3 release post.
</p>

<p>
  We just wanted to describe briefly what you could expect from this new Tube version, and present you our progress
  with this release candidate.
</p>

<p>
  In the meantime, if you try and test <a
    href="https://github.com/Chocobozzz/Tube/releases/tag/v3.0.0-rc.1">Peertube v3 RC</a> and its live feature,
  please be sure to give us some feedback on the code respository issues or <a
    href="https://framacolibri.org/c/tube/38">on our forum</a>.
</p>

<p>
  <translate>Have a great holydays and stay safe,</translate>
  <br />
  Framasoft
</p>
