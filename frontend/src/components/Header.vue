<template>
  <div class="hide-overflow" style="position: relative">
    <v-toolbar class="mb-6" paddless flat color="teal lighten-3">
      <v-spacer></v-spacer>

      <v-toolbar-items class="pa-3">
        <v-btn to="addcities" class="ml-3" outlined color="blue darken-3"> Add Cities </v-btn>
        <v-btn to="showcities" class="ml-3" outlined color="blue darken-3"> Show cities </v-btn>
        <v-btn to="showcountries" class="ml-3" outlined color="blue darken-3"> Show countries </v-btn>
      </v-toolbar-items>
    </v-toolbar>
  </div>
</template>

<script>
export default {
  name: "Toolbar",
};
</script>