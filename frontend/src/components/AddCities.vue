<template>
  <v-layout mt-4>
    <v-flex xs12 sm6 offset-sm3>
      <v-card outlined>

          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="headline mb-1">Add Pet</v-list-item-title>
              <v-list-item-subtitle>Add your pet to the database by filling out the information below</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

        <v-snackbar v-model="snackbar" absolute top right color="success">
          <span>Registration successful!</span>
          <v-icon dark>mdi-check-circle</v-icon>
        </v-snackbar>

        <form>
          <v-container grid-list-xl fluid>
            <v-layout wrap>
              <v-flex xs12>

                <v-text-field
                  v-model="firstName"
                  :error-messages="firstNameErrors"
                  :counter="10"
                  label="First Name"
                  required
                  color="teal"
                  @input="$v.firstName.$touch()"
                  @blur="$v.firstName.$touch()"
                ></v-text-field>

                <v-text-field
                  v-model="lastName"
                  :error-messages="lastNameErrors"
                  :counter="10"
                  label="Last Name"
                  required
                  color="teal"
                  @input="$v.lastName.$touch()"
                  @blur="$v.lastName.$touch()"
                ></v-text-field>

                <v-select
                  v-model="select"
                  :items="items"
                  :error-messages="selectErrors"
                  label="Animal"
                  required
                  color="teal"
                  @change="$v.select.$touch()"
                  @blur="$v.select.$touch()"
                ></v-select>

                <v-textarea
                  v-model="bio"
                  counter
                  auto-grow
                  color="teal"
                  maxlength="256"
                  label="Tell us something about your animal"
                ></v-textarea>

                <v-checkbox
                  v-model="healthy"
                  :error-messages="healthyErrors"
                  label="This animal is healthy"
                  color="teal"
                  @change="$v.healthy.$touch()"
                  @blur="$v.healthy.$touch()"
                ></v-checkbox>

                <v-btn class="ma-0" @click="submit" :disabled="!formIsValid" color="teal" outlined="outlined">submit</v-btn>
                <v-btn class="ma-2" @click="clear" color="grey darken-1" outlined="outlined">clear</v-btn>

              </v-flex>
            </v-layout>
          </v-container>
        </form>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
import axios from "axios";
import { validationMixin } from "vuelidate";
import { required, maxLength } from "vuelidate/lib/validators";

export default {
  mixins: [validationMixin],

  validations: {
    firstName: { required, maxLength: maxLength(10) },
    lastName: { required, maxLength: maxLength(10) },
    select: { required },
    items: { required },
    healthy: {
      checked(val) {
        return val;
      }
    }
  },

  data: () => ({
    firstName: "",
    lastName: "",
    items: ["Dog", "Cat", "Rabbit", "Turtle", "Snake"],
    bio: "",
    select: null,
    healthy: false,
    snackbar: false
  }),

  computed: {
  formIsValid () {
    return (
      this.firstName &&
      this.lastName &&
      this.select
    )
  },

    firstNameErrors() {
      const errors = [];
      if (!this.$v.firstName.$dirty) return errors;
      !this.$v.firstName.maxLength &&
        errors.push("First name must be at most 10 characters long");
      !this.$v.firstName.required && errors.push("First name is required.");
      return errors;
    },
    lastNameErrors() {
      const errors = [];
      if (!this.$v.lastName.$dirty) return errors;
      !this.$v.lastName.maxLength &&
        errors.push("Last name must be at most 10 characters long");
      !this.$v.lastName.required && errors.push("Last name is required.");
      return errors;
    },
    selectErrors() {
      const errors = [];
      if (!this.$v.items.$dirty) return errors;
      !this.$v.items.required && errors.push("Animal is required");
      return errors;
    },
    healthyErrors() {
      const errors = [];
      if (!this.$v.healthy.$dirty) return errors;
      return errors;
    }
  },
  methods: {
    submit() {
      this.snackbar = true;
      this.$v.$touch();
      this.sayHi();
      this.onSubmit();
    },
    clear() {
      this.$v.$reset();
      this.firstName = "";
      this.lastName = "";
      (this.select = null), (this.bio = ""), (this.healthy = false);
    },
    sayHi() {
      console.log("Hi");
    },
    onSubmit() {
      const formData = JSON.stringify({
        firstName: this.firstName,
        lastName: this.lastName,
        animal: this.select,
        bio: this.bio,
        healthy: this.healthy
      });
      console.log(formData);

      let config = {
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Allow-Headers": "Authorization, Content-Type, X-Requested-With",
          "Content-Type": "application/json",
        }
      };

      axios
        .post(`/addpet`, formData, config)
        .then(res => console.log(res))
        .catch(err => console.log(err));
    }
  }
};
</script>