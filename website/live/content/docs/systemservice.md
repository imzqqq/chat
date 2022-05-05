---
title: "Run as a system service"
description: Setup owncast to run as a system service, automatically starting when your server does.
menu:
  docs:
    parent: "guides"
weight: 200
toc: false
---

You can optionally setup Live to run under [systemd](https://systemd.io/) so it's a managed service on your machine that automatically starts when your machine does.

A full example can be found [here](https://github.com/imzqqq/blob/master/examples/owncast-sample.service).

{{< highlight ini >}}
[Unit]
Description=Live
{{< / highlight >}}

This is where the "functional" parts of the service live.

{{< highlight ini >}}
[Service]
Type=simple
WorkingDirectory=[path_to_owncast_root_directory]
ExecStart=[path_to_owncast_executable]
Restart=on-failure
RestartSec=5
{{< / highlight >}}

`WorkingDirectory` should be where you want the owncast folder to live.

### Example

`WorkingDirectory=/home/myuser/owncast`

Similarly the `ExecStart` is the actual owncast binary.

### Example

`ExecStart=/home/myuser/owncast/owncast`

{{< highlight ini >}}
[Install]
WantedBy=multi-user.target
{{< / highlight >}}

### Installation

Just create the file in your systemd configuration directory (typically /etc/systemd/system/), and update the systemd daemon with:
`sudo systemctl daemon-reload`
