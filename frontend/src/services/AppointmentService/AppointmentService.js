const API_URL = 'http://localhost:8080'

export default {
  async getAppointments() {
    const response = await fetch(`${API_URL}/appointment/all`)
    return await response.json()
  },

  async addAppointment(appointment) {
    if(!appointment) {
      throw new Error('Please fill in all the required fields!')
    }

    for(const property in appointment) {
      if(!appointment[property]) {
        throw new Error(`${property} is required`)
      }
    }

    await fetch(`${API_URL}/appointment`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(appointment),
    })
  },

  async deleteAppointment(appointment) {
    if(!appointment) {
      throw new Error('Please fill in all the required fields!')
    }

    for(const property in appointment) {
      if(!appointment[property]) {
        throw new Error(`${property} is required`)
      }
    }

    await fetch(`${API_URL}/appointment?appointmentId=${appointment.appointmentId}`, {
      method: 'DELETE',
    })
  },

  async updateAppointment(appointment) {
    if(!appointment) {
      throw new Error('Please fill in all the required fields!')
    }

    for(const property in appointment) {
      if(!appointment[property]) {
        throw new Error(`${property} is required`)
      }
    }

    await fetch(`${API_URL}/appointment`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(appointment),
    })
  },


}
