# Manage Users & Auth

The user menu allows you to manage all existing users on your instance.

![Window displayed when in user menu](/assets/managing-users-menu.png)

Users will be created when they register to your instance, but you may also create
users manually using the "create user" button at the top right of the user menu.

## Manage users

Under the user menu you can update or delete a user by clicking on the three dots <i data-feather="more-horizontal"></i> at the left of a user info.

![User dot button](/assets/managing-users-dots-button.png)

- **Edit user** will allow you to update user informations - see below.
- **Delete user** will allow you to definitely delete this user. **All of that user's videos will also being deleted**.
- **Ban** will disable connection for this user but videos and comments will be kept as is. No one else will be able to register with the same nickname or email address.

## Editing users

When clicking on _Edit_, you can update parameters for a user such as email, role, video quota and so on. You also can specify if it needs review before a video goes public.

![Window displayed when clicking on Edit user](/assets/managing-users-edit-menu.png)

You have two ways to reset password:

  * by sending a reset link to the user email
  * by changing it manually

### Roles

**Role** defines what a user is allowed to do on the instance. There are three roles on Tube: [Administrator](#Administrator), [Moderator](#Moderator), and [User](#User).

#### Administrator

Administrators can do anything, and have access to the full admin backend.

- Add, Remove, and Edit user accounts, including Admin and Moderator accounts
- View, Edit, Add and Remove instance following and followers
- Delete Local Videos
- Allow or disallow video storage redundancy for followed instances
- Configure the instance:
  - instance name
  - short and full descriptions
  - Terms of Service (ToS)
  - Whether or not the instance is dedicated to NSFW content
  - default landing page
  - default NSFW video display policy (which can be modified by logged-in users)
  - whether signups are enabled or disabled
  - default user quotas
  - whether importing videos is allowed
    - from YouTube
    - from a torrent file or Magnet Link
  - whether or not new videos are automatically blacklisted when uploaded
  - whether other instances can follow your instance, and whether those followers need to be manually approved or not
  - administrator email
  - enable or disable the contact form
  - Twitter account information for the instance link previews (optional)
  - transcoding settings
    - enable or disable transcoding
    - enable or disable mkv, .mov, .avi, and .flv videos (this is all or none)
    - allow or disallow audio file uploads
    - number of threads to use for transcoding on the server
    - which resolutions are enabled (240p, 360p, 480p, 720p, 1080p, 2160p)
  - cache size for previews and captions
  - custom JavaScript and CSS
- Search for and install Plugins and Themes
- View System Jobs, Logs, and Debugging information

Admins also have the ability to perform any action that can be performed by a [Moderator](#Moderator) or a [User](#User)

#### Moderator

Moderators have access to the "Moderation" part of the administration backend which [Administrators](#Administrator) also see.

- View user account information:
  - username
  - email address
  - daily/total quotas
  - role
  - creation date
- Add accounts with the [User](#User) role.
- View video abuse reports
  - reporter
  - abuse report date and time
  - video title (with link to video)
- Delete, Comment, Accept or Reject video abuse reports
- View blacklisted videos
  - video title
  - whether the video is labeled as sensitive or not
  - whether the video has been unfederated or not (only applies to local videos)
  - date and time of the blacklist
- View muted accounts
  - account username and instance
  - date and time of the mute
- View muted instances
  - instance domain
  - date and time of the mute
- Blacklist videos
- Mute Accounts
- Mute Instances

Moderators can also do anything that a [User](#User) account can do.

#### User

User is the default role.

- Create, Edit, and Delete channels associated with their account
- Create, Edit, and Delete playlists associated with their account
- Upload, Edit, and Delete videos to their account, associated with one of their channels
- Comment on videos
- Add videos to playlists, or remove videos from them
- Change user settings ([See the Use Tube page](https://tube.docs.imzqqq.top/use-setup-account?id=update-your-profile) for more information)
- Report videos to the moderators with a comment about why the video is being reported

### Quotas

#### Video Quota

**Video Quota** represents the size limit a user cannot exceed when uploading videos. Each time a user upload a video, Peertube checks if there is enough quota to store it. If not, the upload is denied.
Beware, the quota after an upload is estimated only on the size of the file uploaded. However, after transcoding (which outputs videos of unpredictable size) the video resolutions resulting of the transcoding are also taken into account in a user's quota. If you have enabled multiple resolutions, a user can use more than their quota on disk. Peertube will provide you an estimation of maximal space a user will use according to your transcoding options.
You can change the default for new users in the configuration menu.

#### Daily Video Quota

**Daily Video Quota** represents the max quota a user is allowed to upload by day. You can tune this parameter to adjust the resources your instance will use.
For instance, if you have many users transcoding could take a long time, so limiting upload by user by day could help to share resources between them.
You can change the default for new users in the configuration menu.

Once you are satisfied with your user, you can click on the "Update User" button to save modifications.

## External auths support

You can [install plugins](https://tube.docs.imzqqq.top/admin-customize-instance?id=install-a-plugin) so your users can login using external auths.

For example:
 * [LDAP](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-ldap)
 * [OpenID Connect](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-openid-connect)
 * [SAML2](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-saml2)

See https://www.npmjs.com/search?q=tube%20auth to list available plugins.

If you created a user before installing an auth plugin, you can change the user authentication method in the user update form.
