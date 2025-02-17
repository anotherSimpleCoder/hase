package de.hase.hasev2.participates;
import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.appointment.AppointmentService;
import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.appointment.exceptions.AppointmentNotFoundException;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserService;
import de.hase.hasev2.user.exceptions.UserNotFoundException;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static de.hase.hasev2.database.Tables.*;

@Service
public class ParticipatesService {
    private AppointmentService appointmentService;
    private UserService userService;

    private Logger logger;
    private DSLContext database;

    public ParticipatesService(@Autowired HikariService hikariService, @Autowired AppointmentService appointmentService, @Autowired UserService userService) {
        this.logger = LoggerFactory.getLogger(ParticipatesService.class);

        this.appointmentService = appointmentService;
        this.userService = userService;

        try (var connection = hikariService.getDataSource().getConnection()){
            connection.createStatement().execute("PRAGMA foreign_keys=ON");

            database = DSL.using(hikariService.getDataSource().getConnection());
        } catch(SQLException e){
            this.logger.error("Database Error: "+ e.getMessage());
        }
    }

    private void validMapping(Map<Integer, Integer> appointmentsToUsersMap) throws AppointmentNotFoundException, UserNotFoundException {
        for(int appointmentId : appointmentsToUsersMap.keySet()){
            this.appointmentService.findAppointment(appointmentId)
                    .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

            for (int matrikelNr : appointmentsToUsersMap.values()){
                this.userService.findUser(matrikelNr)
                                .orElseThrow(() -> new UserNotFoundException(matrikelNr));
            }
        }
    }

    public List<Appointment> getAppointmentsForUser(int matrikelNr){
       return database.select(APPOINTMENTS.APPOINTMENTID, APPOINTMENTS.NAME, APPOINTMENTS.DATE, APPOINTMENTS.LOCATION)
               .from(PARTICIPATES.naturalJoin(APPOINTMENTS))
               .where(PARTICIPATES.MATRIKELNR.eq(matrikelNr))
               .fetchInto(Appointment.class);
    }

    public List<User> getUsersForAppointment(int appointmentId){
        return database.select(USERS.MATRIKELNR, USERS.FIRSTNAME, USERS.LASTNAME, USERS.EMAIL, USERS.PASSWORD)
                .from(PARTICIPATES.naturalJoin(USERS))
                .where(PARTICIPATES.APPOINTMENTID.eq(appointmentId))
                .fetchInto(User.class);
    }

    public void addUsersToAppointments(Map<Integer, Integer> appointmentsToUsersMap) throws AppointmentNotFoundException, UserNotFoundException {
        validMapping(appointmentsToUsersMap);

        for(int appointmentId : appointmentsToUsersMap.keySet()){
            for (int matrikelNr : appointmentsToUsersMap.values()){
                database.insertInto(PARTICIPATES, PARTICIPATES.APPOINTMENTID, PARTICIPATES.MATRIKELNR)
                        .values(appointmentId, matrikelNr).onDuplicateKeyIgnore()
                        .execute();

            }
        }
    }

    public void removeUsersFromAppointments(Map<Integer, Integer> appointmentsToUsersMap) throws AppointmentNotFoundException, UserNotFoundException {
        validMapping(appointmentsToUsersMap);
        System.out.println(appointmentsToUsersMap);


        for (int appointmentId : appointmentsToUsersMap.keySet()){
            for (int matrikelNr : appointmentsToUsersMap.values()){
                database.deleteFrom(PARTICIPATES)
                        .where(PARTICIPATES.MATRIKELNR.eq(matrikelNr).and(PARTICIPATES.APPOINTMENTID.eq(appointmentId)))
                        .returningResult()
                        .fetch();
            }
        }
    }

    public void deleteAllMappings() {
        database.deleteFrom(PARTICIPATES)
                .execute();
    }
}
