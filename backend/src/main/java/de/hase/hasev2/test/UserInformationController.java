package de.hase.hasev2.test;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserInformationController {
    @GetMapping("/giveUserInformation")
    public ResponseEntity<List<UserInformation>> giveUserInformation(Authentication authentication) {

        List<UserInformation> users = new ArrayList<>();

        if (authentication == null) {
            UserInformation userInformation = new UserInformation(null,null);
            users.add(userInformation);
            return ResponseEntity.ok(users);
        }

        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            UserInformation userInformation = new UserInformation(oidcUser.getFullName(),oidcUser.getEmail());
            users.add(userInformation);
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            UserInformation userInformation = new UserInformation(oAuth2User.getAttribute("login"),null);
            users.add(userInformation);
        } else {
            UserInformation userInformation = new UserInformation(null,null);
            users.add(userInformation);
        }

        System.out.println(users);

        return ResponseEntity.ok(users);
    }

}
