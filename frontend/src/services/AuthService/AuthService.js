import { ref } from 'vue'

export default {
  loggedIn: ref(!!localStorage.getItem('token')),

  getToken() {
    return localStorage.getItem('token') ? JSON.parse(localStorage.getItem('token')) : undefined
  },

  setToken(token) {
    localStorage.setItem('token', token)
  },

  isLoggedIn() {
    return this.loggedIn.value
  },

  async login(login) {
    if(!login) {
      throw new Error("Please fill in all the required fields!")
    }

    for(const property in login) {
      if(!login[property]) {
        throw new Error(`${property} is required`)
      }
    }

    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(login),
    })

    if (response.status !== 200 || !(response.ok)) {
      throw new Error("Invalid login credentials")
    }

    this.setToken(await response.text())
    this.loggedIn.value = !!localStorage.getItem('token')
  },

  logout() {
    localStorage.removeItem('token')
    this.loggedIn.value = false
  },

  async getMe() {
    const token = this.getToken()
    if(!token) {
      throw new Error('Invalid request: token is missing!')
    }

    try {
      const response = await fetch(`http://localhost:8080/auth/me`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token.token}`
        }
      });

      return await response.json()
    } catch (error) {
      throw new Error('Error handling data:', error)
    }
  },
}
