<template>
  <div v-if="appointments.length > 0">{{ appointments[1].date.split('T')[0] }}</div>
  <div v-if="appointments.length > 0">
    {{ appointments[1].date.split('T')[1].split(':').slice(0, 2).join(':') }}
  </div>
  <div class="week-calendar">
    <div class="calendar-header">
      <button @click="previousWeek">&lt;</button>
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
      <button @click="nextWeek">&gt;</button>
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
          <div
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
    <div>{{ selectedAppointment.appointmentId }}</div>
    <div>{{ selectedAppointment.name }}</div>
    <div>{{ selectedAppointment.date }}</div>
    <div>{{ selectedAppointment.location }}</div>
    <button @click="closePopup()">close</button>
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
      const response = await AppointmentMappingService.getAppointmentsForUser(this.loggedInUser)
      this.appointments = response.data
    },
    openPopup(appointment) {
      this.popupVisible = true
      this.selectedAppointment = appointment
    },
    closePopup() {
      console.log('gfs')
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
  margin: 0 auto;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.calendar-grid {
  display: flex;
}

.time-column,
.day-column {
  flex: 1;
  border: 1px solid #ddd;
}

.time-column {
  transform: translateY(29px);
  flex: 0 0 60px;
}

.time-slot,
.event-slot {
  height: 60px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: center;
}

.day-header {
  text-align: center;
  padding: 5px;
  background-color: #f0f0f0;
  font-weight: bold;
}

.event-slot {
  cursor: pointer;
}

.event-slot:hover {
  background-color: #f0f0f0;
}

.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  width: 70%;
  height: 60%;
}
</style>
