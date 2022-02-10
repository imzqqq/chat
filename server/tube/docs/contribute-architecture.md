# Architecture

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Architecture](#architecture)
  - [Overview](#overview)
      - [Vocabulary](#vocabulary)
      - [Tube instance](#tube-instance)
      - [The Tube player](#the-tube-player)
      - [Communications between instances](#communications-between-instances)
      - [Redundancy between instances](#redundancy-between-instances)
  - [Technical Overview](#technical-overview)
      - [The user](#the-user)
      - [The web interface](#the-web-interface)
      - [The reverse proxy](#the-reverse-proxy)
      - [The REST API server](#the-rest-api-server)
      - [The database](#the-database)
      - [The cache/job queue](#the-cachejob-queue)
  - [Server code](#server-code)
      - [Technologies](#technologies)
      - [Files](#files)
      - [Conventions](#conventions)
      - [Architecture](#architecture-1)
      - [Newcomers](#newcomers)
  - [Client code](#client-code)
      - [Technologies](#technologies-1)
      - [Files](#files-1)
      - [Conventions](#conventions-1)
      - [Concepts](#concepts)
      - [Components tree](#components-tree)
      - [Newcomers](#newcomers-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Overview

#### Vocabulary

**Fediverse**: several servers following one another, several users
following each other. Designates federated communities in general.

**Vidiverse**: same as Fediverse, but federating videos specifically.

**Instance**: a server which runs Tube in the fediverse.

**Origin/local instance**: the instance on which the video was uploaded and that serves the video behind a HTTP server.

**Cache instance**: an instance that decided to mirror a remote video. It sends a [`CacheFile` activity](api-activitypub) to notify the origin instance.

**Following**: the action of a Tube instance which will follow another
instance (subscribe to its videos). You can read more about Follows in the admin doc,
under [following servers](admin-following-instances).

#### Tube instance

- An instance is like a website: it has an administrator, and people can create an account on the instance.
Users have multiple channels in which they decide to upload videos.
- An instance acts like a normal webserver: users can upload videos and the instance will serve files behind an HTTP server.
It also has a [websocket tracker](https://github.com/webtorrent/bittorrent-tracker) which responds to any request on local videos.
Then, a classic video player can play the video using the HTTP protocol, but the tracker can also share some segments of the video using P2P.
- An instance has an administrator that can follow other instances using [ActivityPub protocol](https://www.w3.org/TR/activitypub/) so that other instance videos can be displayed on the local instance.

#### The Tube player

When a user is watching a video:
  - If using the HLS player (depending on the admin transcoding configuration):
    - The player loads the HLS playlist using [hls.js](https://github.com/video-dev/hls.js/) from the origin server
    - Tube provides a [custom loader](https://github.com/chocobozzz/p2p-media-loader) to *hls.js* that downloads segments from HTTP but also from P2P via WebRTC
    - Segments are loaded by HTTP from the origin server/servers that mirrored the video **and** by WebRTC from other web browsers that are watching the video. They are used by *hls.js* to stream them into the `<video>` HTML element
  - If using the WebTorrent player (depending on the admin transcoding configuration):
    - The player uses WebTorrent to fetch the magnet URI in order to download the torrent file from the origin server
    - The torrent file is used to retrieve the video media file segments, and download them over HTTP (using the [WebSeed](http://bittorrent.org/beps/bep_0019.html) protocol) from the origin server/servers that mirrored the video. WebTorrent also sends requests to the tracker to download/share segments from other WebRTC peers.
    - Segments are loaded by HTTP from the origin server/servers that mirrored the video **and** by WebRTC from other web browsers that are watching the video. They are used used by [videostream](https://github.com/jhiesey/videostream/) to stream them into the `<video>` HTML element
  - If the HLS and WebTorrent players failed (bad codecs for example), Tube injects the raw source video file in the `src` attribute of the `<video>` HTML element

#### Communications between instances

![Sharing videos metadata to build an index of more than just local videos](/assets/schema/decentralized.png)

- We use the [server-server ActivityPub](https://www.w3.org/TR/activitypub/) protocol. Activities and model documentation can be found on https://tube.docs.dingshunyu.top/api-activitypub.
- ActivityPub messages are signed with [JSON Linked Data Signatures](https://w3c-dvcg.github.io/ld-signatures/) with the private key of the account that authored the action.
- ActivityPub messages are also signed with [HTTP Signatures](https://datatracker.ietf.org/doc/draft-cavage-http-signatures/) with the private key of the account that sent the message.
- All the requests are retried several times if they fail.
- A Tube instance has always a `tube` ActivityPub `Actor` that other instances can follow. This `tube` actor `Announce` videos uploaded on the instance so other actors are notified by new videos.
- A video is uploaded by an account, that is an `Actor`. This video is also `Announce` by the channel in which it has been uploaded. Then, remote ActivityPub actors can follow the account or the channel to be notified by new videos.

#### Redundancy between instances

A Tube instance can mirror other Tube videos to improve bandwidth use.

The instance administrator can choose between multiple redundancy strategies (cache trending videos or recently uploaded videos etc.), set their maximum size and the minimum duplication lifetime.
Then, they choose the instances they want to cache in `Manage follows -> Following` admin table.

Videos are kept in the cache for at least `min_lifetime`, and then evicted when the cache is full.

When Tube chooses a video to duplicate, it imports all the resolution files (to avoid consistency issues) using their magnet URI/playlist URL and put them in the `storage.redundancy` directory.
Then it sends a `Create -> CacheFile` ActivityPub message to other federated instances. This new instance is injected as [WebSeed](https://github.com/Chocobozzz/Tube/blob/develop/FAQ.md#what-is-webseed) in the torrent file for the WebTorrent player and injected in the video API response for the HLS player.

![Cache servers](/assets/schema/redundancy.png)

## Technical Overview

Tube is more than just a web page. Simplifying things a lot, we can
see it as two parts: a client application that executes in the browser of
each visitor (and that can be replaced entirely with a new client of your
choice, of course), and a server part that resides on the machine of the
instance's system administrator.

As is common among modern applications, it is in reality made of several
more components than just a "client" and "server", each of them fulfilling
a specific mission:

- a modern database engine (PostgreSQL) to store long-term metadata
- a reverse proxy (we officially support Nginx but nothing prevents us from using others) to handle certificates, and directly serve static assets
- a key-value store (Redis) to help application caching and task queueing
- a server application with:
  - controllers to server static and clients files
  - a REST API server providing the actual logic
  - an ActivitPub API to receive messages and present local objects
  - a BitTorrent tracker to allow clients to do P2P
  - a socker.io server to notify clients
  - a task scheduler to send ActivityPub requests, run transcoding jobs etc.
- a  _Single Page Application_ web client that consumes the REST API
- a user that interacts with the web client of his choice

![architecture overview](/assets/architecture-overview.jpg)

Let's give detail of each component:

#### The user

Tube users can interact with your instance using:

- The official web interface
- Third-party apps (other clients using the REST API)

#### The web interface

This refers to Tube's official web interface, which is a Single Page application
written in Angular. This application will interact with Tube's API to retrieve
or send data. Of course any alternative client interface can be used so long as it is
compatible with the API, and Tube can be started without the official web interface
to let you serve your client instead.

#### The reverse proxy

Tube's API server should never be exposed directly to the internet, as we require
a reverse proxy (we provide support for Nginx) for performance and security. The reverse proxy
will receive client HTTP requests, and:

- Proxy them to the API server
- Serve requested static files (Video files, stylesheets, javascript, fonts...)

#### The REST API server

The API server is the central piece of Tube. This component is responsible
for answering and processing user requests, manipulating data from the database, sending long-running
tasks to the local worker, etc.

It's an [Express](https://expressjs.com/) application.

#### The database

Most of the data such as user accounts, video metadata, comments or channels are stored
in a PostgreSQL database.

#### The cache/job queue

Fetching data from the database is sometimes slow or resource hungry. To reduce
the load, Redis is used as a cache for route data meant to be stored temporarily.

It is also a message queue that will deliver tasks to the local worker. Indeed, Tube
uses the [Bull queue](https://github.com/OptimalBits/bull) which doesn't support remote
workers yet.

## Server code

The server is a web server developed with [TypeScript](https://www.typescriptlang.org/)/[Express](http://expressjs.com).


#### Technologies

  * [TypeScript](https://www.typescriptlang.org/) -> Language
  * [PostgreSQL](https://www.postgresql.org/) -> Database
  * [Redis](https://redis.io/) -> Job queue/cache
  * [Express](http://expressjs.com) -> Web server framework
  * [Sequelize](http://docs.sequelizejs.com/en/v3/) -> SQL ORM
  * [WebTorrent](https://webtorrent.io/) -> BitTorrent tracker and torrent creation
  * [Mocha](https://mochajs.org/) -> Test framework


#### Files

The server main file is [server.ts](https://github.com/Chocobozzz/Tube/blob/develop/server.ts).
The server modules descriptions are in the [package.json](https://github.com/Chocobozzz/Tube/blob/develop/package.json) at the project root.
All other server files are in the [server](https://github.com/Chocobozzz/Tube/tree/develop/server) directory:

    server.ts -> app initialization, main routes configuration (static routes...)
    config    -> server YAML configurations (for tests, production...)
    scripts   -> Scripts files for npm run
    server
    |__ assets       -> static assets files (images...)
    |__ controllers  -> API routes/controllers files
    |__ helpers      -> functions used by different part of the project (logger, utils...)
    |__ initializers -> functions used at the server startup (installer, database, constants...)
    |__ lib          -> library function (WebTorrent, OAuth2, ActivityPub...)
    |__ middlewares  -> middlewares for controllers (requests validators, requests pagination...)
    |__ models       -> Sequelize models for each SQL tables (videos, users, accounts...)
    |__ tests        -> API tests and real world simulations (to test the decentralized feature...)


#### Architecture

The server is composed of:

  * a REST API (relying on the Express framework) documented on https://tube.docs.dingshunyu.top/api-rest-reference.html
  * a WebTorrent Tracker (slightly custom version of [webtorrent/bittorrent-tracker](https://github.com/webtorrent/bittorrent-tracker#server))

A video is seeded by the server with the [WebSeed](http://www.bittorrent.org/beps/bep_0019.html) protocol (HTTP).

![Architecture scheme](/assets/schema/architecture-server.png)

When a user uploads a video, the REST API creates the torrent file and then adds it to its database.

If a user wants to watch the video, it will send a request to the tracker to download/share video segments from other peers.
The user also downloads segments using HTTP.


#### Transcoding

Please read the documentation regarding WebTorrent and HLS transcoding first: https://tube.docs.dingshunyu.top/admin-configuration?id=vod-transcoding

After the video upload:
 * An optimize transcoding job is created by Tube.
 This job will transcode the video using `x264/aac` codecs with custom bitrates in a `.mp4` container.
 The purpose of this job is to create a `WebTorrent` compatible file.
 * On optimization success, Tube will:
   * Create the `HLS` job of the optimized file resolution if HLS transcoding is enabled
   * Create lower `WebTorrent` resolution jobs
 * On `WebTorrent` resolution job success, Tube will:
   * Create the `HLS` job for this resolution if HLS transcoding is enabled
 * On `HLS` job success:
   * If `WebTorrent` is disabled and the transcoded file was the highest video resolution:
    * Create lower `HLS` resolution jobs and delete `WebTorrent` files


#### Newcomers

The server entry point is [server.ts](https://github.com/Chocobozzz/Tube/blob/develop/server.ts). Looking at this file is a good start.
Then you can try to understand the [controllers](https://github.com/Chocobozzz/Tube/tree/develop/server/controllers): they are the entrypoints of each API request.


## Client code

The client is a HTML/CSS/JavaScript web application (single page application -> SPA) developed with [TypeScript](https://www.typescriptlang.org/)/[Angular](https://angular.io/).


#### Technologies

  * [TypeScript](https://www.typescriptlang.org/) -> Language
  * [Angular](https://angular.io) -> JavaScript framework
  * [Webpack](https://webpack.js.org/) -> Source builder (compile TypeScript, SASS files, bundle them...)
  * [SASS](http://sass-lang.com/) -> CSS framework
  * [Bootstrap](http://getbootstrap.com/) -> CSS framework
  * [VideoJS](http://videojs.com/) -> JavaScript player framework
  * [WebTorrent](https://webtorrent.io/) -> JavaScript library to use P2P in the browser (via BitTorrent over WebRTC)
  * [P2P Media Loader](https://github.com/chocobozzz/p2p-media-loader) - JavaScript library to use P2P in the browser (via HLS and BitTorrent over WebRTC)
  * [hls.js](https://github.com/video-dev/hls.js/) -> JavaScript library to handle HLS playlists


#### Files

The client files are in the `client` directory. The Webpack configurations files are in `client/config` and the source files in `client/src`.
The client modules descriptions are in the [client/package.json](https://github.com/Chocobozzz/Tube/tree/develop/client/package.json). There are many modules that are used to compile the web application in development or production mode.
Here is the description of the useful `client` files directory:

    tslint.json     -> TypeScript linter rules
    tsconfig.*.json -> TypeScript configuration for the compilation
    webpack         -> Webpack configuration files (to build the embed page)
    e2e             -> E2E tests (using Protractor)
    src
    |__ app          -> TypeScript files for Angular application
    |__ assets       -> static files (images...)
    |__ locale       -> translation files used by Angular and the player
    |__ sass         -> SASS files that are global for the application
    |__ standalone   -> files outside the Angular application (embed HTML page...)
    |__ index.html   -> root HTML file for our Angular application
    |__ main.ts      -> Main TypeScript file that boostraps our Angular application
    |__ polyfills.ts -> Polyfills imports (ES 2015...)

Details of the Angular application file structure. It tries to follow [the official Angular styleguide](https://angular.io/docs/ts/latest/guide/style-guide.html).

    app
    |__ +about                       -> About components (about the instance, about tube...)
    |__ +accounts                    -> Account components (list account videos...)
    |__ +admin                       -> Admin components (followers, users...)
    |__ +login                       -> Login components
    |__ +my-account                  -> My account components (update my profile, notifications...)
    |__ +my-library                  -> My library components (list my channels, list my videos...)
    |__ +search                      -> Search components (search channels/videos...)
    |__ +video-channels              -> Video channels components (list channel videos, channel playlists...)
    |__ +videos                      -> Edit video/watch video/list videos components
    |__ core                         -> Core components/services that should be injected only once
    |__ header                       -> Header components (logo, search...)
    |__ menu                         -> Menu component (on the left)
    |__ shared                       -> Shared components/services used by multiple modules (search component, REST services...)
    |__ app.component.{html,scss,ts} -> Main application component
    |__ app-routing.module.ts        -> Main Angular routes
    |__ app.module.ts                -> Angular root module that imports all submodules we need

#### Concepts

In an Angular application, we create components that we put together. Each component is defined by an HTML structure, a TypeScript file and optionally a SASS file.
If you are not familiar with Angular I recommend you to read the [quickstart guide](https://angular.io/docs/ts/latest/quickstart.html).

#### Newcomers

The main client component is `app.component.ts`. You can begin to look at this file. Then you could navigate in the different submodules to see how components are built.
