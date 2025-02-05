import { createRouter, createWebHistory } from 'vue-router'
import AppointmentView from '@/views/AppointmentView.vue'
import UserView from '@/views/UserView.vue'
import HomeView from '@/views/HomeView.vue'
import RegisterView from '@/views/RegisterView.vue'

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
      path: '/register',
      name: 'Register',
      component: RegisterView,
    },
  ],
})

export default router
