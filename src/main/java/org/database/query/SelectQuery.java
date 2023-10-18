package org.database.query;

import org.database.Database;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectQuery extends Query {
    private static final Logger logger = LoggerFactory.getLogger(SelectQuery.class);
    public String tableName;
    public Set<String> columns;

    public SelectQuery(String selectQuery, Database database) {
        this.sqlQuery = selectQuery;
        this.database = database;
    }

    @Override
    public void execute(String selectQuery, Database database) {
        this.tableName = getTableName(selectQuery);
        this.columns = getColumns(selectQuery);
        this.database = database;
        printOutput(this.tableName, this.columns, this.database);
    }
    private static String  getTableName(String selectQuery) {
        try {
            List<String> words = List.of(selectQuery.split(" "));
            logger.debug("Table name: " + words.get(3).replaceAll(";",""));
            return words.get(3).replaceAll(";","");
        } catch (Exception e) {
            throw new RuntimeException("Could not find table!");
        }
    }

    private static Set<String> getColumns(String selectQuery) {
        List<String> words = List.of(selectQuery.split(" "));
        logger.debug("Columns to be Selected " + Set.of(words.get(1).split(",")));
        return Set.of(words.get(1).split(","));
    }

    private static void printOutput(String tableName, Set<String> columns, Database database) {
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
                logger.info("Finished Execution of Select Query");
            } else {
                System.out.println("Database is Empty!");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to Execute Select Query");
        }
    }

}
