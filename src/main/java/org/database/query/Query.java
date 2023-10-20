package org.database.query;

import org.database.Database;

import java.util.List;

public abstract class Query {
    public String sqlQuery;
    public Database database;
    public List<String> datatypes;
    public abstract void execute(String createQuery, Database database);
}
