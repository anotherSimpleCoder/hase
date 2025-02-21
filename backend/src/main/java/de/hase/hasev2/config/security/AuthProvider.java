package de.hase.hasev2.config.security;


import de.hase.hasev2.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        System.out.println("Pw: " + rawPassword);

        var gottenUser = userService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        if (passwordEncoder.matches(rawPassword, gottenUser.password())) {
            return new UsernamePasswordAuthenticationToken(
                    gottenUser.email(), gottenUser.password(), null);
        } else {
            throw new AuthenticationException("Invalid credentials") {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
