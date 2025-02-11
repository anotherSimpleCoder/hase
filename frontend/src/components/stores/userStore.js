import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
  }),
  actions: {
    isLoggedIn() {
      return this.user !== null
    },
    setUser(userData) {
      this.user = userData
    },
    logoutUser() {
      this.user = null
    },
  },
})
