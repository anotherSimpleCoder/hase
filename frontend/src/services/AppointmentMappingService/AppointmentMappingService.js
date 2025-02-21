import axios from 'axios'
import AuthService from '@/services/AuthService/AuthService.js'

const API_URL = 'http://localhost:8080'

export default {
  async postAppointmentforUser(mapData) {
    if(!mapData) {
      throw new Error('Mapping is required')
    }

    await axios.post(`${API_URL}/user-appointment`, mapData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${AuthService.getToken().token}`
      },
    })
  },

  async getAppointmentsForUser(user) {
    if(!user) {
      throw new Error('User is required')
    }

    const response = await axios.get(`${API_URL}/user-appointment?matrikelNr=${user.matrikelNr}`, {
      headers: {
        'Authorization': `Bearer ${AuthService.getToken().token}`
      }
    })

    return await response
  },

  async removeAppointmentFromUser(mapData) {
    if(!mapData) {
      throw new Error('Mapping is required')
    }

    console.log(mapData)
    await axios.delete(
      `${API_URL}/user-appointment`,
      { data: mapData },
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${AuthService.getToken().token}`
        },
      },
    )
  },
}
