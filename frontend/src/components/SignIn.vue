<template>
  <v-container>
    <v-layout row>
      <v-flex xs12 sm6 offset-sm3>
        <v-card outlined>
          
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="headline mb-1">Sign In</v-list-item-title>
              <v-list-item-subtitle>Use your email and password to log in to your personal workspace</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-card-text>
            <v-container>
              <form @submit.prevent="onSignin">
                <v-layout row>
                  <v-flex xs12>
                    <v-text-field
                      v-model.trim="email"
                      :error-messages="emailErrors"
                      label="E-mail"
                      color="teal"
                      @blur="$v.email.$touch()"
                    ></v-text-field>
                  </v-flex>
                </v-layout>

                <v-layout row>
                  <v-flex xs12>
                    <v-text-field
                      v-model.trim="password"
                      :error-messages="passwordErrors"
                      label="Password"
                      type="password"
                      color="teal"
                      @blur="$v.password.$touch()"
                    ></v-text-field>
                  </v-flex>
                </v-layout>

                <v-layout row>
                  <v-flex xs12>
                    <v-btn
                      outlined
                      type="submit"
                      :disabled="!formIsValid"
                      class="mt-2"
                      color="teal"
                    >Sign in</v-btn>
                  </v-flex>
                </v-layout>
              </form>
            </v-container>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from "axios";
import { validationMixin } from "vuelidate";
import { required, email } from "vuelidate/lib/validators";

export default {
  mixins: [validationMixin],

  validations: {
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
    password: { required }
  },

  data: () => ({
    email: "",
    password: ""
  }),
  methods: {
    onSignin() {
      console.log("Login Succeeded");
      const formData = {
        email: this.email,
        password: this.password
      };
      // console.log(formData)
      this.$store.dispatch("login", {
        email: formData.email,
        password: formData.password
      });
    }
  },
  computed: {
    formIsValid() {
      return this.email && this.password;
    },

    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.required && errors.push("E-mail is required");
      this.$v.email.emailExists &&
        errors.push("No user with this email address is known");
      !this.$v.email.email && errors.push("Must be valid e-mail");
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.password.$dirty) return errors;
      !this.$v.password.required && errors.push("Password is required");
      return errors;
    }
  }
};
</script>