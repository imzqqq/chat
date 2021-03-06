# chat-widget-api

JavaScript/TypeScript SDK for widgets & clients to communicate.

For help and support, visit [#chat-widgets:chat.imzqqq.top](https://to.chat.imzqqq.top/#/#chat-widgets:chat.imzqqq.top) on Chat.

## Not yet ready for usage

This is currently not validated and thus should not be relied upon until this notice goes away. Installation
instructions will take this notice's place.

## Code style

This project aims to target TypeScript with published versions having JS-compatible
code. All files should be written in TypeScript.

Members should not be exported as a default export in general - it causes problems
with the architecture of the SDK (index file becomes less clear) and could
introduce naming problems (as default exports get aliased upon import). In
general, avoid using `export default`.

## Using the API without a bundler

If you're looking to drop the widget-api into a web browser without the use of a bundler, add a `script`
tag similar to the following:

```html
<script src="https://unpkg.com/chat-widget-api@0.1.0/dist/api.min.js"></script>
```

Note that the version number may need changing to match the current release.

Once included, the widget-api will be available under `mxwidgets`. For example, `new mxwidgets.WidgetApi(...)`
to instantiate the `WidgetApi` class.

## Usage for widgets

The general usage for this would be:

```typescript
const widgetId = null; // if you know the widget ID, supply it.
const api = new WidgetApi(widgetId);

// Before doing anything else, request capabilities:
api.requestCapability(MatrixCapabilities.Screenshots);
api.requestCapabilities(StickerpickerCapabilities);

// Add custom action handlers (if needed)
api.on(`action:${WidgetApiToWidgetAction.UpdateVisibility}`, (ev: CustomEvent<IVisibilityActionRequest>) => {
    ev.preventDefault(); // we're handling it, so stop the widget API from doing something.
    console.log(ev.detail); // custom handling here
    api.transport.reply(ev.detail, <IWidgetApiRequestEmptyData>{});
});
api.on("action:com.example.my_action", (ev: CustomEvent<ICustomActionRequest>) => {
    ev.preventDefault(); // we're handling it, so stop the widget API from doing something.
    console.log(ev.detail); // custom handling here
    api.transport.reply(ev.detail, {custom: "reply"});
});

// Start the messaging
api.start();

// If waitForIframeLoad is false, tell the client that we're good to go
api.sendContentLoaded();

// Later, do something else (if needed)
api.setAlwaysOnScreen(true);
api.transport.send("com.example.my_action", {isExample: true});
```

For a more complete example, see the `examples` directory of this repo.

## Usage for web clients

This SDK is meant for use in browser-based applications. The concepts may be transferable to other platforms,
though currently this SDK is intended to only be used by browsers. In the future it may be possible for this
SDK to provide an interface for other platforms.

TODO: Improve this

```typescript
const driver = new CustomDriver(); // an implementation of WidgetDriver
const api = new ClientWidgetApi(widget, iframe, driver);

// The API is automatically started, so we just have to wait for a ready before doing something
api.on("ready", () => {
    api.updateVisibility(true).then(() => console.log("Widget knows it is visible now"));
    api.transport.send("com.example.my_action", {isExample: true});
});

// Eventually, stop the API handling
api.stop();
```
