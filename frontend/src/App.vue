<template>
  <v-app>
    <app-header></app-header>
    <router-view></router-view>
    <app-footer></app-footer>
  </v-app>
</template>

<script>
import Header from './components/Header'
import Footer from './components/Footer'

export default {
  name: "App",
  components: {
    'app-header': Header,
    'app-footer': Footer
  },
  data() {
    return {
      //
    };
  },
};
</script>