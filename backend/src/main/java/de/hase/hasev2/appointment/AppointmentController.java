package de.hase.hasev2.appointment;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

@RestController
@RequestMapping("/appointment")
@CrossOrigin()
public class AppointmentController {
    DSLContext context;

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAppointments() {
        return ResponseEntity.ok(appointmentService.findAllAppointments());

    }


    @PostMapping("/add")
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment){
        System.out.println(appointment);
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

    @DeleteMapping("/delete")
    public ResponseEntity<Appointment> deleteAppointment(@RequestParam("appointmentId") int appointmentId){

        System.out.println(appointmentId);
        return ResponseEntity.ok(
                appointmentService.deleteAppointment(appointmentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

}

