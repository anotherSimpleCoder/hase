package de.hase.hasev2.appointment;

import de.hase.hasev2.config.HikariService;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserBuilder;
import de.hase.hasev2.user.UserService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static de.hase.hasev2.database.Tables.APPOINTMENTS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class AppointmentServiceTests {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    private User testUser;
    private final Appointment testAppointment;


    public AppointmentServiceTests() throws Exception {
            this.testUser = new UserBuilder()
                    .matrikelNr(110117)
                    .email("test@user.de")
                    .firstName("Test")
                    .lastName("User")
                    .password("test")
                    .build();

            this.testAppointment = new AppointmentBuilder()
                    .name("Test appointment")
                    .date(LocalDateTime.of(2001, 9, 11, 12, 0, 0))
                    .location("htw saar")
                    .build();
    }
    @BeforeEach
    void clearDatabaseAfterEachTest() throws Exception {
        this.userService.deleteAllUsers();
        this.appointmentService.deleteAllAppointments();

        this.testUser = this.userService.saveUser(testUser)
                .orElseThrow(() -> new Exception("Test user could not be created!"));
    }

    @Test
    void saveAppointment_shouldBeOk() throws Exception {
        this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Appointment could not be saved."));
    }

    @Test
    void saveAppointment_shouldHaveEqualCreator() throws Exception {
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Appointment could not be saved."));

        assertEquals(savedAppointment.creator(), testUser.matrikelNr());
    }

    @Test
    void saveAppointment_shouldBeEqual() throws Exception{
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        assertEquals(savedAppointment.creator(), testUser.matrikelNr());

        var gottenAppointment = this.appointmentService.findAppointment(savedAppointment.appointmentId())
                .orElseThrow(() -> new Exception("Test Appointment could not be found."));

        assertEquals(savedAppointment, gottenAppointment);
    }

    @Test
    void deleteAppointment_shouldBeOk() throws Exception {
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        this.appointmentService.deleteAppointment(savedAppointment.appointmentId(), testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be deleted."));
    }

    @Test
    void deleteAppointment_shouldHaveEqualCreator() throws Exception {
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var deletedAppointment = this.appointmentService.deleteAppointment(savedAppointment.appointmentId(), testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be deleted."));

        assertEquals(deletedAppointment.creator(), testUser.matrikelNr());
    }

    @Test
    void deleteAppointment_shouldBeEqual() throws Exception{
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var deletedAppointment = this.appointmentService.deleteAppointment(savedAppointment.appointmentId(), testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be deleted."));

        assertEquals(deletedAppointment.creator(), testUser.matrikelNr());
        assertEquals(deletedAppointment, savedAppointment);

        assertThrows(Exception.class, () -> {
            this.appointmentService.findAppointment(savedAppointment.appointmentId())
                    .orElseThrow(() -> new Exception("Test Appointment could not be found."));
        });
    }

    @Test
    void updateAppointment_shouldBeOk() throws Exception {
        var postedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var newUpdated = new AppointmentBuilder()
                .from(postedAppointment)
                .name("New updated appointment")
                .build();

        this.appointmentService.updateAppointment(newUpdated, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be updated."));
    }

    @Test
    void updateAppointment_shouldHaveEqualCreator() throws Exception {
        var postedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var newUpdated = new AppointmentBuilder()
                .from(postedAppointment)
                .name("New updated appointment")
                .build();

        var updatedAppointment = this.appointmentService.updateAppointment(newUpdated, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be updated."));

        assertEquals(updatedAppointment.creator(), testUser.matrikelNr());
    }

    @Test
    void updateAppointment_shouldBeNotEqual() throws Exception{
        var postedAppointment = this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var newUpdated = new AppointmentBuilder()
                .from(postedAppointment)
                .name("New updated appointment")
                .build();

        var updatedAppointment = this.appointmentService.updateAppointment(newUpdated, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be updated."));

        assertNotEquals(postedAppointment ,updatedAppointment);
    }

    @Test
    void saveInvalidAppointment_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.appointmentService.saveAppointment(null, null);
        });
    }

    @Test
    void getInvalidAppointment_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.appointmentService.findAppointment(-4)
                    .orElseThrow(() -> new Exception("Test Appointment could not be found."));
        });
    }

    @Test
    void deleteInvalidAppointment_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.appointmentService.deleteAppointment(-4, null)
                    .orElseThrow(() -> new Exception("Test Appointment could not be found."));
        });
    }

    @Test
    void updateInvalidAppointment_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.appointmentService.updateAppointment(null, null);
        });
    }

    @Test
    void createAppointmentAndDeleteAsNonCreator_shouldThrowException() throws Exception {
        var nonCreatorUser = userService.saveUser(
                new UserBuilder()
                        .matrikelNr(42690)
                        .email("other@user.com")
                        .firstName("Other")
                        .lastName("User")
                        .password("password")
                        .build()
        ).orElseThrow(() -> new Exception("Noncreator could not be created."));

        this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        assertThrows(Exception.class, () -> {
            this.appointmentService.deleteAppointment(testAppointment.appointmentId(), nonCreatorUser)
                    .orElseThrow(() -> new Exception("Test Appointment could not be deleted."));
        });
    }

    @Test
    void createAppointmentAndUpdateAsNonCreator_shouldThrowException() throws Exception {
        var nonCreatorUser = userService.saveUser(
                new UserBuilder()
                        .matrikelNr(42690)
                        .email("other@user.com")
                        .firstName("Other")
                        .lastName("User")
                        .password("password")
                        .build()
        ).orElseThrow(() -> new Exception("Noncreator could not be created."));

        this.appointmentService.saveAppointment(testAppointment, testUser)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var updatedAppointment = new AppointmentBuilder()
                .from(testAppointment)
                .name("New updated appointment")
                .build();

        assertThrows(Exception.class, () -> {
            this.appointmentService.updateAppointment(updatedAppointment, nonCreatorUser)
                    .orElseThrow(() -> new Exception("Test Appointment could not be updated."));
        });
    }
}
