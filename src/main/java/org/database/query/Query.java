package org.database.query;

import org.database.Database;
import org.database.User;

import java.util.List;

public abstract class Query {
    public String sqlQuery;
    public Database database;
    public User user;
    public abstract void execute(String createQuery, Database database, User user);
}
