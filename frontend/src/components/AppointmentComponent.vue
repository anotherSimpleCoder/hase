<template>
  <div>{{ this.loggedInUser }}</div>
  <div class="container">
    <div class="input-container">
      <input v-model="searchRequest" placeholder="🔍 Search for appointments" class="search-input" />
    </div>

    <div class="appointment-list">
      <div
        class="appointment-card"
        v-for="appointment in filteredAppointments"
        :key="appointment.appointmentId"
      >
        <div class="appointment-header">
          <div class="appointment-id">#️⃣ {{ appointment.appointmentId }}</div>
        </div>

        <div class="appointment-content">
          <div v-if="condition" class="appointment-name">📌 {{ appointment.name }}</div>
          <div v-else>📌 <input v-model="appointment.name" class="edit-input" /></div>

          <div class="datum">
            <div v-if="condition" class="appointment-date">📅 {{ formatDate(appointment) }}</div>
            <div v-else>
              📅
              <DatePicker
                v-model="appointment.date"
                showTime
                hourFormat="24"
                dateFormat="dd.mm.yy"
                showIcon
              />
            </div>
          </div>

          <div v-if="condition" class="appointment-location">📍 {{ appointment.location }}</div>
          <div v-else>📍 <input v-model="appointment.location" class="edit-input" /></div>
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
          <button
           class ="book-btn" @click="getAppointmentforUser(appointment.appointmentId, this.loggedInUser.matrikelNr)"
          >
            ✅Book appointment
          </button>
        </div>
      </div>
    </div>

    <button class="add-button" @click="togglePopup">➕ Make a new Appointment!</button>

    <div v-if="popupVisible" class="popup-overlay" @click="togglePopup">
      <div class="popup-content" @click.stop>
        <h3>Book an appointment!</h3>
        <input
          type="text"
          placeholder="✍️Name of appointment"
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
          ><template #footer>
            <button @click="closeDatePicker">Close</button>
          </template>
        </DatePicker>
        <input
          type="text"
          placeholder="🛖Location"
          v-model="newAppointment.location"
          class="popup-input"
        />
        <button class="add-btn" @click="addAppointment">add Appointment</button>
      </div>
    </div>
    <footer class="footer">
      <p>📌 Click "➕ make a new Appointment!" to make a new appointment.</p>
      <p>✏️ Use the "Edit" button to modify an appointment (Only creator can edit an appointment).</p>
      <p>🗑️ Click "Delete" to remove an appointment (Only creator can delete an appointment).</p>
      <p>✅ Click "Book appointment" to book an appointment and add it to the calender!</p>
    </footer>
  </div>
</template>

<script>
import { DatePicker } from 'primevue'
import AuthService from '@/services/AuthService/AuthService.js'
import AppointmentService from '@/services/AppointmentService/AppointmentService.js'
import AppointmentMappingService from '@/services/AppointmentMappingService/AppointmentMappingService.js'

export default {
  name: 'AppointmentComponent',
  components: { DatePicker },
  setup() {
    return {
      loggedInUser: null,
    }
  },
  async beforeCreate() {
    if (AuthService.isLoggedIn()) {
      this.loggedInUser = await AuthService.getMe()
    }
  },
  data() {
    return {
      appointments: [],
      popupVisible: false,
      newAppointment: { name: '', date: new Date(), location: '' },
      searchRequest: '',
      condition: true,
      mapData: {
        1: 123,
      },
    }
  },
  methods: {
    async getAppointments() {
      this.appointments = await AppointmentService.getAppointments()
    },
    formatDate(appointment) {
      const date = new Date(appointment.date)
      return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes().toString().padStart(2, '0')}`
    },
    togglePopup() {
      this.popupVisible = !this.popupVisible
    },
    addAppointment() {
      AppointmentService.addAppointment(this.newAppointment).then(this.getAppointments)
      this.togglePopup()
    },
    deleteAppointment(appointment) {
      AppointmentService.deleteAppointment(appointment).then(this.getAppointments)
    },
    async updateAppointment(appointment) {
      await AppointmentService.updateAppointment(appointment)
    },
    toggleCondition() {
      this.condition = !this.condition
    },
    getAppointmentforUser(appointmentId, matrikelNr) {
      const mapData = { [appointmentId]: matrikelNr }
      console.log(mapData)

      AppointmentMappingService.postAppointmentforUser(mapData)
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
  transform: translateY(50%);
}
.appointment-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}
.appointment-card {
  background: white;
  border: 1px solid #474747;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.appointment-card:hover {
  transform: translate(2px, -3px);
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

.add-btn {
  transform: translateY(20%);
}

.add-btn:hover {
  transform: translateY(20%);
  scale: 1.025;
}

.edit-btn:hover,
.confirm-btn:hover,
.delete-btn:hover {
  transform: scale(1.05);
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
  display:flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  animation: slideUp 0.3s ease-out;
}
date-time-container {
  display: flex;
  gap: 15px;
}

.p-datepicker {
  font-size: 1.2rem;
  background-color: white;
  border: 2px solid #ccc;
  border-radius: 8px;
  width: 219px;
  margin-bottom: 3px;
  margin-top: 3px;
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

input {
  border: 2px solid #ccc;
  border-radius: 8px;
  padding: 10px;
  font-size: 16px;
  transition: border-color 0.3s ease;
  height: 20px;
}

input:focus {
  border-color: #007bff;
  outline: none;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
.footer{
  padding: 5px;
  text-align: center;
}
.book-btn{
  background-color: rgb(0,97,148);
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}
.book-btn:hover{
  transform: scale(1.05);
}
</style>
