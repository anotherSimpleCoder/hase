package de.hase.hasev2.user;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private  UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user){
        return ResponseEntity.ok(
                userService.saveUser(user)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @DeleteMapping()
    public ResponseEntity<User> deleteUser(@RequestParam("matrikelNr") int matrikelNr){
        return ResponseEntity.ok(
                userService.deleteUser(matrikelNr)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser){
        System.out.println(updatedUser);
        return ResponseEntity.ok(userService.updateUser(updatedUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Appointment not found"))
        );
    }


}
