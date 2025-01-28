package de.hase.hasev2.user;

import de.hase.hasev2.appointment.AppointmentService;
import de.hase.hasev2.config.HikariService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static de.hase.hasev2.database.tables.Users.USERS;

@Service
    public class UserService {
        private Logger logger;
        private DSLContext database;

        private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);

    public UserService(@Autowired HikariService hikariService) {
        this.logger = LoggerFactory.getLogger(UserService.class);

        try {
            database = DSL.using(hikariService.getDataSource().getConnection());
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
            User newUser = new User(user.matrikelNr(), user.firstName(), user.lastName(), user.email(), encoder.encode(user.password()));

            return database.insertInto(USERS, USERS.FIRSTNAME,USERS.LASTNAME,USERS.EMAIL, USERS.PASSWORD)
                    .values( user.firstName(), user.lastName(), user.email(), newUser.password()).onDuplicateKeyIgnore()
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

