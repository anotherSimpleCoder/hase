package de.hase.hasev2.auth;

import de.hase.hasev2.auth.exceptions.EmailNotFoundException;
import de.hase.hasev2.auth.exceptions.InvalidPasswordException;
import de.hase.hasev2.auth.token.Token;
import de.hase.hasev2.auth.token.TokenService;
import de.hase.hasev2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Token> login(@RequestBody Login login) {
        try {
            return ResponseEntity.ok(
                    this.authService.login(login)
            );
        } catch (EmailNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<List<User>> getLoggedInUser(@RequestParam("email") String email) {
        try {
            return ResponseEntity.ok(
                    this.authService.getLoggedInUser(email)
            );
        } catch (EmailNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
