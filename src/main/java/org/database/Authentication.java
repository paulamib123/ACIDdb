package org.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Authentication {
    public Map<String, User> accounts = new HashMap<>();

    Authentication() {
        Database database = new Database();
        Database database1 = new Database();

        this.accounts.put("xyz", new User("xyz", "bye",database));
        this.accounts.put("abc", new User("abc", "bye",database1));
        this.accounts.put("pqr", new User("pqr", "bye",database));
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // You can perform authentication or other actions with the username and password here.
        User user = this.accounts.getOrDefault(username, null);
        if (user == null) {
            throw new RuntimeException("Invalid Username");
        } else {
            if (user.isValid(password)) {
                new TransactionMenu(user.database);
            } else {
                throw new RuntimeException("Invalid Password");
            }
        }

        scanner.close(); // Close the scanner when done.
    }

}
