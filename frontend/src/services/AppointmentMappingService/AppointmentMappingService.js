import axios from 'axios'

const API_URL = 'http://localhost:8080'

export default {
  async postAppointmentforUser(mapData) {
    if(!mapData) {
      throw new Error('Mapping is required')
    }

    await axios.post(`${API_URL}/user-appointment`, mapData, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
  },

  async getAppointmentsForUser(matrikelNr) {
    if(!matrikelNr) {
      throw new Error('Matrikelnummer is required')
    }

    const response = await axios.get(`${API_URL}/user-appointment?matrikelNr=${matrikelNr}`)

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
        },
      },
    )
  },
}
