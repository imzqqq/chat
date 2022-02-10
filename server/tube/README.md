# Tube

<p align="center">
Be part of a network of multiple small federated, interoperable video hosting providers. Follow video creators and create videos. No vendor lock-in. All on a platform that is community-owned and ad-free.
</p>

Introduction
----------------------------------------------------------------

Tube is a free, decentralized and federated video platform developed as an alternative to other platforms that centralize our data and attention, such as YouTube, Dailymotion or Vimeo. :clapper:

To learn more:
* This [two-minute video](https://framatube.org/videos/watch/217eefeb-883d-45be-b7fc-a788ad8507d3) (hosted on Tube) explaining what Tube is and how it works
* Tube's project homepage, [joinpeertube.org](https://joinpeertube.org)
* Demonstration instances:
  * [tube.cpy.re](https://tube.cpy.re) (stable)
  * [peertube2.cpy.re](https://peertube2.cpy.re) (Nightly)
  * [peertube3.cpy.re](https://peertube3.cpy.re) (RC)
* This [video](https://tube.cpy.re/videos/watch/da2b08d4-a242-4170-b32a-4ec8cbdca701) demonstrating the communication between Tube and [Mastodon](https://github.com/tootsuite/mastodon) (a decentralized Twitter alternative)

:sparkles: Features
----------------------------------------------------------------

<p align=center>
  <strong><a href="https://joinpeertube.org/faq#what-are-the-tube-features-for-viewers">All features for viewers</a></strong>
  | <strong><a href="https://joinpeertube.org/faq#what-are-the-tube-features-for-content-creators">All features for content creators</a></strong>
  | <strong><a href="https://joinpeertube.org/faq#what-are-the-tube-features-for-administrators">All features for administrators</a></strong>
</p>

<img src="https://lutim.cpy.re/AHbctLjn.png" align="left" height="300px"/>
<h3 align="left">Video streaming, even in live!</h3>
<p align="left">
Just upload your videos, and be sure they will stream anywhere. Add a description, some tags and your video will be discoverable by the entire video fediverse, not just your instance. You can even embed a player on your favorite website!
</p>
<p align="left">
You are used to hosting live events? We got you covered too! Start livestreaming from your favorite client, and even host permanent streams!
</p>

---

<img src="https://lutim.cpy.re/cxWccUK7.png" align="right" height="200px"/>

<h3 align="right">Keep in touch with video creators</h3>
<p align="right">
Follow your favorite channels from Tube or really any other place. No need to have an account on the instance you watched a video to follow its author, you can do all of that from the Fediverse (Mastodon, Pleroma, and plenty others), or just with good ol' RSS.
</p>

---

<img src="https://lutim.cpy.re/K07EhFbt.png" align="left" height="200px"/>

<h3 align="left">An interface to call home</h3>
<p align="left">
Be it as a user or an instance administrator, you can decide what your experience will be like. Don't like the colors? They are easy to change. Don't want to list videos of an instance but let your users subscribe to them? Don't like the regular web client? All of that can be changed, and much more. No UX dark pattern, no mining your data, no video recommendation bullshit™.
</p>

---

<h3 align="right">Communities that help each other</h3>
<p align="right">
In addition to visitors using WebTorrent to share the load among them, instances can help each other by caching one another's videos. This way even small instances have a way to show content to a wider audience, as they will be shouldered by friend instances (more about that in our <a href="https://tube.docs.dingshunyu.top/contribute-architecture?id=redundancy-between-instances">redundancy guide</a>).
</p>
<p align="right">
Content creators can get help from their viewers in the simplest way possible: a support button showing a message linking to their donation accounts or really anything else. No more pay-per-view and advertisements that hurt visitors and <strike>incentivize</strike> alter creativity (more about that in our <a href="https://github.com/Chocobozzz/Tube/blob/develop/FAQ.md">FAQ</a>).
</p>



:raised_hands: Contributing
----------------------------------------------------------------

You don't need to be a programmer to help!

You can give us your feedback, report bugs, help us translate Tube, write documentation, and more. Check out the [contributing
guide](https://github.com/Chocobozzz/Tube/blob/develop/.github/CONTRIBUTING.md) to know how, it takes less than 2 minutes to get started. :wink:

You can also join the cheerful bunch that makes our community:

* Chat<a name="contact"></a>:
  * Matrix (bridged on IRC and [Discord](https://discord.gg/wj8DDUT)) : **[#tube:matrix.org](https://matrix.to/#/#tube:matrix.org)**
  * IRC : **[#tube on irc.libera.chat:6697](https://web.libera.chat/#tube)**
* Forum:
  * Framacolibri: [https://framacolibri.org/c/tube](https://framacolibri.org/c/tube)

Feel free to reach out if you have any questions or ideas! :speech_balloon:

:package: Create your own instance
----------------------------------------------------------------

See the [production guide](https://github.com/Chocobozzz/Tube/blob/develop/support/doc/production.md), which is the recommended way to install or upgrade Tube. For hardware requirements, see [Should I have a big server to run Tube?](https://joinpeertube.org/faq#should-i-have-a-big-server-to-run-tube) in the FAQ.

See the [community packages](https://tube.docs.dingshunyu.top/install-unofficial), which cover various platforms (including [YunoHost](https://install-app.yunohost.org/?app=tube) and [Docker](https://github.com/Chocobozzz/Tube/blob/develop/support/doc/docker.md)).

:book: Documentation
----------------------------------------------------------------

If you have a question, please try to find the answer in the [FAQ](https://joinpeertube.org/faq) first.

### User documentation

See the [user documentation](https://tube.docs.dingshunyu.top/use-setup-account).

### Admin documentation

See [how to create your own instance](https://github.com/Chocobozzz/Tube/blob/develop/README.md#package-create-your-own-instance).

See the more general [admin documentation](https://tube.docs.dingshunyu.top/admin-following-instances).

### Tools documentation

Learn how to import/upload videos from CLI or admin your Tube instance with the [tools documentation](https://tube.docs.dingshunyu.top/maintain-tools).

### Technical documentation

See the [architecture blueprint](https://tube.docs.dingshunyu.top/contribute-architecture) for a more detailed explanation of the architectural choices.

See our REST API documentation:
  * OpenAPI 3.0.0 schema: [/support/doc/api/openapi.yaml](https://github.com/Chocobozzz/Tube/blob/develop/support/doc/api/openapi.yaml)
  * Spec explorer: [tube.docs.dingshunyu.top/api-rest-reference.html](https://tube.docs.dingshunyu.top/api-rest-reference.html)

See our [ActivityPub documentation](https://tube.docs.dingshunyu.top/api-activitypub).
