<template>
  <v-layout mt-4 ml-4 mr-4>
    <v-flex>
      <v-card outlined>
        <v-data-table
          :headers="headers"
          :items="countries"
          sort-by="name"
          class="elevation-1"
        >
          <template v-slot:top>
            <v-toolbar flat>
              <v-toolbar-title>Countries</v-toolbar-title>
              <v-divider class="mx-4" inset vertical></v-divider>
              <v-spacer></v-spacer>
              <v-dialog v-model="dialog" max-width="500px">
                <!-- <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="primary"
                    dark
                    class="mb-2"
                    v-bind="attrs"
                    v-on="on"
                  >
                    New Item
                  </v-btn>
                </template> -->
                <v-card>
                  <!-- <v-card-title> -->
                  <!-- <span class="headline">{{ formTitle }}</span> -->
                  <!-- </v-card-title> -->

                  <v-card-text>
                    <v-container>
                      <v-row>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.name"
                            label="Country name"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.continent"
                            label="Continent"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.region"
                            label="Region"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.surfaceArea"
                            label="Surface area"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.independenceYear"
                            label="Independence year"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.population"
                            label="Population"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.lifeExpectancy"
                            label="Life expectancy"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.gnp"
                            label="GNP"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.governmentForm"
                            label="Government form"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <v-text-field
                            v-model="editedItem.headOfState"
                            label="Head of state"
                          ></v-text-field>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="close">
                      Cancel
                    </v-btn>
                    <v-btn color="blue darken-1" text @click="save">
                      Save
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
              <v-dialog v-model="dialogDelete" max-width="500px">
                <v-card>
                  <v-card-title class="headline"
                    >Are you sure you want to delete this item?</v-card-title
                  >
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="closeDelete"
                      >Cancel</v-btn
                    >
                    <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                      >OK</v-btn
                    >
                    <v-spacer></v-spacer>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-toolbar>
          </template>
          <template v-slot:item.actions="{ item }">
            <v-icon small class="mr-2" @click="editItem(item)">
              mdi-pencil
            </v-icon>
            <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
          </template>
          <template v-slot:no-data>
            <v-btn color="primary" @click="initialize"> Reset </v-btn>
          </template>
        </v-data-table>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
import axios from "axios";

const instance = axios.create({ baseURL: "http://localhost:9000" });

export default {
  data: () => ({
    dialog: false,
    dialogDelete: false,
    headers: [
      {
        text: "Code",
        align: "start",
        sortable: false,
        value: "code",
      },
      { text: "Name", value: "name" },
      { text: "Continent", value: "continent" },
      { text: "Region", value: "region" },
      { text: "Surface area", value: "surfaceArea" },
      { text: "Independence Year", value: "independenceYear" },
      { text: "Population", value: "population" },
      { text: "Life Expectancy", value: "lifeExpectancy" },
      { text: "GNP", value: "gnp" },
      { text: "Government Form", value: "governmentForm" },
      { text: "Head of state", value: "headOfState" },
      { text: "Actions", value: "actions", sortable: false },
    ],
    countries: [],
    editedIndex: -1,
    editedItem: {
      name: "",
      continent: "",
      region: "",
      surfaceArea: 0,
      independenceYear: 0,
      population: 0,
      lifeExpectancy: 0,
      gnp: 0,
      governmentForm: "",
      headOfState: "",
    },
    defaultItem: {
      name: "",
      continent: "",
      region: "",
      surfaceArea: 0,
      independenceYear: 0,
      population: 0,
      lifeExpectancy: 0,
      gnp: 0,
      governmentForm: "",
      headOfState: "",
    },
  }),

  computed: {
    //     formTitle() {
    //       return this.editedIndex === -1 ? "New Item" : "Edit Item";
    //     },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  created() {
    this.initialize();
  },

  // mounted() {
    // instance.get("/get-all-countries").then((response) => {
    //   this.cities = response.data;
    // });
  // },

  methods: {
    initialize() {
      instance.get("/get-all-countries").then((response) => {
        this.countries = response.data;
      });
    },

    editItem(item) {
      this.editedIndex = this.countries.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.countries.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.countries.splice(this.editedIndex, 1);
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.countries[this.editedIndex], this.editedItem);
      } else {
        this.countries.push(this.editedItem);
      }
      this.close();
    },

    updateCountry() {
      const formData = JSON.stringify({
        code: this.code,
        name: this.name,
        continent: this.continent,
        region: this.region,
        surfaceArea: this.surfaceArea,
        independenceYear: this.independenceYear,
        population: this.population,
        lifeExpectancy: this.lifeExpectancy,
        gnp: this.gnp,
        governmentForm: this.governmentForm,
        headOfState: this.headOfState,
      });
      console.log(formData);

      instance
        .put("/update-country", formData)
        .then((res) => {
          console.log(res);
          const data = res.data;
          const countries = [];
          for (let key in data) {
            countries.push(data[key]);
          }
        })
        .catch((err) => console.log(err));
    },

    deleteCountry(countryId) {
      instance.delete("/delete-country" / countryId).then((res) => {
        console.log(res);
      });
    },
  },
};
</script>