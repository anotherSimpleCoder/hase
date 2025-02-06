package de.hase.hasev2.user;

import de.hase.hasev2.config.HikariService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static de.hase.hasev2.database.tables.Users.USERS;

@Service
    public class UserService {
        private final Logger logger;
        private DSLContext database;

        private final PasswordEncoder encoder;

    public UserService(@Autowired HikariService hikariService) {
        this.logger = LoggerFactory.getLogger(UserService.class);
        this.encoder = new Argon2PasswordEncoder(16, 21, 1, 60000, 10);

        try {
            database = DSL.using(hikariService.getDataSource().getConnection());
        } catch (SQLException e) {
            this.logger.error("Database error: " + e.getMessage());
        }
    }

        public List<User> findAllUsers() {
            return database.selectFrom(USERS)
                    .fetchInto(User.class);
        }

        public Optional<User> findUser(int matrikelNr) {
            return database.selectFrom(USERS)
                    .where(USERS.MATRIKELNR.eq(matrikelNr))
                    .fetchOptionalInto(User.class);
        }

        public Optional<User> findUserByEmail(String email) {
            return database.selectFrom(USERS)
                    .where(USERS.EMAIL.eq(email))
                    .fetchOptionalInto(User.class);
        }

        public Optional<User> saveUser(User user) {
            return database.insertInto(USERS, USERS.MATRIKELNR, USERS.FIRSTNAME,USERS.LASTNAME,USERS.EMAIL, USERS.PASSWORD)
                    .values(user.matrikelNr(), user.firstName(), user.lastName(), user.email(), encoder.encode(user.password())).onDuplicateKeyIgnore()
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

        public void deleteAllUsers() {
            database.deleteFrom(USERS)
                    .execute();
        }
    }

