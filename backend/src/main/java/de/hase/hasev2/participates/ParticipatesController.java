package de.hase.hasev2.participates;

import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ParticipatesController {
    @Autowired
    private ParticipatesService participatesService;

    @GetMapping("/user-appointment")
    public ResponseEntity<List<Appointment>> getAppointmentsForUser(@RequestParam("matrikelNr") int matrikelNr){
        return ResponseEntity.ok(
                participatesService.getAppointmentsForUser(matrikelNr)
        );
    }

    @GetMapping("/appointment-user")
    public ResponseEntity<List<User>> getUsersFromAppointment(@RequestParam("appointmentId") int appointmentId) {
        return ResponseEntity.ok(
                participatesService.getUsersForAppointment(appointmentId)
        );
    }

    @PostMapping("/user-appointment")
    public ResponseEntity<Boolean> addUsersToAppointments(@RequestBody Map<Integer, Integer> appointmentsToUsersMap){
        try {
            participatesService.addUsersToAppointments(appointmentsToUsersMap);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user-appointment")
    public ResponseEntity<Boolean> removeUsersFromAppointments(@RequestBody Map<Integer, Integer> appointmentsToUsersMap){
        try {
            participatesService.removeUsersFromAppointments(appointmentsToUsersMap);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
