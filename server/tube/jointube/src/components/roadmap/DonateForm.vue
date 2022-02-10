<template>

  <b-form
    id="form_don"
    @submit="payRedirection"
  >
    <b-row
      id="howmuch"
      align-v="center"
      align-h="center"
    >
      <b-col md="10">
        <b-input-group
          size="lg"
          prepend="€"
          class=""
        >
          <b-form-input
            id="other1"
            v-model.number="form.other1"
            :aria-label="amountLabel"
            min="1"
            max="100000"
            step="any"
            pattern="/^\d+([\.\,]\d{1,2})?$/"
            :placeholder="amountPlaceholder"
            type="number"
            @change="check('other1')"
          />
        </b-input-group>

        <!-- Email -->
        <b-input-group>
          <template v-slot:prepend>
            <b-input-group-text>
              <i
                class="fa fa-envelope-o fa-fw fa-lg"
                aria-hidden="true"
              ></i>
            </b-input-group-text>
          </template>
          <b-form-input
            id="email"
            v-model="form.email"
            :placeholder="emailPlaceholder"
            required
            :title="form.society
              ? emailTitleSociety
              : emailTitleElse"
            type="email"
            @focusout="check('email')"
          />
        </b-input-group>

        <!-- Nickname -->
        <b-input-group>
          <template v-slot:prepend>
            <b-input-group-text>
              <i v-if="form.state.anonymous"
                class="fa fa-user-secret fa-fw fa-lg"
                aria-hidden="true"
              ></i>
              <i v-else
                class="fa fa-user fa-fw fa-lg"
                aria-hidden="true"
              ></i>
            </b-input-group-text>
          </template>

          <b-form-input
            id="nickname"
            v-model="form.nickname"
            :placeholder="nicknamePlaceholder"
            :title="nicknameTitle"
            :readonly="form.state.anonymous"
            required
            @focusout="check('nickname')"
          />
        </b-input-group>

        <b-form-checkbox
          v-model="form.state.anonymous"
          name="anonymous"
          switch
          class="text-muted"
          @change="anonymize()"
        >
          <span v-translate>I want my donation to remain anonymous</span>
        </b-form-checkbox>

        <!-- Receipt ? -->
        <b-form-checkbox
          v-model="form.state.receipt"
          name="receipt"
          switch
          class="text-muted"
          @change="fillReceiptFields()"
        >
          <span v-translate>I would like to receive a tax receipt</span>
        </b-form-checkbox>
      </b-col>
    </b-row>

    <!-- Receipt form -->
    <div id="whoAreYou">
      <b-card
        v-show="form.state.receipt"
        no-body
        class="my-4 pt-2 pb-4 px-3 mx-3"
      >
        <p v-translate>Please complete this information so we can prepare your receipt</p>

        <!-- Individual / Society -->

        <b-form-group
          label-cols-sm="4"
          label-cols-lg="3"
          :label="representLabel"
          class="m-0 px-4"
        >
          <b-form-radio-group
            v-model="form.society"
            buttons
            button-variant="link text-uppercase text-dark"
            size="sm"
            @change="form.firstname = !form.society ? ' ' : ''"
          >
            <b-form-radio
              :value="false"
              class="text-decoration-none"
            >
              <i
                class="fa fa-id-card-o"
                aria-hidden="true"
              ></i>
              <span v-translate>an individual</span>
            </b-form-radio>
            <b-form-radio
              :value="true"
              class="text-decoration-none"
              :title="representCompany"
            >
              <i
                class="fa fa-institution"
                aria-hidden="true"
              ></i>
              <span v-translate>a company</span>
            </b-form-radio>
          </b-form-radio-group>
        </b-form-group>
        <div>
          <!-- Lastname / Firstname -->
          <b-col v-if="!form.society">
            <b-input-group>
              <template v-slot:prepend>
                <b-input-group-text>
                  <i
                    class="fa fa-id-card-o fa-fw"
                    aria-hidden="true"
                  ></i>
                </b-input-group-text>
              </template>
              <b-form-input
                id="lastname"
                v-model="form.lastname"
                :placeholder="lastnamePlaceholder"
                required
                :state="form.ok.lastname"
                :title="lastnameTitle"
                @focusout="check('lastname')"
              />
              <b-form-input
                id="firstname"
                v-model="form.firstname"
                :placeholder="firstnamePlaceholder"
                required
                :state="form.ok.firstname"
                :title="firstnameTitle"
                @focusout="check('firstname')"
              />
            </b-input-group>
          </b-col>

          <!-- Society -->
          <b-col v-else>
            <b-input-group>
              <template v-slot:prepend>
                <b-input-group-text>
                  <i
                    class="fa fa-institution fa-fw"
                    aria-hidden="true"
                  ></i>
                </b-input-group-text>
              </template>
              <b-form-input
                id="lastname"
                v-model="form.lastname"
                :placeholder="legalEntityPlaceholder"
                required
                :state="form.ok.lastname"
                :title="legalEntityTitle"
                @focusout="check('lastname')"
              />
            </b-input-group>
          </b-col>

          <!-- Address -->
          <b-col>
            <b-input-group>
              <template v-slot:prepend>
                <b-input-group-text>
                  <i
                    class="fa fa-map-marker fa-fw"
                    aria-hidden="true"
                  ></i>
                </b-input-group-text>
              </template>
              <b-form-input
                id="address1"
                v-model="form.address1"
                :placeholder="addressPlaceholder"
                required
                :state="form.ok.address1"
                :title="addressTitle"
                @focusout="check('address1')"
              />
            </b-input-group>
          </b-col>
          <b-col>
            <b-input-group>
              <template v-slot:prepend>
                <b-input-group-text>
                  <i
                    class="fa fa-inverse fa-fw"
                    aria-hidden="true"
                  ></i>
                </b-input-group-text>
              </template>
              <b-form-input
                id="address2"
                v-model="form.address2"
                :placeholder="address2Placeholder"
                :title="address2Title"
                @focusout="check('address2')"
              />
            </b-input-group>
          </b-col>

          <!-- Zip / City -->
          <b-col>
            <b-input-group>
              <template v-slot:prepend>
                <b-input-group-text>
                  <i
                    class="fa fa-inverse fa-fw"
                    aria-hidden="true"
                  ></i>
                </b-input-group-text>
              </template>
              <b-form-input
                id="zip"
                v-model="form.zip"
                :placeholder="postalCodePlaceholder"
                required
                :state="form.ok.zip"
                :title="postalCodeTitle"
                @focusout="check('zip')"
              />
              <b-form-input
                id="city"
                v-model="form.city"
                :placeholder="cityPlaceholder"
                required
                :state="form.ok.city"
                :title="cityTitle"
                @focusout="check('city')"
              />
            </b-input-group>
          </b-col>

          <!-- Country -->
          <b-col>
            <b-input-group>
              <template v-slot:prepend>
                <b-input-group-text>
                  <i
                    class="fa fa-inverse fa-fw"
                    aria-hidden="true"
                  ></i>
                </b-input-group-text>
              </template>
              <b-form-select
                id="country"
                v-model="form.country"
                required
                class="custom-select"
              >
                <option
                  v-for="(country, key) in countries"
                  :key="key"
                  :value="key"
                >
                  {{ country }}
                </option>
              </b-form-select>
            </b-input-group>
          </b-col>
        </div>

        <!-- French defisc -->
        <p
          v-if="/(FR|GP|GF|RE|MQ|YT|NC|PF|PM|WF)/.test(form.country)"
          class="text-muted px-4 mb-0 mt-2"
        >
          <small>
            <translate :translate-params="{ percent: calcDefisc().percent, amount: calcDefisc().amount, defisc: calcDefisc().defisc }">
              In France, thanks to the %{ percent } tax deduction, your donation of %{ amount } will cost you %{ defisc }.
            </translate>
          </small>
        </p>
      </b-card>
    </div>
    <!-- Payment mode -->
    <div id="payment">
      <b-form-radio-group
        v-model="form.pay_mode"
        buttons
        button-variant="light text-uppercase col-12 col-md-4 mx-4"
        class="row justify-content-center d-flex"
      >
        <b-form-radio value="cb">
          <i
            class="d-block mb-2 fa fa-lg fa-credit-card"
            aria-hidden="true"
          ></i>
          <span v-translate>Credit card</span>
        </b-form-radio>
        <b-form-radio value="pp">
          <i
            class="d-block mb-2 fa fa-lg fa-paypal"
            aria-hidden="true"
          ></i>
          <span>Paypal</span>
        </b-form-radio>
      </b-form-radio-group>
    </div>

    <!-- Donate -->
    <p class="text-center">
      <b-button
        id="btnVerif"
        class="btn btn-lg"
        type="submit">

        <span style="font-size: 22px;" v-translate>
          Give
        </span>

        <br />

        <span v-if="!isNaN(form.don)">{{ formatCurrency(form.don) + ' ' }}</span>

        <span v-translate>now</span>
      </b-button>
    </p>
  </b-form>
</template>

<style lang="scss">
  @import '../../scss/_variables.scss';

  .money img {
    display: block;
    margin: auto;
  }

  #support {
    .fa {
      color: #757575;
    }
  }

  #howmuch {
    margin-top: 20px;

    #other1 {
      height: 50px;
    }

    .active, :active {
      box-shadow: none;
    }

    .btn i { color: #757575; }

    .btn.active {
      span { border-bottom: 3px solid $primary; }
      i { color: $primary; }
    }

    .input-group {
      margin-bottom: 20px;

      .input-group-text,
      .form-control {
        border: 2px solid #eee;
        border-radius: 0;
      }

      .input-group-text {
        background: #fff;
      }

      .input-group-prepend {
        margin-right: -2px;

        .input-group-text {
          border-color: #fff;
        }
      }
    }

    .input-group-lg {
      .input-group-prepend .input-group-text {
        border-color: $orange;
        background: $orange;
        color: #fff;
      }

      .form-control {
        border-color: $orange;
      }
    }

    .email .input-group,
    .nickname .input-group {
      margin-bottom: 10px;
    }

    #nickname[readonly="readonly"] {
      color: transparent;
      text-shadow: 0 0 4px rgba(34, 34, 34, 0.5);
    }

    .custom-switch {
      padding-left: 50px; /* = .input-group-text */
      label {
        padding-left: 3px;
      }
    }
  }

  #whoAreYou {
    legend {
      line-height: 15px;
    }

    .btn-link {
      color: $secondary;
      text-decoration: none;
      margin-bottom: 10px;

      &:hover, &:focus {
        color: darken($secondary, 10%);
      }

      i {
        margin-right: 5px;
      }

      &.active {
        i { color: $primary; }
        i + span {
          border-bottom: 3px solid $primary;
        }
      }
    }

     .card {
      border-color:#dfd0bf;
      background:rgba(254,232,198,.4);
    }

    p.pull-left {
      line-height: 30px;
      margin: 0;
    }

    .btn-group {
      .active, :active {
        box-shadow: none;
      }
      i { color: #757575; }
    }

    .input-group-prepend .input-group-text {
      color: #757575;
      background: #fff;
      border-top: 0px;
      border-left: 0px;
      border-right: 0px;
      border-bottom-color: #fff;
      border-radius: 0;
    }

    input, select {
      border-top: 0px;
      border-left: 0px;
      border-right: 0px;
      border-radius: 0;
      box-shadow: none;
    }

    #address1 {
      border-bottom-style: dashed;
    }

    #email,
    #nickname,
    #country {
      border-bottom-color: #fff;
    }

    #nickname[readonly="readonly"] {
      border-bottom-color: #e9ecef;
    }

    #firstname, #city {
      border-left: 1px solid #ccc;
    }
  }

  #payment {
    label.btn {
      width: 100%;
      border-radius: 0;
      margin-bottom: 10px;
      background-color: #fff;
      border-bottom-width: 3px;
      i { color: #757575; }
    }

    .active, :active {
      box-shadow: none;
    }

    margin-top: 20px;

    .btn {
      border-color: #eee;
      color: #6c757d;
      &:hover, &:focus {
        border-color: #6c757d;
      }
      &.active {
        border-color: $primary;
        color: #222;
        i { color: $primary; }
      }
    }
  }

  #btnVerif {
    font-size: 24px;
    padding: 0.6em 2em;
    margin: 0.75em 0;
    color: #fff;
    background-color: $primary;
    border: 1px solid $primary;

    &:hover, &:focus {
      opacity: 0.9;
    }
  }

  .help-block li {
    font-size: 12px;
  }

  .clearfix.help-block {
    margin-left: -2em;
    margin-right: -2em;
    margin-bottom: -1em;
    margin-top: 0;
    padding: 1em 2em;
    background: #f5f5f5;
    border-top: 1px solid #eee;
  }
</style>

<script>
  import {
    BButton,
    BCard,
    BCol,
    BRow,
    BInputGroup,
    BInputGroupText,
    BFormSelect,
    BFormInput,
    BFormGroup,
    BFormRadioGroup,
    BFormRadio,
    BFormCheckbox,
    BForm
  } from 'bootstrap-vue'
  import { countries } from '../../data/countries'

  export default {
    components: {
      BButton,
      BCard,
      BCol,
      BRow,
      BInputGroup,
      BInputGroupText,
      BFormSelect,
      BFormInput,
      BFormGroup,
      BFormRadioGroup,
      BFormRadio,
      BFormCheckbox,
      BForm
    },

    computed: {
      amountLabel () { return this.$gettext('Amount') },
      amountPlaceholder () { return this.$gettext('Amount (e.g.: 42)') },

      emailPlaceholder () { return this.$gettext('E-mail') },
      emailTitleSociety () { return this.$gettext('e.g. contact@fsf.org') },
      emailTitleElse () { return this.$gettext('e.g. r.stallman@outlock.com') },

      nicknamePlaceholder () { return this.$gettext('Nickname') },
      nicknameTitle () { return this.$gettext('e.g. Lady AAL') },

      representLabel () { return this.$gettext('I represent') },

      representCompany () { return this.$gettext('Company, association, community...') },

      lastnamePlaceholder () { return this.$gettext('Last name') },
      lastnameTitle () { return this.$gettext('e.g. Lovelace') },

      firstnamePlaceholder () { return this.$gettext('First name') },
      firstnameTitle () { return this.$gettext('e.g. Ada') },

      legalEntityPlaceholder () { return this.$gettext('Legal entity') },
      legalEntityTitle () { return this.$gettext('e.g. Free Software Foundation') },

      addressPlaceholder () { return this.$gettext('Address') },
      addressTitle () { return this.$gettext('e.g. 12, Freedom Street') },

      address2Placeholder () { return this.$gettext('Address line 2') },
      address2Title () { return this.$gettext('e.g. Building VI') },

      postalCodePlaceholder () { return this.$gettext('Postal/Zip code') },
      postalCodeTitle () { return this.$gettext('e.g. 42100') },

      cityPlaceholder () { return this.$gettext('City') },
      cityTitle () { return this.$gettext('e.g. Saint-Etienne') }
    },

    data () {
      return {
        countries,
        form: {
          don: 10,
          monthly: false,
          other1: 10,
          society: false,
          nickname: '',
          lastname: '',
          firstname: '',
          email: '',
          address1: '',
          address2: '',
          zip: '',
          city: '',
          country: 'FR',
          pay_mode: 'cb',
          pay_send: '',
          ok: {
            nickname: null,
            lastname: null,
            firstname: null,
            email: null,
            address1: null,
            zip: null,
            city: null
          },
          state: {
            anonymous: false,
            receipt: false,
            newsletter: false
          }
        }
      }
    },
    methods: {
      check (input) {
        if (!this.form.state.receipt) {
          // Fill requiered fields for DB
          this.form.lastname = ' '
          this.form.firstname = ' '
          this.form.address1 = ' '
          this.form.city = ' '
          this.form.zip = ' '
        }
        const el = document.getElementById(input)
        switch (input) {
          case 'other1':
            if (!Number.isNaN(this.form.other1)) {
              this.form.other1 = Number.parseFloat(this.form.other1).toFixed(2)
              this.form.don = this.form.other1
            } else {
              this.form.don = 50
            }
            this.form.otherM = ''
            break
          case 'email':
            if (el.validity.valueMissing) {
              this.form.ok.email = false
              el.setCustomValidity(this.$gettext('This field must be filled in.'))
            } else if (el.validity.typeMismatch) {
              this.form.ok.email = false
              el.setCustomValidity(this.$gettext('You must enter a valid email address.'))
            } else {
              this.form.ok.email = true
              el.setCustomValidity('')
            }
            break
          default:
            if (el.validity.valueMissing) {
              this.form.ok[input] = false
              el.setCustomValidity(this.$gettext('This field must be filled in.'))
            } else {
              this.form.ok[input] = true
              el.setCustomValidity('')
            }
            break
        }
      },
      addFormField (form, key, value) {
        const hiddenField = document.createElement('input')
        Object.assign(hiddenField, {
          'type': 'hidden',
          'name': key,
          'value': value
        })
        form.appendChild(hiddenField)
      },
      payRedirection (e) {
        const form = document.createElement('form')
        const url = [
          'https://www.paypal.com/cgi-bin/webscr?'
        ]

        let lg = this.getShortLocale().toUpperCase()
        // Choose in available Monetico languages (fallback EN)
        lg = [ 'DE', 'EN', 'ES', 'FR', 'IT', 'JA', 'NL', 'PT', 'SV' ].includes(lg)
          ? lg
          : 'EN'
        const custom = [
          'campaign=peertube3',
          `nickname=${encodeURIComponent(this.form.nickname)}`,
          `anonymous=${+this.form.state.anonymous}`,
          `receipt=${+this.form.state.receipt}`,
          // `newsletter=${+this.form.state.newsletter}`,
          `lg=${lg}`
        ]

        switch (this.form.pay_mode) {
          // TPE Monetico / CM-CIC
          case 'cb':
            form.setAttribute('method', 'POST')
            form.setAttribute('action', 'https://soutenir.framasoft.org/monetico/before_paiement_2828136.php')

            this.addFormField(form, 'nom', this.form.lastname)
            this.addFormField(form, 'prenom', this.form.firstname)
            this.addFormField(form, 'mail', this.form.email)
            this.addFormField(form, 'adresse1', this.form.address1)
            this.addFormField(form, 'adresse2', this.form.address2)
            this.addFormField(form, 'ville', this.form.city)
            this.addFormField(form, 'cp', this.form.zip)
            this.addFormField(form, 'pays', this.countries[this.form.country])
            this.addFormField(form, 'montant', this.form.don)
            this.addFormField(form, 'texte_libre', encodeURIComponent(custom.join('&')))
            this.addFormField(form, 'lgue', lg)

            document.body.appendChild(form)
            form.submit()
            break
          // Paypal
          case 'pp':
            url.push(
              'cmd=_donations',
              `&amount=${this.form.don}`,
              `&item_name=${encodeURIComponent('Framasoft (don ponctuel)')}`,
              '&business=6HSVUPKRDAGC2', // Framasoft Paypal ID
              `&return=${encodeURIComponent('https://joinpeertube.org/thank-you')}`,
              '&currency_code=EUR',
              '&address_override=1', // Prefill Paypal’s form
              '&charset=utf-8',
              `&last_name=${encodeURIComponent(this.form.lastname)}`,
              `&first_name=${encodeURIComponent(this.form.firstname)}`,
              `&email=${encodeURIComponent(this.form.email)}`,
              `&address1=${encodeURIComponent(this.form.address1)}`,
              `&address2=${encodeURIComponent(this.form.address2)}`,
              `&zip=${encodeURIComponent(this.form.zip)}`,
              `&city=${encodeURIComponent(this.form.city)}`,
              `&country=${this.form.country}`,
              `&custom=${encodeURIComponent(custom.join('&'))}`
            )
            this.form.pay_send = url.join('')
            window.location.href = this.form.pay_send
            break

          // no-default
        }

        e.preventDefault()
      },
      anonymize () {
        if (this.form.state.anonymous) {
          this.form.nickname = ''
          this.form.ok.nickname = false
        } else {
          this.form.nickname = 'Anonymous'
          this.form.ok.nickname = true
        }
      },
      fillReceiptFields () {
        const fields = [ 'lastname', 'firstname', 'address1', 'zip', 'city' ]
        if (!this.form.state.receipt) {
          // Reinit fields
          fields.forEach((field) => {
            this.form[field] = this.form[field].replace(/^ $/, '')
            this.form.ok[field] = null
          })
          if (this.form.society) this.form.firstname = ' '
        } else {
          // Fill void fields with space
          fields.forEach((field) => {
            this.form[field] = this.form[field].replace(/^$/, ' ')
          })
        }
      },
      calcDefisc () {
        const defisc = this.form.society ? 0.60 : 0.66
        return {
          percent: new Intl.NumberFormat(this.getShortLocale(), { style: 'percent' }).format(defisc),
          amount: this.formatCurrency(this.form.don),
          defisc: this.formatCurrency(this.form.don - this.form.don * defisc)
        }
      }
    }
  }
</script>
