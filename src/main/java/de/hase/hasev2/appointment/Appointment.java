package de.hase.hasev2.appointment;

import java.util.Date;

public record Appointment(
        int appointment,
        String name,
        Date date,
        String location
) {}
