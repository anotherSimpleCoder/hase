package de.hase.hasev2.appointment.exceptions;

public class AppointmentNotFoundException extends Exception {
    public AppointmentNotFoundException(int appointmentId) {
        super("Appointment " + appointmentId + " not found");
    }
}
