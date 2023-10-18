package org.database;

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
}
