<template>
  <v-layout mt-4 ml-4 mr-4>
    <v-flex>
      <!--  xs12 sm6 offset-sm3 -->
      <v-card outlined>
        <v-card-title>
          <b>Overview of Cities</b>
          <v-spacer></v-spacer>
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
            color="teal"
          ></v-text-field>
        </v-card-title>
        <v-data-table :headers="headers" :items="cities" :search="search">
          <template v-slot:items="props">
            <td class="text-xs-left">{{ props.item.id }}</td>
            <td class="text-xs-left">{{ props.item.name }}</td>
            <td class="text-xs-left">{{ props.item.countryCode }}</td>
            <td class="text-xs-left">{{ props.item.district }}</td>
            <td class="text-xs-left">{{ props.item.population }}</td>
          </template>
          <template v-slot:no-results>
            <v-alert :value="true" color="error" icon="warning"
              >Your search for "{{ search }}" found no results.</v-alert
            >
          </template>
        </v-data-table>
        <!-- 
        <div class="text-xs-center">
          <v-btn @click="getCities" color="teal">Get Cities</v-btn>
        </div> -->
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
import axios from "axios";

const instance = axios.create({ baseURL: "http://localhost:9000" });

export default {
  data() {
    return {
      search: "",
      headers: [
        { text: "ID", value: "id", sortable: false },
        { text: "Name", value: "name" },
        { text: "Country code", value: "countryCode" },
        { text: "District", value: "district" },
        { text: "Population", value: "population" },
      ],
      cities: [],
    };
  },
  mounted() {
    instance.get("/get-all-cities").then((response) => {
      this.cities = response.data;
    });
  },
  methods: {
    getCities() {
      axios
        .get("/cities")
        .then((res) => {
          console.log(res);
          const data = res.data;
          const cities = [];
          for (let key in data) {
            // can also use this instead of code below: cities.push(data[key])
            const user = data[key];
            user.id = key;
            cities.push(user);
          }
          //   console.log("cities", cities);
          //   this.firstName = cities[0].firstName;
          //   this.lastName = cities[0].lastName;
        })
        .catch((err) => console.log(err));
    },
  },
};
</script>
