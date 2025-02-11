package de.hase.hasev2.auth;

import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthServiceTests {
    private final UserService userService;

    private final AuthService authService;

    private final User testUser;

    public AuthServiceTests(@Autowired UserService userService, @Autowired AuthService authService, @Autowired HikariService hikariService) throws Exception{
        this.testUser = new UserBuilder()
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .password("password")
                .build();

        this.userService = userService;
        this.authService = authService;
    }

    @BeforeEach
    void setUp() {
        userService.saveUser(testUser);
    }

    @AfterEach
    void removeTestUser() {
        userService.deleteAllUsers();
    }

    @Test
    void loginShouldBeOk() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testUser.password())
                .build();

        assertTrue(this.authService.login(login));
    }

    @Test
    void loginWithWrongPassword_shouldThrowException() {
        var login = new LoginBuilder()
                .email(testUser.email())
                .build();

        assertThrows(Exception.class, () -> {
            this.authService.login(login);
        });
    }

    @Test
    void loginWithWrongEmail_shouldThrowException() {
        var login = new LoginBuilder()
                .password(testUser.password())
                .build();

        assertThrows(Exception.class, () -> {
            this.authService.login(login);
        });
    }

    @Test
    void loginWithWrongEmailAndPassword_shouldThrowException() {
        var login = new LoginBuilder().build();

        assertThrows(Exception.class, () -> {
            this.authService.login(login);
        });
    }
}
