package de.hase.hasev2.participates;

import de.hase.hasev2.appointment.Appointment;
import de.hase.hasev2.database.tables.Appointments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RequestMapping("/user-appointment")
@RestController
@CrossOrigin
public class ParticipatesController {
    @Autowired
    private participatesService participatesService;

    @GetMapping("")
    public ResponseEntity<List<Appointment>> getAppointmentForUsers(@RequestParam("user") int matrikelNr){
        return ResponseEntity.ok(
                participatesService.getAppointmentsForUser(matrikelNr)
        );
    }

    @PostMapping("")
    public ResponseEntity<Boolean> addUsersToAppointments(@RequestBody Map<Integer, Integer> appointmentsToUsersMap){
        participatesService.addUsersToAppointments(appointmentsToUsersMap);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> removeUsersFromAppointments(@RequestBody Map<Integer, Integer> appointmentsToUsersMap){
        participatesService.removeUsersFromAppointments(appointmentsToUsersMap);
        return ResponseEntity.ok(true);
    }
}
