# Customizations

Chat Web and the React SDK support "customization points" that can be used to
easily add custom logic specific to a particular deployment of Chat Web.

An example of this is the [security customizations
module](../../matrix-react-sdk/src/customizations/Security.ts).
This module in the React SDK only defines some empty functions and their types:
it does not do anything by default.

To make use of these customization points, you will first need to fork Chat
Web so that you can add your own code. Even though the default module is part of
the React SDK, you can still override it from the Chat Web layer:

1. Copy the default customization module to
   `element-web/src/customizations/YourNameSecurity.ts`
2. Edit customizations points and make sure export the ones you actually want to
   activate
3. Tweak the Chat build process to use the customized module instead of the
   default by adding this to the `additionalPlugins` array in `webpack.config.js`:

```js
new webpack.NormalModuleReplacementPlugin(
    /src[\/\\]customizations[\/\\]Security\.ts/,
    path.resolve(__dirname, 'src/customizations/YourNameSecurity.ts'),
),
```

If we add more customization modules in the future, we'll likely improve these
steps to remove the need for build changes like the above.

By isolating customizations to their own module, this approach should remove the
chance of merge conflicts when updating your fork, and thus simplify ongoing
maintenance.
