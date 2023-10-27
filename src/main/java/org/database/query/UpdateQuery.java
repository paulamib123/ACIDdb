package org.database.query;


import org.database.Database;
import org.database.GlobalLogger;
import org.database.User;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQuery extends Query {
    public String tableName;
    public Map<String, String> updatedColumns;
    public List<String> updateFilter;

    public UpdateQuery(String updateQuery, Database database, User user) {
        this.sqlQuery = updateQuery;
        this.database = database;
        this.user = user;
    }

    @Override
    public void execute(String updateQuery, Database database, User user) {
        this.tableName = getTableName(updateQuery);
        this.updatedColumns = getUpdatedColumns(updateQuery);
        this.updateFilter = getUpdateFilter(updateQuery);
        this.database = database;
        try {
            getRowToBeUpdated(this.updateFilter, this.updatedColumns, this.database, this.tableName);
            database.writeData(tableName);
            GlobalLogger.log(updateQuery, user.username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String getTableName(String updateQuery) {
        try {
            List<String> words = List.of(updateQuery.split(" "));
            return words.get(1);
        } catch (Exception e) {
            throw new RuntimeException("Could not find table!");
        }
    }

    public static Map<String, String> getUpdatedColumns(String updateQuery) {
        Map<String, String> columnValueMap = new HashMap<>();

        Pattern pattern = Pattern.compile("SET\\s+(.*?)\\s+WHERE", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(updateQuery);

        if (matcher.find()) {
            String setClause = matcher.group(1);
            String[] columnValuePairs = setClause.split(",");

            for (String columnValuePair : columnValuePairs) {
                String[] parts = columnValuePair.split("=");
                if (parts.length == 2) {
                    String column = parts[0].trim();
                    String value = parts[1].trim();
                    columnValueMap.put(column, value);
                }
            }
        }
        return columnValueMap;
    }

    public static List<String> getUpdateFilter(String updateQuery) {
        List<String> columnValueMap = new ArrayList<>();
        Pattern pattern = Pattern.compile("WHERE(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(updateQuery);


        if (matcher.find()) {
            String condition = matcher.group(1).trim();
            String column = condition.split("=")[0];
            String value = condition.split("=")[1].replaceAll(";", "");
            columnValueMap.add(column);
            columnValueMap.add(value);
        }

        return columnValueMap;
    }

    public void getRowToBeUpdated(List<String> updateFilter,
                                  Map<String, String> updatedColumns,
                                  Database database,
                                  String tableName) {
        Map<String, Set<Map<String, String>>> tables = database.tableByRow;
        Set<Map<String, String>> rows = tables.getOrDefault(tableName, new HashSet<>());

        try {
            String toUpdateColumn = updateFilter.get(0);
            String toUpdateValue = updateFilter.get(1);

            boolean flag = false;

            if (!rows.isEmpty()) {
                for (Map<String, String> row : rows) {
                    String currentValue = row.getOrDefault(toUpdateColumn, "");
                    if (!currentValue.isEmpty() && currentValue.equals(toUpdateValue)) {
                        flag = true;
                    }
                    if (flag) {
                        for (Map.Entry<String, String> entry : updatedColumns.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (row.containsKey(key)) {
                                row.put(key, value);
                            }
                        }
                        flag = false;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute Update Query!");
        }
    }
}
