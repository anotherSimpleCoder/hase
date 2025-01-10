package de.hase.hasev2.appointment;

import de.hase.hasev2.database.tables.Appointments;
import de.hase.hasev2.database.tables.records.AppointmentsRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


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
                                .selectFrom(Appointments.APPOINTMENTS)
                                .fetchInto(Appointment.class)
        );

    }
}
