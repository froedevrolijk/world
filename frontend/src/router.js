import Vue from 'vue'
import Router from 'vue-router'
import ShowCities from './components/ShowCities'
import AddCities from './components/AddCities'
import SignIn from './components/SignIn'
import SignUp from './components/SignUp'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'showcities',
            component: ShowCities
        },
        {
            path: '/addcities',
            name: 'addcities',
            component: AddCities
        },
        {
            path: '/signin',
            name: 'signin',
            component: SignIn
        },
        {
            path: '/signup',
            name: 'signup',
            component: SignUp
          }
    ]
})
