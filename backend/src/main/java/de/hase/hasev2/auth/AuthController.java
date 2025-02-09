package de.hase.hasev2.auth;

import de.hase.hasev2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody Login login) {
        System.out.println(login);
        try {
            return ResponseEntity.ok(
                    this.authService.login(login)
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<List<User>> getLoggedInUser(@RequestParam("email") String email) {
        System.out.println(email);
        try {
            System.out.println(this.authService.getLoggedInUser(email));
            return ResponseEntity.ok(
                    this.authService.getLoggedInUser(email)
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
