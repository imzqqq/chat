---
id: release-3.4
title: Tube v3.4 is out!
date: September 08, 2021
---

Hi everybody,

At Framasoft, as we didn't spend the whole summer basking in the sun, we took time to improve our favourite software. With this version 3.4, we offer you new features to make your Tube use more pleasant. Let's look around and see what it brings us...

#### Filters on every Tube pages

We have added a video filtering system on video pages of accounts and channels, but also on common video pages available on each instance (Trending / Recently added / Local videos).

Since v3.2, it was already possible to sort videos display according to several criteria: date of publication, most viewed, most liked, longest, etc. With this new filtering system, you can sort videos according to:

 * video language,
 * sensitive content level,
 * video scope: local videos (from the instance you are on) or federated videos (from the instances you follow),
 * video type: live video or VOD - or both,
 * video categories.

![img](/img/news/release-3.4/en/EN-filters.png)

To do this, just click on the *More filters* button at the top left of each page where videos are listed and complete the fields. You may have noticed that next to this *More filters* button, default filters of your account already appear. Look at the screenshot above where filters like *Sensitive content: hidden* and *Scope: Federated* are activated. Very useful to find your way around!

#### Subscribe as an instance to a channel or an account

As a Tube instance administrator, you could federate your instance with others to create your own federation bubble. With 3.4, you can follow an account or a channel without necessarily federating with the instance that hosts it.

To do this is very simple: you just have to go to the *Administration* menu, *Federation* tab, *Following* section. The list of instances to which your instance has subscribed appears. If you click on the orange *Follow* button, you can then manually add channel or account handles. Then, users of your instance will be able to see content published by this channel (or account).

![img](/img/news/release-3.4/en/EN-abo-chaine.png)

#### Filter videos search results of an instance

You could already use many filters when searching for videos. Now, you have the possibility to filter videos by indicating a specific instance for which you wish to limit your search. Let's take an example: you search for videos on permaculture and you have identified content on xxxx.xyz instance which have a great editorial work. Enter the instance URL you want to search in the "Tube Instance Host" field. You will then only see videos about permaculture available on this instance.

![img](/img/news/release-3.4/en/EN-searchfilter-instance-host.png)

#### Video Player Update

We have updated the HLS.js library that is used by the Tube video player. Tube now detects and remembers your bandwidth. Previously, the player used the "medium-quality" by default and you could have noticed a quality change after a few seconds if you had a good network connection. Now, the player automatically identifies your last bandwidth and chooses the most suitable resolution. And if you still want to choose the resolution of the video you are watching, the change is immediate.

#### And also:

Tube 3.4 natively supports saving video files in `object-storage` systems (`s3`). Still in beta, this new feature allows Tube instance administrators to host videos of their instance with this on-demand storage system. Thus, administrators no longer have to worry about the storage size of their server.

We are still trying to identify performance issues that large Tube instances (with many users, videos, viewers or federated with many instances) may hit. If you see any scalability issues with your instance, don't hesitate to explain it to us on [our forum](https://framacolibri.org/c/tube).

We have made many other improvements in this new version. You can read the whole list on https://github.com/Chocobozzz/Tube/blob/develop/CHANGELOG.md.

Thanks to all Tube contributors!
Framasoft
