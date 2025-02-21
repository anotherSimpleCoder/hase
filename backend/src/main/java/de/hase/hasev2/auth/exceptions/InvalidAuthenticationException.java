package de.hase.hasev2.auth.exceptions;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("The given authentication is invalid");
    }
}
