<template>
  <div v-for="user in users" v-bind:key="user.matrikelNr">
    <div>{{ user.matrikelNr }}</div>
    <div>{{ user.firstName }}</div>
    <div>{{ user.lastName }}</div>
    <div>{{ user.email }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      users: [],
    }
  },

  methods: {
    async getUsers() {
      const response = await fetch('http://localhost:8080/users/all')

      const result = await response.json()
      this.users = result
      console.log(result)
    },
  },

  mounted() {
    this.getUsers()
  },
}
</script>
