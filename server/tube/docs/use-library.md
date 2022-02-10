# User library

## Video channels

You might have noticed it: your account on a Tube instance has the notion of "channels". On the contrary to YouTube where an account corresponds to a channel, on Tube you can group your videos per theme; those interested in your cat videos will then be able to subscribe to their dedicated channel, whereas those interested in dog videos won't see cats by just subscribing to the channel where you publish your dog videos.

### List your channels

You can of course list and delete channels via the dedicated menu in your settings by clicking **_My Library_** near your username > **_Channels_**.

![Menu presenting options to create a channel](/assets/video-channel-create.png)

### Create a new channel

To create a new channel, you have to:

  1. click **My Library** near to your username in the left menu
  1. click **Create video channel** button
  1. enter informations:
    1. **Name**: your channel's name (e.g.: `cats@my-insance.tld`)
    1. **Display Name**: can be different from **Name**
    1. **Description**: what's you channel is about? (supports markdown)
    1. **Support**: where you can link to financing platforms or directly materials to support your channel
  1. click **Create** button

Once your channel created, you can change its avatar by:

  1. clicking **Update** button in front of its name in your channels list
  1. clicking <i data-feather="edit-2"></i> icon on the default avatar
  1. selecting an image on your computer
  1. clicking **Update**


Your channel is all set to have some videos!

### Notify your audience when a video is published

Your friends and your broader audience can see and get notified of new videos without having to check them out regularly. To achieve that, they can "follow" one of your channels (or your whole profile):

* via their Tube account
* via their Fediverse account (supposes they have an account on a federated platform like Mastodon or Pleroma)
* via a syndication format (supposes they have an RSS or Atom aggregator)

![Popover presenting options to follow a channel via syndication feeds](/assets/video-channel-rss.png)

## Video history

By default Tube is storing a history of the videos you have watched, a setting you can disable anytime in `My Library` > `History` on the left menu. Nothing is done with this data except for the following usages, and you can delete the history anytime via the button placed on the right of the history list.

### Catching up where you left off watching a video

Once you have begun watching a video, it will get into your history. But more than that, if you leave the video before the end, the history will remember at what time so that you can resume watching it the next time.

The feature is represented by a coloured bar below the video miniature.

![The video has been watched halfway, an orange bar represents it](/assets/video-history-miniature-bar.jpg)


## Playlist

Much like what you might expect on other video platforms, a playlist on Tube is an ordered collection of videos that can be shared or kept private. You can freely add to a playlist your own videos, or videos from others.

You actually already have a private playlist associated with your account: your "Watch later" playlist!

### Creating a playlist

Creating new playlists can be done in the `My Library` section on the left menu > `Playlists`.

![List of a user's playlists](/assets/video-playlist-list.png)

Notice that a playlist can be associated with no channel, or any channel of your choice. However, when making a public playlist, it has to be associated with a channel.

You can choose a thumbnail for the playlist, as well as a title and a description. They will be the first thing seen when users see when doing a search.

![Form to create a playlist](/assets/video-playlist-creation.png)

### Adding videos to playlists

Quick actions appearing on miniatures on hover help you add videos quickly to your playlists.

![Quickly adding a video to playlists](/assets/video-playlist-quick-action.jpg)

#### Adding video to your watch-it later list

![Quickly adding to watch it later list gif](/assets/add-to-watch-it-later.gif)

  1. click <i data-feather="more-vertical"></i>
  2. click **Save to playlist**
  3. click **Watch later** checkbox

#### Adding video to a playlist

  1. click <i data-feather="more-vertical"></i>
  2. click **Save to playlist**
  3. click your playlist name

**or**, if you want to add it into a new playlist:

  1. click <i data-feather="more-vertical"></i>
  2. click **Save to playlist**
  3. click <i data-feather="plus-circle"></i> **Create a private playlist**
  4. add a display name for your playlist
  5. click **Create** button
  6. click your playlist checkbox

The video is now in your selected playlist.

### Ordering a playlist

Playlists are ordered, so that viewers can watch videos sequentially. It is up to you to order videos in your playlist once you have gathered them. Click the "Edit" button in the list of playlists to access a playlist drag and drop ordering list.

![Ordering a playlist](/assets/video-playlist-ordering.jpg)

### Watching a playlist

Viewing a playlist triggers a special mode of the video player: the videos of the current playlist are listed in a pane on the right of the player, to quickly navigate among them, and see the upcoming videos.

![Watching a playlist](/assets/video-playlist-watching.png)

In the right panel, you can:
  * (de)activate autoplay by clicking <i data-feather="play"></i>
  * loop the playlist video by clicking <i data-feather="repeat"></i>
