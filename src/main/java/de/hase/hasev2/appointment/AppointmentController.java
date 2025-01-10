package de.hase.hasev2.appointment;

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

import static de.hase.hasev2.database.tables.Appointments.APPOINTMENTS;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    DSLContext context;



    public AppointmentController(){

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:./database/database.sqlite");
            context = DSL.using(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAppointments() {

        return ResponseEntity.ok(context.selectFrom(APPOINTMENTS).fetchInto());

    }
}
