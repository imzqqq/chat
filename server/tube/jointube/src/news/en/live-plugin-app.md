---
id: live-plugin-app
title: New external tools for Tube Live!
date: November 4, 2021
---

A Tube spiced up with third-party software is possible! Framasoft has funded and supported two external developments to bring interesting features to live videos. Here is an overview of these two tools, which will bring something new to your live stream.

### Tube Live App: being live from your smartphone

ℹ️ *This app is for content creators who want to broadcast live from a mobile phone.*

#### Why Tube Live App?

Tube is the free-libre solution to decentralize and share your videos, but... what if you could broadcast live from your mobile phone, whether it's a conference, a concert or a protest? Live streaming on an independent platform, through an easy to use application, didn't exist yet.

This is a new opportunity. Until now, you could only live stream on Tube from your computer [https://framablog.org/2021/01/07/tube-v3-its-a-live-a-liiiiive/](https://framablog.org/2021/01/07/tube-v3-its-a-live-a-liiiiive/)... which isn't really convenient when you are in the middle of a crowd!

![](/img/news/live-plugin-app/en/ENG-meme-sans-tube-live-app.jpg)

It's a fact: at Framasoft, we didn't have the skills to develop an Android application. So we looked externally and contacted Schoumi, a contributor to [Exodus Privacy](https://exodus-privacy.eu.org/en/), who accepted the project. After initial exchanges in April 2020, it was in May 2021 (the covid slowed us down a bit) that the Tube Live app started to be developed. After some improvements, we are now ready to show you the tool.


#### How do I live stream from my smartphone?

First step: you will need an account on a Tube instance that allows live streaming (see the instance's terms and conditions). Then download the Tube Live application, available on [Google Play store](https://play.google.com/store/apps/details?id=fr.mobdev.peertubelive) and on the [F-Droid app store](https://f-droid.org/en/packages/fr.mobdev.peertubelive/) (Note: this application is only available for Android mobiles).

We advise you to configure the application before going live (at the risk of losing some time choosing the settings - be warned!):

   * Add the web address of your instance and the login details of your account on the interface
   * Click on the *"+"* at the top right
   * Configure the live settings (Title, channel, privacy settings, resolution, publish a replay of not, etc.)
   * To go live, all you have to do now is press the big black button
   * The same button lets you end the live stream
   * If you have chosen to *"Automatically publish a replay when your live stream ends"*, remember to be patient. The publication delay can vary according to the length of the live, the quality/resolution or the computing power of the server hosting your instance

![](/img/news/live-plugin-app/en/ENG-Capture-ecran-PT-Live-App@3x.jpg)


#### Tube Live App needs you!

There are a few important limitations to point out. First of all, we have done very little testing on the app. In short, the paint is fresh, as they say, so bugs may occur.

Secondly, the application is not developed by Framasoft. Its evolution and improvement depend entirely on... you! You want to contribute to the improvement of the application? Here is how to participate:

   * Code of the application (for tech-savvy people): [https://codeberg.org/Schoumi/PeerTubeLive](https://codeberg.org/Schoumi/PeerTubeLive)
   * Help with the translation (you don't need to know how to code!): [https://hosted.weblate.org/projects/tube-live/app/](https://hosted.weblate.org/projects/tube-live/app/)
   * Support Schoumi, the developer of Tube Live: [https://en.liberapay.com/Schoumi/](https://en.liberapay.com/Schoumi/)


### Tube Live Chat: give your instance's audience the ability to chat during live streams

ℹ️ *Tube Live Chat is a plugin for administrators of a Tube instance.*

#### Why PeerTubeLive Chat?

More importantly, why didn't we add a chat feature when we developed the live stream feature for Tube? Well, it was a 100% conscious choice to focus only on live stream to begin with (we needed to manage priorities).

However, we soon noticed a developer working on a chat feature (and that was really convenient!): [John Livingston](http://john-livingston.fr/). So we offered to co-fund the improvement of his code to make it more user-friendly for the general audience.

The project was launched in April 2021. It was essential that the plugin communicate properly with the Tube core code, which meant improving the Tube plugin API. After much discussions between Chocobozzz (Tube developer) and John, the plugin is ready!

#### How do you install it?

You need to be an administrator of a Tube instance to install the plugin on your server, and follow these steps:

   * [Prosody](https://prosody.im/) Server Installation (version 0.11.9 or later). You can refer to [the documentation](https://github.com/JohnXLivingston/tube-plugin-livechat/blob/main/documentation/prosody.md)
   * Installation of the *"livechat"* plugin via the Tube administration interface
   * Choose in the configuration *"Prosody server controlled by Tube"*

The chat will now be displayed during live video broadcasts.


#### How do you use it?

Once the plugin is installed on the instance, the audience will be able to chat during the live videos they watch. However, this will only be possible from a computer.

If you are logged in to your Tube account, you will be recognized directly by the plugin. If you are not logged in, you can simply enter a nickname.

![](/img/news/live-plugin-app/en/PT-Live-Chat-Interface@2x.jpg)

You will then have direct access to the chat and its features. Moderation can be entrusted to one or more people.

![](/img/news/live-plugin-app/en/Zoom-Fonctionnalites@2x.jpg)


#### It can always be improved!

As the plugin is very young, there is room for improvement:

   * Its installation can be tedious, even cumbersome, depending on certain versions of the software
   * We have only tested the tool a little, for the moment
   * The interface could be more intuitive.

At the time of writing this news, John Livingston has just received a new €4000 sponsorship from the company [Code Lutin](https://www.codelutin.com/) to continue his work on Tube Live Chat. This is great news for this very useful plugin. We look forward to see it grow.

If you want to support this development, too, please go here: [https://liberapay.com/JohnLivingston/](https://liberapay.com/JohnLivingston/). End to contribute to the code, it's there: [https://github.com/JohnXLivingston/tube-plugin-livechat](https://github.com/JohnXLivingston/tube-plugin-livechat).


#### Contribute to the contributions

These two tools add great value to Tube and our freedom to distribute... livestreams! Their improvement now depends on your contributions.

We are increasingly aware of the richness of the Tube ecosystem: [clients](https://tube.docs.dingshunyu.top/use-third-party-application), [plugins](https://joinpeertube.org/plugins-selection)... Numerous external contributions offer new options and new freedoms to the community: thank you very much!

If we have been able to finance and support these two external developments, it is partly thanks to the NLnet grant we mentioned when announcing the path to [v4 of Tube](https://joinpeertube.org/news#roadmap-v4), and partly thanks to your donations which finance all the actions of Framasoft [https://soutenir.framasoft.org/en/](https://soutenir.framasoft.org/en/).

Thanks again for your trust!
















