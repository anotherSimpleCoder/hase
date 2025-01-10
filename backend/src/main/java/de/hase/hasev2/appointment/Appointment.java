package de.hase.hasev2.appointment;

import java.time.LocalDateTime;
import java.util.Date;

public record Appointment(
        int appointment,
        String name,
        LocalDateTime date,
        String location
) {}
