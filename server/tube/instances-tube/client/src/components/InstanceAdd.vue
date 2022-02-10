<template>
  <div>
    <div class="container flex">
      <div class="row justify-content-center align-items-center">
        <div class="col-12 col-sm-10 col-md-7">
          <div v-if="err" class="alert alert-danger">
            {{ err }}
          </div>

          <div v-if="successForHost" class="alert alert-success">
            <router-link :to="'/instances?search=' + successForHost">{{ successForHost }}</router-link> has been successfully added.
          </div>
        </div>
      </div>

      <div class="row justify-content-center align-items-center">

        <div class="col-12 col-sm-10 col-md-7">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">Add your instance</h5>
              <form @submit="onSubmit">

                <div class="form-group mt-4">
                  <div class="input-group">
                    <div class="input-group-prepend">
                      <span class="input-group-text">https://</span>
                    </div>
                    <input v-model="host" class="form-control" type="text" name="host" id="host" placeholder="domain.tld" />
                  </div>
                </div>

                <div class="text-right">
                  <input :disabled="loading === true" class="btn btn-primary" type="submit" value="Add instance" />
                </div>
              </form>
            </div>
          </div>
        </div>

      </div>
    </div>

  </div>
</template>

<script lang="ts">
  import { Component, Vue } from 'vue-property-decorator'
  import { addInstance } from '../shared/instance-http'
  import { httpErrorToString } from '../shared/utils'

  @Component
  export default class InstanceAdd extends Vue {
    host = ''
    err = ''
    successForHost = ''
    loading = false

    onSubmit (event: Event) {
      event.preventDefault()

      this.loading = true
      this.err = ''
      this.successForHost = ''

      addInstance(this.host)
        .then(() => {
          this.loading = false
          this.successForHost = this.host
          this.host = ''
        })
        .catch(err => {
          this.loading = false
          this.err = httpErrorToString(err)
        })
    }
  }
</script>
