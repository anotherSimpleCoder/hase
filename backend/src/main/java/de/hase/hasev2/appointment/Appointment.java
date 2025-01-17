package de.hase.hasev2.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record Appointment(
        int appointmentId,
        String name,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime date,

        String location
) {}
