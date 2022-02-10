# Setup your account

## Create an account on Tube

To be able to upload a video, you must have an account on an _instance_ (a server running Tube). The Tube project maintains a list of
public instances at [instances.joinpeertube.org](https://instances.joinpeertube.org/instances) (note that instance administrators must add themselves
to this list manually, it is not automatically generated).

Once you've found an instance that suits you, click the button "Create an account" and fill in your **login** (nickname), an **email address** and a **password**.

![Registration page after clicking on "Create an account"](/assets/profile-registration-01.png)

Once this first step done, you have to setup your default channel by indicate its name (`id`) and display's name.

![Channel first creation](/assets/profile-registration-02.png)

## Connect to your Tube instance

To log in, you must go to the address of the particular instance that you registered on. Instances share each other's videos, but
each instance's index of accounts is not federated. Click on the "Login" button in the top-left corner, then fill in your login/email address and
password. Note that the login name and password are **case-sensitive**!

Once logged in, your name and display name will appear below the instance name.

## Update your profile

To update your user profile, change your avatar, change your password, etc... Click on <i data-feather="user"></i> **My Account** under your profile name in the sidebar. You then have a few options:

* Settings
* Notifications
* Applications
* Moderation
  * Muted accounts
  * Muted servers
  * Abuse reports

![user profile view](/assets/en-profile-library.png)

### Settings

**Settings** tab has several sections.

Before any of the other sections, you can:

* See/change your Avatar. The allowed filetypes and file-size should be displayed by clicking <i data-feather="edit-2"></i> and move the mouse over the upload button.
* View your Upload Quota and used space. Your upload quota is determined by the instance hosting your account. If your instance creates mulitple versions of your videos in different qualities then all versions count towards your quota.

#### Profile

* View your Upload Quota and used space. Your upload quota is determined by the instance that hosts your account. If your instance creates mulitple versions of your videos in different qualities then all versions count towards your quota.
* Modify your **Display Name**. Note that your Display Name is not the same as your Username (used for logging in), which cannot be modified.
* Add or modify your **User Description**. This will be displayed on your Public Profile in the "About" section.

#### Video Settings

* Change how videos with sensitive content are displayed. You can choose to list them normally, blur their content, or hide them completely. This feature may be disabled on your instance.
* Select which languages you want to see videos in. Languages which are not selected will be hidden. You can have up to 20 languages (or all languages) selected.
* Select if you want to help sharing videos being played or not (by using, or not, P2P).
* Change whether videos autoplay or not.
* Change whether you want to automatically start next video or not.

#### Notifications

Change which notifications you receive. You can choose to receive notifications in Tube, via Email, both, or neither for:

  * New video from your subscriptions
  * New comment on your video
  * [admin] New abuse
  * [admin] Video blocked automatically waiting review
  * One of your video is blocked/unblocked
  * Video published (after transcoding/scheduled update)
  * Video import finished
  * [admin] A new user registered on your instance
  * You or your channel(s) has a new follower
  * Someone mentioned you in video comments
  * [admin] Your instance has a new follower
  * [admin] Your instance automatically followed another instance
  * An abuse report received a new message
  * One of your abuse reports has been accepted or rejected by moderators

When **Web** notifications are enabled, there are visible above your avatar:

![notification numbre image](/assets/use-settings-notifications-indicator.png)

By clicking it you can see your notification list. You can click each notification, or:

  * <i data-feather="inbox"></i> mark all as read
  * <i data-feather="settings"></i> update your notifications preferences

#### Interface

Change your Theme. In earlier versions (<=v1.3.1) the theme was selected in the sidebar.

?> You have to refresh the page to see changes

#### Password

Change your Password. To change your password you will need to enter your current password, and then enter your new password twice before clicking **_Change Password_**.

#### Email

Change your Email. You will need to enter your password again to change this setting.

#### Danger Zone

 Delete your account.

!> **This is irreversible. This will delete all your data, including channels, videos and comments. Content cached by other servers and other third-parties might make longer to be deleted.**

You need to confirm your username to proceed.
