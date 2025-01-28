<template>
  <div>Register:</div>

  <input v-model="newUser.matrikelNr" />
  <input v-model="newUser.firstName" />
  <input v-model="newUser.lastName" />
  <input v-model="newUser.email" />
  <input v-model="newUser.password" />
  <button @click="addUser(this.newUser)">register</button>
  <div>{{ newUser.firstName }}</div>

  <div>
    <div>Login:</div>
  </div>
</template>

<script>
import LoginService from '@/services/LoginService.js'
export default {
  components: {
    LoginService,
  },
  data() {
    return {
      newUser: {
        matrikelNr: 0,
        firstName: '',
        lastName: '',
        email: '',
        password: '',
      },
    }
  },

  methods: {
    async addUser(user) {
      const response = await LoginService.register(user)
    },
  },
}
</script>
