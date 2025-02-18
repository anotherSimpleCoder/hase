import axios from 'axios'

const API_URL = 'http://localhost:8080'

export default {
  async getAppointmentforUser(mapData) {
    await axios.post(`${API_URL}/user-appointment`, mapData, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
  },

  async getAppointmentsForUser(matrikelNr) {
    const response = await axios.get(`${API_URL}/user-appointment?matrikelNr=${matrikelNr}`)

    return await response
  },

  async removeAppointmentFromUser(mapData) {
    console.log(mapData)
    await axios.delete(
      `${API_URL}/user-appointment`,
      { data: mapData },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      },
    )
  },
}
