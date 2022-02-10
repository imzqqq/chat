<template>
  <main id="faq">
    <div class="blocks">

      <div class="title-block">
        <h1 v-translate>FAQ</h1>

        <p v-translate>A few questions to discover Tube</p>

        <div class="separator"></div>
      </div>

      <div class="block" role="tablist">
        <h2 v-translate>Tube Presentation</h2>

        <accordion-element id="What is Tube?">
          <template v-slot:title>
            {{ 'What is Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube is software that you install on a web server. It allows you to create a video hosting website, so create your
              "homemade YouTube".
            </p>

            <p v-translate>
              The difference to YouTube is that it's not intended to create a huge platform centralizing videos from the whole world on a
              single server farm (which is horribly expensive).
            </p>

            <p v-translate>
              On the contrary, Tube's concept is to create a network of multiple small interconnected video hosting providers.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="What are the main advantages of Tube?">
          <template v-slot:title>
            {{ 'What are the main advantages of Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube is unique because (as far as we know) it's the only video hosting web application which combines three
              advantages:
            </p>

            <div>
              <h3 v-translate>An open code under a free/libre license</h3>

              <ul>
                <li v-translate>Tube is freely provided, no need to pay to install it on your server;</li>
                <li v-translate>We can look under the hood of Tube (its source code): it's auditable, transparent;</li>
                <li v-translate>It can be enhanced by everyone's contributions.</li>
              </ul>
            </div>

            <div>
              <h3 v-translate>A federation of interconnected hosting providers</h3>

              <ul>
                <li v-translate>It decentralizes video storage and decision-making power;</li>
                <li v-translate>We can display videos and accounts of other Tube websites;</li>
                <li v-translate>It's based on <a target="_blank" rel="noopener noreferrer" href="https://activitypub.rocks">ActivityPub</a> to connect with tools <a target="_blank" rel="noopener noreferrer" href="https://tube.cpy.re/videos/watch/da2b08d4-a242-4170-b32a-4ec8cbdca701">like Mastodon</a> for example.</li>
              </ul>
            </div>

            <h3 v-translate>Peer-to-peer broadcasting</h3>

            <ul>
              <li v-translate>It reduces server bandwidth overload if a video becomes viral;</li>
              <li v-translate>We become an actor of the video broadcasting;</li>
              <li v-translate>It's based on <a target="_blank" rel="noopener noreferrer" href="https://en.wikipedia.org/wiki/WebRTC">WebRTC</a>, a free and open-source project for web browsers.</li>
            </ul>

          </div>
        </accordion-element>

        <accordion-element id="Who is working on Tube?">
          <template v-slot:title>
            {{ 'Who is working on Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube is developed by <a target="_blank" rel="noopener noreferrer" href="https://framasoft.org">Framasoft</a>, a French non-profit popular educational organization on digital issues.
            </p>

            <p v-translate>
              It employs a developer, <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz">Chocobozzz</a>, who works since 2018 on Tube projects:
            </p>

            <ul>
              <li v-translate>Development of new features and software maintenance;</li>
              <li v-translate>Thoughts on the architecture, the design and the future of Tube;</li>
              <li v-translate>New stable releases preparation and communication (blog posts, changelog, etc.);</li>
              <li v-translate>
                <a target="_blank" rel="noopener noreferrer" href="https://joinpeertube.org">Official Tube website</a> development and maintenance;
              </li>
              <li v-translate>
                <a target="_blank" rel="noopener noreferrer" href="https://packages.joinpeertube.org/api/v1/ping">Tube plugins index</a> development and maintenance;
              </li>
              <li v-translate>
                <a target="_blank" rel="noopener noreferrer" href="https://instances.joinpeertube.org">Tube instances index</a> development and maintenance;
              </li>
              <li v-translate>
                <a target="_blank" rel="noopener noreferrer" href="https://search.joinpeertube.org/">Tube videos/channels search index</a> development and maintenance;
              </li>
              <li v-translate>
                <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top">Tube documentation website</a> development and maintenance;
              </li>
              <li v-translate>Tube support and discussions on bugtrackers, forum, IRC or by email;</li>
              <li v-translate>Pull requests/merge requests reviews, merges, and support of features added by external contributions (bug fixes, documentation, refactor, etc.);</li>
              <li v-translate>Maintenance of several Tube instances (tube.cpy.re, peertube2.cpy.re, etc.).</li>
            </ul>

            <p v-translate>
              Moreover, Chocobozzz works on internal Framasoft's projects (<a target="_blank" rel="noopener noreferrer" href="https://framapad.org">Framapad</a>, <a target="_blank" rel="noopener noreferrer" href="https://framatalk.org">Framatalk</a>, etc.).
              In other words, the management of Tube projects is operated by only one employee, who has other responsabilities in addition to his workload.
            </p>

            <p v-translate>
              Other Framasoft's volunteers and salaried members also contribute to the Tube project on various aspects (strategy, communication, development, community animation).
              Nevertheless, Tube is not developed by a startup with a team of 50 people, and a workflow full of hyper-formatted processes.
            </p>

            <p v-translate>
              Framasoft will therefore takes the time to improve the software progressively, at its own pace and with its atypical methods which, so far, have proven to be rather effective.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Is Tube a neutral software?">
          <template v-slot:title>
            {{ 'Is Tube a neutral software?' | translate }}
          </template>

          <div>
            <p v-translate>
              No it is not.
            </p>

            <p v-translate>
              Tube is a software created, developed and maintained by <a target="_blank" rel="noopener noreferrer" href="https://framasoft.org">Framasoft</a>, a French non-profit popular educational organization on digital issues.
              Framasoft has a French culture, respects French laws and is a political actor (« who participates in the life of the city-state ») of civil society.
            </p>

            <p v-translate>
              Tube is part of several actions assembled by Framasoft in the <a target="_blank" rel="noopener noreferrer" href="https://contributopia.org">Contributopia roadmap</a>.
              These actions aim to propose alternative digital tools to those coming from surveillance capitalism, so that people who do not recognize themselves in such a system can create spaces of freedom.
            </p>

            <p v-translate>
              Consequently, Tube development and/or management of websites associated with the Tube project (<a target="_blank" rel="noopener noreferrer" href="https://joinpeertube.org">joinpeertube.org</a>, <a target="_blank" rel="noopener noreferrer" href="https://instances.joinpeertube.org">instances.joinpeertube.org</a>, <a target="_blank" rel="noopener noreferrer" href="https://search.joinpeertube.org/">search.joinpeertube.org</a>, etc.) may not please everyone (in particular because of different ideas, ideologies of cultures).
            </p>

            <p v-translate>
              We respect that, and remind that all software of the Tube project is free software and can be installed with their own domain name.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Is Tube's purpose to replace YouTube?">
          <template v-slot:title>
            {{ 'Is Tube\'s purpose to replace YouTube?' | translate }}
          </template>

          <div>
            <p v-translate>We can answer with certainty: no!</p>

            <p v-translate>The ambition remains to be <strong>a free and decentralized alternative</strong>: the goal of an alternative is
              not to replace, but to propose something else, with different values, in parallel to what already exists.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Is Tube a registered trademark?">
          <template v-slot:title>
            {{ 'Is "Tube" a registered trademark?' | translate }}
          </template>

          <div>
            <p v-translate>Yes, Framasoft holds the "Tube" trademark in several countries for preventive purposes.</p>

            <p v-translate>
              Framasoft affirms here its intention to not use intellectual property for predatory purposes or to constrain
              legitimate uses of the software.
              However, Framasoft reserves the right to use trademark law, especially in case of damage to the image of the Tube
              project, edited by Framasoft.
            </p>

            <p v-translate>
              We recommend you to create your own digital identity, without using "Tube" for your project
              (software name, website domain...) to not confuse people about who is behind the website/project.
            </p>

            <p v-translate>
              For more information, please refer to our <a target="_blank" rel="noopener noreferrer" href="https://framasoft.org/en/trademarks">Trademark Code of Conduct</a>.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="I don't like the name Tube">
          <template v-slot:title>
            {{ 'I don\'t like the name "Tube"' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube is just the name of the software. You can install it on your server, and choose the name you want.
              For example, Framasoft's Tube instance is named
              <a target="_blank" rel="noopener noreferrer" href="https://framatube.org/">Framatube</a>.
            </p>
          </div>
        </accordion-element>
      </div>

      <div class="block" role="tablist">
        <h2 v-translate>For those who want to watch videos</h2>

        <accordion-element id="What are the Tube features for viewers?">
          <template v-slot:title>
            {{ 'What are the Tube features for viewers?' | translate }}
          </template>

          <div>
            <ul class="multi">
              <li>
                <strong v-translate>Become a part of the broadcasting of the videos you are watching</strong>

                <ul>
                  <li v-translate>Help the Tube website to share videos and lives using P2P</li>
                  <li v-translate>Easily disable P2P in your account if logged-in or in your anonymous settings</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Customize the interface</strong>

                <ul>
                  <li v-translate>Update your name, avatar or description in <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-setup-account?id=update-your-profile">your profile</a></li>
                  <li v-translate>Set your appropriate NSFW policy (display, blur or hide NSFW videos)</li>
                  <li v-translate>Choose a Tube theme to change the look and feel of the interface</li>
                  <li v-translate>Automatically filter videos depending on their language</li>
                  <li v-translate>Choose your autoplay policy</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Manage your library</strong>

                <ul>
                  <li v-translate>Use the built-in <em>Watch Later</em> playlist and add easily videos in it</li>
                  <li v-translate>Organize videos in public or private <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-library?id=video-history">playlists</a></li>
                  <li v-translate>Automatically resume videos from the last time you watched them</li>
                  <li v-translate>Check your watch history in a dedicated page</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Subscribe to channels</strong>

                <ul>
                  <li v-translate>Subscribe to local or remote channels to be notified when they publish videos</li>
                  <li v-translate>Display videos of your subscriptions in a dedicated page</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Share and interact with videos and playlists</strong>

                <ul>
                  <li v-translate><a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-watch-video">Share</a> the video or the playlist URL/embed with attributes (automatically start/stop at, loop enabled, muted...)</li>
                  <li v-translate>Download a video using a dedicated modal</li>
                  <li v-translate>Comment, reply and rate videos using Tube or an ActivityPub compatible software (Mastodon, Pleroma...)</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Search videos or channels</strong>

                <ul>
                  <li v-translate><a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-search">Search</a> local or remote videos and channels</li>
                  <li v-translate>Use many advanced filters (tags, category, licence...)</li>
                  <li v-translate>Use the video URL or channel handle/URL to fetch remote content</li>
                </ul>
              </li>
            </ul>
          </div>
        </accordion-element>

        <accordion-element id="Why do you let bad people use or upload bad videos on Tube?">
          <template v-slot:title>
            {{ 'Why do you let bad people use or upload bad videos on Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube is not a service, Tube is free software.
              This means that anyone can download and install Tube to host videos on a specific domain name (this is called "hosting a Tube instance/website").
              Framasoft has no control over who downloads Tube, and who installs or uses it.
              Nor does Microsoft or Adobe have any control over who uses Word or Photoshop and for what purposes.
            </p>

            <p v-translate>
              Framasoft only hosts and promotes one Tube instance: <a href="https://framatube.org" target="_blank">https://framatube.org</a>
            </p>

            <p v-translate>
              Also, Framasoft hosts and promotes only one Tube search index: <a href="https://sepiasearch.org" target="_blank">https://sepiasearch.org</a>
            </p>
          </div>
        </accordion-element>

        <accordion-element id="There are many bad/porn videos on Tube!">
          <template v-slot:title>
            {{ 'There are many bad/porn videos on Tube!' | translate }}
          </template>

          <div>
            <p v-translate>
              This is not true. In September 2021, the 900 instances listed on our
              <a target="_blank" rel="noopener noreferrer" href="https://instances.joinpeertube.org/instances">public index</a>
              host ~1% videos tagged as NSFW (i. e. the content is sensitive, which could be something else than pornography).
            </p>

            <p v-translate>
              Administrators have full control of the content they want to display, and how they want to display it.
              In the default configuration, Tube hides NSFW videos.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="How do I report Tube content?">
          <template v-slot:title>
            {{ 'How do I report Tube content?' | translate }}
          </template>

          <div>
            <p v-translate>
              See the dedicated section in the Tube documentation: <a href="https://tube.docs.dingshunyu.top/use-report" target="_blank">https://tube.docs.dingshunyu.top/use-report</a>
            </p>
          </div>
        </accordion-element>

        <accordion-element id="How does Tube count views?">
          <template v-slot:title>
            {{ 'How does Tube count views?' | translate }}
          </template>

          <div>
            <p v-translate>
              Your web browser sends a view to the server after 30 seconds of playback.
              If a video is less than 30 seconds in length, a view is sent after 75% of the video duration.
              After giving a view, that IP address cannot add another view in the next hour.
              Views are buffered, so don't panic if the view counter stays the same after you watched a video.
            </p>
          </div>
        </accordion-element>
      </div>

      <div class="block" role="tablist">
        <h2 v-translate>For those who want to upload videos</h2>

        <accordion-element id="What are the Tube features for content creators?">
          <template v-slot:title>
            {{ 'What are the Tube features for content creators?' | translate }}
          </template>

          <div>
            <ul class="multi">
              <li>
                <strong v-translate>Find the Tube website that suits you best</strong>

                <ul>
                  <li v-translate><a target="_blank" rel="noopener noreferrer" href="/instances">Join a Tube community</a> depending on its topic, terms of services or code of conduct</li>
                  <li v-translate>Talk to administrators and moderators that are real people, not robots</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Upload and import videos</strong>

                <ul>
                  <li v-translate>Upload or import videos from the web (Youtube, Dailymotion...) or from a torrent file</li>
                  <li v-translate>Set metadata: category, licence, language, tags, description, thumbnail...</li>
                  <li v-translate>Choose <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-create-upload-video?id=video-confidentiality-options-what-do-they-mean">video privacy</a>: public, unlisted, internal or private</li>
                  <li v-translate>Add <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-create-upload-video?id=captions">subtitles</a></li>
                  <li v-translate>Easily disable comments or download</li>
                  <li v-translate>Upload an audio file and an image and let Tube take care of creating the video!</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Live streaming</strong>

                <ul>
                  <li v-translate><a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-create-upload-video?id=publish-a-live-in-tube-gt-v3">Publish live videos</a> in Tube using your favorite RTMP compatible software (OBS, Restream, ffmpeg...)</li>
                  <li v-translate>Create permanent lives to stream multiple times inside the same live (URL doesn't change for the viewers)</li>
                  <li v-translate>Let Tube create a replay of your live</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Manage your channels</strong>

                <ul>
                  <li v-translate>Create <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-library?id=video-channels">multiple channels</a> inside your account to group your videos by topic</li>
                  <li v-translate>Brand your channel using a dedicated name, a banner and an avatar</li>
                  <li v-translate>Set a <em>Support</em> button for your channel to explain how to support your work</li>
                  <li v-translate>People can subscribe to your channel from a Tube website or from an ActivityPub compatible software (Mastodon, Pleroma...)</li>
                  <li v-translate>Create public <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/use-library?id=playlist">playlists</a> assigned to your channel to organize your content or remote videos</li>
                </ul>
              </li>
            </ul>
          </div>
        </accordion-element>

        <accordion-element id="If it's free, can we upload illegal stuff on Tube?">
          <template v-slot:title>
            {{ 'If it\'s free, can we upload illegal stuff on Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              Being free doesn't mean being above the law! Each Tube hosting provider can decide on its own general conditions of use,
              abiding by their local laws.
            </p>

            <p v-translate>
              For example, in France, discriminatory content
              <a target="_blank" rel="noopener noreferrer" href="https://fr.wikipedia.org/wiki/Lois_contre_le_racisme_et_les_discours_de_haine">is prohibited</a>
              and may be reported to authorities. Tube allows users to report problematic videos, and administrator must then
              apply its moderation in accordance with its terms and conditions and the law.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="What is Tube's policy about content monetization?">
          <template v-slot:title>
            {{ 'What is Tube\'s policy about content monetization?' | translate }}
          </template>

          <div>
            <p v-translate>There are none at the moment: Tube is a tool that we wanted neutral in terms of monetization.</p>

            <p v-translate>
              For now, the solution proposed the <strong>support</strong> button under the video. This button
              displays a frame in which people who upload videos can display text, images, and links freely
              (link to Patreon, Tipeee, Paypal, Liberapay...).
            </p>

            <p v-translate>
              We did not go any further, as we refuse to tie our code to a particular content funding method,
              that might not fit all communities and deter others.
              It's the reason why we encourage developers to use the
              <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/contribute-plugins">Tube plugin API</a>
              to create their own monetization system.
            </p>
          </div>
        </accordion-element>
      </div>

      <div class="block" role="tablist">
        <h2 v-translate>For Tube administrators</h2>

        <accordion-element id="What are the Tube features for administrators?">
          <template v-slot:title>
            {{ 'What are the Tube features for administrators?' | translate }}
          </template>

          <div>
            <ul class="multi">
              <li>
                <strong v-translate>Free and open-source software</strong>

                <ul>
                  <li v-translate>Install Tube on your server and create your "homemade YouTube"</li>
                  <li v-translate>Official <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/install-docker">Docker support</a></li>
                  <li v-translate>Documented <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/api-rest-reference.html">REST API</a> and <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/maintain-tools">CLI tools</a></li>
                </ul>
              </li>

              <li>
                <strong v-translate>Federation with ActivityPub</strong>

                <ul>
                  <li v-translate>Enable or disable federation for your instance</li>
                  <li v-translate>Display videos/accounts of other instances on yours</li>
                  <li v-translate><a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-following-instances?id=automatically-follow-other-instances">Automatically follow</a> instances from a public index</li>
                </ul>
              </li>

              <li>
                <strong v-translate>P2P with WebRTC</strong>

                <ul>
                  <li v-translate>Reduce your server bandwidth using P2P on VOD and live videos</li>
                  <li v-translate>Enable or disable easily P2P for your instance</li>
                  <li v-translate>Help (or get help) other instances using <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-following-instances?id=instances-redundancy">Tube redundancy</a></li>
                </ul>
              </li>

              <li>
                <strong v-translate>Video transcoding</strong>

                <ul>
                  <li v-translate>Optimize videos and generate <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-configuration?id=vod-transcoding">multiple resolutions</a> using ffmpeg</li>
                  <li v-translate>Enable additional video extension (.mkv, .mov, .avi...) or audio uploads</li>
                  <li v-translate>Generate HLS playlists or raw MP4 files</li>
                  <li v-translate>Customize ffmpeg transcoding profile using Tube plugins</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Live streaming transcoding</strong>

                <ul>
                  <li v-translate>Enable <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-configuration?id=live-streaming">live streaming</a> for your instance</li>
                  <li v-translate>Add ability for your users to publish a replay of live videos</li>
                  <li v-translate>Set simultaneous live streaming limits (per user/instance)</li>
                  <li v-translate>Enable live streaming transcoding in multiple resolutions</li>
                  <li v-translate>Customize ffmpeg live transcoding profile using Tube plugins</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Video imports</strong>

                <ul>
                  <li v-translate>Enable video import using youtube-dl (Youtube, Dailymotion...)</li>
                  <li v-translate>Enable video import using webtorrent (torrent file or magnet URI)</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Moderation tools</strong>

                <ul>
                  <li v-translate>Enable signups and set a max users and upload limits</li>
                  <li v-translate>Assign <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-moderation">moderator/admin</a> roles</li>
                  <li v-translate>Manage abuse reports on videos, accounts or comments using a dedicated dashboard</li>
                  <li v-translate>Automatically block videos from untrusted users awaiting a review by moderators</li>
                  <li v-translate>Block specific videos and specify a reason</li>
                  <li v-translate>Ban local users, mute accounts or instances</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Highly configurable</strong>

                <ul>
                  <li v-translate>Easily set instance metadata: name, description, main categories...</li>
                  <li v-translate>Search and install Tube <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-customize-instance">plugins or themes</a></li>
                  <li v-translate>Use external auth (LDAP, OpenID Connect...) using auth plugins</li>
                  <li v-translate>Set the default NSFW policy (hide, blur or display these videos)</li>
                  <li v-translate>Choose your trending algorithm</li>
                  <li v-translate>Broadcast a message to users using a banner</li>
                </ul>
              </li>
            </ul>
          </div>
        </accordion-element>

        <accordion-element id="How do I install Tube?">
          <template v-slot:title>
            {{ 'How do I install Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              The <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/install-any-os">installation guide is here</a>
              (only in English).
            </p>

            <p v-translate>
              If you need help, check the <a target="_blank" rel="noopener noreferrer" href="https://joinpeertube.org/help">help page</a>.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Should I have a big server to run Tube?">
          <template v-slot:title>
            {{ 'Should I have a big server to run Tube?' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube should run happily on a virtual machine with 2 threads/vCPUs, at least 1 Gb of RAM and enough storage for videos.
              In terms of bandwidth, a lot will depend on which Tube instances you federate with and what your relation with
              them is (more about that below).
            </p>

            <p v-translate>
              As a real life example, the Tube demonstration server https://tube.cpy.re runs on 2 vCores and 2GB of RAM.
              Average consumption is:
            </p>

            <ul>
              <li v-translate>CPU: nginx ~ 2%, tube ~ 10%, postgres ~ 1%, redis ~ 1%</li>
              <li v-translate>RAM: nginx ~ 1MB, tube ~ 150MB, postgres ~ 30MB, redis ~ 20MB</li>
              <li v-translate>Network: ~200GB sent per month (https://framatube.org: ~1.5TB sent per month)</li>
            </ul>

            <strong v-translate>CPU</strong>

            <p v-translate>
              Except for video transcoding, a Tube instance is not CPU bound. Neither Nginx, Tube itself, PostgreSQL nor
              Redis require a lot of computing power. If it were only for those, one could easily get by with just one thread/vCPU.
            </p>

            <p v-translate>
              You will hugely benefit from at least a second thread though, because of transcoding. Transcoding is very cpu intensive.
              It serves two purposes on a Tube instance: it ensures all videos can be played optimally in the web interface, and it
              generates different resolutions for the same video. Tube support for offloading transcoding to other machines is
              <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube/issues/947">being discussed</a>,
              but not yet implemented.
            </p>

            <strong v-translate>RAM</strong>

            <p v-translate>
              1/2 GB of RAM should be plenty for a basic Tube instance, which usually takes at most 150 MB in RAM.
              The only reason you might want more would be if you colocate your Redis or PostgreSQL services on a non-SSD system.
            </p>

            <strong v-translate>Storage</strong>
            <p v-translate>
              There are two important angles to storage: disk space usage and sustained read speed.
              To make a rough estimate of your disk space usage requirements, you want to know the answer to three questions:
            </p>

            <ul>
              <li v-translate>What is the total size of the videos you wish to stream?</li>
              <li v-translate>
                Do you want to enable transcoding? If so, do you want to provide multiple resolutions per video? Try this out with a
                few videos and you will get an idea of how much extra space is required per video and estimate a multiplication factor for
                future space allocation.
              </li>
              <li v-translate>
                Which sharing mechanisms do you want to enable? Just WebTorrent, or also HLS with p2p? If you want both,
                this will double your storage needs.
              </li>
            </ul>

            <p v-translate>
              In terms of read speed, you want to make sure that you can saturate your network uplink serving Tube videos.
              This should not be a problem with SSD disks, whereas traditional HDD should be accounted for: typical sustained read rates
              for a well tuned system with a 7200rpm hard disk should hover around 120 MB/s or 960 Mbit/s. The latter should be enough
              for a typical 1 Gbit/s network uplink.
            </p>

            <strong v-translate>Network</strong>
            <p v-translate>
              A rough estimate of a traditional server's video streaming network capacity is usually quite straightforward.
              You simply divide your server's available bandwidth by the average bandwidth per stream, and you have an upper bound.
            </p>

            <p v-translate>
              Take a server for example with a 1 Gbit/s uplink for example pushing out 1080p60 streams at 5 Mbit/s per stream.
              That means the absolute theoretical upper capacity bound is 200 simultaneous viewers if your server's disk i/o can keep up.
              Expect a bit less in practice.
            </p>

            <p v-translate>
              But what if you need to serve more users? That's where Tube's federation feature shines. If other Tube instances
              following yours, chances are they have decided to mirror part of your instance! The feature is called "server redundancy"
              and caches your most popular videos to help serve additional viewers. While viewers themselves contribute a little additional
              bandwidth while watching the video in their browsers (mostly during surges), mirroring servers have a much greater uplink
              and will help your instance with sustained higher concurrent streaming.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Does Tube support horizontal scaling?">
          <template v-slot:title>
            {{ 'Does Tube support horizontal scaling?' | translate }}
          </template>

          <div>
            <p v-translate>
              No it doesn't. You can't deploy multiple Tube nodes behind a load balancer.
            </p>

            <p v-translate>
              As far as we know, there are 3 limitations to handle a large amount of users in Tube:
            </p>

            <ul>
              <li v-translate>Bandwidth: can be mitigated using <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-following-instances?id=instances-redundancy">Tube redundancy system</a> and cache servers that serve video static files in front of your Tube instance</li>
              <li v-translate>Storage: can be mitigated using <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-remote-storage">S3/Object storage</a></li>
              <li v-translate>Video transcoding: we may implement <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube/issues/947">transcoding by remote workers</a> in the future</li>
            </ul>

            <p v-translate>
              If you notice performance problems or a bottleneck at your scale, don't hesitate to <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube/issues/new/choose">create an issue</a> to discuss it.
            </p>
          </div>

        </accordion-element>

        <accordion-element id="What is the Tube release policy?">
          <template v-slot:title>
            {{ 'What is the Tube release policy?' | translate }}
          </template>

          <div>
            <p v-translate>
              Framasoft tries to make around 4 major releases per year.
              Releasing a new Tube version takes a lot of our time, so we cannot change this release cycle.
            </p>

            <p v-translate>
              There is no LTS version: only the latest stable version of Tube is supported by Framasoft.
            </p>

            <p v-translate>
              If you want to test the Tube develop branch,
              we provide <a target="_blank" rel="noopener noreferrer" href="https://builds.joinpeertube.org/nightly/">nightly builds</a>.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Does Tube ensure federation compatibility with previous versions?">
          <template v-slot:title>
            {{ 'Does Tube ensure federation compatibility with previous versions?' | translate }}
          </template>

          <div>
            <p v-translate>
              We <strong>try</strong> to keep compatibility with the latest minor version (2.3.1 with 2.2 for example).
              Unfortunately we don't have enough resources to keep compatibility with other versions.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Tube does not contain all the tools I need to manage my instance">
          <template v-slot:title>
            {{ 'Tube does not contain all the tools I need to manage my instance' | translate }}
          </template>

          <div>
            <blockquote v-translate>
              "It's outrageous and unconscionable: you're releasing a Tube version that doesn't contain the necessary tools
              to effectively manage videos claimed by rights holders, or to effectively manage the issue of online harassment in comments,
              or to effectively manage monetization through advertising, or to (insert here your request to Tube). It will never work!
              What do you intend to do about it?"
            </blockquote>

            <p v-translate>
              You're right. Tube is not the perfect tool, far from it. And we never promised this software would include all
              the features corresponding to every use cases.
            </p>

            <p v-translate>
              Remember that Tube has only <a href="#who-is-working-on-tube">one full time developer</a> and a small handful
              of very involved volunteers. It is not a product developed by a start-up with a full time team and significant financial
              support.
            </p>

            <p v-translate>
              So if you feel that Tube does not currently meet your needs, it's simple: don't use it right
              now. We remind you that we don't make money developing Tube, and if we obviously hope for its success,
              Framasoft does not depend on it to continue its activities.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Does Tube offer moderation tools?">
          <template v-slot:title>
            {{ 'Does Tube offer moderation tools?' | translate }}
          </template>

          <div>
            <p v-translate>
              <a href="#what-are-the-tube-features-for-administrators">Yes it does!</a>
              Since the first stable release of Tube in October 2018, <strong>every release</strong> added or improved moderation
              features:
            </p>

            <ul>
              <li v-translate><strong>Tube 1.1</strong> added bulk actions in the admin users table and instance/account muting;</li>
              <li v-translate><strong>Tube 1.2</strong> added the ability to unfederate a video on blacklist; the notification system
                now shows reports, and new users;
              </li>
              <li v-translate><strong>Tube 1.3</strong> added the possibility for admins to automatically quarantine new videos for
                new/untrusted users until a moderator reviews it. The instance's followers management was also reworked: UI is improved,
                follows can be denied;
              </li>
              <li v-translate><strong>Tube 1.4</strong> added a plugin system, and made sure plugins can for instance automatically
                blacklist videos or reject comments based on any rule (similar to Pleroma MRF);
              </li>
              <li v-translate><strong>Tube 2.0</strong> setup now asks the admin to answer questions regarding their moderation policy
                and dedication to it. Make a feature to automatically follow a public index of Tube instances part of core Tube,
                instead of leaving admins to script it themselves. This allows instance administrators to automatically follow instances of
                a "follow list" of their choice; a list which is self-hostable, so that communities can grow at their own pace;
              </li>
              <li v-translate><strong>Tube 2.1</strong> added an <em>internal</em> privacy mode to videos (such videos won't be shared
                outside of the instance), and added quick access to moderation tools below the comments and hooks to create registration
                plugins (geoblocking or captchas for example); some third-party plugins already demonstrate this ability. This release also
                put emphasis on describing moderation features within the interface: warnings for features that might increase moderation
                work like autofollow, and descriptions to the action dropdowns.
              </li>
              <li v-translate><strong>Tube 2.2</strong> greatly improved video abuses management
                (search, abuses display, actions on the video or account etc), added moderation hooks and helpers in the plugins API and
                Framasoft developed an experimental <em>Auto mute</em> plugin based on public lists.
              </li>
              <li v-translate><strong>Tube 2.3</strong> improved the report modal and added predefined reasons selection.
                This version made it possible to delete all the comments of an account and Framasoft developed an experimental
                <em>Auto block videos</em> plugin based on public lists.
              </li>
              <li v-translate><strong>Tube 2.4</strong> added ability to report accounts and comments, a messaging system between the
                local reporter of an abuse and moderators, and an abuse management view for users.
              </li>
              <li v-translate><strong>Tube 3.0</strong> added a dedicated view to manage comments of the instance (list, filter, remove
                in bulk etc).
              </li>
              <li>...</li>
            </ul>

            <p v-translate render-html="true">
              Tube developers are committed to develop and improve moderation tools to make instances always easier to manage.
              We welcome you to take part in discussions to
              <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube/issues">help ongoing efforts</a>
              in that direction or
              <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube/issues/new">suggest new ones</a>!
            </p>
          </div>
        </accordion-element>
      </div>

      <div class="block" role="tablist">
        <h2 v-translate>Technical</h2>

        <accordion-element id="What are the Tube features for developers?">
          <template v-slot:title>
            {{ 'What are the Tube features for developers?' | translate }}
          </template>

          <div>
            <ul class="multi">
              <li>
                <strong v-translate>Free and open-source software</strong>

                <ul>
                  <li v-translate>View, share and update <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube">Tube code</a></li>
                  <li v-translate>Join a community of admins, video makers and viewers</li>
                </ul>
              </li>

              <li>
                <strong v-translate>Tube REST API</strong>

                <ul>
                  <li v-translate>Test Tube REST API with the <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/api-rest-getting-started">getting started guide</a></li>
                  <li v-translate>Check the <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/api-rest-reference.html">REST API documentation</a></li>
                  <li v-translate>Get help on <a target="_blank" rel="noopener noreferrer" href="https://framacolibri.org/c/tube">Tube forum</a></li>
                </ul>
              </li>

              <li>
                <strong v-translate>Player embed API</strong>

                <ul>
                  <li v-translate>Use the Tube player embed on your website</li>
                  <li v-translate>Control the Tube player embed using the <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/api-embed-player">embed API</a></li>
                  <li v-translate>Get help on <a target="_blank" rel="noopener noreferrer" href="https://framacolibri.org/c/tube">Tube forum</a></li>
                </ul>
              </li>

              <li>
                <strong v-translate>Plugins & Themes</strong>

                <ul>
                  <li v-translate>Develop plugins and themes using the <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/contribute-plugins">getting started guide</a></li>
                  <li v-translate>Check the complete <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/api-plugins">plugin/theme documentation</a></li>
                  <li v-translate>Get help on <a target="_blank" rel="noopener noreferrer" href="https://framacolibri.org/c/tube">Tube forum</a> or ask new plugin/theme API in <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube">Tube issues</a></li>
                </ul>
              </li>
            </ul>
          </div>
        </accordion-element>

        <accordion-element id="Why does Tube use ActivityPub and WebRTC? Why not IPFS / DTube / Steemit?">
          <template v-slot:title>
            {{ 'Why does Tube use ActivityPub and WebRTC? Why not IPFS / DTube / Steemit?' | translate }}
          </template>

          <div>
            <p v-translate>
              Tube uses ActivityPub because this federation protocol is recommended by the W3C and is also used by other projects like
              the social network <a target="_blank" rel="noopener noreferrer" href="https://joinmastodon.org/">Mastodon</a>.
            </p>

            <p v-translate>
              IPFS is a great technology, but it still seems too young for streaming large files.
              The Tube P2P system based on well established protocols like HTTP and WebRTC, and the
              <a target="_blank" rel="noopener noreferrer" href="https://tube.docs.dingshunyu.top/admin-following-instances?id=instances-redundancy">redundancy system</a>
              are more easy for us to use and to maintain.
            </p>

            <p v-translate>
              DTube does not seem open source, because publishing only compiled code hinders freedom of modification.
            </p>

            <p v-translate>
              Tube does not impose any remuneration model. This is the choice we have made, and others (like DTube) have made
              other choices. So it's up to you to see what suits you.
            </p>
          </div>
        </accordion-element>

        <accordion-element id="Why hosting the code on GitHub and Framagit?">
          <template v-slot:title>
            {{ 'Why hosting the code on GitHub and Framagit?' | translate }}
          </template>

          <div>
            <p v-translate>Historical reason.</p>
          </div>
        </accordion-element>

        <accordion-element id="I have found a security vulnerability in Tube. Where and how should I report it?">
          <template v-slot:title>
            {{ 'I have found a security vulnerability in Tube. Where and how should I report it?' | translate }}
          </template>

          <div>
            <p v-translate>
              We have a policy for contributions related to security. Please refer to
              <a target="_blank" rel="noopener noreferrer" href="https://github.com/Chocobozzz/Tube/security">our security policy and advisories</a>.
            </p>
          </div>
        </accordion-element>
      </div>

    </div>
  </main>
</template>

<style lang="scss">
  @import '../scss/_variables.scss';

  #faq {
    header a {
      color: #000;
    }

    .block h2 {
      font-size: 22px;
      margin-top: 30px;
      margin-bottom: 15px;
      width: fit-content;
      border-bottom: 2px solid $orange;
    }

    h3 {
      font-size: 16px;
      font-family: 'PT Sans', sans-serif;
    }

    .card {
      margin: 5px 0;
    }

    .blocks {
      margin: 60px auto;
    }

    blockquote {
      padding: 10px 20px;
      margin: 0 0 20px;
      font-size: 17.5px;
      border-left: 5px solid #eee;
    }

    ul.multi > li {
      margin-bottom: 20px;
    }
  }
</style>

<script>
  import AccordionElement from '../components/AccordionElement'

  export default {
    components: {
      AccordionElement
    },

    metaInfo: function () {
      return {
        title: this.$gettext('FAQ')
      }
    }
  }
</script>
