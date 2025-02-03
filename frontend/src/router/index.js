import { createRouter, createWebHistory } from 'vue-router'
import AppointmentView from '@/views/AppointmentView.vue'
import UserView from '@/views/UserView.vue'
import LoginView from '@/views/LoginView.vue'
import HomeView from '@/views/HomeView.vue'

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
    },
    {
      path: '/appointments',
      name: 'Appointments',
      component: AppointmentView,
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
    },
  ],
})

export default router
