package de.hase.hasev2.participates;

import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.appointment.AppointmentBuilder;
import de.hase.hasev2.appointment.AppointmentService;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class ParticipatesServiceTests {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipatesService participatesService;

    private User testUser;
    private Appointment testAppointment;

    public ParticipatesServiceTests() {
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
        this.testAppointment = this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Could not save appointment"));

        this.testUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("Could not save user"));
    }

    @AfterEach
    void cleanup() {
        this.appointmentService.deleteAllAppointments();
        this.userService.deleteAllUsers();
        this.participatesService.deleteAllMappings();
    }

    @Test
    void testMapUserToAppointment_shouldBeOk() throws Exception {
        this.participatesService.addUsersToAppointments(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr()));
    }

    @Test
    void testMapUserToAppointment_shouldBeIncluded() throws Exception {
        this.participatesService.addUsersToAppointments(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr()));

        var userAppointments = this.participatesService.getAppointmentsForUser(this.testUser.matrikelNr());
        assertTrue(userAppointments.contains(this.testAppointment));

        var appointmentUsers = this.participatesService.getUsersForAppointment(this.testAppointment.appointmentId());
        assertTrue(appointmentUsers.contains(this.testUser));
    }

    @Test
    void testUnmapUserToAppointment_shouldBeOk() throws Exception {
        this.participatesService.addUsersToAppointments(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr()));

        this.participatesService.removeUsersFromAppointments(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr()));
    }

    @Test
    void testUnmapUserToAppointment_shouldBeEmpty() throws Exception {
        this.participatesService.addUsersToAppointments(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr()));

        this.participatesService.removeUsersFromAppointments(Map.of(this.testAppointment.appointmentId(), this.testUser.matrikelNr()));

        var userAppointments = this.participatesService.getAppointmentsForUser(this.testUser.matrikelNr());
        assertFalse(userAppointments.contains(this.testAppointment));

        var appointmentUsers = this.participatesService.getUsersForAppointment(this.testAppointment.appointmentId());
        assertFalse(appointmentUsers.contains(this.testUser));
    }

    @Test
    void testMapInvalidUserToInvalidAppointment_shouldThrowException() {
        assertThrows(Exception.class, () -> this.participatesService.addUsersToAppointments(Map.of(-1, -3)));
    }

    @Test
    void testMapUserToInvalidAppointment_shouldThrowException() {
        assertThrows(Exception.class, () -> this.participatesService.addUsersToAppointments(Map.of(-1, this.testUser.matrikelNr())));
    }

    @Test
    void testMapAppointmentToInvalidAppointment_shouldThrowException() {
        assertThrows(Exception.class, () -> this.participatesService.addUsersToAppointments(Map.of(this.testAppointment.appointmentId(), -1)));
    }
}
