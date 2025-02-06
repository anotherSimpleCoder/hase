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
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

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
        userService.deleteUser(testUser.matrikelNr());
    }

    @Test
    void loginShouldBeOk() throws Exception {
        assertTrue(this.authService.login(testUser.email(), testUser.password()));
    }

    @Test
    void loginWithWrongPassword_shouldThrowException() {
        assertThrows(Exception.class, () -> {
            this.authService.login(testUser.email(), "");
        });
    }

    @Test
    void loginWithWrongEmail_shouldThrowException() {
        assertThrows(Exception.class, () -> {
            this.authService.login("", testUser.password());
        });
    }

    @Test
    void loginWithWrongEmailAndPassword_shouldThrowException() {
        assertThrows(Exception.class, () -> {
            this.authService.login("", "");
        });
    }
}
