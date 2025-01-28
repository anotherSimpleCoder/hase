package de.hase.hasev2.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@CrossOrigin
public class AppointmentController {


    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAppointments() {
        System.out.println(appointmentService.findAllAppointments());
        return ResponseEntity.ok(appointmentService.findAllAppointments());

    }


    @PostMapping()
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment){
        return ResponseEntity.ok(
                appointmentService.saveAppointment(appointment)
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
    public ResponseEntity<Appointment> deleteAppointment(@RequestParam("appointmentId") int appointmentId){
        return ResponseEntity.ok(
                appointmentService.deleteAppointment(appointmentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @PutMapping()
    public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment updatedAppointment){
        return ResponseEntity.ok(appointmentService.updateAppointment(updatedAppointment)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Appointment not found"))
        );
    }


}

