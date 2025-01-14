package de.hase.hasev2.appointment;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

@Service
public class AppointmentService {
    private DSLContext database;

    public AppointmentService() {
        try {
            String dbPath = new File("database/database.sqlite").getAbsolutePath();

            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            database = DSL.using(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> findAllAppointments() {
        return database.selectFrom(APPOINTMENTS)
                .fetchInto(Appointment.class);
    }

    public Optional<Appointment> findAppointment(int appointmentId) {
        return database.selectFrom(APPOINTMENTS)
                .where(APPOINTMENTS.APPOINTMENTID.eq(appointmentId))
                .fetchOptionalInto(Appointment.class);
    }

    public Optional<Appointment> saveAppointment(Appointment appointment) {
        return database.insertInto(APPOINTMENTS, APPOINTMENTS.NAME, APPOINTMENTS.DATE, APPOINTMENTS.LOCATION)
                .values(appointment.name(), appointment.date(), appointment.location())
                .returningResult()
                .fetchOptionalInto(Appointment.class);
    }

    public Optional<Appointment> deleteAppointment(int appointmentId) {
        return database.deleteFrom(APPOINTMENTS)
                .where(APPOINTMENTS.APPOINTMENTID.eq(appointmentId))
                .returningResult()
                .fetchOptionalInto(Appointment.class);
    }

    public Optional<Appointment> updateAppointment(int appointmentId, Appointment updatedAppointment){
        return database.update(APPOINTMENTS)
                .set(APPOINTMENTS.DATE, updatedAppointment.date())
                .set(APPOINTMENTS.NAME, updatedAppointment.name())
                .set(APPOINTMENTS.LOCATION, updatedAppointment.location())
                .where(APPOINTMENTS.APPOINTMENTID.eq(appointmentId))
                .returningResult()
                .fetchOptionalInto(Appointment.class);
    }

}
