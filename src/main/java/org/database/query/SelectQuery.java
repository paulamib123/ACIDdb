package org.database.query;

import org.database.Database;

import java.util.*;

import org.database.GlobalLogger;
import org.database.User;

public class SelectQuery extends Query {
    public String tableName;
    public List<String> columns;

    public SelectQuery(String selectQuery, Database database, User user) {
        this.sqlQuery = selectQuery;
        this.database = database;
        this.user = user;
    }

    @Override
    public void execute(String selectQuery, Database database, User user) {
        this.tableName = getTableName(selectQuery);
        this.columns = getColumns(selectQuery);
        this.database = database;
        printOutput(this.tableName, this.columns, this.database);
        GlobalLogger.log(selectQuery, user.username);
    }
    private static String  getTableName(String selectQuery) {
        try {
            List<String> words = List.of(selectQuery.split(" "));
            return words.get(3).replaceAll(";","");
        } catch (Exception e) {
            throw new RuntimeException("Could not find table!");
        }
    }

    private static List<String> getColumns(String selectQuery) {
        List<String> words = List.of(selectQuery.split(" "));
        return List.of(words.get(1).split(","));
    }


private void printOutput(String tableName, List<String> columns, Database database) {
        Map<String, ArrayList<Map<String, String>>> tables = database.tableByRow;
        ArrayList<Map<String, String>> rows = tables.getOrDefault(tableName, new ArrayList<>());

        try {
            if (!rows.isEmpty()) {
                for (Map<String, String> row: rows) {
                    for (Map.Entry<String, String> entry : row.entrySet()) {
                        String key = entry.getKey();
                        if (columns.contains("*")) {
                            String value = entry.getValue();
                            System.out.println(key + " : " + value);
                        }
                        else if (columns.contains(key)) {
                            String value = entry.getValue();
                            System.out.println(key + " : " + value);
                        }
                    }
                    System.out.println();
                }
            } else {
                System.out.println("Database is Empty!");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to Execute Select Query");
        }
    }

}
