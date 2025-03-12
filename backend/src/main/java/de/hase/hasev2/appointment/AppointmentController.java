package de.hase.hasev2.appointment;

import de.hase.hasev2.appointment.exceptions.AppointmentNotFoundException;
import de.hase.hasev2.appointment.exceptions.NotAppointmentCreatorException;
import de.hase.hasev2.auth.AuthService;
import de.hase.hasev2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointment")
@CrossOrigin
public class AppointmentController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AppointmentService appointmentService;

    private User getCreator(Authentication authentication) throws ResponseStatusException {
        return authService.getMe(authentication)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAppointments() {
        return ResponseEntity.ok(appointmentService.findAllAppointments());

    }

    @PostMapping()
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment, Authentication authentication) {
        return ResponseEntity.ok(
                appointmentService.saveAppointment(appointment, getCreator(authentication))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @GetMapping()
    public ResponseEntity<Appointment> getAppointment(@RequestParam("appointmentId") int appointmentId) {
        return ResponseEntity.ok(
                appointmentService.findAppointment(appointmentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @DeleteMapping()
    public ResponseEntity<Appointment> deleteAppointment(@RequestParam("appointmentId") int appointmentId, Authentication authentication){
        try {
            return ResponseEntity.ok(
                    appointmentService.deleteAppointment(appointmentId, getCreator(authentication))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            );
        } catch (AppointmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch(NotAppointmentCreatorException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment updatedAppointment, Authentication authentication){
        try {
            return ResponseEntity.ok(appointmentService.updateAppointment(updatedAppointment, getCreator(authentication))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Appointment not found"))
            );
        } catch (AppointmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch(NotAppointmentCreatorException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}