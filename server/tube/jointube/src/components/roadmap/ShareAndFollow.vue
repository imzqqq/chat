<template>
  <ul class="list-inline share-follow d-flex justify-content-between">
    <li class="newsletter">
      <a rel="nofollow" class="btn"
         href="https://framalistes.org/sympa/info/tube-newsletter"
         :title="newsletterTitle"
      >
        <i class="fa fa-fw fa-lg fa-envelope" aria-hidden="true"></i>
        <span class="sr-only" v-translate>Newsletter</span>
      </a>
    </li>

    <li class="mastodon" v-if="mastodon !== ''">
      <a target="_blank" rel="noopener noreferrer nofollow" class="btn"
         :href="mastodon"
         :title="mastodonTitle"
      >
        <i class="fa fa-fw fa-lg fa-mastodon" aria-hidden="true"></i>
        <span class="sr-only">Mastodon</span>
      </a>
    </li>

    <li class="diaspora">
      <a target="_blank" rel="noopener noreferrer nofollow" class="btn"
         :href="diaspora.join('')"
         :title="diasporaTitle"
      >
        <i class="fa fa-fw fa-lg fa-diaspora" aria-hidden="true"></i>
        <span class="sr-only">Diaspora</span>
      </a>
    </li>

    <li class="twitter">
      <a target="_blank" rel="noopener noreferrer nofollow" class="btn"
         :href="twitter.join('')"
         :title="twitterTitle"
      >
        <i class="fa fa-fw fa-lg fa-twitter" aria-hidden="true"></i>
        <span class="sr-only">Twitter</span>
      </a>
    </li>

    <li class="facebook">
      <a target="_blank" rel="noopener noreferrer nofollow" class="btn"
         :href="facebook.join('')"
         :title="facebookTitle"
      >
        <i class="fa fa-fw fa-lg fa-facebook" aria-hidden="true"></i>
        <span class="sr-only">Facebook</span>
      </a>
    </li>
  </ul>
</template>

<style lang="scss">
  .share-follow {
    display: inline-flex;
    margin: 0 auto 30px;

    a {
      margin-right: 5px;
      padding: 1rem;
      border-radius: 0;
      font-size: 1.5rem;
    }

    li {
      a {
        color: #fff;
        border: 2px solid transparent;
        &:hover, &:focus {
          opacity: 0.8;
          color: #fff;
        }
      }
    }

    .newsletter a {
      background: transparent;
      color: #393C58 !important;
      border-color: #d9d9d9;
    }
    .mastodon a { background: #3088D4; }
    .diaspora a { background: #464646; }
    .twitter a { background: #6DABCE; }
    .facebook a { background: #405897; }
    .instagram a { background: linear-gradient(45deg, #f09433 0%,#e6683c 25%,#dc2743 50%,#cc2366 75%,#bc1888 100%); }
  }

  @media (max-width: 576px) {
    #roadmap .share-follow a {
      padding: 0.75rem;
      font-size: 1.25rem;
    }
  }
</style>

<script>
  export default {
    computed: {
      newsletterTitle () { return this.$gettext('Stay up-to-date with Tube') },

      mastodonTitle () { return this.$gettext('Share on Mastodon') },
      diasporaTitle () { return this.$gettext('Share on Diaspora') },
      twitterTitle () { return this.$gettext('Share on Twitter') },
      facebookTitle () { return this.$gettext('Share on Facebook') }
    },

    data () {
      const picture = `https://joinpeertube.org${this.buildImgUrl('card-opengraph.jpg')}`
      const link = 'https://joinpeertube.org'
      const text = this.$gettext('Join me and support Tube, a free software to take back control of your videos!')
      const title = this.$gettext('Let’s support Tube')

      return {
        mastodon: '', // Todo add pouet link
        diaspora: [
          'https://share.diasporafoundation.org/',
          `?title=${encodeURIComponent(text)}`,
          `&amp;url=${link}`
        ],
        twitter: [
          'https://twitter.com/intent/tweet',
          `?text=${encodeURIComponent(text)} ${link}`
        ],
        facebook: [
          'https://www.facebook.com/sharer/sharer.php',
          `?u=${encodeURIComponent(link)}`,
          `&amp;picture=${encodeURIComponent(picture)}`,
          `&amp;title=${encodeURIComponent(title)}`,
          `&amp;description=${encodeURIComponent(text)}`
        ]
      }
    }
  }
</script>
