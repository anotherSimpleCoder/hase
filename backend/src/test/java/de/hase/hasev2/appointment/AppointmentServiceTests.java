package de.hase.hasev2.appointment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AppointmentServiceTests {
    @Autowired
    private AppointmentService appointmentService;

    private final Appointment testAppointment = new Appointment(0, "Test appointment", LocalDateTime.of(2001, 9, 11, 12, 0, 0), "htw saar");

    @Test
    void saveAppointment_shouldBeOk() throws Exception {
        this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Appointment could not be saved."));
    }

    @Test
    void saveAppointment_shouldBeEqual() throws Exception{
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var gottenAppointment = this.appointmentService.findAppointment(savedAppointment.appointmentId())
                .orElseThrow(() -> new Exception("Test Appointment could not be found."));

        assertEquals(savedAppointment, gottenAppointment);
    }

    @Test
    void deleteAppointment_shouldBeOk() throws Exception {
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        this.appointmentService.deleteAppointment(savedAppointment.appointmentId())
                .orElseThrow(() -> new Exception("Test Appointment could not be deleted."));
    }

    @Test
    void deleteAppointment_shouldBeEqual() throws Exception{
        var savedAppointment = this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var deletedAppointment = this.appointmentService.deleteAppointment(savedAppointment.appointmentId())
                .orElseThrow(() -> new Exception("Test Appointment could not be deleted."));

        assertEquals(deletedAppointment, savedAppointment);

        assertThrows(Exception.class, () -> {
            this.appointmentService.findAppointment(savedAppointment.appointmentId())
                    .orElseThrow(() -> new Exception("Test Appointment could not be found."));
        });
    }

    @Test
    void updateAppointment_shouldBeOk() throws Exception {
        var postedAppointment = this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var newUpdated = new Appointment(postedAppointment.appointmentId(), "New updated appointment", LocalDateTime.of(2001, 9, 11, 12, 0, 0), "htw saar");

        this.appointmentService.updateAppointment(newUpdated)
                .orElseThrow(() -> new Exception("Test Appointment could not be updated."));
    }

    @Test
    void updateAppointment_shouldBeNotEqual() throws Exception{
        var postedAppointment = this.appointmentService.saveAppointment(testAppointment)
                .orElseThrow(() -> new Exception("Test Appointment could not be saved."));

        var newUpdated = new Appointment(postedAppointment.appointmentId(), "New updated appointment", LocalDateTime.of(2001, 9, 11, 12, 0, 0), "htw saar");

        var updatedAppointment = this.appointmentService.updateAppointment(newUpdated)
                .orElseThrow(() -> new Exception("Test Appointment could not be updated."));

        assertNotEquals(postedAppointment ,updatedAppointment);
    }

    @Test
    void saveInvalidAppointment_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.appointmentService.saveAppointment(null);
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
            this.appointmentService.deleteAppointment(-4)
                    .orElseThrow(() -> new Exception("Test Appointment could not be found."));
        });
    }

    @Test
    void updateInvalidAppointment_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> {
            this.appointmentService.updateAppointment(null);
        });
    }
}
