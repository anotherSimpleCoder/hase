package de.hase.hasev2.user;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.hase.hasev2.appointment.AppointmentService;
import de.hase.hasev2.config.HikariService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static de.hase.hasev2.database.Tables.APPOINTMENTS;
import static de.hase.hasev2.database.Tables.USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private UserController userController;
    private DSLContext dslContext;
    private JsonAdapter<User> jsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(User.class);

    private final User testUser = new User(0, "Test", "User", "test@mail.de", "test");

    public UserControllerTests(@Autowired HikariService hikariService) throws Exception {
        dslContext = DSL.using(hikariService.getDataSource().getConnection());
    }
    @BeforeEach
    void clearDatabaseAfterEachTest(){
        dslContext.deleteFrom(USERS).execute();
    }

    @Test
    void testPostingUser_shouldBeOk() throws Exception {
        http.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(testUser))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostingAndDeletingUser_shouldBeEqual() throws Exception {
        var postedUser = jsonAdapter.fromJson(
                http.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testUser))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        var deletedUser = jsonAdapter.fromJson(
                http.perform(delete("/users")
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
                http.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testUser))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        http.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(postedUser))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }
}
