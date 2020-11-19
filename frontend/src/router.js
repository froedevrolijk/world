import Vue from 'vue'
import Router from 'vue-router'
import ShowCities from './components/ShowCities'
import AddCities from './components/AddCities'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/showcities',
            name: 'showcities',
            component: ShowCities
        },
        {
            path: '/addcities',
            name: 'addcities',
            component: AddCities
        }
    ]
})
