package de.hase.hasev2.participates;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.appointment.AppointmentBuilder;
import de.hase.hasev2.appointment.AppointmentService;
import de.hase.hasev2.auth.AuthService;
import de.hase.hasev2.auth.LoginBuilder;
import de.hase.hasev2.auth.token.Token;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import de.hase.hasev2.utils.LocalDateTimeAdapter;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ParticipatesControllerTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private AuthService authService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipatesService participatesService;

    private Token token;

    private final JsonAdapter<Map<Integer, Integer>> json;
    private final JsonAdapter<List<Appointment>> appointmentListJson;
    private final JsonAdapter<List<User>> userListJson;

    private User testUser;
    private Appointment testAppointment;

    public ParticipatesControllerTests() {
        this.json = new Moshi
                .Builder()
                .build()
                .adapter(Types.newParameterizedType(Map.class, Integer.class, Integer.class));

        this.appointmentListJson = new Moshi
                .Builder()
                .add(new LocalDateTimeAdapter())
                .build()
                .adapter(Types.newParameterizedType(List.class, Appointment.class));

        this.userListJson = new Moshi
                .Builder()
                .build()
                .adapter(Types.newParameterizedType(List.class, User.class));

        this.testUser = new UserBuilder()
                .firstName("Test")
                .lastName("User")
                .email("test@mail.de")
                .password("test")
                .build();

        this.testAppointment = new AppointmentBuilder()
                .name("Test appointment")
                .date(LocalDateTime.of(2001, 9, 11, 12, 0, 0))
                .location("htw saar")
                .build();
    }

    @BeforeEach
    void setup() throws Exception {
        this.testUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("Could not save user"));


        this.testAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Could not save appointment"));

        this.token = this.authService.login(new LoginBuilder().email(testUser.email()).password("test").build());
    }

    @AfterEach
    void cleanup() {
        this.appointmentService.deleteAllAppointments();
        this.userService.deleteAllUsers();
        this.participatesService.deleteAllMappings();
    }

    @Test
    void testPostUserToAppointmentMapping_shouldBeOk() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostUserToAppointmentMapping_shouldBeIncluded() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        var userAppointments = appointmentListJson.fromJson(
                http.perform(get("/user-appointment")
                        .header("Authorization", String.format("Bearer %s", this.token.token()))
                        .param("matrikelNr", String.valueOf(this.testUser.matrikelNr()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(userAppointments);
        assertTrue(userAppointments.contains(this.testAppointment));

        var appointmentUsers = userListJson.fromJson(
                http.perform(get("/appointment-user")
                        .header("Authorization", String.format("Bearer %s", this.token.token()))
                        .param("appointmentId", String.valueOf(this.testAppointment.appointmentId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(appointmentUsers);
        assertTrue(appointmentUsers.contains(this.testUser));
    }

    @Test
    void testDeleteUserToAppointmentMapping_shouldBeOk() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        http.perform(delete("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserToAppointmentMapping_shouldBeNotIncluded() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        http.perform(delete("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        var userAppointments = appointmentListJson.fromJson(
                http.perform(get("/user-appointment")
                        .header("Authorization", String.format("Bearer %s", this.token.token()))
                        .param("matrikelNr", String.valueOf(this.testUser.matrikelNr()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(userAppointments);
        assertFalse(userAppointments.contains(this.testAppointment));

        var appointmentUsers = userListJson.fromJson(
                http.perform(get("/appointment-user")
                        .header("Authorization", String.format("Bearer %s", this.token.token()))
                        .param("appointmentId", String.valueOf(this.testAppointment.appointmentId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );

        assertNotNull(appointmentUsers);
        assertFalse(appointmentUsers.contains(this.testUser));
    }

    @Test
    void testPostInvalidUserToInvalidAppointmentMapping_shouldBe400() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(-1, -1)))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostUserToInvalidAppointmentMapping_shouldBe400() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(-1, this.testUser.matrikelNr())))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostInvalidUserToAppointmentMapping_shouldBe400() throws Exception {
        http.perform(post("/user-appointment")
                .header("Authorization", String.format("Bearer %s", this.token.token()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(Map.of(this.testAppointment.appointmentId(), -1)))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
}
