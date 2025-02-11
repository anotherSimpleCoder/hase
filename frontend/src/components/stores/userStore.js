import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => {
    if (localStorage.getItem('store')) return JSON.parse(localStorage.getItem('store'))
    return {}
  },
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
  persist: true,
})
