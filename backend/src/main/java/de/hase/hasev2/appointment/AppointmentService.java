package de.hase.hasev2.appointment;

import de.hase.hasev2.appointment.exceptions.AppointmentNotFoundException;
import de.hase.hasev2.appointment.exceptions.NotAppointmentCreatorException;
import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.user.User;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

@Service
public class AppointmentService {
    private Logger logger;
    private DSLContext database;

    public AppointmentService(@Autowired HikariService hikariService) {
        this.logger = LoggerFactory.getLogger(AppointmentService.class);

        try {
            database = DSL.using(hikariService.getDataSource().getConnection());
        } catch (SQLException e) {
            this.logger.error("Database error: " + e.getMessage());
        }
    }

    private boolean isCreator(int appointmentId, int userId) throws AppointmentNotFoundException {
        var appointment = this.findAppointment(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        return appointment.creator() == userId;
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

    public Optional<Appointment> saveAppointment(Appointment appointment, User creator) {
        return database.insertInto(APPOINTMENTS, APPOINTMENTS.NAME, APPOINTMENTS.CREATOR, APPOINTMENTS.DATE, APPOINTMENTS.LOCATION)
                .values(appointment.name(), creator.matrikelNr(), appointment.date(), appointment.location())
                .returningResult()
                .fetchOptionalInto(Appointment.class);
    }

    public Optional<Appointment> deleteAppointment(int appointmentId, User creator) throws AppointmentNotFoundException, NotAppointmentCreatorException {
        if(!this.isCreator(appointmentId, creator.matrikelNr())) {
            throw new NotAppointmentCreatorException(appointmentId, creator.matrikelNr());
        }

        return database.deleteFrom(APPOINTMENTS)
                .where(APPOINTMENTS.APPOINTMENTID.eq(appointmentId))
                .returningResult()
                .fetchOptionalInto(Appointment.class);
    }

    public Optional<Appointment> updateAppointment(Appointment updatedAppointment, User creator) throws AppointmentNotFoundException, NotAppointmentCreatorException {
        if(!this.isCreator(updatedAppointment.appointmentId(), creator.matrikelNr())) {
            throw new NotAppointmentCreatorException(updatedAppointment.appointmentId(), creator.matrikelNr());
        }

        return database.update(APPOINTMENTS)
                .set(APPOINTMENTS.DATE, updatedAppointment.date())
                .set(APPOINTMENTS.NAME, updatedAppointment.name())
                .set(APPOINTMENTS.CREATOR, updatedAppointment.creator())
                .set(APPOINTMENTS.LOCATION, updatedAppointment.location())
                .where(APPOINTMENTS.APPOINTMENTID.eq(updatedAppointment.appointmentId()))
                .returningResult()
                .fetchOptionalInto(Appointment.class);
    }

    public void deleteAllAppointments() {
        database.deleteFrom(APPOINTMENTS)
                .execute();
    }
}
