package org.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    public Map<String, Map<String, String>> tableByColumns;
    public Map<String, ArrayList<Map<String, String>>> tableByRow;

    public Database() {
        this.tableByColumns = new HashMap<>();
        this.tableByRow = new HashMap<>();
    }

    public void readData(String tableName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(tableName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Map<String, String> row = new HashMap<>();
            for (int i = 0; i < parts.length; i++) {
                String[] pair = parts[i].split(":");
                row.put(pair[0], pair[1]);
            }

            if (!tableByRow.containsKey(tableName)) {
                tableByRow.put(tableName, new ArrayList<>());
            }
            tableByRow.get(tableName).add(row);
        }
        reader.close();
    }

    public void writeData(String tableName) throws IOException {
        PrintWriter writer = new PrintWriter(tableName);
        for (Map.Entry<String, ArrayList<Map<String, String>>> entry : tableByRow.entrySet()) {
            String rowKey = entry.getKey();
            ArrayList<Map<String, String>> rows = entry.getValue();

            writer.print(rowKey + ":");
            for (Map<String, String> row : rows) {
                for (Map.Entry<String, String> rowEntry : row.entrySet()) {
                    writer.print(rowEntry.getKey() + "," + rowEntry.getValue() + ":");
                }
            }
            writer.println();
        }
        writer.close();
    }
}
