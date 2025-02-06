package de.hase.hasev2.auth;

public class LoginBuilder {
    private String email = "";
    private String password = "";

    public LoginBuilder email(String email) {
        this.email = email;
        return this;
    }

    public LoginBuilder password(String password) {
        this.password = password;
        return this;
    }

    public Login build() {
        return new Login(email, password);
    }
}
