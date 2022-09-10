package com.company.Objects;

public class User {

    private String username;
    private String password;
    private String authority;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + ',' +
                ", password='" + password + ',' +
                ", authority='" + authority + ',' +
                '}';
    }

    public User(String username, String password, String authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userPassword) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String userAuthority) {
        this.authority = authority;
    }
}
