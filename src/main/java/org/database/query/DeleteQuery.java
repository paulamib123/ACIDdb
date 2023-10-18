package org.database.query;

import org.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*DELETE FROM table_name WHERE condition;*/
public class DeleteQuery extends Query {
    public String tableName;
    public List<String> deleteFilter;

    private static final Logger logger = LoggerFactory.getLogger(DeleteQuery.class);

    public DeleteQuery(String deleteQuery, Database database) {
        this.sqlQuery = deleteQuery;
        this.database = database;
    }

    @Override
    public void execute(String deleteQuery, Database database) {
        this.tableName = getTableName(deleteQuery);
        this.deleteFilter = getDeleteFilter(deleteQuery);
        this.database = database;
        executeDelete(this.deleteFilter, this.database, this.tableName);
    }


    private static String getTableName(String updateQuery) {
        try {
            List<String> words = List.of(updateQuery.split(" "));
            logger.debug("Table name: " + words.get(2).replaceAll(";", ""));
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

        logger.debug("Delete Clause: " + columnValueMap);
        return columnValueMap;
    }

    public void executeDelete(List<String> deleteFilter,
                                  Database database,
                                  String tableName) {
        List<Map<String, String>> rowsToDelete = new ArrayList<>();
        Map<String, ArrayList<Map<String, String>>> tables = database.tableByRow;
        ArrayList<Map<String, String>> rows = tables.getOrDefault(tableName, new ArrayList<>());

        if (deleteFilter.isEmpty()) {
            rows.clear();
            logger.info("Deleted All Rows");
            System.out.println("Table is Empty!");
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
                        logger.info("Deleted Row");
                        flag = false;
                    }
                }
            }
            rows.removeAll(rowsToDelete);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute Delete Query!");
        }
    }
}
