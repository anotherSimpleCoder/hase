package de.hase.hasev2.auth.exceptions;

public class EmailNotFoundException extends Exception {
    public EmailNotFoundException(String email) {
        super(email + " is not found");
    }
}
