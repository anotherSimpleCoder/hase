package de.hase.hasev2.auth;

import de.hase.hasev2.auth.exceptions.EmailNotFoundException;
import de.hase.hasev2.auth.exceptions.InvalidPasswordException;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class AuthServiceTests {
    private final UserService userService;

    private final AuthService authService;

    private final JwtDecoder jwtDecoder;

    private final String testPassword = "password";
    
    private User testUser;

    public AuthServiceTests(@Autowired UserService userService, @Autowired AuthService authService, @Autowired JwtDecoder jwtDecoder) throws Exception{
        this.testUser = new UserBuilder()
                .matrikelNr(5011657)
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .password(testPassword)
                .build();

        this.userService = userService;
        this.authService = authService;
        this.jwtDecoder = jwtDecoder;
    }

    @BeforeEach
    void setUp() throws Exception {
        testUser = userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("Could not save test user"));
    }

    @AfterEach
    void removeTestUser() {
        userService.deleteAllUsers();
    }

    @Test
    void testLoginShouldBeOk() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testPassword)
                .build();

        assertNotNull(this.authService.login(login));
    }

    @Test
    void testLoginshouldBeEqual() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testPassword)
                .build();

        var token = this.authService.login(login);
        var jwt = this.jwtDecoder.decode(token.token());

        assertEquals(jwt.getClaim("sub"), testUser.email());
    }

    @Test
    void testLoginAndGetMe_shouldBeEqual() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testPassword)
                .build();

        var token = this.authService.login(login);
        var jwt = this.jwtDecoder.decode(token.token());
        var authentication = new JwtAuthenticationToken(jwt);

        var authenticatedUser = this.authService.getMe(authentication)
                .orElseThrow(() -> new EmailNotFoundException(testUser.email()));


        assertEquals(authenticatedUser, testUser);
    }

    @Test
    void testLoginWithWrongPassword_shouldThrowInvalidPasswordException() {
        var login = new LoginBuilder()
                .email(testUser.email())
                .build();

        assertThrows(InvalidPasswordException.class, () -> {
            this.authService.login(login);
        });
    }

    @Test
    void testLoginWithWrongEmail_shouldThrowEmailNotFoundException() {
        var login = new LoginBuilder()
                .password(testPassword)
                .build();

        assertThrows(EmailNotFoundException.class, () -> {
            this.authService.login(login);
        });
    }

    @Test
    void testLoginWithWrongEmailAndPassword_shouldThrowEmailNotFoundException() {
        var login = new LoginBuilder().build();

        assertThrows(EmailNotFoundException.class, () -> {
            this.authService.login(login);
        });
    }
}
