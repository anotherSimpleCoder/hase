package de.hase.hasev2;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.utils.LocalDateTimeAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentTests {
    @Autowired
    private MockMvc http;

    private JsonAdapter<Appointment> jsonAdapter = new Moshi
            .Builder()
            .add(new LocalDateTimeAdapter())
            .build()
            .adapter(Appointment.class);

    private final Appointment testAppointment = new Appointment(0, "Test appointment", LocalDateTime.of(2001, 9, 11, 12, 0, 0), "htw saar");

    @Test
    void testPostingAppointment_shouldBeOk() throws Exception {
        http.perform(post("/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(testAppointment))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostingAndDeletingAppointment_shouldBeEqual() throws Exception {
        var postedAppointment = jsonAdapter.fromJson(
                http.perform(post("/appointment")
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

}
