<template>
  <Appointment></Appointment>
</template>

<script>
import Appointment from '@/components/AppointmentComponent.vue'

export default {
  components: {
    Appointment,
  },
}
</script>

<style>
body {
  background-color: #ccc !important;
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
</style>
