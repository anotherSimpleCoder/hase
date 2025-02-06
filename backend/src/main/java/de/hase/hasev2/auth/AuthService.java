package de.hase.hasev2.auth;

import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.user.UserService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AuthService {
    private final  UserService userService;
    private final Logger logger;
    private DSLContext database;
    private final PasswordEncoder encoder;

    public AuthService(@Autowired HikariService hikariService, @Autowired UserService userService) {
        this.logger = LoggerFactory.getLogger(UserService.class);
        this.encoder = new Argon2PasswordEncoder(16, 21, 1, 60000, 10);
        this.userService = userService;

        try {
            database = DSL.using(hikariService.getDataSource().getConnection());
        } catch (SQLException e) {
            this.logger.error("Database error: {}", e.getMessage());
        }
    }

    public boolean login(String email, String password) throws Exception {
        //Get user of given email
        var gottenUser = this.userService.findUserByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        if(!encoder.matches(password, gottenUser.password())) {
            throw new Exception("Wrong password");
        }

        return true;
    }
}
