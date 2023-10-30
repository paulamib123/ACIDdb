package org.database.query;

import org.database.Database;
import org.database.GlobalLogger;
import org.database.User;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQuery extends Query {
    public String tableName;
    public Map<String, String> columns;

    public List<String> datatypes;

    public CreateQuery(String createQuery, Database database, User user) {
        this.sqlQuery = createQuery;
        this.database = database;
        this.user = user;
        this.datatypes = new ArrayList<>();
        this.datatypes.add("string");
        this.datatypes.add("int");
        this.datatypes.add("bool");
        this.datatypes.add("double");
    }

    public boolean isValidDatatype(String type) {
        return datatypes.contains(type);
    }

    @Override
    public void execute(String createQuery, Database database, User user) {
        this.tableName = getTableName(createQuery);
        this.columns = getColumns(createQuery);
        createTable(this.database, this.tableName, this.columns);
        this.database.saveMetadataToFile();
        GlobalLogger.log(createQuery, user.username);
    }

    private static void createTable(Database database, String tableName, Map<String, String> columns) {
        database.tableByColumns.put(tableName, columns);
        database.tableByRow.put(tableName, new HashSet<>());
    }

    private static String  getTableName(String createQuery) {
        try {
            List<String> words = List.of(createQuery.split(" "));
            return words.get(2);
        } catch (Exception e) {
            throw new RuntimeException("Could not find table!");
        }
    }

    private Map<String, String> getColumnsAndDataType(String createQuery) {
        Pattern pattern = Pattern.compile("(\\w+)\\s(\\w+)");
        Matcher matcher = pattern.matcher(createQuery);
        Map<String, String> columnByDataType = new HashMap<>();

        while(matcher.find()){
            String columnName = matcher.group(1);
            String dataType = matcher.group(2);
            if (this.isValidDatatype(dataType)) {
                columnByDataType.put(columnName, dataType);
            } else {
                throw new RuntimeException("Invalid Datatype!");
            }


            //System.out.println("Column: " + columnName + ", DataType: " + dataType);
        }

        return columnByDataType;
    }

    private Map<String, String> getColumns(String createQuery) {
        Pattern pattern = Pattern.compile("\\((.*?)\\);"); // Regex to match text inside parentheses

        Matcher matcher = pattern.matcher(createQuery);
        if (matcher.find()) {
            String columnDefinitions = matcher.group(1); // Extract the text inside parentheses
            return getColumnsAndDataType("(" + columnDefinitions + ")");
        }
        return new HashMap<>();
    }
}
