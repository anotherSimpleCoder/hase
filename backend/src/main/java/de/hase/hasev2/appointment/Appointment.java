package de.hase.hasev2.appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Appointment(
        int appointmentId,
        String name,
        LocalDateTime date,
        String location
) {

}
