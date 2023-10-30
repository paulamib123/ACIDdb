package org.database;

import java.io.*;
import java.util.*;

public class Database {
    public Map<String, Map<String, String>> tableByColumns;
    public Map<String, Set<Map<String, String>>> tableByRow;

    String PATH = "src/main/resources/data/";

    public Database() {
        this.tableByColumns = new HashMap<>();
        this.tableByRow = new HashMap<>();
        try {
            File directory = new File(PATH);

            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            String fileName = file.getName();
                            this.readData(fileName);
                        }
                    }
                }
            }
            loadMetadataFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readData(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PATH + filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Map<String, String> row = new HashMap<>();
            for (int i = 0; i < parts.length; i++) {
                String[] pair = parts[i].split(":");
                if (pair.length == 2) {
                    row.put(pair[0], pair[1]);
                }
            }
            String tableName = filename.split("\\.")[0];
            if (!tableByRow.containsKey(tableName)) {
                tableByRow.put(tableName, new HashSet<>());
            }
            tableByRow.get(tableName).add(row);
        }
//        System.out.println("Reading Data");
//        System.out.println(tableByRow);
        reader.close();
    }

    public void writeData(String tableName) throws IOException {
//        System.out.println("Writing Data");
//        System.out.println(tableByRow);
        File table = new File(PATH + tableName + ".txt");
        if (!table.exists()) {
            // If the file does not exist, create it
            table.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(table, false);

        PrintWriter writer = new PrintWriter(fileWriter);
        Set<Map<String, String>> rows = tableByRow.get(tableName);

        for (Map<String, String> row : rows) {
            for (Map.Entry<String, String> rowEntry : row.entrySet()) {
                writer.print(rowEntry.getKey() + ":" + rowEntry.getValue() + ",");
            }
            writer.println();
        }

        writer.close();
    }

        public void saveMetadataToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH + "metadata.txt"))) {
                for (Map.Entry<String, Map<String, String>> tableEntry : tableByColumns.entrySet()) {
                    String tableName = tableEntry.getKey();
                    Map<String, String> columnMap = tableEntry.getValue();
                    StringBuilder tableMetadata = new StringBuilder(tableName + "-");

                    for (Map.Entry<String, String> columnEntry : columnMap.entrySet()) {
                        String columnName = columnEntry.getKey();
                        String dataType = columnEntry.getValue();
                        tableMetadata.append(columnName + ":" + dataType + ",");
                    }

                    writer.write(tableMetadata.toString());
                    writer.newLine();
                }
                System.out.println("write to file");
                System.out.println(tableByColumns);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void loadMetadataFromFile() throws IOException {

            BufferedReader reader = new BufferedReader(new FileReader(PATH + "metadata.txt"));
                String line;
            while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("-");
                    if (parts.length == 2) {
                        String tableName = parts[0];
                        String[] columnData = parts[1].split(",");
                        Map<String, String> columnMap = new HashMap<>();
                        for (String column : columnData) {
                            String[] colInfo = column.split(":");
                            if (colInfo.length == 2) {
                                String colName = colInfo[0];
                                String dataType = colInfo[1];
                                columnMap.put(colName, dataType);
                            }
                        }
                        this.tableByColumns.put(tableName, columnMap);
                    }
                }
            System.out.println("read from file");
            System.out.println(tableByColumns);
            }

        }
