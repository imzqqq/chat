
const QUOTES = [
 {
  quote: 'TIL: how to install #owncast on a vps in 5 minutes and stream to it with OBS',
  url: 'https://sonomu.club/@luka/105610570654392787',
  name: '@luka@sonomu.club',
 },
 {
  quote: 'wow, i rented a linode server and managed to set up owncast in like 5 minutes. This is fun and very promising',
  url: 'https://twitter.com/stamp_gal/status/1349088683887104002',
  name: '@stamp_gal',
 },
 {
  quote: "Want to stream without worrying about the music you're going to play? Just run OwnCast and have an equivalent to Twitch.",
  url: 'https://twitter.com/strycore/status/1347825083503427585',
  name: '@strycore',
 },
 {
  quote: "This is exactly what I was looking for a couple of years ago. This might be a good way to break from Twitch.",
  url: 'https://twitter.com/veridical_22/status/1341916444909588481',
  name: '@veridical_22',
 },

 {
  quote: "I'm amazed, #owncast seems to perform as well as any proprietary platforms out there.",
  url: 'https://fosstodon.org/@lopeztel/105466043426592961',
  name: '@lopeztel',
 },
 {
  quote: "Finally",
  url: 'https://old.reddit.com/r/selfhosted/comments/kgo6ct/selfhosted_live_video_streaming_server_with/gggfudh/',
  name: 'User digitalEarthling',
 },
 {
  quote: "many thanks for your work, owncast is exactly what we were looking for !",
  url: 'https://owncast.rocket.chat/channel/general?msg=jTGXEkNTMcqyNcDTs',
  name: 'Juju',
 },

 {
  quote: "With new tools like Live, you can run a livestream for all the guests who can't make it to your event in person. And you can do so without giving up control of your content, or acceding to the whims of companies who might not have your best interest at heart.",
  url: 'https://steele.blue/indieweb-wedding-livestream/',
  name: 'Matt Steele',
 },
 {
  quote: "Bless the owncast people, for real. Getting it up is dummy easy in hindsight",
  url: 'https://gitlab.com/libremiami/libremiami/-/issues/42#note_503367630',
  name: 'Roberto Beltran',
 },
 {
  quote: "Ahh! I love it! I setup #owncast and it was super easy! Take that other streaming services!",
  url: 'https://vulpine.club/@latrans/105749660754537997',
  name: '@latrans',
 },
 {
  quote: "Live is the best open source project.  Well, at least in the top 10.",
  url: 'https://stream.kylebronsdon.com/',
  name: 'Kyle Bronsdon',
 },
 {
  quote: "Live arrived right on time! I'm using it for my remote physics classes.",
  url: 'https://mamot.fr/@lutzray/105925070357006477',
  name: '@lutzray',
 },
 {
  quote: "Seriously, #owncast is amazing!",
  url: 'https://twitter.com/glider_space/status/1374856958650556417',
  name: '@glider_space',
 },
 {
  quote: "Very professional.  I hope to see this for every city council.",
  url: 'https://uelfte.club/@jannik/105985943638208608',
  name: 'City council meeting using Live',
 },
 {
  quote: "We shouldn’t need Amazon’s permission to goof off in a battle royale in front of our digital friends. Live offers a promising alternative glimpse into a more democratic, live streaming future that’s ripe for seizing.",
  url: 'https://www.pcmag.com/reviews/owncast',
  name: 'PC Mag Review of Live',
 },
 {
  quote: "For true democratic freedom, go with Live, a service that lets you stream whatever you want to whoever you want without corporate oversight.",
  url: 'https://www.pcmag.com/picks/best-video-game-live-streaming-services',
  name: 'PC Mag The Best Streaming Services for 2021',
 },
 {
  quote: "Went from hearing about Live to having it up and running in a frighteningly short amount of time.",
  url: 'https://hexdsl.co.uk/log/20210507-owncast.html',
  name: 'HexDSL',
 },
 {
  quote: "The projects aim is simple. Provide a self-hosted alternative to Twitch. Sounds like one of those things that gets complicated fast doesn’t it? Well, you know what… they went and did it!",
  url: 'https://hexdsl.co.uk/log/20210507-owncast.html',
  name: 'HexDSL',
 }

];

(function(){
  function createQuoteItem(divId) {
    const container = document.getElementById(divId);
    if (!container || !QUOTES.length) {
      return;
    }
    const index = Math.floor(Math.random() * Math.floor(QUOTES.length));
    const { quote, url, name } = QUOTES.splice(index, 1)[0];
    container.className = 'quotebox ' + divId;
    const content = '<p class="comment">' + quote + '</p><p class="commentor"><a href="'+ url +'" target="_blank" rel="noopener noreferrer">- '+ name +'</a></p>';
    container.innerHTML = content;
  }


  createQuoteItem('quote1');
  createQuoteItem('quote2');
}());