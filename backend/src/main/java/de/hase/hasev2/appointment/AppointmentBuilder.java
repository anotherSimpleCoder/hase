package de.hase.hasev2.appointment;

import java.time.LocalDateTime;

public class AppointmentBuilder {
    private String name;
    private LocalDateTime date;
    private String location;

    public AppointmentBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AppointmentBuilder date(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public AppointmentBuilder location(String location) {
        this.location = location;
        return this;
    }

    public Appointment build() {
        return new Appointment(-1, name, date, location);
    }
}
