# Configuration

You can configure the app by copying `config.sample.json` to
`config.json` and customising it:

For a good example, see <../config/config.sample.json>.

1. `default_server_config` sets the default homeserver and identity server URL for
   Chat to use. The object is the same as returned by [https://<server_name>/.well-known/matrix/client](https://matrix.org/docs/spec/client_server/latest.html#get-well-known-matrix-client),
   with added support for a `server_name` under the `m.homeserver` section to display
   a custom homeserver name. Alternatively, the config can contain a `default_server_name`
   instead which is where Chat will go to get that same object, although this option is
   deprecated - see the `.well-known` link above for more information on using this option.
   Note that the `default_server_name` is used to get a complete server configuration
   whereas the `server_name` in the `default_server_config` is for display purposes only.
   * *Note*: The URLs can also be individually specified as `default_hs_url` and
     `default_is_url`, however these are deprecated. They are maintained for backwards
     compatibility with older configurations. `default_is_url` is respected only
     if `default_hs_url` is used.
   * Chat will fail to load if a mix of `default_server_config`, `default_server_name`, or
     `default_hs_url` is specified. When multiple sources are specified, it is unclear
     which should take priority and therefore the application cannot continue.
   * As of Chat 1.4.0, identity servers are optional. See [Identity servers](#identity-servers) below.
2. `sso_immediate_redirect`: When `true`, Chat will assume the default server supports SSO
   and attempt to send the user there to continue (if they aren't already logged in). Default
   `false`. Note that this disables all usage of the welcome page.
3. `features`: Lookup of optional features that may be force-enabled (`true`) or force-disabled (`false`).
   When features are not listed here, their defaults will be used, and users can turn them on/off if `showLabsSettings`
   allows them to. The available optional experimental features vary from release to release and are
   [documented](labs.md). The feature flag process is [documented](feature-flags.md) as well.
4. `showLabsSettings`: Shows the "labs" tab of user settings. Useful to allow users to turn on experimental features
   they might not otherwise have access to.
5. `brand`: String to pass to your homeserver when configuring email notifications, to let the
   homeserver know what email template to use when talking to you.
6. `branding`: Configures various branding and logo details, such as:
    1. `welcomeBackgroundUrl`: An image to use as a wallpaper outside the app
       during authentication flows. If an array is passed, an image is chosen randomly for each visit.
    2. `authHeaderLogoUrl`: An logo image that is shown in the header during
       authentication flows
    3. `authFooterLinks`: a list of links to show in the authentication page footer:
      `[{"text": "Link text", "url": "https://link.target"}, {"text": "Other link", ...}]`
7. `reportEvent`: Configures the dialog for reporting content to the homeserver
   admin.
    1. `adminMessageMD`: An extra message to show on the reporting dialog to
       mention homeserver-specific policies. Accepts Markdown.
8. `integrations_ui_url`: URL to the web interface for the integrations server. The integrations
   server is not Chat and normally not your homeserver either. The integration server settings
   may be left blank to disable integrations.
9. `integrations_rest_url`: URL to the REST interface for the integrations server.
10. `integrations_widgets_urls`: list of URLs to the REST interface for the widget integrations server.
11. `bug_report_endpoint_url`: endpoint to send bug reports to (must be running a
   <https://github.com/matrix-org/rageshake> server). Bug reports are sent when a user clicks
   "Send Logs" within the application. Bug reports can be disabled/hidden by leaving the
   `bug_report_endpoint_url` out of your config file.
12. `roomDirectory`: config for the public room directory. This section is optional.
13. `roomDirectory.servers`: List of other homeservers' directories to include in the drop
   down list. Optional.
14. `default_theme`: name of theme to use by default (can be one of 'light', 'dark' or 'system'), defaults to 'system',
   set the default light and dark theme via the `light_theme` and `dark_theme` in `settingDefaults` respectively and
   provide [custom themes](https://github.com/Chat/element-web/blob/sc/docs/theming.md#custom-themes) via `custom_themes`
15. `update_base_url` (electron app only): HTTPS URL to a web server to download
   updates from. This should be the path to the directory containing `macos`
   and `win32` (for update packages, not installer packages).
16. `piwik`: Analytics can be disabled by setting `piwik: false` or by leaving the piwik config
   option out of your config file. If you want to enable analytics, set `piwik` to be an object
   containing the following properties:
    1. `url`: The URL of the Piwik instance to use for collecting analytics
    2. `whitelistedHSUrls`: a list of HS URLs to not redact from the analytics
    3. `whitelistedISUrls`: a list of IS URLs to not redact from the analytics
    4. `siteId`: The Piwik Site ID to use when sending analytics to the Piwik server configured above
17. `welcomeUserId`: the user ID of a bot to invite whenever users register that can give them a tour
18. `embeddedPages`: Configures the pages displayed in portions of Chat that
   embed static files, such as:
    1. `welcomeUrl`: Initial content shown on the outside of the app when not
       logged in. Defaults to `welcome.html` supplied with Chat.
    2. `homeUrl`: Content shown on the inside of the app when a specific room is
       not selected. By default, no home page is configured. If one is set, a
       button to access it will be shown in the top left menu.
    3. `loginForWelcome`: Overrides `welcomeUrl` to make the welcome page be the
       same page as the login page when `true`. This effectively disables the
       welcome page.
19. `defaultCountryCode`: The ISO 3166 alpha2 country code to use when showing
   country selectors, like the phone number input on the registration page.
   Defaults to `GB` if the given code is unknown or not provided.
20. `settingDefaults`:  Defaults for settings that support the `config` level,
   as an object mapping setting name to value (note that the "theme" setting
   is special cased to the `default_theme` in the config file).
21. `disable_custom_urls`: disallow the user to change the
   default homeserver when signing up or logging in.
22. `permalinkPrefix`: Used to change the URL that Chat generates permalinks with.
   By default, this is "https://to.chat.imzqqq.top" to generate to.chat.imzqqq.top (spec) permalinks.
   Set this to your Chat instance URL if you run an unfederated server (eg:
   "https://element.example.org").
23. `jitsi`: Used to change the default conference options. Learn more about the
   Meet options at [jitsi.md](./jitsi.md).
    1. `preferredDomain`: The domain name of the preferred Meet instance. Defaults
       to `jitsi.riot.im`. This is used whenever a user clicks on the voice/video
       call buttons - integration managers may use a different domain.
24. `enable_presence_by_hs_url`: The property key should be the URL of the homeserver
    and its value defines whether to enable/disable the presence status display
    from that homeserver. If no options are configured, presence is shown for all
    homeservers.
25. `disable_guests`: Disables guest access tokens and auto-guest registrations.
    Defaults to false (guests are allowed).
26. `disable_login_language_selector`: Disables the login language selector. Defaults
    to false (language selector is shown).
27. `disable_3pid_login`: Disables 3rd party identity options on login and registration form
    Defaults to false (3rd party identity options are shown).
28. `default_federate`: Default option for room federation when creating a room
    Defaults to true (room federation enabled).
29. `desktopBuilds`: Used to alter promotional links to the desktop app. By default
   the builds are considered available and accessible from <https://apps.chat.imzqqq.top>. This
   config option is typically used in the context of encouraging encrypted message
   search capabilities (Seshat). All the options listed below are required if this
   option is specified.
30. `available`: When false, the desktop app will not be promoted to the user.
31. `logo`: An HTTP URL to the avatar for the desktop build. Should be 24x24, ideally
      an SVG.
32. `url`: An HTTP URL for where to send the user to download the desktop build.
33. `mobileBuilds`: Used to alter promotional links to the mobile app. By default the
   builds are considered available and accessible from <https://apps.chat.imzqqq.top>. This config
   option is typically used in a context of encouraging the user to try the mobile app
   instead of a mobile/incompatible browser.
34. `ios`: The URL to the iOS build. If `null`, it will be assumed to be not available.
       If not set, the default apps.chat.imzqqq.top builds will be used.
35. `android`: The URL to the Android build. If `null`, it will be assumed to be not available.
       If not set, the default apps.chat.imzqqq.top builds will be used.
36. `fdroid`: The URL to the FDroid build. If `null`, it will be assumed to be not available.
      If not set, the default apps.chat.imzqqq.top builds will be used.
37. `mobileGuideToast`: Whether to show a toast a startup which nudges users on
   iOS and Android towards the native mobile apps. The toast redirects to the
   mobile guide if they accept. Defaults to false.
38. `audioStreamUrl`: If supplied, show an option on Meet widgets to stream
   audio using Meet's live streaming feature. This option is experimental and
   may be removed at any time without notice.
39. `voip`: Behaviour related to calls
40. `obeyAssertedIdentity`: If set, MSC3086 asserted identity messages sent
      on VoIP calls will cause the call to appear in the room corresponding to the
      asserted identity. This *must* only be set in trusted environments.
41. `posthog`: [Posthog](https://posthog.com/) integration config. If not set, Posthog analytics are disabled.
42. `projectApiKey`: The Posthog project API key
43. `apiHost`: The Posthog API host
44. `sentry`: [Sentry](https://sentry.io/) configuration for rageshake data being sent to sentry.
45. `dsn`: the Sentry [DSN](https://docs.sentry.io/product/sentry-basics/dsn-explainer/)
46. `environment`: (optional) The [Environment](https://docs.sentry.io/product/sentry-basics/environments/) to pass to sentry

Note that `index.html` also has an og:image meta tag that is set to an image
hosted on riot.im. This is the image used if links to your copy of Chat
appear in some websites like Facebook, and indeed Chat itself. This has to be
static in the HTML and an absolute URL (and HTTP rather than HTTPS), so it's
not possible for this to be an option in config.json. If you'd like to change
it, you can build Chat, but run
`RIOT_OG_IMAGE_URL="http://example.com/logo.png" yarn build`.
Alternatively, you can edit the `og:image` meta tag in `index.html` directly
each time you download a new version of Chat.

## Identity servers

The identity server is used for inviting other users to a room via third party
identifiers like emails and phone numbers. It is not used to store your password
or account information.

As of Chat 1.4.0, all identity server functions are optional and you are
prompted to agree to terms before data is sent to the identity server.

Chat will check multiple sources when looking for an identity server to use in
the following order of preference:

1. The identity server set in the user's account data
   * For a new user, no value is present in their account data. It is only set
     if the user visits Settings and manually changes their identity server.
2. The identity server provided by the `.well-known` lookup that occurred at
   login
3. The identity server provided by the Classic_Chat config file

If none of these sources have an identity server set, then Chat will prompt the
user to set an identity server first when attempting to use features that
require one.

Currently, the only two public identity servers are <https://vector.im> and
<https://chat.imzqqq.top>, however in the future identity servers will be
decentralized.

## Desktop app configuration

See <../../element-desktop/README.md#user-specified-configjson>

## UI Features

Parts of the UI can be disabled using UI features. These are settings which appear
under `settingDefaults` and can only be `true` (default) or `false`. When `false`,
parts of the UI relating to that feature will be disabled regardless of the user's
preferences.

Currently, the following UI feature flags are supported:

* `UIFeature.urlPreviews` - Whether URL previews are enabled across the entire application.
* `UIFeature.feedback` - Whether prompts to supply feedback are shown.
* `UIFeature.voip` - Whether or not VoIP is shown readily to the user. When disabled,
  Meet widgets will still work though they cannot easily be added.
* `UIFeature.widgets` - Whether or not widgets will be shown.
* `UIFeature.flair` - Whether or not community flair is shown in rooms.
* `UIFeature.communities` - Whether or not to show any UI related to communities. Implicitly
  disables `UIFeature.flair` when disabled.
* `UIFeature.advancedSettings` - Whether or not sections titled "advanced" in room and
  user settings are shown to the user.
* `UIFeature.shareQrCode` - Whether or not the QR code on the share room/event dialog
  is shown.
* `UIFeature.shareSocial` - Whether or not the social icons on the share room/event dialog
  are shown.
* `UIFeature.identityServer` - Whether or not functionality requiring an identity server
  is shown. When disabled, the user will not be able to interact with the identity
  server (sharing email addresses, 3PID invites, etc).
* `UIFeature.thirdPartyId` - Whether or not UI relating to third party identifiers (3PIDs)
  is shown. Typically this is considered "contact information" on the homeserver, and is
  not directly related to the identity server.
* `UIFeature.registration` - Whether or not the registration page is accessible. Typically
  useful if accounts are managed externally.
* `UIFeature.passwordReset` - Whether or not the password reset page is accessible. Typically
  useful if accounts are managed externally.
* `UIFeature.deactivate` - Whether or not the deactivate account button is accessible. Typically
  useful if accounts are managed externally.
* `UIFeature.advancedEncryption` - Whether or not advanced encryption options are shown to the
  user.
* `UIFeature.roomHistorySettings` - Whether or not the room history settings are shown to the user.
  This should only be used if the room history visibility options are managed by the server.
