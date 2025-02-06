<template>
  <div class="container">
    <div class="input-container">
      <input v-model="searchRequest" placeholder="🔍 Suche..." class="search-input" />
    </div>

    <div class="appointment-list">
      <div
        class="appointment-card"
        v-for="appointment in filteredAppointments"
        :key="appointment.appointmentId"
      >
        <div class="appointment-header">
          <div class="appointment-id">#️⃣{{ appointment.appointmentId }}</div>
          <button class="delete-btn" @click="deleteAppointment(appointment)">🗑️</button>
        </div>

        <div class="appointment-content">
          <div v-if="condition" class="appointment-name">📌 {{ appointment.name }}</div>
          <input v-else v-model="appointment.name" class="edit-input" />

          <div class="datum">
            <div v-if="condition" class="appointment-date">📅 {{ formatDate(appointment) }}</div>
            <DatePicker
              v-else
              v-model="appointment.date"
              showTime
              hourFormat="24"
              dateFormat="dd.mm.yy"
            />
          </div>

          <div v-if="condition" class="appointment-location">📍 {{ appointment.location }}</div>
          <input v-else v-model="appointment.location" class="edit-input" />
        </div>

        <div class="button-group">
          <button class="edit-btn" @click="toggleCondition">✏️ edit</button>
          <button
            v-if="!condition"
            class="confirm-btn"
            @click="(updateAppointment(appointment), toggleCondition)"
          >
            ✔️ Bestätigen
          </button>
        </div>
      </div>
    </div>

    <button class="add-button" @click="togglePopup">➕ New Appointment</button>

    <div v-if="popupVisible" class="popup-overlay" @click="togglePopup">
      <div class="popup-content" @click.stop>
        <input
          type="text"
          placeholder="Name of Appointment"
          v-model="newAppointment.name"
          class="popup-input"
        />
        <input v-model="newAppointment.date" type="date" class="date-input" />
        <input v-model="newAppointment.time" type="time" class="time-input" />
        <input
          type="text"
          placeholder="Location"
          v-model="newAppointment.location"
          class="popup-input"
        />
        <button class="add-btn" @click="addAppointment">add Appointment</button>
      </div>
    </div>
  </div>
</template>

<script>
import { DatePicker } from 'primevue'

export default {
  name: 'Appointment',
  components: { DatePicker },
  data() {
    return {
      appointments: [],
      popupVisible: false,
      newAppointment: { name: '', date: new Date(), location: '' },
      searchRequest: '',
      condition: true,
    }
  },
  methods: {
    async getAppointments() {
      const response = await fetch('http://localhost:8080/appointment/all')
      this.appointments = await response.json()
    },
    formatDate(appointment) {
      const date = new Date(appointment.date)
      return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes().toString().padStart(2, '0')}`
    },
    togglePopup() {
      this.popupVisible = !this.popupVisible
    },
    async addAppointment() {
      await fetch('http://localhost:8080/appointment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.newAppointment),
      })
      this.getAppointments()
      this.togglePopup()
    },
    async deleteAppointment(appointment) {
      await fetch(`http://localhost:8080/appointment?appointmentId=${appointment.appointmentId}`, {
        method: 'DELETE',
      })
      this.getAppointments()
    },
    async updateAppointment(appointment) {
      await fetch('http://localhost:8080/appointment', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(appointment),
      })
    },
    toggleCondition() {
      this.condition = !this.condition
    },
  },
  computed: {
    filteredAppointments() {
      return this.appointments.filter((appointment) =>
        appointment.name.toLowerCase().includes(this.searchRequest.toLowerCase()),
      )
    },
  },
  mounted() {
    this.getAppointments()
  },
}
</script>

<style scoped>
.container {
  max-width: 900px;
  margin: auto;
  font-family: Arial, sans-serif;
}
.input-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
.search-input {
  padding: 10px;
  width: 80%;
  border-radius: 8px;
  border: 1px solid #ccc;
}
.appointment-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}
.appointment-card {
  background: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
.delete-btn {
  color: red;
  background: none;
  border: none;
  cursor: pointer;
}
.edit-btn,
.confirm-btn,
.add-btn {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}
.add-button {
  display: block;
  margin: 20px auto;
  background: #733f8f;
  color: white;
  border: none;
  padding: 10px 15px;

  font-size: 20px;
  cursor: pointer;
}
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.popup-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
}
date-time-container {
  display: flex;
  gap: 15px;
}

.date-input,
.time-input {
  flex: 1;
  margin-right: 5%;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.popup-input {
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
