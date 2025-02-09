<template>
  <header>
    <div>
      <nav>
        <RouterLink to="/">Home</RouterLink> | <RouterLink to="/about">About</RouterLink> |
        <RouterLink to="/appointments">Appointents</RouterLink>
      </nav>
    </div>
    <button @click="logout()">Logout</button>
  </header>
</template>

<script>
import { RouterLink, RouterView } from 'vue-router'
import { useUserStore } from './stores/userStore'

export default {
  setup() {
    const userStore = useUserStore()
    return {
      userStore,
    }
  },
  data() {
    return {}
  },
  methods: {
    logout() {
      this.userStore.logoutUser()
    },
  },
}
</script>

<style scoped></style>
