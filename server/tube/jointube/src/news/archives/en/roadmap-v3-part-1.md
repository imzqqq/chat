---
id: roadmap-v3-part-1
title: "[V3 Roadmap] Global Search ✅, let's go to moderation tools!"
date: July 2, 2020
mastodon: https://framapiaf.org/@tube/104444899900997017
twitter: https://twitter.com/joinpeertube/status/1278708073897566208
---

Here is another step in the <a href="https://joinpeertube.org/roadmap" target="_blank">Tube roadmap</a> leading to v3! June has been dedicated to implement a system to enable global video search across Tube instances.
Thank you all for being so many in funding this solution! This allows us to be free minded for the next few weeks to work on moderation tools.

<h4 id="roadmap-v3-part-1-find-videos-global-search">Find videos more easily with the global search</h4>

Thanks to <a href="https://joinpeertube.org/roadmap/#support" target="_blank">your support</a> we've been able to develop the system of global video search on Tube during June. We have published a tool which index all videos and channels from all Tube instances listed on the <a href="https://joinpeertube.org/instances#instances-list" target="_blank">public directory</a>.

This <a href="https://framagit.org/framasoft/tube/search-index" target="_blank">index engine code</a> is under FLOSS license so that anyone can host their own index engine and set their own eligibility rules. As an example, if you want to set up a search interface that only allows you to search for videos hosted on instances dedicated to video games, you will just have to create a list including all instances URLs you have selected and put it online. So that the index engine can refer to it and return you the appropriate results.

In the next version of the software, each Tube instance administrator will be able to choose to use one of these index engines to allow specific searches in the search bar of its instance.

![](/img/news/roadmap-v3-part-1/en/index-search.png)

This index engine has common features with the Tube search engine (API). So, you could use this global search system in two different ways: by creating a dedicated web interface for it or by integrating it on a Tube instance search. We will give you more details on this feature on mid-July when we will publish the 2.3 release.

#### But that's not all!

In the last few months, we have created 2 new plugins:

- <a target="_blank" href="https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-mute">tube-plugin-auto-mute</a>, which allows to automatically hide accounts and instances according to a public list;
- <a target="_blank" href="https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-block-videos">tube-plugin-auto-block-videos</a>, which allows to automatically block videos from a public list.

We haven't yet identified any Tube instance administrator who use these plugins and generate a public list. So we are requesting the community to create these lists <a target="_blank" href="https://framacolibri.org/t/lists-for-plugin-auto-block-videos-plugin-auto-mute/8484">on our forum</a>.

Meanwhile, we have developed an information message system which allow Tube instance administrators to display information to people who visit it. This is very handy to indicate that your instance will be in maintenance on a certain day at a certain time and that the service may be disrupted.

![](/img/news/roadmap-v3-part-1/en/broadcast-message.png)

#### July: spending time on moderation tools

As announced in the roadmap, the upcoming weeks will be dedicated to improve and add moderation tools. As each new version of Tube has [added new moderation features](https://joinpeertube.org/faq#tube-developers-did-not-add-moderation-tools), there is still a lot of work to be done to facilitate contents and accounts management in the fediverse. That's why we will take several weeks of development exclusively on this topic.

Tube 2.2 version already features improvements on video reporting interface, such as search filters, quick actions on videos and accounts, video thumbnails, quick access to embed, etc.

![](/img/news/roadmap-v3-part-1/en/moderation.jpg)

During July we will continue to develop features such as:

- comments moderation
- moderation reports linked to an account
- moderation history
- returns on actions taken (or not) following a report
- fight against spam.

This list isn't exhaustive and it's possible that we may include other developments, depending on feedback from [the community](https://github.com/Chocobozzz/Tube/labels/Component%3A%20Moderation%20%3Agodmode%3A).

#### We still need your support

After a boom start during the first few weeks, this rising funds campaign is now stagnating at just over €27,000. So we still need your support to finance the third step of development (dedicated to plugins and playlists) which will start in August. Also, feel free to share [the roadmap](https://joinpeertube.org/roadmap/) around you.
