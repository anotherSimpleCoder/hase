package de.hase.hasev2.appointment;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    DSLContext context;



    public AppointmentController(){

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:./backend/database/database.sqlite");
            context = DSL.using(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAppointments() {

        return ResponseEntity.ok(context
                                .selectFrom(APPOINTMENTS)
                                .fetchInto(Appointment.class)
        );

    }


    @PostMapping()
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment){
        var storedAppointment = context.insertInto(APPOINTMENTS, APPOINTMENTS.NAME, APPOINTMENTS.DATE, APPOINTMENTS.LOCATION)
                .values(appointment.name(), appointment.date(), appointment.location())
                .returningResult()
                .fetchOneInto(Appointment.class);

        return ResponseEntity.ok(
                storedAppointment
        );
    }

    @GetMapping()
    public ResponseEntity<Appointment> getAppointment(@RequestParam("appointmentId") int appointmentId){
        var fetchedAppointment = context.selectFrom(APPOINTMENTS)
                .where(APPOINTMENTS.APPOINTMENTID.eq(appointmentId))
                .fetchOneInto(Appointment.class);
        return ResponseEntity.ok(fetchedAppointment);
    }

    @DeleteMapping()
    public void deleteAppointment(@RequestParam("appointmentId") int appointmentId){
        var deletedAppointment = context.deleteFrom(APPOINTMENTS)
                .where(APPOINTMENTS.APPOINTMENTID.eq(appointmentId))
                .execute();
    }
}

