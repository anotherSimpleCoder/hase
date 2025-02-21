<template>
  <div v-for="user in users" v-bind:key="user.matrikelNr">
    <div>{{ user.matrikelNr }}</div>
    <div>{{ user.firstName }}</div>
    <div>{{ user.lastName }}</div>
    <div>{{ user.email }}</div>
  </div>
</template>

<script>
import AuthService from '@/services/AuthService/AuthService.js'

export default {
  data() {
    return {
      users: [],
    }
  },

  methods: {
    async getUsers() {
      const response = await fetch('http://localhost:8080/users/all', {
        headers: {
          'Authorization': `Bearer ${AuthService.getToken().token}`
        }
      })

      const result = await response.json()
      this.users = result
    },
  },

  mounted() {
    this.getUsers()
  },
}
</script>
