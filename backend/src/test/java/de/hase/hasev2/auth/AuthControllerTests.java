package de.hase.hasev2.auth;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private UserService userService;

    private final JsonAdapter<Login> jsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(Login.class);

    private final User testUser = new UserBuilder()
            .firstName("Test")
            .lastName("User")
            .email("test@mail.de")
            .password("test")
            .build();

    @BeforeEach
    void setUp() throws Exception {
        userService.saveUser(testUser);
    }

    @AfterEach
    void clearDatabaseAfterEachTest(){
        userService.deleteAllUsers();
    }

    @Test
    void login_shouldBeOk() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testUser.password())
                .build();

        this.http.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(login))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldReturnTrue() throws Exception {
        var login = new LoginBuilder()
                .email(testUser.email())
                .password(testUser.password())
                .build();

        var response = Boolean.parseBoolean(this.http.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(login))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString());

        assertTrue(response);
    }
}
