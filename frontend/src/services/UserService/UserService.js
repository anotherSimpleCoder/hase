import AuthService from '@/services/AuthService/AuthService.js'

const API_URL = 'http://localhost:8080'

export default {
  async getUsers() {
    const response = await fetch(`${API_URL}/users/all`, {
      headers: {
        Authorization: `Bearer ${AuthService.getToken().token}`,
      },
    })
    return await response.json()
  },

  async getUserByMatrikelNr(matrikelNr) {
    const response = await fetch(`${API_URL}/users/userById?matrikelNr=${matrikelNr}`, {
      headers: {
        Authorization: `Bearer ${AuthService.getToken().token}`,
      },
    })
    return response.json()
  },
}
