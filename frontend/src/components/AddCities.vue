<template>
  <v-layout mt-4>
    <v-flex xs12 sm6 offset-sm3>
      <v-card outlined>
        <v-list-item three-line>
          <v-list-item-content>
            <v-list-item-title class="headline mb-1"
              >Add City</v-list-item-title
            >
            <v-list-item-subtitle
              >Add your city to the database by filling out the information
              below</v-list-item-subtitle
            >
          </v-list-item-content>
        </v-list-item>

        <v-snackbar v-model="snackbar" absolute top right text color="success">
          <span>Registration successful!</span>
        </v-snackbar>

        <form>
          <v-container grid-list-xl fluid>
            <v-layout wrap>
              <v-flex xs12>
                <v-text-field
                  v-model="city"
                  :error-messages="cityErrors"
                  :counter="10"
                  label="City name"
                  required
                  color="teal"
                  @input="$v.city.$touch()"
                  @blur="$v.city.$touch()"
                ></v-text-field>

                <v-select
                  v-model="select"
                  :items="items"
                  :error-messages="selectErrors"
                  label="District"
                  required
                  color="teal"
                  @change="$v.select.$touch()"
                  @blur="$v.select.$touch()"
                ></v-select>

                <v-btn
                  class="ma-0"
                  @click="submit"
                  :disabled="!formIsValid"
                  color="teal"
                  outlined="outlined"
                  >submit</v-btn
                >
                <v-btn
                  class="ma-2"
                  @click="clear"
                  color="grey darken-1"
                  outlined="outlined"
                  >clear</v-btn
                >
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
    city: { required, maxLength: maxLength(10) },
    items: { required },
    select: { required },
  },

  data: () => ({
    city: "",
    items: [
      "Drenthe",
      "Flevoland",
      "Friesland",
      "Gelderland",
      "Groningen",
      "Limburg",
      "Noord-Brabant",
      "Noord-Holland",
      "Overijssel",
      "Utrecht",
      "Zeeland",
      "Zuid-Holland",
    ],
    select: null,
    snackbar: false,
  }),

  computed: {
    formIsValid() {
      return (
        this.city &&
        this.items &&
        this.select
      )
    },

    cityErrors() {
      const errors = [];
      if (!this.$v.city.$dirty) return errors;
      !this.$v.city.maxLength &&
        errors.push("First name must be at most 10 characters long");
      !this.$v.city.required && errors.push("First name is required.");
      return errors;
    },
    selectErrors() {
      const errors = [];
      if (!this.$v.items.$dirty) return errors;
      !this.$v.items.required && errors.push("District is required");
      return errors;
    },
  },
  methods: {
    submit() {
      this.snackbar = true;
      this.$v.$touch();
      this.onSubmit();
    },
    clear() {
      this.$v.$reset();
      this.city = "";
      (this.select = null);
    },
    onSubmit() {
      const formData = JSON.stringify({
        city: this.city,
        district: this.select,
      });
      console.log(formData);

      let config = {
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Allow-Headers":
            "Authorization, Content-Type, X-Requested-With",
          "Content-Type": "application/json",
        },
      };

      axios
        .post(`/addpet`, formData, config)
        .then((res) => console.log(res))
        .catch((err) => console.log(err));
    },
  },
};
</script>