package de.hase.hasev2.user.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int matrikelNr) {
        super("User with Matrikelnummer " + matrikelNr + " not found");
    }
}
