import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => {
    return localStorage.getItem('store') ? JSON.parse(localStorage.getItem('store')) : null
  },
  actions: {
    isLoggedIn() {
      return this.user
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
