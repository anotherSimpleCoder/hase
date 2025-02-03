package de.hase.hasev2.user;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc http;

    private JsonAdapter<User> jsonAdapter = new Moshi
            .Builder()
            .build()
            .adapter(User.class);

    private final User testUser = new User(0, "Test", "User", "test@mail.de", "test");

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
