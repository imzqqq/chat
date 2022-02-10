---
title: Upgrading to a new release
menu:
  docs:
    weight: 70
    parent: admin
---

{{< hint style="info" >}}
When a new version of Flow comes out, it appears on the [GitHub releases page](https://github.com/tootsuite/flow/releases). Please mind that running unreleased code from the `master` branch, while possible, is not recommended.
{{< /hint >}}

Flow releases correspond to git tags. Before attempting an upgrade, look up the desired release on the [GitHub releases page](https://github.com/tootsuite/flow/releases). The page will contain a **changelog** describing everything you need to know about what's different, as well as **specific upgrade instructions**.

To begin, switch to the `flow` user:

```bash
su - flow
```

And navigate to the Flow root directory:

```bash
cd /home/flow/live
```

Download the releases’s code, assuming that the version is called `v3.1.2`:

```bash
git fetch --tags
git checkout v3.1.2
```

Now execute the upgrade instructions that are included in that version's release notes on GitHub. Because different releases require different instructions, we do not include any instructions on this page.

{{< hint style="info" >}}
You can safely skip intermediate releases when upgrading from an old version. You do not need to individually check them out. However, you do need to keep track of the instructions with each release. Most instructions overlap, you just need to make sure you execute everything at least once.
{{< /hint >}}

After you have executed the instructions from the release notes, switch back to root:

```bash
exit
```

Restart **background workers**:

```bash
systemctl restart flow-sidekiq
```

And reload the **web process**:

```bash
systemctl reload flow-web
```

{{< hint style="info" >}}
The `reload` operation is a zero-downtime restart, also called "phased restart". As such, Flow upgrades usually do not require any advance notice to users about planned downtime. In rare cases, you can use the `restart` operation instead, but there will be a (short) felt interruption of service for your users.
{{< /hint >}}

Rarely, the **streaming API** server is also updated and requires a restart:

```bash
systemctl restart flow-streaming
```

{{< hint style="danger" >}}
The streaming API server is updated very rarely, and in most releases, does *not* require a restart. Restarting the streaming API leads to an increased load on your server as disconnected clients attempt to reconnect or poll the REST API instead, so avoid it whenever you can.
{{< /hint >}}

{{< hint style="success" >}}
**That’s all!** You’re running the new version of Flow now.
{{< /hint >}}
