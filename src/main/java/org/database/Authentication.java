package org.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Authentication {
    public Map<String, User> accounts = new HashMap<>();

    Authentication() {
        getUsers();
    }

    //generate hash to validate password
    public static String generateMD5Hash(String input) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] inputBytes = input.getBytes();
            md.update(inputBytes);
            byte[] md5Bytes = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                hexString.append(Integer.toHexString(0xFF & md5Byte));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //generate random string for CAPTCHA
    public static String generateRandomString(int length) {
        String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumeric.length());
            sb.append(alphanumeric.charAt(randomIndex));
        }

        return sb.toString();
    }

    public void getUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    Database database = new Database();
                    this.accounts.put(username, new User(username, password, database));
                } else {
                    System.out.println("Invalid line in the file: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        String hash = generateMD5Hash(password);

        User user = this.accounts.getOrDefault(username, null);
        if (user == null) {
            System.out.println("Invalid Username");
            return;
        } else {
            if (user.isValid(hash))
            {
                String captcha = generateRandomString(6);
                System.out.println("CAPTCHA: " + captcha);
                System.out.print("Enter CAPTCHA: ");
                String userEnteredCaptcha = scanner.nextLine();
                if (userEnteredCaptcha.equals(captcha)) {
                    new TransactionMenu(user);
                } else {
                    System.out.println("Invalid Captcha");
                    return;
                }

            } else {
                System.out.println("Invalid Password");
                return;
            }
        }

        scanner.close();
    }

    public void authenticationMenu() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Menu:");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
