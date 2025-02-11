import axios from 'axios'

export default {
  async login(login) {
    const response = await axios.post('http://localhost:8080/auth/login', login)

    if (response.status !== 200) {
      throw new Error(response.statusText)
    }
    return response

  },
  async getUser(login) {
    console.log(login)
    try {
      const response = await fetch(`http://localhost:8080/auth/login?email=${login}`)
      const data = await response.json()
      console.log(data)
      return data
    } catch (error) {
      console.error('Fehler beim bearbeiten der Daten:', error)
    }
  },
}
