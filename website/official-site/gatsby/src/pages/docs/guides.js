import React from 'react'
import { graphql } from 'gatsby'

import Helmet from 'react-helmet'
import { Layout, MXContentMain } from '../../components'
import config from '../../../config'


const Guides = ({data}) => {

    return (<Layout navmode="discover">
        <MXContentMain>
          <Helmet title={`Guides | ${config.siteTitle}`} />

        <h2 id="recommended-by-matrixorg">Guides Recommended by matrix.org</h2>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>If you want to…</th>
        <th>Then read…</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Just get started!</td>
        <td>
          <strong><a href="/docs/guides/getting-involved">Getting Involved</a></strong>, which explains various ways of getting started with Chat<br /><br />or<br /><br />
          <strong><a href="/faq/">the FAQ</a></strong>, where we try to answer all your questions relating to Chat</td>
      </tr>
      <tr>
        <td>Get started with the CS API using …</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>   … JavaScript</td>
        <td><strong><a href="/docs/guides/usage-of-the-matrix-js-sdk">Usage of the matrix-js-sdk</a></strong></td>
      </tr>
      <tr>
        <td>   … Python</td>
        <td><strong><a href="https://matrix-org.github.io/matrix-python-sdk/">Chat Python SDK documentation</a></strong></td>
      </tr>
      <tr>
        <td>Understand Application Services</td>
        <td>The <strong><a href="/docs/guides/application-services">Application services</a></strong> guide introduces them and explains what they can be used for</td>
      </tr>
      <tr>
        <td>See an example of a simple bot</td>
        <td><strong><a href="https://github.com/4nd3r/tiny-matrix-bot">tiny-matrix-bot</a></strong> source code</td>
      </tr>
      <tr>
        <td>Create a complete working chatbot</td>
        <td><strong><a href="/docs/guides/using-matrix-to-make-chatbot-software-from-the-1960-s-available-in-2018">Using Chat to make Chatbot software from the 1960s available in 2018</a></strong> goes from the basics, brings in <a href="https://github.com/turt2live/matrix-bot-sdk">matrix-bot-sdk</a>, and ends with deployment on a Raspberry Pi</td>
      </tr>
      <tr>
        <td>Work on Bridges to Chat</td>
        <td><strong><a href="/docs/guides/types-of-bridging">Types of Bridging</a></strong> should be read by all bridge developers to ensure everyone has the same mental map of terminology when implementing bridges</td>
      </tr>
      <tr>
        <td>Migrate from the old v1 CS API</td>
        <td>the <strong><a href="/docs/guides/migrating-from-client-server-api-v-1">v1 migration guide</a></strong> which justs lists the changes from v1 to r0</td>
      </tr>
      <tr>
        <td>Use E2E in a client application</td>
        <td>The <strong><a href="/docs/guides/end-to-end-encryption-implementation-guide">End-to-end Encryption Implementation Guide</a></strong> is intended for client developers who wish to add support for end-to-end encryption to their clients</td>
      </tr>
      <tr>
        <td>Install Chat</td>
        <td><strong><a href="/docs/guides/installing-synapse">Installing Chat Guide</a></strong></td>
      </tr>
      <tr>
        <td>   …  using Docker</td>
        <td><strong><a href="https://github.com/matrix-org/synapse/tree/master/docker">the Chat Docker README</a></strong></td>
      </tr>
      <tr>
        <td>Understand Moderation in Chat</td>
        <td><strong><a href="/docs/guides/moderation">Moderation in Chat</a></strong></td>
      </tr>
    </tbody>
  </table>
</div>

<div className="width-100">
  <table className="legacy-table">
    <tbody>
      <tr>
      </tr>
    </tbody>
  </table>
</div>

<h2 id="introduction-for-users">Introduction for Users</h2>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>Article</th>
        <th>Author</th>
        <th>Language</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><a href="https://www.snoyman.com/blog/2018/05/guide-to-matrix-riot">Guide to matrix.org and riot.im</a></td>
        <td>Michael Snoyman</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="http://laforesta.net/riot-matrix/">Riot Chat from La Foresta</a></td>
        <td>La Foresta team</td>
        <td>Italian</td>
      </tr>
      <tr>
        <td><a href="https://opensource.com/article/17/5/introducing-riot-IRC">IRC for the 21st Century: Introducing Riot</a></td>
        <td>Justin W. Flory</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://zinz.dev">Introduction à Chat et guide d'utilisation</a></td>
        <td><a href="https://luxeylab.net">Adrien Luxey</a></td>
        <td>French</td>
      </tr>
      <tr>
        <td><a href="https://blog.sp-codes.de/werde-teil-der-matrix-matrix-teil-1/">Werde Teil der Chat</a></td>
        <td><a href="https://sp-codes.de">Samuel</a></td>
        <td>German</td>
      </tr>
    </tbody>
  </table>
</div>

<h2 id="installing-synapse">Installing Chat</h2>

<p>To install Chat, we recommend taking a look at the <a href="/docs/guides/installing-synapse">Installing Chat</a> guide page. Other articles are listed below.</p>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>Article</th>
        <th>Author</th>
        <th>Language</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><a href="https://www.natrius.eu/dokuwiki/doku.php?id=digital:server:matrixsynapse">Install Chat Chat</a></td>
        <td><a href="https://to.chat.imzqqq.top/#/#synapseguide:matrix.org">Stefan</a></td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.mytinydc.com/en/blog/matrix-synapse-installationserver-riotclient/">Chat-Riot.im : How to install Chat-Chat server - Raspberry PI - Rock64</a></td>
        <td>dHENRY</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.foxypossibilities.com/2018/02/04/running-matrix-synapse-on-nixos/">Running Chat Chat on NixOS</a></td>
        <td><a href="https://www.foxypossibilities.com/">Kiba Fox</a></td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://blog.cryptoaustralia.org.au/2017/03/21/run-your-end-to-end-encrypted-chat-server-matrix-riot/">Run your end-to-end encrypted chat server using Chat and Riot</a></td>
        <td>Gabor Szathmari</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.howtoforge.com/tutorial/how-to-install-matrix-synapse-on-ubuntu-1804/">How to Install Chat Chat Chat on Ubuntu 18.04 LTS</a></td>
        <td>Muhammad Arul</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.digitalocean.com/community/tutorials/how-to-install-matrix-synapse-on-ubuntu-16-04">How To Install Chat Chat on Ubuntu 16.04</a></td>
        <td>Oliver Lumby for DigitalOcean</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://gist.github.com/attacus/cb5c8a53380ca755b10a5b37a636a0b9">Running your own encrypted chat service with Chat and Riot</a></td>
        <td><a href="https://github.com/attacus">github.com/attacus</a></td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.gigenet.com/blog/how-to-secure-your-chats-with-matrix/">How to secure your chats with Chat</a></td>
        <td>Zachary Muller</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://linuxconfig.org/install-riot-matrix-chat-client-on-debian-and-ubuntu">Install Riot Chat Chat Client On Debian and Ubuntu</a></td>
        <td>Nick Congleton</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://jonnev.se/matrix-homeserver-synapse-v0-99-1-1-with-traefik/">Chat homeserver (v1.0.0) with Docker and Traefik</a></td>
        <td>Jon Neverland</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.topbug.net/blog/2016/12/05/set-up-synapse-matrix-homeserver-on-ubuntu-16-04/">Set up Chat (Chat Homeserver) on Ubuntu 16.04</a></td>
        <td>Hong Xu</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://decatec.de/home-server/matrix-synapse-auf-ubuntu-server-20-04-lts-mit-nginx-postgresql-und-lets-encrypt/">Chat Chat auf Ubuntu Server 20.04 LTS mit nginx, PostgreSQL und Let’s Encrypt</a></td>
        <td><a href="https://decatec.de/">Jan</a></td>
        <td>German</td>
      </tr>
      
    </tbody>
  </table>
</div>

<h2 id="synapse-maintenance">Chat Maintenance</h2>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>Article</th>
        <th>Author</th>
        <th>Language</th>
      </tr>
    </thead>
    <tbody>
      <tr>
          <td><a href="https://levans.fr/shrink-synapse-database.html">Compressing Chat database</a></td>
          <td>Victor Berger / Levans</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/metrics-howto.md">How to monitor Chat metrics using Prometheus</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/postgres.md">Using Postgres</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/turn-howto.md">How to enable VoIP relaying on your Home Server with TURN</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/federate.md">Setting up Federation</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/server_notices.md">Use of Server Notices</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/opentracing.md">Use of OpenTracing</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://github.com/matrix-org/synapse/blob/master/docs/workers.md">Scaling synapse via workers</a></td>
          <td>Chat Team</td>
          <td>English</td>
      </tr>
      <tr>
          <td><a href="https://hub.netzgemeinde.eu/articles/buckaroo/synapseworkers">Boosting matrix/synapse by using workers </a></td>
          <td>Mark Nowiasz</td>
          <td>English</td>
      </tr>
    </tbody>
  </table>
</div>

There are many other <a href="https://github.com/matrix-org/synapse/tree/master/docs">howtos and documents in the Chat repo</a>.

<h2 id="understanding-end-to-end-encryption">Understanding End-to-End Encryption</h2>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>Article</th>
        <th>Author</th>
        <th>Language</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><a href="https://www.uhoreg.ca/blog/20170910-2110">An introduction to end-to-end encryption in Chat and Riot</a></td>
        <td>Hubert Chathi</td>
        <td>English</td>
      </tr>
    </tbody>
  </table>
</div>

<div className="width-100">
  <table className="legacy-table">
    <tbody>
      <tr>
      </tr>
    </tbody>
  </table>
</div>

<h2 id="translations">Translations</h2>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>Article</th>
        <th>Author</th>
        <th>Language</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><a href="https://github.com/vector-im/riot-web/blob/develop/docs/translating.md">How to translate Riot-Web</a></td>
        <td>Riot-Web Team</td>
        <td>English</td>
      </tr>
    </tbody>
  </table>
</div>

<h2 id="video-guides">Video Guides</h2>

<div className="width-100">
  <table className="legacy-table">
    <thead>
      <tr>
        <th>Article</th>
        <th>Author</th>
        <th>Language</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><a href="https://www.youtube.com/watch?v=ZNEzgYRLj8g">Chat Bridging to IRC, Slack and Telegram</a></td>
        <td>Jon Spriggs</td>
        <td>English</td>
      </tr>
      <tr>
        <td><a href="https://www.youtube.com/watch?v=TUgQ7Qh754w">Riot - Messagerie Instantanée Décentralisée &amp; Sécurisée !</a></td>
        <td>Nouts</td>
        <td>French</td>
      </tr>
    </tbody>
  </table>
</div>

        </MXContentMain>
    </Layout>)
}


export const query = graphql`
{
    allFile(filter: { sourceInstanceName: { eq: "projects" } }) {

        edges {
            node {
                childMdx {
                    frontmatter {
                        title
                        maturity
                        description
                        thumbnail
                        featured
                        categories
                        language
                        author
                        repo
                        room
                        e2e
                    }
                    body
                }
                absolutePath
            }
        }
    }
}
`
export default Guides
