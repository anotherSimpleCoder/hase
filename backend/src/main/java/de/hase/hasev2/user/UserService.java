package de.hase.hasev2.user;

import database.tables.Users;
import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.appointment.AppointmentService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static database.tables.Appointments.APPOINTMENTS;
import static database.tables.Users.USERS;

    @Service
    public class UserService {
        private Logger logger;
        private DSLContext database;

        public UserService() {
            this.logger = LoggerFactory.getLogger(de.hase.hasev2.user.UserService.class);

            try {
                String dbPath = new File("./database/database.sqlite").getAbsolutePath();
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                database = DSL.using(connection);
            } catch (SQLException e) {
                this.logger.error(e.getMessage());
            }
        }

        public List<User> findAllUsers() {
            return database.selectFrom(USERS)
                    .fetchInto(User.class);
        }

        public Optional<User> findUsers(int MatrikelNr) {
            return database.selectFrom(USERS)
                    .where(USERS.MATRIKELNR.eq(MatrikelNr))
                    .fetchOptionalInto(User.class);
        }

        public Optional<User> saveUsers(User user) {
            return database.insertInto(USERS, USERS.MATRIKELNR,USERS.FIRSTNAME,USERS.LASTNAME,USERS.LASTNAME)
                    .values(user.matrikelNr(), user.firstName(), user.lastName(), user.email())
                    .returningResult()
                    .fetchOptionalInto(User.class);
        }

        public Optional<User> deleteUser(int matrikelNr) {
            return database.deleteFrom(USERS)
                    .where(USERS.MATRIKELNR.eq(matrikelNr))
                    .returningResult()
                    .fetchOptionalInto(User.class);
        }

        public Optional<User> updateUser(User updatedUser){
            return database.update(USERS)
                    .set(USERS.EMAIL, updatedUser.email())
                    .where(USERS.MATRIKELNR.eq(updatedUser.matrikelNr()))
                    .returningResult()
                    .fetchOptionalInto(User.class);
        }

    }

