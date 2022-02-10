# Plugins & Themes API

This is the API reference for plugins and themes. An introduction and quickstart into its use is provided in [the corresponding Contribute guide](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/support/doc/plugins/guide.md).

## Hooks

### Server hooks (only plugins)

[server hooks](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/server-hook.model.ts ':include :type=code')

### Client hooks

[client hooks](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/client-hook.model.ts ':include :type=code')
[client scopes](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/plugin-client-scope.type.ts ':include :type=code')


## Server register/unregister (only plugins)

Your library file should export a `register` and `unregister` functions:

[register function](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/server/types/plugins/plugin-library.model.ts ':include :type=code')

Tube provides different helpers to the `register` function:

[register options](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/server/types/plugins/register-server-option.model.ts ':include :type=code')

### Register hook options

To register hook listeners:

[register settings](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/register-server-hook.model.ts ':include :type=code')


### Register settings options

To register settings:

[register settings](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/settings/register-server-setting.model.ts ':include :type=code')

[](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/register-client-form-field.model.ts ':include :type=code')


### Settings manager API

You can save/load registered settings:

[settings manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-settings-manager.model.ts ':include :type=code')


### Storage manager API

To save/load JSON (please don't put too much data in there because we store it in the Tube database):

[storage manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-storage-manager.model.ts ':include :type=code')


### Register auth methods API

To register id and pass auth methods (LDAP etc), or external auth (OpenID, SAML2 etc) methods:

[storage manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/server/types/plugins/register-server-auth.model.ts ':include :type=code')



### Video categories manager API

[categories manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-video-category-manager.model.ts ':include :type=code')


### Video languages manager API

[languages manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-video-language-manager.model.ts ':include :type=code')


### Video licences manager API

[licences manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-video-licence-manager.model.ts ':include :type=code')

### Video privacy manager API

[privacies manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-video-privacy-manager.model.ts ':include :type=code')

### Video playlist privacy manager API

[playlist privacies manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-playlist-privacy-manager.model.ts ':include :type=code')

### Video transcoding manager API

To add profile and encoders priority to ffmpeg transcoding jobs (profile needs to be selected by the admin in the Tube configuration):

[video transcoding manager](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/server/managers/plugin-transcoding-manager.model.ts ':include :type=code')

[video transcoding models](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/videos/transcoding/video-transcoding.model.ts ':include :type=code')

## Client register

Your client script should export a `register` function:

[register function](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/client/src/types/client-script.model.ts ':include :type=code')

Tube provides different helpers to the `register` function:

[register options](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/client/src/types/register-client-option.model.ts ':include :type=code')


### Register hook options

To register hook listeners:

[register hook](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/register-client-hook.model.ts ':include :type=code')


### Register video form field options

[](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/register-client-form-field.model.ts ':include :type=code')

### Register settings script

[](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/register-client-settings-script.model.ts ':include :type=code')


## Client plugin selectors

*Selector ids are prefixed by `plugin-selector-`. For example: `plugin-selector-login-form`*

[](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/plugin-selector-id.type.ts ':include :type=code')

## Client placeholder elements

*Element ids are prefixed by `plugin-placeholder-`. For example: `plugin-placeholder-player-next`*

[](https://raw.githubusercontent.com/Chocobozzz/Tube/develop/shared/models/plugins/client/plugin-element-placeholder.type.ts ':include :type=code')
