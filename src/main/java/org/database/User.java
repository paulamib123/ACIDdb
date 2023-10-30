package org.database;

public class User {
    public String username;
    public String password;

    public Database database;

    User(String username, String password, Database database) {
        this.username = username;
        this.password = password;
        this.database = database;
    }
    public boolean isValid(String password) {
        return password.equals(this.password);
    }



}
