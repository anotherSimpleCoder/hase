package de.hase.hasev2.auth;

import de.hase.hasev2.auth.exceptions.EmailNotFoundException;
import de.hase.hasev2.auth.exceptions.InvalidPasswordException;
import de.hase.hasev2.auth.token.Token;
import de.hase.hasev2.auth.token.TokenService;
import de.hase.hasev2.user.User;
import de.hase.hasev2.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private TokenService tokenService;

    public AuthService(@Autowired UserService userService, @Autowired PasswordEncoder encoder, @Autowired TokenService tokenService) {
        this.encoder = encoder;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public Token login(Login login) throws EmailNotFoundException, InvalidPasswordException {
        //Get user of given email
        var gottenUser = this.userService.findUserByEmail(login.email())
                .orElseThrow(() -> new EmailNotFoundException(login.email()));

        if(!encoder.matches(login.password(), gottenUser.password())) {
            throw new InvalidPasswordException();
        }

        var authentication = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        return this.tokenService.generateToken(authentication);
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
