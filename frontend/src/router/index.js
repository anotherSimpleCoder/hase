import { createRouter, createWebHistory } from 'vue-router'
import AuthService from '@/services/AuthService/AuthService.js'

import AppointmentView from '@/views/AppointmentView.vue'
import UserView from '@/views/UserView.vue'
import HomeView from '@/views/HomeView.vue'
import RegisterView from '@/views/RegisterView.vue'
import LoginView from '@/views/LoginView.vue'
import UserAppointmentView from '@/views/UserAppointmentView.vue'
import CalenderView from '@/views/CalenderView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/users',
      name: 'User',
      component: UserView,
      beforeEnter: (to, from, next) => {
        if (AuthService.isLoggedIn()) {
          next()
        } else {
          next({
            name: 'Login',
          })
        }
      },
    },
    {
      path: '/appointments',
      name: 'Appointments',
      component: AppointmentView,
      beforeEnter: (to, from, next) => {
        if (AuthService.isLoggedIn()) {
          next()
        } else {
          next({
            path: '/login',
          })
        }
      },
    },
    {
      path: '/my-appointments',
      name: 'My Appointments',
      component: UserAppointmentView,
      beforeEnter: (to, from, next) => {
        if (AuthService.isLoggedIn()) {
          next()
        } else {
          next({
            name: 'Login',
          })
        }
      },
    },
    {
      path: '/register',
      name: 'Register',
      component: RegisterView,
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
    },
    {
      path: '/calender',
      name: 'Calender',
      component: CalenderView,
    },
  ],
})

export default router
