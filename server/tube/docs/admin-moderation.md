# Moderate your instance

## Administrators

As an administrator, you have full control over what instances you follow, and which
users you welcome on your instance. Some features have an impact over these two flows,
and can increase or decrease the need for moderation afterwards.
They are usually marked with a warning within the administration interface,
but here are some of them:
 * opening registrations (instead, you can answer to requests for accounts via the contact form, by requesting the new user to change their password)
 * leaving users to upload videos without limitation (instead, prefer a low quota or an automatic quarantine with a medium quota)
 * automatically follow instances (instead, host a list of instances with like-minded admins)

## Moderators

When you create/update another user, you can put it as "moderator".
A moderator can:
 * Manage video blocks
 * Manage video abuses
 * Remove any video, channel, playlist or comment
 * Update any video
 * See all the videos (including unlisted and private videos)
 * Mute accounts/instances for the entire instance
 * Manage regular users (update, ban, delete)

## Video abuses

Any local or remote users can report a video with a particular "reason".
All video abuses are listed in `Administration > Moderation > Video abuses`
Admins/moderators can act on the video (updating/removing/blocking it for example).

Admins/moderators can:
 * Delete the report
 * Mark it as accepted/rejected (this is just an indicator for other admins/moderators, it will not send a message to the account that sent the report... yet)
 * Update the moderation comment (is only displayed to other admins/moderators)


## Mute accounts/instances

See https://tube.docs.imzqqq.top/use-mute

You can also try the auto mute plugin: https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-mute
It automatically mutes accounts or instances depending on a public mute list (hosted on Github, Gitlab, pastebin etc).


## Videos blocks

### Manual block

Admins/moderators can block any videos (local or remote).
When you block a local video that is already federated, you can choose to **Unfederate**
it, meaning that the video will be removed from other instances.
Manually blocked videos are listed in `Administration > Moderation > Video blocks`,
and are shown along regular video blocks unless you filter them out with the advanced block filters.

### Auto block

You can enable this feature in `Administration > Configuration > Basic configuration`.
When enabled, every new uploaded videos will be hidden by default until a moderator manually approves them.
Automatically blocked videos are listed in `Administration > Moderation > Video blocks`,
and are shown along regular video blocks unless you filter them out with the advanced block filters.

You can then individually allow users you trust to bypass videos quarantine in `Administrations > Users > Update a user`.


## List comments

To track video comments added on your instance, go in `Administration > Moderation > Video comments`.
This page will list comments added on your instance (by a remote instance or by a local user).
Then, you can:

 * Delete comments using `Actions > Delete this comment`
 * Delete all comments of an account using `Actions > Delete all comments of this account`


## Display private/unlisted videos

You can see private/unlisted or videos not yet published using the `Display all videos (private, unlisted or not yet published)` filter (behind the star wheel on the top right) of the following pages:
 * Local videos
 * Account videos
 * Channel videos


## Plugins

Framasoft developed two plugins to automatically mute or block accounts, instances or videos based on public blocklists:
 * [tube-plugin-auto-mute](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-mute)
 * [tube-plugin-auto-block-videos](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auto-block-videos)

You can install them using the admin interface.


## Tips

### Dealing with spam

With open registrations on your server, chances are you will have to deal with spam.
There is no simple and definitive way to solve this problem while keeping registrations open,
but there are several that could help you limit spam and its impact on your instance.

Here are some things that could help:
 * **Enable videos quarantine/auto video block**
 * **Disable new registrations:** you can disable new registrations, and update your instance description to explain that you accept new users on demand. They can for example use the contact button of the about page.
 * **Require email verification:** force email verification on signup in `Administration > Configuration > Basic configuration`. Unfortunately this feature is not really useful since most spammers use a real email account (gmail etc) and automate the validation process.


### Understand what happens on your instance

To check what videos are uploaded on your server, or what your users do you can:
 * Check your [standard & audit logs](https://tube.docs.imzqqq.top/admin-logs)
 * List all private/unlisted/public videos in the `Local` page, and then click on `Display all videos (private, unlisted or not yet published)`
 * Subscribe to your global videos feed (for example: https://peertube2.cpy.re/feeds/videos.xml?sort=-publishedAt&filter=local)
 * Subscribe to your global comments feed (for example: https://peertube2.cpy.re/feeds/video-comments.xml?sort=-publishedAt&filter=local)
