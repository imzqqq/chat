export default {
  data: function () {
    return {
      pluginSelectionsEN: [
        {
          title: 'glavliiit',
          subtitle: 'A plugin to improve moderation',
          preview: 'glavliiit.png',
          url: 'https://gitlab.com/grin/tube-plugin-glavliiit',
          description: 'Once installed, glavliiit allows administrators to block the registration on their instance of certain email by listing emails, domain names or users names to be blocked. This plugin also block videos containing certain words or phrases. Really handy!'
        },
        {
          title: 'upload-limits',
          subtitle: 'A plugin to alert about upload limits',
          preview: 'upload-limits.png',
          url: 'https://github.com/kimsible/tube-plugin-upload-limits',
          description: 'This plugin displays an instructions alert when user uploads contents that doesn\'t respect upload limits (file size, video bit rate, audio bit rate) previously defined by instance\'s administrators. The upload is then blocked.'
        },
        {
          title: 'video-annotation',
          subtitle: 'A plugin to set annotations on video contents',
          preview: 'video-annotation.png',
          url: 'https://framagit.org/framasoft/tube/official-plugins/-/tree/master/tube-plugin-video-annotation',
          description: 'This annotation plugin allows you to display information in the player at a given time of a video. To do so, you just have to go on the uploaded video information page, open the Plugin settings tab and then add your annotations and their time code. The placement of the annotations is in the player top right corner by default, but you can choose other locations.'
        },
        {
          title: 'chapters',
          subtitle: 'A  plugin to add chapters to a video',
          preview: 'chapters.png',
          url: 'https://github.com/samlich/tube-plugin-chapters',
          description: 'This plugin allows content creators to add a chaptering system to their videos. Thus, watchers don\'t need to watch all the video and can choose to watch only certain parts of it. It\'s very easy to add chapters : you just have to update the video, open the Plugin settings tab and then add your chapters names and their time code. An additional button then appears in the player.'
        }
      ]
    }
  }
}
