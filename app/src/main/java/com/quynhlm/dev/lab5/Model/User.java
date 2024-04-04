package com.quynhlm.dev.lab5.Model;

public class User {
    private String _id;
    private String username;
    private String password;

    public User() {
    }

    public User(String _id, String username, String password) {
        this._id = _id;
        this.username = username;
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
