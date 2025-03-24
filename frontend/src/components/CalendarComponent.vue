<template>
  <div class="week-calendar">
    <div class="calendar-header">
      <button class="week-button" @click="previousWeek"> Previous week </button>
      <h2>
        {{
          weekStartDate.toLocaleDateString('de-DE', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
          })
        }}
        -
        {{
          weekEndDate.toLocaleDateString('de-DE', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
          })
        }}
      </h2>
      <button class="week-button" @click="nextWeek">Next week </button>
    </div>
    <div class="calendar-grid">
      <div class="time-column">
        <div v-for="time in timeSlots" :key="time" class="time-slot">
          {{ time }}
        </div>
      </div>
      <div v-for="day in weekDays" :key="day.date" class="day-column">
        <div class="day-header">{{ day.name }}</div>
        <div
          v-for="time in timeSlots"
          :key="`${day.date}-${time}`"
          class="event-slot"
          :data-date="day.date"
          :data-time="time"
        >
          <div class="textInCalender"
            v-for="appointment in appointments"
            :key="appointment.appointmentId"
            @click="openPopup(appointment)"
          >
            <div
              v-if="
                appointment.date.split('T')[0] === day.date &&
                appointment.date.split('T')[1].split(':').slice(0, 2).join(':') >= time &&
                appointment.date.split('T')[1].split(':').slice(0, 2).join(':') <
                  timeSlots[timeSlots.indexOf(time) + 1]
              "
            >
              {{ appointment.name }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-if="popupVisible" class="popup">
    <div> Appointment Number: {{ selectedAppointment.appointmentId }}</div>
    <div>Name: {{ selectedAppointment.name }}</div>
    <div>Date and Time: {{ selectedAppointment.date }}</div>
    <div>Location: {{ selectedAppointment.location }}</div>
    <button class="close-PopUp" @click="closePopup()">close</button>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import AuthService from '@/services/AuthService/AuthService.js'
import AppointmentMappingService from '@/services/AppointmentMappingService/AppointmentMappingService.js'

export default {
  setup() {
    const currentDate = ref(new Date())
    const appointments = ref([])

    const weekStartDate = computed(() => {
      const date = new Date(currentDate.value)
      date.setDate(date.getDate() - date.getDay() + 1)
      return date
    })

    const weekEndDate = computed(() => {
      const date = new Date(weekStartDate.value)
      date.setDate(date.getDate() + 6)
      return date
    })

    const weekDays = computed(() => {
      const days = []
      for (let i = 0; i < 7; i++) {
        const date = new Date(weekStartDate.value)
        date.setDate(date.getDate() + i)
        days.push({
          name: date.toLocaleDateString('de-DE', { weekday: 'short' }),
          date: date.toISOString().split('T')[0],
        })
      }
      return days
    })

    const timeSlots = ref([
      '07:00',
      '08:00',
      '09:00',
      '10:00',
      '11:00',
      '12:00',
      '13:00',
      '14:00',
      '15:00',
      '16:00',
      '17:00',
      '18:00',
    ])

    const previousWeek = () => {
      currentDate.value.setDate(currentDate.value.getDate() - 7)
      currentDate.value = new Date(currentDate.value)
    }

    const nextWeek = () => {
      currentDate.value.setDate(currentDate.value.getDate() + 7)
      currentDate.value = new Date(currentDate.value)
    }

    return {
      weekStartDate,
      weekEndDate,
      weekDays,
      timeSlots,
      previousWeek,
      nextWeek,
      appointments,
    }
  },
  data() {
    return {
      loggedInUser: null,
      popupVisible: false,
      selectedAppointment: null,
    }
  },
  methods: {
    async getAppointments() {
      this.loggedInUser = await AuthService.getMe()
      this.appointments = await AppointmentMappingService.getAppointmentsForUser(this.loggedInUser)
    },
    openPopup(appointment) {
      this.popupVisible = true
      this.selectedAppointment = appointment
    },
    closePopup() {
      this.popupVisible = !this.popupVisible
      console.log(this.popupVisible)
      this.selectedAppointment = null
    },
  },
  mounted() {
    this.getAppointments()
  },
}
</script>

<style scoped>
.week-calendar {
  max-width: 1000px;
  margin: 20px auto;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0px 4px 10px rgba(0,0,0,0.1);
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: bold;
  color: black;
}

.calendar-grid {
  display: flex;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  overflow: hidden;
}

.time-column,
.day-column {
  flex: 1;
  border: 1px solid #ddd;
}

.time-column {
  transform: translateY(29px);
  flex: 0 0 60px;
  background-color: #e3e3e3;
  border-right: 1px solid #ccc;
}

.time-slot,
.event-slot {
  height: 60px;
  font-size: 14px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  color: black;
  font-weight: bold;
}

.day-header {
  text-align: center;
  padding: 5px;
  background-color: rgb(0, 97, 148);
  color: white;
  font-weight: bold;
  border-bottom: 2px solid rgb(0, 97, 148);
}

.event-slot {
  cursor: pointer;
  height: 60px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.3s;
}

.event-slot:hover {
  background-color: #f0f0f0;
  background: rgba(106, 165, 228, 0.1);
}

.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0,0,0,0.2);
  border: 1px solid #ccc;
  width: 35%;
  max-width: 400px;
  height: 20%;
  animation: fadeIn 0.3s ease-in-out;
  justify-content: center;
  align-items: center;
  display: flex;
  flex-direction: column;
  overflow: auto;
}

.week-button {
  background-color: rgb(0, 97, 148);
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
}
 .week-button:hover{
  background-color: rgb(0,202,50);
  color: white;
 }

.close-PopUp{
  cursor: pointer;
  margin-top: auto;
  align-self: center;
  padding: 7px 13px;
  background-color: rgb(0, 97, 148);
  color: white;
  border: none;
  border-radius: 5px;
}

.close-PopUp:hover{
  background-color: rgb(0,202,50);
  color: white;
}

.textInCalender{
  padding-top: 20px;
  padding-bottom: 20px;
}
</style>
