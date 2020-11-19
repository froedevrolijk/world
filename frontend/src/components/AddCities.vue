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
              >Add your City to the database by filling out the information
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
                  v-model="name"
                  :error-messages="nameErrors"
                  :counter="20"
                  label="City"
                  required
                  color="teal"
                  @input="$v.name.$touch()"
                  @blur="$v.name.$touch()"
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

                <v-text-field
                  v-model="population"
                  :error-messages="populationErrors"
                  label="Population"
                  required
                  color="teal"
                  type="number"
                  @change="$v.population.$touch()"
                  @blur="$v.population.$touch()"
                ></v-text-field>

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
    name: { required, maxLength: maxLength(20) },
    items: { required },
    select: { required },
    population: { required },
  },

  data: () => ({
    name: "",
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
    population: null,
    snackbar: false,
  }),

  computed: {
    formIsValid() {
      return this.name && this.items && this.select && this.population;
    },

    nameErrors() {
      const errors = [];
      if (!this.$v.name.$dirty) return errors;
      !this.$v.name.maxLength &&
        errors.push("City name must be at most 20 characters long");
      !this.$v.name.required && errors.push("City name is required.");
      return errors;
    },
    selectErrors() {
      const errors = [];
      if (!this.$v.items.$dirty) return errors;
      !this.$v.items.required && errors.push("District is required");
      return errors;
    },
    populationErrors() {
      const errors = [];
      if (!this.$v.population.$dirty) return errors;
      !this.$v.population.required && errors.push("Population is required");
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
      this.name = "";
      this.select = null;
      this.population = null;
    },
    onSubmit() {
      const formData = JSON.stringify({
        name: this.name,
        district: this.select,
        countryCode: "NLD",
        population: this.population,
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