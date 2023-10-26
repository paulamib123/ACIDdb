package org.database;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalLogger {

    public static void log(String message, String username) {
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.log", true); // Use "true" to append to the file
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            fileWriter.write(timestamp + " : " + username + " : " + message + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
