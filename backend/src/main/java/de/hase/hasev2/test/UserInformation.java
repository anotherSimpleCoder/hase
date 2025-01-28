package de.hase.hasev2.test;

public class UserInformation {
    private String username;
    private String email;

    public UserInformation(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
