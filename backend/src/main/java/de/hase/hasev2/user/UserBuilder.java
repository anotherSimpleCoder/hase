package de.hase.hasev2.user;

import java.util.Random;

public class UserBuilder {
    private int matrikelNr;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserBuilder matrikelNr(int matrikelNr) {
        this.matrikelNr = matrikelNr;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        if (matrikelNr == 0) {
            this.matrikelNr = new Random().nextInt(1, 1000);
        }

        return new User(matrikelNr, firstName, lastName, email, password);
    }
}
