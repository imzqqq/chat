<template>
  <footer>
    <div class="container">
      <span v-html="footer" v-once></span> ·
      <a target="_blank" href="https://framagit.org/framasoft/tube/instances-tube">
        Hack this website!
      </a>
    </div>
  </footer>
</template>

<script>
module.exports = {
  data() {
    return {
      footer: process.env.VUE_APP_FOOTER
    }
  }
}
</script>

<style scoped>
  footer {
    width: 100%;
    height: 30px;
    line-height: 30px;
    background-color: #f8f9fa;

    font-size: 0.7em;
    text-align: center;
  }
  
  p {
    color: lightgreen;
    text-align: center;
  }

  a {
    color: #f2690d;
  }
</style>
