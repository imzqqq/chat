# Configurations

## Tube configuration

Tube configuration is loaded with [node-config](https://www.npmjs.com/package/config). You can have multiple config files that will be selected due to a specific [file load order](https://github.com/lorenwest/node-config/wiki/Configuration-Files#file-load-order).

The configuration file is parsed during application start, which means that Tube has to be restarted for the changes to have effect.

You can find the configuration options documented in the [default.yaml](https://github.com/Chocobozzz/Tube/blob/develop/config/default.yaml).


## Environment variables

 * `PT_INITIAL_ROOT_PASSWORD`: Set up an initial administrator password. It must be 6 characters or more
 * `FFMPEG_PATH` and `FFPROBE_PATH`: Use custom FFmpeg/FFprobe binaries
 * `HTTP_PROXY` and `HTTPS_PROXY`: (**Tube >= 3.4**) Use proxy for HTTP requests
 * `YOUTUBE_DL_DOWNLOAD_HOST`: Use a custom host to download youtube-dl binaries (default is `https://yt-dl.org/downloads/latest/youtube-dl`). Should respect `youtube-dl` URL behaviour:
   * `YOUTUBE_DL_DOWNLOAD_HOST` should redirect (`302`) to another URL that should ends with `/:date/youtube-dl` (for example: `https://example.com/youtube-dl/downloads/2021.06.06/youtube-dl`)
   * This new URL should point to the `youtube-dl` binary (redirections are supported)


## Security

Installing Tube following the production guide should be secure enough by default. We list here suggestions
to tighten the security of some parts of Tube.

### Systemd Unit with reduced privileges

A systemd unit template is provided at `support/systemd/tube.service`.

#### `PrivateDevices`

<div class="alert alert-warning" role="alert">
  <strong>Warning:</strong> this won't work on Raspberry Pi. That's
  why we don't enable it by default.
</div>

`PrivateDevices=true` sets up a new `/dev` mount for the Peertube process and
only adds API pseudo devices like `/dev/null`, `/dev/zero`, or `/dev/random`
but not physical devices.

#### `ProtectHome`

`ProtectHome=true` sandboxes Peertube such that the service can not access the
`/home`, `/root`, and `/run/user` folders. If your local Peertube user has its
home folder in one of the restricted places, either change the home directory
of the user or set this option to `false`.


## Cache

Fine-tuning the cache is key to good performance, especially if you have a big instance and/or want to
get the most out of your hardware. The following recommendations may be specific to some situations and
no solution fitting everyone is given.

### Leveraging Nginx Caching

!> **Warning:** this section is experimental and subject to change. Don't copy commands without adapting them to your situation first.

If you are using Nginx, you might want to benefit from its caching capabilities. Of course Tube does
application caching itself, but it's nowhere as efficient as a reverse proxy like Nginx when it comes to
static assets (and videos are a static asset, so we have some easy improvement there!). From the [documentation
of Nginx](https://www.nginx.com/blog/nginx-caching-guide/) (which we recommend you read for an in-depth
explanation of what follows):

> A content cache sits in between a client and an “origin server”, and saves copies of all the content it
> sees. If a client requests content that the cache has stored, it returns the content directly without
> contacting the origin server. This improves performance as the content cache is closer to the client,
> and more efficiently uses the application servers because they don’t have to do the work of generating
> pages from scratch each time.

Using the `proxy_cache` directive from Nginx, we can optimize the static route `/static/webseed` that we
already proxy in our configuration.

```nginx
  location /static/webseed {
    # comment this line out:
    # alias /var/www/tube/storage/videos;

    # add these lines:
    proxy_pass http://localhost:1234;
    proxy_cache peertube_videos;
  }
```

And at the end of the file, outside any server blocks, add this:

```nginx
proxy_cache_path /var/cache/tube/videos/
    levels=1:2 keys_zone=peertube_videos:10m
    max_size=15g inactive=7d use_temp_path=off;
proxy_cache_valid 200 60m;

server {
  listen localhost:1234;
  location /static/webseed {
    alias /var/www/tube/storage/videos;
  }
}
```
