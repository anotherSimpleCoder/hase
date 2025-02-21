package de.hase.hasev2.appointment;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.hase.hasev2.auth.AuthService;
import de.hase.hasev2.auth.LoginBuilder;
import de.hase.hasev2.auth.token.Token;
import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import de.hase.hasev2.utils.LocalDateTimeAdapter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static de.hase.hasev2.database.Tables.APPOINTMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AppointmentControllerTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private Token token = null;

    private final JsonAdapter<Appointment> jsonAdapter = new Moshi
            .Builder()
            .add(new LocalDateTimeAdapter())
            .build()
            .adapter(Appointment.class);

    private final Appointment testAppointment = new Appointment(0, "Test appointment", LocalDateTime.of(2001, 9, 11, 12, 0, 0), "htw saar");

    @BeforeEach
    void setup() throws Exception {
        var testUser = new UserBuilder()
                .firstName("Test")
                .lastName("User")
                .email("test@test.com")
                .password("password")
                .build();

        userService.saveUser(testUser);
        token = authService.login(new LoginBuilder()
                .email(testUser.email())
                .password(testUser.password()).build());
    }

    @AfterEach
    void tearDown(){
        userService.deleteAllUsers();
        appointmentService.deleteAllAppointments();
    }

    @Test
    void testPostingAppointment_shouldBeOk() throws Exception {
        http.perform(post("/appointment")
                .header("Authorization", String.format("Bearer %s", token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(testAppointment))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostingAndDeletingAppointment_shouldBeEqual() throws Exception {
        var postedAppointment = jsonAdapter.fromJson(
                http.perform(post("/appointment")
                        .header("Authorization", String.format("Bearer %s", token.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testAppointment))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        var gottenAppointment = jsonAdapter.fromJson(
                http.perform(get("/appointment")
                        .header("Authorization", String.format("Bearer %s", token.token()))
                        .param("appointmentId", String.valueOf(postedAppointment.appointmentId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertEquals(postedAppointment, gottenAppointment);

        var deletedAppointment = jsonAdapter.fromJson(
                http.perform(delete("/appointment")
                        .header("Authorization", String.format("Bearer %s", token.token()))
                        .param("appointmentId", String.valueOf(postedAppointment.appointmentId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertEquals(gottenAppointment, deletedAppointment);
    }

    @Test
    public void testPostingAppointmentAndUpdate_shouldBeOk() throws Exception{
        var postedAppointment =  jsonAdapter.fromJson(
                http.perform(post("/appointment")
                        .header("Authorization", String.format("Bearer %s", token.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testAppointment))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );


        http.perform(put("/appointment")
                .header("Authorization", String.format("Bearer %s", token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(postedAppointment))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }


}
