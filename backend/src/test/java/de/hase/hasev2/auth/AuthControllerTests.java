package de.hase.hasev2.auth;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import de.hase.hasev2.auth.token.Token;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import de.hase.hasev2.utils.InstantAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AuthControllerTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;

    private final JsonAdapter<Login> jsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(Login.class);

    private final JsonAdapter<Token> tokenJsonAdapter = new Moshi
            .Builder()
            .add(new InstantAdapter())
            .build()
            .adapter(Token.class);

    private final JsonAdapter<User> userJsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(User.class);

    private final String testPassword = "test";

    private User testUser = new UserBuilder()
            .firstName("Test")
            .lastName("User")
            .email("test@mail.de")
            .password(testPassword)
            .build();

    @BeforeEach
    void setUp() throws Exception {
        testUser = userService.saveUser(testUser)
                .orElseThrow(() -> new RuntimeException("Test User could not be saved"));
    }

    @AfterEach
    void clearDatabaseAfterEachTest(){
        userService.deleteAllUsers();
    }

    @Test
    void testLogin_shouldBeOk() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testPassword)
                .build();

        this.http.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(login))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_shouldBeEqual() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testPassword)
                .build();

        var response = this.tokenJsonAdapter.fromJson(
                this.http.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(login))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(response);

        var claims = this.jwtDecoder.decode(response.token());
        assertEquals(claims.getClaim("sub"), testUser.email());
    }

    @Test
    void testLoginAndGetMe_shouldBeEqual() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testPassword)
                .build();

        var response = this.tokenJsonAdapter.fromJson(
                this.http.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(login))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(response);

        var authenticatedUser = this.userJsonAdapter.fromJson(
                this.http.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + response.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(authenticatedUser);
        assertEquals(testUser, authenticatedUser);
    }

    @Test
    void testLoginWithInvalidEmail_shouldThrow404() throws Exception {
        var login = new LoginBuilder()
                .email("invalid@mail.de")
                .password(testPassword)
                .build();

        this.http.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonAdapter.toJson(login))
            .characterEncoding("utf-8"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testLoginWithInvalidPassword_shouldThrow401() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password("invalidPassword")
                .build();

        this.http.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonAdapter.toJson(login))
            .characterEncoding("utf-8"))
            .andExpect(status().isUnauthorized());
    }
}
