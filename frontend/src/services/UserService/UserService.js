import AuthService from '@/services/AuthService/AuthService.js'
import axios from 'axios'

const API_URL = 'http://localhost:8080'

export default {
  async register(user) {
    if (!user) {
      throw new Error('Please fill in all the required fields!')
    }

    for (const property in user) {
      if (!user[property]) {
        throw new Error(`${property} is required!`)
      }
    }

    const response = await axios.post('http://localhost:8080/users/register', user)

    if (response.status !== 200) {
      throw new Error(response.statusText)
    }

    return response
  },

  async getUsers() {
    const response = await fetch(`${API_URL}/users/all`, {
      headers: {
        Authorization: `Bearer ${AuthService.getToken().token}`,
      },
    })
    return await response.json()
  },

  async getUserByMatrikelNr(matrikelNr) {
    if(!matrikelNr) {
      throw new Error("MatrikelNr is required")
    }

    const response = await fetch(`${API_URL}/users/userById?matrikelNr=${matrikelNr}`, {
      headers: {
        Authorization: `Bearer ${AuthService.getToken().token}`,
      },
    })
    return response.json()
  },
}
