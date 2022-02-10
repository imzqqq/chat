# Client markup

**Tube >= 3.3**

Tube supports custom markdown/HTML markup in some markdown fields (to create your homepage etc).

Full markdown is supported and basic HTML tags are allowed (`<a>`, `<strong>`, `<p>` etc).
The `style` attribute is also supported.

## Custom Tube HTML tags

Additionnaly, Tube supports custom HTML tags that can be configured using `data-*` attributes.

### tube-container

Create a container with a specific layout that can inject a title and a description.

 * `data-width`: container width attribute
 * `data-title`: title injected in the container
 * `data-description`: description injected in the container
 * `data-layout`: `row` or `column`
 * `data-justify-content`: (**Tube >= 3.4**) `normal` or `space-between` (default to `space-between` to fill all available width)

```
<tube-container data-layout="row" data-title="Local videos" data-description="Only local videos are displayed">
  <tube-videos-list data-count="10" data-only-local="true" data-max-rows="2"></tube-videos-list>
</tube-container>
```

### tube-button

Inject a Tube button.

 * **required** `data-href`: button link
 * **required** `data-label`: button label
 * `data-theme`: `primary` or `secondary`
 * `data-blank-target`: open or not the link in the current tab

```
<tube-button data-href="https://framatube.org" data-blank-target="true" data-theme="secondary" data-label="My super button"></tube-button>
```


### tube-video-embed

Inject a Tube video embed.

 * **required** `data-uuid`: video UUID

```
<tube-video-embed data-uuid="164f423c-ebed-4f84-9162-af5f311705da"></tube-video-embed>
```


### tube-playlist-embed

Inject a Tube playlist embed.

 * **required** `data-uuid`: playlist UUID

```
<tube-playlist-embed data-uuid="4b83a1cc-8e3b-4926-b1aa-8ed747557bc9"></tube-playlist-embed>
```


### tube-video-miniature

Inject a Tube video miniature.

 * **required** `data-uuid`: video UUID
 * `data-only-display-title`: display or not miniature attributes (views, channel etc)

```
<tube-video-miniature data-only-display-title="true" data-uuid="164f423c-ebed-4f84-9162-af5f311705da"></tube-video-miniature>
```


### tube-playlist-miniature

Inject a Tube playlist miniature.

 * **required** `data-uuid`: playlist UUID

```
<tube-playlist-miniature data-uuid="4b83a1cc-8e3b-4926-b1aa-8ed747557bc9"></tube-playlist-miniature>
```


### tube-channel-miniature

Inject a channel miniature.

 * **required** `data-name`: channel name
 * `data-display-latest-video`: display or not the latest published video
 * `data-display-description`: display or not the channel description

```
<tube-channel-miniature data-name="my_channel_username"></tube-channel-miniature>
<tube-channel-miniature data-name="my_channel_username" data-display-latest-video="false" data-display-description="false"></tube-channel-miniature>
```


### tube-videos-list

Inject a list of videos.

 * `data-sort`: sort the videos (`-publishedAt`, `-views`, `views` etc)
 * `data-count`: limit number of displayed videos
 * `data-max-rows`: limit number of video rows
 * `data-only-local`: only display local videos?
 * `data-is-live` (**Tube >= 3.4**): only display VOD or live videos
 * `data-category-one-of`: coma separated Tube categories (use keys of https://tube.docs.dingshunyu.top/api-rest-reference.html#operation/getCategories)
 * `data-language-one-of`:  coma separated Tube languages (use keys of https://tube.docs.dingshunyu.top/api-rest-reference.html#operation/getLanguages)
 * `data-channel-handle`:  only display videos of this channel (`chocobozzz_channel` for a local channel, or `chocobozzz_channel@peertube2.cpy.re` for a remote channel)
 * `data-account-handle`:  only display videos of this account (`chocobozzz` for a local account, or `chocobozzz@peertube2.cpy.re` for a remote account)
 * `data-only-display-title`: display or not miniature attributes (views, channel etc)

```
<tube-videos-list
  data-only-display-title="false" data-title="Listed by languages" data-language-one-of="fr,en" data-count="3"
  data-only-local="false" data-max-rows="2" data-sort="-publishedAt" data-is-live="false"
></tube-videos-list>

<tube-videos-list data-channel-handle="chocobozzz@peertube2.cpy.re"></tube-videos-list>

<tube-videos-list data-account-handle="chocobozzz"></tube-videos-list>
```


