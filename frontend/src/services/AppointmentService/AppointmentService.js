import axios from 'axios'

const API_URL = 'http://localhost:8080'

export default {
  async getAppointments() {
    const response = await fetch(`${API_URL}/appointment/all`)
    return await response.json()
  },
  async addAppointment(appointment) {
    await fetch(`${API_URL}/appointment`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(appointment),
    })
  },
  async deleteAppointment(appointment) {
    await fetch(`${API_URL}/appointment?appointmentId=${appointment.appointmentId}`, {
      method: 'DELETE',
    })
  },
  async updateAppointment(appointment) {
    await fetch(`${API_URL}/appointment`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(appointment),
    })
  },
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
