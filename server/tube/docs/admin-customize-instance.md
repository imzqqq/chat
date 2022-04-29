# Customize your instance

## Install a theme

You can install themes created by the Tube community in `Administration > Plugins/Themes > Themes > Search`.

![](/assets/search-themes.png)

Then, you can set your default instance theme in `Administration > Configuration > Basic configuration`.

Your users will be able to override this setting in their preference, so they can choose another theme among those you have installed.

**Learn how to create a theme: https://tube.docs.imzqqq.top/contribute-plugins**

## Customize your instance CSS

If you just want to update some elements of your instance, you can also inject some CSS code configured in  `Administration > Configuration > Advanced configuration`.

It's easy to change the main Tube colors using CSS variables using:

```css
body#custom-css {
  --mainColor: #E8252D;
}
```

Check all available variables in [_variables.scss](https://github.com/Chocobozzz/Tube/blob/develop/client/src/sass/include/_variables.scss#L68).


## Install a plugin

Administrators can install plugins to change the behaviour of their Tube application. Plugins can inject CSS, change the UI and the server logic.

A plugin could for example:
 * Hide some buttons (counters, menu entries...)
 * Reject specific comments or videos (depending on many criterias)
 * Put videos in quarantine
 * Add client analytics ([Matomo plugin](https://www.npmjs.com/package/tube-plugin-matomo), etc.)
 * Add a CAPTCHA in the registration form (see [CAPTCHA plugins](https://www.npmjs.com/search?q=keywords%3Apeertube%20plugin%20captcha))
 * Block some IPs
 * Customize the player
 * Add auth methods to your Tube instance ([LDAP](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-ldap), [OpenId](https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-auth-openid-connect), etc.)
 * ...

You can install plugins created by the Tube community in `Administration > Plugins/Themes > Plugins > Search`.

![](/assets/search-plugins.png)

**Learn how to create a plugin: https://tube.docs.imzqqq.top/contribute-plugins**

## Inject JavaScript in client

If you just want to update a particular client behaviour of your instance, you can also inject some JavaScript code configured in  `Administration > Configuration > Advanced configuration`.
