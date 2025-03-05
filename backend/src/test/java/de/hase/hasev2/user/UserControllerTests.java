package de.hase.hasev2.user;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.hase.hasev2.auth.AuthService;
import de.hase.hasev2.auth.Login;
import de.hase.hasev2.auth.LoginBuilder;
import de.hase.hasev2.auth.token.Token;
import de.hase.hasev2.utils.InstantAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserControllerTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private final JsonAdapter<User> jsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(User.class);

    private final JsonAdapter<Token> tokenJsonAdapter = new Moshi
            .Builder()
            .add(new InstantAdapter())
            .build()
            .adapter(Token.class);

    private final JsonAdapter<Login> loginJsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(Login.class);

    private final UserBuilder testUserBuilder = new UserBuilder()
            .firstName("Test")
            .lastName("User")
            .email("test@mail.de")
            .password("test");


    @BeforeEach
    void clearDatabaseAfterEachTest(){
        userService.deleteAllUsers();
    }

    @Test
    void testPostingUser_shouldBeOk() throws Exception {
        http.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(testUserBuilder.build()))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostingAndDeletingUser_shouldBeEqual() throws Exception {
        var postedUser = jsonAdapter.fromJson(
                http.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testUserBuilder.build()))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        var token = this.tokenJsonAdapter.fromJson(
                this.http.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJsonAdapter.toJson(new LoginBuilder().email("test@mail.de").password("test").build()))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).token();

        var deletedUser = jsonAdapter.fromJson(
                http.perform(delete("/users")
                        .header("Authorization", String.format("Bearer %s", token))
                        .param("matrikelNr", String.valueOf(postedUser.matrikelNr()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertEquals(postedUser, deletedUser);
    }

    @Test
    void testPostingUserAndUpdatingUser_shouldBeOk() throws Exception {
        var postedUser = jsonAdapter.fromJson(
                http.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testUserBuilder.build()))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        var token = this.tokenJsonAdapter.fromJson(
                this.http.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJsonAdapter.toJson(new LoginBuilder().email("test@mail.de").password("test").build()))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).token();

        http.perform(put("/users")
                .header("Authorization", String.format("Bearer %s", token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(postedUser))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }
}
