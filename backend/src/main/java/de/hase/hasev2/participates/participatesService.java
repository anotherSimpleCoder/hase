package de.hase.hasev2.participates;
import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.config.HikariService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static de.hase.hasev2.database.Tables.APPOINTMENTS;
import static de.hase.hasev2.database.Tables.PARTICIPATES;

@Service
public class participatesService {
    private Logger logger;
    private DSLContext database;

    public participatesService(@Autowired HikariService hikariService){
        this.logger = LoggerFactory.getLogger(participatesService.class);
        try{
            database = DSL.using(hikariService.getDataSource().getConnection());
        } catch(SQLException e){
            this.logger.error("Database Error: "+ e.getMessage());
        }
    }

    public List<Appointment> getAppointmentsForUser(int matrikelNr){
       return database.select(APPOINTMENTS.APPOINTMENTID, APPOINTMENTS.NAME, APPOINTMENTS.DATE, APPOINTMENTS.LOCATION)
               .from(PARTICIPATES.naturalJoin(APPOINTMENTS))
               .where(PARTICIPATES.MATRIKELNR.eq(matrikelNr))
               .fetchInto(Appointment.class);
    }

    public void addUsersToAppointments(Map<Integer, Integer> appointmentsToUsersMap){
        for(int appointmentId : appointmentsToUsersMap.keySet()){
            for (int matrikelNr : appointmentsToUsersMap.values()){
                    database.insertInto(PARTICIPATES, PARTICIPATES.APPOINTMENTID, PARTICIPATES.MATRIKELNR)
                            .values(appointmentId, matrikelNr)
                            .execute();
            }
        }
    }
    public void removeUsersFromAppointments(Map<Integer, Integer> appointmentsToUsersMap){
        for (int appointmentId : appointmentsToUsersMap.keySet()){
            for (int matrikelNr : appointmentsToUsersMap.values()){
                database.deleteFrom(PARTICIPATES)
                        .where(PARTICIPATES.MATRIKELNR.eq(matrikelNr).and(PARTICIPATES.APPOINTMENTID.eq(appointmentId)))
                        .execute();
            }
        }
    }
}
