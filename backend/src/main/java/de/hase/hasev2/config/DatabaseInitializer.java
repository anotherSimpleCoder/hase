package de.hase.hasev2.config;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

import de.hase.hasev2.appointment.AppointmentService;
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
    }
}
