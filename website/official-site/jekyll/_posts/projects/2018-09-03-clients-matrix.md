---
layout: default
categories: projects
---

<style>
#wrapper {
    max-width: 1280px;
}
table {
    width: 100%
}

table tr td {
    width: 6%
}

table tr td:nth-child(1) {
    width: 18% !important;
}

#document table:first-of-type tr td:nth-child(1) {
  white-space: &#10007;wrap;
}

table tr:nth-child(even) {
  background-color: #f5f5f5;
}

table:nth-of-type(2) tr:nth-child(5)  {
    font-size: small;
}

.green {
    color: #78A830;
    font-weight: bold;
     font-size: x-large;
}
.orange {
    color: #F0A800;
}
.red {
    color: #D84830;
}
small {
    font-size: small;
    font-weight: normal;
}
h2 {
    padding-top: 10px;
}
</style>
<!-- https://www.colourlovers.com/palette/65580/traffic_light <-  &#10003; pls -->
<script>
jQuery(document).ready(function () {
    jQuery("td").each(function( index ) {
        var text = jQuery( this ).text();
        if (text.startsWith("Yes") || text.startsWith("✓")) {
            jQuery(this).addClass("green");
        }
        if (text.match(/Build from source|WIP|Predefined|Images|Partial|Text only/))
        {
            jQuery(this).addClass("orange");
        }
        if (text === "No" || text.startsWith("✗")) {
            jQuery(this).addClass("red");
        }
    });
});
</script>

# Chat Clients Chat

To connect to the Chat federation, you will use a client. These are some of the most popular Chat clients available today, and more are available at  [try-matrix-now](try-matrix-now). To get started using Chat, pick a client and join [#matrix:matrix.org](https://to.chat.dingshunyu.top/#/#matrix:matrix.org)

## Platform Availability

||    Riot Web    |    Riot Android    |    Riot iOS    |    Nheko    |weechat-matrix|    Gomuks    |    Quaternion    |    Fractal    |    Seaglass    |    Spectral    |    uMatriks    | FluffyChat|
:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:
Linux|&#10003;<br /><small>Electron</small>| |          |     &#10003;|      &#10003;| &#10003;     |  &#10003;        |   &#10003;    |                |        &#10003;|                |   &#10003;|
Mac|&#10003;<br /><small>Electron</small>||             |     &#10003;|      &#10003;|      &#10003;|  &#10003;        |            WIP|        &#10003;|        &#10003;|
Windows|&#10003;<br /><small>Electron</small>||         |     &#10003;|      &#10003;|      &#10003;|          &#10003;|               |                |        &#10003;|
Android||                      &#10003;|                |             |              ||||
iOS|||                                          &#10003;|             |              ||||
Ubuntu Touch|           |              |                |             |              |              |                  |               |                |                |        &#10003;|   &#10003;|

## Details

||    Riot Web             |    Riot Android    |    Riot iOS    |    Nheko    |weechat-matrix|    Gomuks    |    Quaternion    |    Fractal    |    Seaglass    |    Spectral    |    uMatriks    | FluffyChat|
:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:
Repo|[riot-web](https://github.com/vector-im/riot-web/)|[riot-android](https://github.com/vector-im/riot-android/)|[riot-ios](https://github.com/vector-im/riot-ios/)|[nheko](https://github.com/Nheko-Reborn/nheko)|[weechat-matrix]|[gomuks](https://github.com/tulir/gomuks)|[Quaternion](https://github.com/QMatrixClient/Quaternion/)|[Fractal](https://gitlab.gnome.org/GNOME/fractal)|[Seaglass](https://github.com/neilalexander/seaglass)|[Spectral](https://gitlab.com/spectral-im/spectral)|[uMatriks](https://github.com/uMatriks/uMatriks)|[FluffyChat](https://github.com/ChristianPauly/fluffychat)|
Chat Room|<small>[#riot:<br />matrix.org](https://to.chat.dingshunyu.top/#/#riot:matrix.org)</small>|<small>[#riot-android:<br />matrix.org](https://to.chat.dingshunyu.top/#/#riot-android:matrix.org)</small>|<small>[#riot-ios:<br />matrix.org](https://to.chat.dingshunyu.top/#/#riot-ios:matrix.org)</small>|<small>[#nheko-reborn:<br />matrix.org](https://to.chat.dingshunyu.top/#/#nheko-reborn:matrix.org)</small>|<small>[#weechat-matrix:<br />termina.org.uk](https://to.chat.dingshunyu.top/#/#weechat-matrix:termina.org.uk)</small>|<small>[#gomuks:<br />maunium.net](https://to.chat.dingshunyu.top/#/#gomuks:maunium.net)</small>|<small>[#qmatrixclient:<br />matrix.org](https://to.chat.dingshunyu.top/#/#qmatrixclient:matrix.org)</small>|<small>[#fractal-gtk:<br />matrix.org](https://to.chat.dingshunyu.top/#/#fractal-gtk:matrix.org)</small>|<small>[#seaglass:<br />matrix.org](https://to.chat.dingshunyu.top/#/#seaglass:matrix.org)</small>|<small>[#spectral:<br />encom.eu.org](https://to.chat.dingshunyu.top/#/#spectral:encom.eu.org)</small>|<small>[#uMatriks:<br />matrix.org](https://to.chat.dingshunyu.top/#/#uMatriks:matrix.org)</small>|<small>[#fluffychat:<br />matrix.org](https://to.chat.dingshunyu.top/#/#fluffychat:matrix.org)</small>
Platform| Web (React)|          Android|             iOS|           Qt|Weechat<br />Terminal|Go<br />Terminal|              Qt|           GTK+|macOS<br />Cocoa|              Qt|Qt<br />Ubuntu Touch|Qt<br />Ubuntu Touch|
Language| JavaScript (React)|      Java|     Objective-C|          C++|C &amp; Python plugin|            Go|               C++|           Rust|           Swift|        QML, C++|             C++|QML
SDK | matrix-js-sdk, matrix-react-sdk| matrix-android-sdk|matrix-ios-sdk|      |  [matrix-nio]|gomatrix (fork)|libqmatrixclient|             |  matrix-ios-sdk|libqmatrixclient|libqmatrixclient||
Target Spec Version|r0.4.0|             r0.4.0|             r0.4.0|      r0.4.0|     r0.4.0|            r0.4.0|                r0.4.0|             r0.4.0|              r0.4.0|              r0.4.0|              r0.4.0|r0.4.0|

## Features

|                      |    Riot Web    |    Riot Android    |  Riot iOS   |     Nheko|weechat-matrix|    Gomuks| Quaternion|  Fractal|  Seaglass|  Spectral|  uMatriks| FluffyChat|
:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:
<br/>*Room Management*|
Room directory|                 &#10003;|            &#10003;|     &#10003;|  &#10007;|      &#10003;|  &#10007;|   &#10007;|   &#10003;|  &#10007;|  &#10007;|  &#10007;|   &#10003;|
Room tag showing|               &#10003;|          Predefined|   Predefined|  &#10003;|      &#10007;|  &#10003;|   &#10003;| Predefined|  &#10007;|Predefined|  &#10007;|   &#10003;|
Room tag editing|             Predefined|          Predefined|   Predefined|  &#10007;|      &#10007;|  &#10007;|   &#10003;| Predefined|  &#10007;|Predefined|  &#10007;|   &#10003;|
Search joined rooms|            &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10007;|  &#10003;|    Partial|   &#10003;|  &#10003;|  &#10003;|  &#10007;|    Partial|
<br/>*Room Features*|
Room user list|                 &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10003;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10003;|   &#10003;|
Display Room Description|       &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10003;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10003;|   &#10003;|
Edit Room Description|          &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10007;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10007;|   &#10003;|
Highlights |                    &#10003;|            &#10003;|     &#10003;|  &#10007;|      &#10003;|  &#10003;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10003;|   &#10003;|
Pushrules |                     &#10003;|            &#10003;|     &#10003;|  &#10007;|      &#10007;|  &#10003;|   &#10007;|   &#10007;|  &#10007;|  &#10007;|  &#10007;|   &#10003;|
Send read markers|              &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10003;|   &#10003;|   &#10003;|  &#10007;|  &#10003;|  &#10007;|   &#10003;|
Display read markers |          &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10007;|  &#10007;|   Only own|   &#10007;|  &#10007;|  Only own|  &#10007;|    Partial|
Sending Invites|                &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10007;|   &#10003;|   &#10003;|  &#10007;|  &#10003;|  &#10007;|   &#10003;|
Accepting Invites|              &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10007;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10007;|   &#10003;|
Typing Notification|            &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10003;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10007;|   &#10003;|
<br/>*Message Features*|
E2E|                            &#10003;|            &#10003;|     &#10003;| Text only|      &#10003;|  &#10007;|   &#10007;|   &#10007;|  &#10003;|  &#10007;|  &#10007;|   &#10007;|
Replies|                        &#10003;|            &#10003;|     &#10003;|  &#10007;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|  &#10007;|  &#10003;|  &#10007;|   &#10003;|
Attachment uploading|           &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10007;|   &#10003;|   &#10003;|  &#10007;|  &#10003;|  &#10007;|   &#10003;|
Attachment downloading|         &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|    Images|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10003;|   &#10003;|
Send stickers|                  &#10003;|            &#10003;|     &#10003;|  &#10007;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|  &#10007;|  &#10007;|  &#10007;|   &#10003;|
Send formatted messages (markdown)|&#10003;|         &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10003;|   &#10007;|   &#10003;|  &#10003;|  &#10003;|  &#10007;|   &#10007;|
Rich Text Editor for formatted messages| &#10003;|   &#10007;|     &#10007;|  &#10007;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|  &#10007;|  &#10007;|  &#10007;|   &#10007;|
Display formatted messages|     &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10003;|   &#10003;|   &#10007;|  &#10003;|  &#10003;|  &#10007;|   &#10007;|
Redacting |                     &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10003;|  &#10007;|   &#10003;|   &#10003;|  &#10003;|  &#10003;|  &#10007;|   &#10003;|
<br/>*Other Features*|
Multiple Chat Accounts|       &#10007;|            &#10007;|     &#10007;|  &#10007;|      &#10003;|  &#10007;|   &#10003;|   &#10007;|  &#10007;|  &#10003;|  &#10007;|   &#10007;|
New user registration|          &#10003;|            &#10003;|     &#10003;|  &#10003;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|  &#10007;|  &#10007;|  &#10007;|   &#10003;|
voip|                           &#10003;|            &#10003;|     &#10003;|  &#10007;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|  &#10007;|  &#10007;|  &#10007;|   &#10007;|

## Future-specification Features

|                      |    Riot Web    |    Riot Android    |    Riot iOS    |    Nheko|weechat-matrix|    Gomuks| Quaternion|    Fractal|    Seaglass| Spectral|  uMatriks| FluffyChat|
:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:
Communities Display|            &#10003;|            &#10003;|        &#10003;| &#10003;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|    &#10007;| &#10007;|  &#10007;|   &#10007;|
Communities Creation|           &#10003;|            &#10007;|        &#10007;| &#10007;|      &#10007;|  &#10007;|   &#10007;|   &#10007;|    &#10007;| &#10007;|  &#10007;|   &#10007;|

*These features are in common usage in the Chat ecosystem and could be added to a future version of the spec.*

[weechat-matrix]: https://github.com/poljar/weechat-matrix
[matrix-nio]: https://github.com/poljar/matrix-nio
