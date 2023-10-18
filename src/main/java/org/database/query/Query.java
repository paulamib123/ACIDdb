package org.database.query;

import org.database.Database;

public abstract class Query {
    public String sqlQuery;
    public Database database;
    public abstract void execute(String createQuery, Database database);
}
