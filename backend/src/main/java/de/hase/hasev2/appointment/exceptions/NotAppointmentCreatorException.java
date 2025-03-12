package de.hase.hasev2.appointment.exceptions;

public class NotAppointmentCreatorException extends RuntimeException {
    public NotAppointmentCreatorException(int appoinmentId, int userId) {
      super(userId + " is not permitted to edit the appointment " + appoinmentId);
    }
}
