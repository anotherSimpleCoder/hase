import { createRouter, createWebHistory } from 'vue-router'
import AppointmentView from '@/views/AppointmentView.vue'
import UserView from '@/views/UserView.vue'
import Callback from '@/components/Callback.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
      path: '/callback',
      name: 'Callback',
      component: Callback,
    },
  ],
})

export default router
