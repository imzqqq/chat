---
title: "Configuration"
description: Configuration is done through the Live administration page. Learn what you have control over and what customizations can be made.
menu:
  docs:
    parent: "configure"
weight: 100
toc: true
---

Configuration is done through the Live administration page located on your server under `/admin`. The login username is `admin` and the password is your stream key, the default being `abc123`.

Some common items many people would want to update after installing Live are:

- Your site name, logo, description and external links that are displayed on the [web site](/docs/website).
- The **stream key** to gain access to broadcasting to your stream and your admin.
- Enable your stream to show up in the [Live Directory](/docs/directory).

## Web site details

Your site name, logo, description, and page content can be set in the admin. You can also add links to your social profiles and web sites that exist elsewhere. [See details about the web site and chat interface](/docs/website).
{{<versionsupport feature="Changing page settings in the admin panel" version="0.0.6">}}

{{< figure src="/docs/img/admin-general-settings.png" caption="Live general settings" width="80%" >}}

## Video output

Depending on your hardware you may be able to configure your server to support multiple output variants for multiple different viewing conditions. [Learn how to configure your video and see how it directly effects your CPU usage](/docs/encoding).

{{< figure src="/docs/img/admin-config-video-variant.png" caption="Live video settings" width="80%" >}}

{{<versionsupport feature="Changing video settings in the admin panel" version="0.0.6">}}

## Custom Ports

Per default, Live will run a `http` web server on port `8080` and a RTMP server on port `1935`. You can change the ports in the the admin. You must restart Live for these changes to take effect.

You can also set the port for the web server on the command line via the `-webserverport` flag.

{{< figure src="/docs/img/admin-server-settings.png" caption="Live server settings" width="80%" >}}
{{<versionsupport feature="Custom Ports" version="0.0.4">}}
{{<versionsupport feature="Port settings in the admin panel" version="0.0.6">}}

## External storage providers

Instead of serving video directly from your personal server you can use a S3 compatible storage provider to offload the bandwidth and storage requirements elsewhere. [See how to configure the storage provider of your choice](/docs/storage).
