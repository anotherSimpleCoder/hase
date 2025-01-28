<template>
  <button v-if="!user" @click="login">Google</button>
  <button v-if="!user" @click="loginWithGithub">Github</button>
  <button v-else @click="logout">Abmelden</button>
</template>

<script>
import auth from '@/auth'

export default {
  name: 'Login',
  emits: ['login'],
  data() {
    return {
      user: null,
    }
  },
  methods: {
    async login() {
      try {
        await auth.loginWithGoogle()
        this.user = await auth.getUser()
      } catch (error) {
        console.error('Login failed:', error)
      }
    },

    async loginWithGithub() {
      try {
        await auth.loginWithGithub()
        this.user = await auth.getUser()
      } catch (error) {
        console.error('Login failed:', error)
      }
    },

    logout() {
      auth.logout()
      this.user = null
      this.$emit('login', null)
    },
  },
  async mounted() {
    this.user = await auth.getUser()
  },
}
</script>
