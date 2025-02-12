package de.hase.hasev2.config;

import static de.hase.hasev2.database.Tables.PARTICIPATES;
import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;
import static de.hase.hasev2.database.tables.Users.USERS;
import static org.jooq.impl.DSL.*;

import de.hase.hasev2.appointment.AppointmentService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseInitializer implements ApplicationRunner {
    private Logger logger;
    private DSLContext database;

    public DatabaseInitializer(@Autowired Environment environment) {
        this.logger = LoggerFactory.getLogger(AppointmentService.class);
        this.checkForDatabaseDirectory();

        try {
            String dbUrl = environment.getProperty("spring.datasource.url");
            assert dbUrl != null;

            Connection connection = DriverManager.getConnection(dbUrl);
            database = DSL.using(connection);
        } catch (SQLException e) {
            this.logger.error("Database error: {}", e.getMessage());
        }
    }

    private void checkForDatabaseDirectory() {
        Path databaseDirectory = Paths.get("./database");

        try {
            if(!Files.exists(databaseDirectory)) {
                Files.createDirectory(databaseDirectory);
            }
        } catch (IOException e) {
            this.logger.error("IO Error: {}", e.getMessage());
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        database
                .query("pragma foreign_keys = on;")
                .execute();

        appointmentsTable();
        usersTable();
        participatesTable();
    }

    private void appointmentsTable() {
        database.createTableIfNotExists(APPOINTMENTS)
                .column(APPOINTMENTS.APPOINTMENTID, SQLDataType.BIGINT.identity(true))
                .column(APPOINTMENTS.NAME, SQLDataType.VARCHAR(30).notNull())
                .column(APPOINTMENTS.DATE, SQLDataType.LOCALDATETIME.notNull())
                .column(APPOINTMENTS.LOCATION, SQLDataType.VARCHAR(30))
                .primaryKey(APPOINTMENTS.APPOINTMENTID)
                .execute();

        this.logger.info("Created Appointment table");
    }

    private void usersTable() {
        database.createTableIfNotExists(USERS)
                .column(USERS.MATRIKELNR, SQLDataType.BIGINT)
                .column(USERS.FIRSTNAME, SQLDataType.VARCHAR(30).notNull())
                .column(USERS.LASTNAME, SQLDataType.VARCHAR(30).notNull())
                .column(USERS.EMAIL, SQLDataType.VARCHAR(30).notNull())
                .column(USERS.PASSWORD, SQLDataType.VARCHAR(50).notNull())
                .unique(USERS.EMAIL)
                .primaryKey(USERS.MATRIKELNR)
                .constraint(
                        check(USERS.EMAIL.like("%@%.%"))
                )
                .execute();

        this.logger.info("Created Users table");
    }

    private void participatesTable() {
        database.createTableIfNotExists(PARTICIPATES)
                .column(PARTICIPATES.APPOINTMENTID, SQLDataType.BIGINT.notNull())
                .column(PARTICIPATES.MATRIKELNR, SQLDataType.BIGINT.notNull())
                .primaryKey(PARTICIPATES.APPOINTMENTID, PARTICIPATES.MATRIKELNR)
                .constraints(
                        foreignKey(PARTICIPATES.APPOINTMENTID).references(APPOINTMENTS, APPOINTMENTS.APPOINTMENTID),
                        foreignKey(PARTICIPATES.MATRIKELNR).references(USERS, USERS.MATRIKELNR)
                )
                .execute();

        this.logger.info("Created Participates table");
    }
}
