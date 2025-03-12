package de.hase.hasev2.appointment;

import java.time.LocalDateTime;

public class AppointmentBuilder {
    private int appointmentId = -1;
    private String name;
    private LocalDateTime date;
    private String location;
    private int creator;

    public AppointmentBuilder from(Appointment appointment) {
        this.appointmentId = appointment.appointmentId();
        this.name = appointment.name();
        this.date = appointment.date();
        this.location = appointment.location();
        this.creator = appointment.creator();
        return this;
    }

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

    public AppointmentBuilder creator(int creator) {
        this.creator = creator;
        return this;
    }

    public Appointment build() {
        return new Appointment(appointmentId, name, creator, date, location);
    }
}
