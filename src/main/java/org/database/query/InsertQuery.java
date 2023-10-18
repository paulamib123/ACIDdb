package org.database.query;

import org.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*INSERT INTO students (id, name, class) VALUES (1, "jay", 10)*/
public class InsertQuery extends Query {
    public String tableName;
    public Map<String, String> row;

    public InsertQuery(String insertQuery, Database database) {
        this.sqlQuery = insertQuery;
        this.database = database;
    }

    @Override
    public void execute(String insertQuery, Database database) {
        this.tableName = getTableName(insertQuery, database);
        this.row = getColumnValues(insertQuery);
        this.database = database;
        insertRowInTable(this.database, this.tableName, this.row);
    }

    private static String getTableName(String insertQuery, Database database) {
        List<String> words = List.of(insertQuery.split(" "));
        String tableName = words.get(2);
        //System.out.println("Table name: " + tableName);
        if (database.tableByColumns.containsKey(tableName)) {
            return tableName;
        } else {
            throw new RuntimeException("Invalid Table Name");
        }
    }

    private static void insertRowInTable(Database database, String tableName, Map<String, String> row) {
        ArrayList<Map<String, String>> rows = database.tableByRow.getOrDefault(tableName, new ArrayList<>());
        rows.add(row);
        database.tableByRow.put(tableName, rows);
    }

    private static Map<String, String> getColumnValues(String insertQuery) {
        // Extract column names
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(insertQuery);
        ArrayList<String> columns = new ArrayList<>();
        if(matcher.find()){
            for(String col : matcher.group(1).split(",")) {
                columns.add(col.trim().replaceAll("'", ""));
            }
        }

        // Extract values
        pattern = Pattern.compile("VALUES \\((.*)\\)");
        matcher = pattern.matcher(insertQuery);
        ArrayList<String> values = new ArrayList<>();
        if(matcher.find()){
            for(String val : matcher.group(1).split(",")) {
                values.add(val.trim().replaceAll("'", ""));
            }
        }

        //System.out.println(columns);
        //System.out.println(values);

        // Create map
        Map<String, String> columnByValue = new HashMap<>();
        for(int i=0; i<columns.size(); i++) {
            columnByValue.put(columns.get(i), values.get(i));
        }

        // Print map
        //System.out.println(columnByValue);
        return columnByValue;
    }


}