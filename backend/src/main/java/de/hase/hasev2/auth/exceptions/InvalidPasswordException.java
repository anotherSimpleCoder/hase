package de.hase.hasev2.auth.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Wrong password");
    }
}
