package org.database.query;

import org.database.Database;
import org.database.GlobalLogger;
import org.database.User;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteQuery extends Query {
    public String tableName;
    public List<String> deleteFilter;

    public DeleteQuery(String deleteQuery, Database database, User user) {
        this.sqlQuery = deleteQuery;
        this.database = database;
        this.user = user;
    }

    @Override
    public void execute(String deleteQuery, Database database, User user) {
        this.tableName = getTableName(deleteQuery);
        this.deleteFilter = getDeleteFilter(deleteQuery);
        this.database = database;
        executeDelete(this.deleteFilter, this.database, this.tableName);
        try {
            database.writeData(tableName);
            GlobalLogger.log(deleteQuery, user.username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getTableName(String updateQuery) {
        try {
            List<String> words = List.of(updateQuery.split(" "));
            return words.get(2).replaceAll(";", "");
        } catch (Exception e) {
            throw new RuntimeException("Could not find table!");
        }
    }

    public static List<String> getDeleteFilter(String deleteQuery) {
        List<String> columnValueMap = new ArrayList<>();
        Pattern pattern = Pattern.compile("WHERE(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(deleteQuery);


        if (matcher.find()) {
            String condition = matcher.group(1).trim();
            String column = condition.split("=")[0];
            String value = condition.split("=")[1].replaceAll(";", "");
            columnValueMap.add(column);
            columnValueMap.add(value);
        }

        return columnValueMap;
    }

    public void executeDelete(List<String> deleteFilter,
                                  Database database,
                                  String tableName) {
        Set<Map<String, String>> rowsToDelete = new HashSet<>();
        Map<String, Set<Map<String, String>>> tables = database.tableByRow;
        Set<Map<String, String>> rows = tables.getOrDefault(tableName, new HashSet<>());

        if (deleteFilter.isEmpty()) {
            //delete all rows
            rows.clear();
            database.tableByRow.put(tableName, rows);
            return;
        }

        try {
            String column = deleteFilter.get(0);
            String value = deleteFilter.get(1);

            boolean flag = false;

            if (!rows.isEmpty()) {
                for (Map<String, String> row : rows) {
                    String currentValue = row.getOrDefault(column, "");
                    if (!currentValue.isEmpty() && currentValue.equals(value)) {
                        flag = true;
                    }
                    if (flag) {
                        //delete this row from the arraylist
                        rowsToDelete.add(row);
                        flag = false;
                    }
                }
            }
            for (Map<String, String> row: rowsToDelete) {
                rows.remove(row);
            }
            database.tableByRow.put(tableName, rows);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute Delete Query!");
        }
    }
}
