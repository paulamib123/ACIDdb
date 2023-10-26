package org.database;

import org.database.query.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class TransactionMenu {

    public List<Query> transactions;

    TransactionMenu(User user) {
        this.transactions = new ArrayList<>();
        displayMenu(user.database, user);
    }

    public void checkSQLKeyword(String input, Database database, User user) {
        if (input.isEmpty()) {
            System.out.println("Invalid SQL Query");
            return;
        }

        String lowercaseInput = input.split(" ")[0].toLowerCase();

        switch (lowercaseInput) {
            case "select":
                SelectQuery select = new SelectQuery(input, database, user);
                this.transactions.add(select);
                break;
            case "insert":
                InsertQuery insert = new InsertQuery(input, database, user);
                this.transactions.add(insert);
                break;
            case "delete":
                DeleteQuery delete = new DeleteQuery(input, database, user);
                this.transactions.add(delete);
                break;
            case "update":
                UpdateQuery update = new UpdateQuery(input, database, user);
                this.transactions.add(update);
                break;
            case "create":
                CreateQuery create = new CreateQuery(input, database, user);
                this.transactions.add(create);
                break;
            default:
                System.out.println("Invalid SQL Query");
                break;
        }
    }

    public void displayMenu(Database database, User user) {
        Scanner scanner = new Scanner(System.in);

        int choice;
        boolean isTransactionInProgress = false;

        do {
            System.out.println("Transaction Menu:");
            System.out.println("1. Begin Transaction");
            System.out.println("2. Commit");
            System.out.println("3. Rollback");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (isTransactionInProgress) {
                        System.out.println("A transaction is already in progress.");
                    } else {
                        scanner.nextLine();
                        String input;
                        while (true) {
                             System.out.println("Enter your SQL Query or Type End to End Transaction");
                             input = scanner.nextLine();
                             if (input.equalsIgnoreCase("End")) {
                                break;
                            } else {
                                 this.checkSQLKeyword(input, database, user);
                             }
                        }
                        isTransactionInProgress = true;
                    }
                    break;
                case 2:
                    if (isTransactionInProgress) {
                        for (Query query: transactions) {
                            query.execute(query.sqlQuery, query.database, query.user);
                        }
                        this.transactions.clear();
                        System.out.println("Transaction committed");
                        isTransactionInProgress = false;
                    } else {
                        System.out.println("No transaction in progress.");
                    }
                    break;
                case 3:
                    if (isTransactionInProgress) {
                        this.transactions.clear();
                        System.out.println("Transaction rolled back.");
                        isTransactionInProgress = false;
                    } else {
                        System.out.println("No transaction in progress.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting the program.");
                    exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
