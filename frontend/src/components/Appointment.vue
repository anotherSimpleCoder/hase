<template>
  <div
    class="appointment-planner"
    v-for="appointment in appointments"
    v-bind:key="appointment.appointmentId"
  >
    <div class="appointment">
      <div class="appointment-id">{{ appointment.appointmentId }}</div>
      <div class="appointment-name">{{ appointment.name }}</div>
      <div class="appointment-date">📅 {{ formatDate(appointment) }}</div>
      <div class="appointment-location">📍 {{ appointment.location }}</div>
    </div>
  </div>

  <button class="add-button" @click="toggle()">+</button>
  <div>{{ popupVisible }}</div>
  <div v-if="popupVisible" class="popup">
    <div class="popup-content">
      <input type="text" placeholder="Name" v-model="newAppointment.name" />
      <DatePicker
        v-model="newAppointment.date"
        showTime
        hourFormat="24"
        dateFormat="dd.mm.yy"
      ></DatePicker>
      <input type="text" placeholder="Ort" v-model="newAppointment.location" />
      <button @click="addAppointment()">Hinzufügen</button>
    </div>
  </div>
  <div>{{ tempDate }}</div>
</template>

<script>
import { DatePicker } from "primevue";

export default {
  name: "Appointment",
  components: { DatePicker },
  data() {
    return {
      appointments: [],
      popupVisible: true,
      newAppointment: {
        name: "",
        date: "",
        location: "",
      },
      tempDate: new Date().toISOString(),
    };
  },
  methods: {
    async getAppointments() {
      const response = await fetch("http://localhost:8080/appointment/all");

      const result = await response.json();
      this.appointments = result;
    },
    formatDate(appointment) {
      const date = new Date(appointment.date);
      return `${date.getDate()}.${
        date.getMonth() + 1
      }.${date.getFullYear()} ${date.getHours()}:${date
        .getMinutes()
        .toString()
        .padStart(2, "0")}`;
    },
    toggle() {
      this.popupVisible = !this.popupVisible;
    },
    async addAppointment() {
      await fetch("http://localhost:8080/appointment/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(this.newAppointment),
      });

      window.location.reload();
    },
  },

  mounted() {
    this.getAppointments();
  },
};
</script>

<style>
.appointment-planner {
  max-width: 800px;
  margin: 20px auto;
  font-family: Arial, sans-serif;
}

.appointment {
  background-color: #f9f9f9;
  border-left: 4px solid #4caf50;
  margin-bottom: 15px;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;
}

.appointment:hover {
  transform: translateY(-3px);
}

.appointment-id {
  font-size: 0.9em;
  color: #888;
  margin-bottom: 5px;
}

.appointment-name {
  font-size: 1.2em;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.appointment-date {
  font-size: 1em;
  color: #4caf50;
  margin-bottom: 5px;
}

.appointment-location {
  font-size: 1em;
  color: #666;
}

@media (max-width: 600px) {
  .appointment-planner {
    padding: 0 15px;
  }
}
</style>
