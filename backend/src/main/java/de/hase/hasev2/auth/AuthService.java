package de.hase.hasev2.auth;

import de.hase.hasev2.auth.exceptions.EmailNotFoundException;
import de.hase.hasev2.auth.exceptions.InvalidPasswordException;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthService {
    private final  UserService userService;
    private final PasswordEncoder encoder;

    public AuthService(@Autowired UserService userService) {
        this.encoder = new Argon2PasswordEncoder(16, 21, 1, 60000, 10);
        this.userService = userService;
    }

    public boolean login(Login login) throws EmailNotFoundException, InvalidPasswordException {
        //Get user of given email
        var gottenUser = this.userService.findUserByEmail(login.email())
                .orElseThrow(() -> new EmailNotFoundException(login.email()));

        if(!encoder.matches(login.password(), gottenUser.password())) {
            throw new InvalidPasswordException();
        }

        return true;
    }

    public List<User> getLoggedInUser(String email) throws EmailNotFoundException {
        //Get user of given email
        var gottenUser = this.userService.findUserByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        List<User> gottenUserList = new ArrayList<>();
        gottenUserList.add(gottenUser);

        return gottenUserList;
    }
}
