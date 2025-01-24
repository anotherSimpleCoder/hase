package de.hase.hasev2.config;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;
import static de.hase.hasev2.database.tables.Users.USERS;

import de.hase.hasev2.appointment.AppointmentService;
import de.hase.hasev2.database.tables.Users;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class DatabaseInitializer implements ApplicationRunner {
    private Logger logger;
    private DSLContext database;

    public DatabaseInitializer() {
        this.logger = LoggerFactory.getLogger(AppointmentService.class);

        try {
            String dbPath = new File("./database/database.sqlite").getAbsolutePath();
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            database = DSL.using(connection);
        } catch (SQLException e) {
            this.logger.error(e.getMessage());
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        database.createTableIfNotExists(APPOINTMENTS)
                .column(APPOINTMENTS.APPOINTMENTID, SQLDataType.BIGINT.identity(true))
                .column(APPOINTMENTS.NAME, SQLDataType.VARCHAR(30))
                .column(APPOINTMENTS.DATE, SQLDataType.LOCALDATETIME)
                .column(APPOINTMENTS.LOCATION, SQLDataType.VARCHAR(30))
                .primaryKey(APPOINTMENTS.APPOINTMENTID)
                .execute();



        if (!database.fetchExists(database.selectOne().from(APPOINTMENTS))) {
            database.insertInto(APPOINTMENTS, APPOINTMENTS.NAME, APPOINTMENTS.DATE, APPOINTMENTS.LOCATION)
                    .values("Test-Termin", LocalDateTime.now(), "Test-Location")
                    .execute();
        }

        database.createTableIfNotExists(USERS)
                .column(USERS.MATRIKELNR, SQLDataType.BIGINT.identity(true))
                .column(USERS.FIRSTNAME, SQLDataType.VARCHAR(30))
                .column(USERS.LASTNAME, SQLDataType.VARCHAR(30))
                .column(USERS.EMAIL, SQLDataType.VARCHAR(30))
                .primaryKey(USERS.MATRIKELNR)
                .execute();

        if (!database.fetchExists(database.selectOne().from(USERS))) {
        database.insertInto(USERS, USERS.FIRSTNAME, USERS.LASTNAME, USERS.EMAIL).values("MAX","MUSTERMANN","maxmustermann@muster.de").execute();
        }
    }
}
