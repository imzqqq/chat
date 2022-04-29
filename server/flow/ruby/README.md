# Flow Server

Flow is a **free, open-source social network server** based on ActivityPub where users can follow friends and discover new ones. On Flow, users can publish anything they want: links, pictures, text, video. All Flow servers are interoperable as a federated network (users on one server can seamlessly communicate with users from another one, including non-Flow software that implements ActivityPub)!

## Features

<img src="http://flow.docs.imzqqq.top/elephant.svg" align="right" width="30%" />

**No vendor lock-in: Fully interoperable with any conforming platform**

It doesn't have to be Flow, whatever implements ActivityPub is part of the social network! [Learn more](https://blog.joinmastodon.org/2018/06/why-activitypub-is-the-future/)

**Real-time, chronological timeline updates**

See the updates of people you're following appear in real-time in the UI via WebSockets. There's a firehose view as well!

**Media attachments like images and short videos**

Upload and view images and WebM/MP4 videos attached to the updates. Videos with no audio track are treated like GIFs; normal videos are looped - like vines!

**Safety and moderation tools**

Private posts, locked accounts, phrase filtering, muting, blocking and all sorts of other features, along with a reporting and moderation system. [Learn more](https://blog.joinmastodon.org/2018/07/cage-the-mastodon/)

**OAuth2 and a straightforward REST API**

Flow acts as an OAuth2 provider so 3rd party apps can use the REST and Streaming APIs, resulting in a rich app ecosystem with a lot of choices!

## Development

Run

```bash
sudo systemctl stop 'flow-*.service'
RAILS_ENV=production bundle exec rails assets:precompile
RAILS_ENV=production ./bin/tootctl feeds build
sudo systemctl daemon-reload
sudo systemctl enable --now flow-web flow-sidekiq flow-streaming
```

## Deployment

**Tech stack:**

- **Ruby on Rails** powers the REST API and other web pages
- **React.js** and Redux are used for the dynamic parts of the interface
- **Node.js** powers the streaming API

**Requirements:**

- **PostgreSQL** 9.5+
- **Redis** 4+
- **Ruby** 2.5+
- **Node.js** 12+

The repository includes deployment configurations for **Docker and docker-compose**, but also a few specific platforms like **Heroku**, **Scalingo**, and **Nanobox**. The [**stand-alone** installation guide](https://flow.docs.imzqqq.top/admin/install) is available in the documentation.

A **Vagrant** configuration is included for development purposes.
