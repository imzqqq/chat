---
id: release-1-4-0
title: 'Tube 1.4 is out!'
date: 'September 25, 2019'
---

Hi everybody,

Peertube 1.4 just came out! Here's a quick overview of what's new…

#### Plug-in system

Since Tube's launch, we have been aware that every administrator and user wishes to see the software fulfill their needs. As Framasoft cannot and will not develop every feature that could be hoped for, we have from the start of the project planned on creating a plug-in system.

We are pleased to announce that the foundation stones of this system have been laid in this 1.4 release! It might be very basic for now, but we plan on improving it bit by bit in Peertube's future releases.

Now, this system allows each administrator to __create specific plug-ins__ depending on their needs. They may install extensions created by other people on their instance as well. For example, it is now possible to install community created graphical themes to change the instance visual interface.

#### A better interface

We strive to improve Tube's interface by collecting users' opinions so that we know what is causing them trouble (in terms of understanding and usability for example). Even though this is a time-consuming undertaking, this new release already offers you a few modifications.

First of all, we realized that most people who discover Tube have a hard time understanding __the difference between a channel and an account__. Indeed, on others video broadcasting services (such as YouTube) these two things are pretty much the same.

However, on Tube each account is linked to one or multiple channels that can be named as the users sees fit. You also have to create at least one channel when creating an account. Once the channels have been created, users can upload videos to each channel to organize their contents (for example, you could have a channel about cooking and another one about biking).

<figure>
  <img loading="lazy" src="/img/news/release-1.4/en/channel.png" alt="2 channels on Framasoft's account on FramaTube instance">
  <figcaption>2 channels on Framasoft's account on FramaTube instance</figcaption>
</figure>

In order to make this channel idea more understandable, we have changed the sign-up form, which from now on consists of two steps:

- Step 1: account creation (choosing your username, password, email, etc.)
- Step 2: choosing your default channel name via a new form

<figure>
  <img loading="lazy" src="/img/news/release-1.4/en/account-creation.png" alt="the new sign-up form in 2 steps">
  <figcaption>the new sign-up form in 2 steps</figcaption>
</figure>


- We also aimed to differentiate a channel homepage from that of an account. These two pages used to list videos, whereas now the account homepage lists all the channel linked to the account by showing under each channel name the thumbnail from the last videos uploaded on it.
- Another unclear element was the <b>video sharing pop-up</b>. We have improved it, and it is now possible to share or embed a video by making it start and/or finish at a precise moment (time-code feature), to decide which subtitles will appear by default, and to loop the video. These new options will surely be greatly enjoyed.

<figure>
  <img loading="lazy" src="/img/news/release-1.4/en/share-popup.png" alt="customization options when video sharing">
  <figcaption>customization options when video sharing</figcaption>
</figure>

#### More features

Our wonderful community of translators is once again to thank for their work, after they enriched Tube with <b>3 new languages</b>: Finnish, Greek and Scottish Gaelic, making Tube now available in 22 languages.

We also added a new feature allowing you to <b>upload an audio file</b> directly to Tube: the software will automatically create a video from the audio file. This much awaited for feature should make life easier for music makers :)

This new release includes many other improvements. You can see the complete list on https://github.com/Chocobozzz/Tube/releases/tag/v1.4.0.

Thanks to all Tube contributors!
Framasoft
