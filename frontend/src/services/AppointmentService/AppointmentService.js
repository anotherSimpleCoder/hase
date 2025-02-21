import AuthService from '@/services/AuthService/AuthService.js'

const API_URL = 'http://localhost:8080'

export default {
  async getAppointments() {
    const response = await fetch(`${API_URL}/appointment/all`, {
      headers: {
        'Authorization': `Bearer ${AuthService.getToken().token}`
      }
    })
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
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${AuthService.getToken().token}`
      },
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
      headers: {
        'Authorization': `Bearer ${AuthService.getToken().token}`
      }
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
      headers: {
        'Content-Type': 'application/json' ,
        'Authorization': `Bearer ${AuthService.getToken().token}`
      },
      body: JSON.stringify(appointment),
    })
  },


}
