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
</template>

<script>
export default {
  name: "Appointment",
  data() {
    return {
      appointments: [],
    };
  },
  methods: {
    async getTermine() {
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
  },

  mounted() {
    this.getTermine();
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
