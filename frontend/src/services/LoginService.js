import axios from 'axios'

export default {
  async addUser(user) {
    console.log(user)
    await fetch('http://localhost:8080/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user),
    })
  },

  register(user) {
    return axios.post('http://localhost:8080/users', user)
  },
}
