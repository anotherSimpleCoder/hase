package de.hase.hasev2.appointment;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

@RestController
@RequestMapping("/appointment")
@CrossOrigin("http://localhost:8081")
public class AppointmentController {
    DSLContext context;

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAppointments() {
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
}

