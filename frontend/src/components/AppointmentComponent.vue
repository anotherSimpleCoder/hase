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
          <button class="edit-btn" @click="toggleCondition">✏️ Edit</button>
          <button class="delete-btn" @click="deleteAppointment(appointment)">🗑️ Delete</button>
          <button
            v-if="!condition"
            class="confirm-btn"
            @click="(updateAppointment(appointment), toggleCondition())"
          >
            ✔️ Confirm
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
        <DatePicker
          class="custom-datepicker"
          v-model="newAppointment.date"
          showTime
          hourFormat="24"
          dateFormat="dd.mm.yy"
          showIcon
        />
        <input
          type="text"
          placeholder="Location"
          v-model="newAppointment.location"
          class="popup-input"
        />
        <button class="add-btn" @click="addAppointment">add Appointment</button>
      </div>
    </div>
    <DatePicker inputClass="text-center text-2xl bg-gray-200" panelClass="custom-panel">
    </DatePicker>
  </div>
</template>

<script>
import { DatePicker } from 'primevue'
import { useUserStore } from './stores/userStore'

export default {
  name: 'AppointmentComponent',
  components: { DatePicker },
  setup() {
    const userStore = useUserStore()
    return {
      userStore,
    }
  },
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

<style>
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
  background-color: red;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
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
  width: 200px;
}

.p-datepicker {
  font-size: 1.2rem; /* Größerer Text */
  background-color: white;
  border: 1px solid black; /* Hintergrundfarbe */
  border-radius: 4px;
  width: 200px;
}

.p-datepicker input {
  text-align: center;
}

.p-datepicker-header {
  background-color: rgb(51, 97, 148) !important;
}

.p-datepicker-calendar {
  background-color: lightgray;
  width: 20rem;

  border-top: 3px solid black;
  border-right: 3px solid black;
  border-left: 3px solid black !important;
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
}

.p-datepicker-calendar td:hover {
  background-color: #4caf50;
  border-radius: 50%; /* Grüne Hintergrundfarbe beim Hovern */
  color: white;
}

.p-datepicker-calendar td > span {
  border-radius: 50%; /* Kreisförmige Hervorhebung der Tage */
  width: 2.5rem;
  height: 2.5rem;
  line-height: 2.5rem;
}

.p-datepicker-time-picker {
  background-color: lightgrey !important;
  border-bottom: 3px solid black;
  border-right: 3px solid black;
  border-left: 3px solid black;
  border-bottom-right-radius: 5px;
  border-bottom-left-radius: 5px;
  text-align: center;
}

.p-datepicker-time-picker > div {
  /* Styles für die Stunden-, Minuten- und Sekundenauswahl */
  font-size: 1.2rem;
}

.p-datepicker-time-picker span {
  /* Styles für die einzelnen Zeiteinheiten */
  padding: 5px;
  border-radius: 50%;
}

.p-datepicker-time-picker button :hover {
  /* Hover-Effekt für die Buttons */
  background-color: #4caf50;
  border-radius: 50%;
}

.p-datepicker-weekday-cell {
  background-color: #4caf50;
}

.p-datepicker-calendar button :hover {
  background-color: #2e6a30;
  border-radius: 50%;
}

/* .p-datepicker {
  background-color: #ffffff;
  border-radius: 8px;
}

.p-datepicker-header {
  background-color:  #3d1212;
}

.p-datepicker-calendar {
  background-color: #383838;
  color: #ffffff;
  size: 30%;
}

.p-datepicker-time {
  background-color: #383838;
} */
</style>
