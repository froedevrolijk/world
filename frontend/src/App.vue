<template>
  <v-app>
    <app-header></app-header>
    <router-view></router-view>
    <app-footer></app-footer>
  </v-app>
</template>

<script>
import Toolbar from './components/Toolbar'
import Footer from './components/Footer'

export default {
  name: "App",
  components: {
    'app-header': Toolbar,
    'app-footer': Footer
  },
  data() {
    return {
      //
    };
  },
};
</script>