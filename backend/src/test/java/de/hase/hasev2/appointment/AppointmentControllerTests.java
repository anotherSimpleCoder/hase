package de.hase.hasev2.appointment;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.utils.LocalDateTimeAdapter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static de.hase.hasev2.database.Tables.APPOINTMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTests {
    @Autowired
    private MockMvc http;

    private DSLContext dslContext;

    private JsonAdapter<Appointment> jsonAdapter = new Moshi
            .Builder()
            .add(new LocalDateTimeAdapter())
            .build()
            .adapter(Appointment.class);

    private final Appointment testAppointment = new Appointment(0, "Test appointment", LocalDateTime.of(2001, 9, 11, 12, 0, 0), "htw saar");

    public AppointmentControllerTests(@Autowired HikariService hikariService) throws Exception {
        dslContext = DSL.using(hikariService.getDataSource().getConnection());
    }

    @BeforeEach
    void clearDatabaseAfterEachTest(){
        dslContext.deleteFrom(APPOINTMENTS).execute();
    }

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

    @Test
    public void testPostingAppointmentAndUpdate_shouldBeOk() throws Exception{
        var postedAppointment =  jsonAdapter.fromJson(
                http.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAdapter.toJson(testAppointment))
                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );


        http.perform(put("/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAdapter.toJson(postedAppointment))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }


}
