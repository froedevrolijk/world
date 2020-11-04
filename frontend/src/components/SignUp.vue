<template>
  <v-container>
    <v-layout row>
      <v-flex xs12 sm6 offset-sm3>
        <v-card outlined>

          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="headline mb-1">Sign Up</v-list-item-title>
              <v-list-item-subtitle>Create a profile and sign up for this project</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-card-text>
              <form>
                <v-text-field
                  v-model.trim="name"
                  :error-messages="nameErrors"
                  :counter="10"
                  label="Username"
                  color="teal"
                  required
                  @blur="$v.name.$touch()"
                ></v-text-field>

                <v-text-field
                  v-model.trim="email"
                  :error-messages="emailErrors"
                  label="E-mail"
                  color="teal"
                  required
                  @blur="$v.email.$touch()"
                ></v-text-field>

                <v-text-field
                  v-model.trim="password"
                  :error-messages="passwordErrors"
                  label="Password"
                  type="password"
                  color="teal"
                  required
                  @blur="$v.password.$touch()"
                ></v-text-field>

                <v-text-field
                  v-model.trim="confirmPassword"
                  :error-messages="confirmPasswordErrors"
                  label="Confirm Password"
                  type="password"
                  color="teal"
                  required
                  @blur="$v.confirmPassword.$touch()"
                ></v-text-field>

                <!-- <v-select
                  v-model="select"
                  :items="countries"
                  :error-messages="selectErrors"
                  label="Country"
                  color="teal"
                  required
                  @blur="$v.select.$touch()"
                ></v-select> -->

              </form>
          </v-card-text>

          <v-form ref="form" v-model="form" class="pa-3 pt-4">
            <v-checkbox
              v-model="agreement"
              :rules="[rules.required]"
              color="teal"
              @change="$v.select.$touch()"
            >
              <template slot="label">
                I agree to the&nbsp;
                <a href="#" @click.stop.prevent="dialog = true">Terms of Service</a>
                &nbsp;and&nbsp;
                <a
                  href="#"
                  @click.stop.prevent="dialog = true"
                >Privacy Policy</a>*
              </template>
            </v-checkbox>

            <v-btn @click="onSignup" class="ma-0" :disabled="!formIsValid" color="teal" outlined="outlined">submit</v-btn>
            <!-- color="accent" -->
            <v-btn @click="clear" class="ma-2" color="grey darken-1" outlined="outlined">clear</v-btn>
          </v-form>
          <v-divider></v-divider>

          <v-dialog v-model="dialog" absolute max-width="400" persistent>
            <v-card>
              <v-card-title class="headline grey lighten-3">Legal</v-card-title>
              <v-card-text>Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                 sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                  Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions>
                <v-btn text @click="agreement = false, dialog = false">No</v-btn>
                <v-spacer></v-spacer>
                <v-btn
                  class="white--text"
                  color="teal"
                  @click="agreement = true, dialog = false"
                >Yes</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import {
  required,
  minLength,
  maxLength,
  email,
  sameAs
} from "vuelidate/lib/validators";
import { validationMixin } from "vuelidate";
import axios from "axios";

export default {
  mixins: [validationMixin],

  validations: {
    name: { required, maxLength: maxLength(10) },
    email: {
      required,
      email,
      emailExists: val => {
        if (val === "") return true;
        return axios
          .get('/users.json?orderBy="email"&equalTo="' + val + '"')
          .then(res => {
            return Object.keys(res.data).length === 0;
          });
      }
    },
    select: { required },
    // checkbox: {required},
    password: { required, minLength: minLength(6) },
    confirmPassword: { required, sameAsPassword: sameAs("password") }
  },

  data: () => ({
    name: "",
    email: "",
    select: null,
    password: "",
    confirmPassword: "",
    countries: ["Netherlands", "Germany", "USA", "India"],
    agreement: false,
    dialog: false,
    form: false,
    isLoading: false,
    rules: {
      required: v => !!v || "This field is required"
    }
    // checkbox: false
    // dialog: false
  }),

  computed: {
    formIsValid() {
      return (
        this.name &&
        this.email &&
        this.password &&
        this.confirmPassword &&
        this.form
      )
    },

    checkboxErrors() {
      const errors = [];
      if (!this.$v.checkbox.$dirty) return errors;
      !this.$v.checkbox.required && errors.push("You must agree to continue!");
      return errors;
    },
    selectErrors() {
      const errors = [];
      if (!this.$v.select.$dirty) return errors;
      !this.$v.select.required && errors.push("Country is required");
      return errors;
    },
    nameErrors() {
      const errors = [];
      if (!this.$v.name.$dirty) return errors;
      !this.$v.name.maxLength &&
        errors.push("Username must be at most 10 characters long");
      !this.$v.name.required && errors.push("Username is required.");
      return errors;
    },
    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.email && errors.push("Must be valid e-mail");
      !this.$v.email.required && errors.push("E-mail is required");
      // !this.$v.email.emailExists && errors.push("Email already exists");
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.password.$dirty) return errors;
      !this.$v.password.minLength &&
        errors.push("Password must be at least 6 characters long");
      !this.$v.password.required && errors.push("Password is required");
      return errors;
    },
    confirmPasswordErrors() {
      const errors = [];
      if (!this.$v.confirmPassword.$dirty) return errors;
      !this.$v.confirmPassword.sameAsPassword &&
        errors.push("Passwords must be identical");
      !this.$v.confirmPassword.required &&
        errors.push("Confirm password is required");
      return errors;
    }
  },
  watch: {
    // After succesful signup redirect user to the homepage by using a watcher
    // user (value) {
    //   if (value !== null && value !== undefined) {
    //     this.$router.push('/')
    //   }
    // }
  },
  methods: {
    onSignup() {
      this.$v.$touch();
      const formData = {
        name: this.name,
        email: this.email,
        password: this.confirmPassword,
        confirmPassword: this.confirmPassword,
        select: this.select,
        checkbox: this.checkbox,
        agreement: this.agreement
      };
      // console.log(formData)
      this.$store.dispatch("signup", formData);
    },
    clear() {
      this.$v.$reset();
      this.name = "";
      this.email = "";
      this.select = null;
      this.checkbox = false;
      this.password = "";
      this.confirmPassword = "";
      this.agreement = false;
    }
  }
};
</script>
