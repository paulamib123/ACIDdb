package org.database;

import org.database.query.*;

public class Main {

//    public static void main2() {
//        Database database = new Database();
//
//        String createQuery = "CREATE TABLE students (id int, name String, class int);";
//        String insertQuery = "INSERT INTO students (id, name, class) VALUES (1, 'akshu', 10);";
//        String insertQuery2 = "INSERT INTO students (id, name, class) VALUES (2, 'cutie akshu', 12);";
//        String selectQuery = "SELECT * FROM students;";
//        String selectQuery2 = "SELECT id,name FROM students;";
//        String updateQuery = "UPDATE students SET name=sexy akshu WHERE id=1";
//        String updateQuery2 = "UPDATE students SET name=handsome akshu WHERE id=2";
//        String insertQuery3 = "INSERT INTO students (id, name, class) VALUES (3, 'pb loves akshu', 9);";
//        String deleteQuery = "DELETE FROM students WHERE id=1;";
//        String deleteQuery2 = "DELETE FROM students;";
//        CreateQuery create = new CreateQuery(createQuery, database);
//        InsertQuery insert = new InsertQuery(insertQuery, database);
//        InsertQuery insert2 = new InsertQuery(insertQuery2, database);
//        SelectQuery select = new SelectQuery(selectQuery, database);
//        UpdateQuery update = new UpdateQuery(updateQuery, database);
//        UpdateQuery update2 = new UpdateQuery(updateQuery2, database);
//        SelectQuery select2 = new SelectQuery(selectQuery, database);
//        InsertQuery insert3 = new InsertQuery(insertQuery3, database);
//        SelectQuery select3 = new SelectQuery(selectQuery2, database);
//        DeleteQuery delete = new DeleteQuery(deleteQuery, database);
//        SelectQuery select4 = new SelectQuery(selectQuery, database);
//        DeleteQuery delete2 = new DeleteQuery(deleteQuery2, database);
//        SelectQuery select5 = new SelectQuery(selectQuery, database);
//
//    }

    public static void main(String[] args) {
        Database database = new Database();

        String createQuery = "CREATE TABLE students (id int, name String, class int);";
        String insertQuery = "INSERT INTO students (id, name, class) VALUES (1, 'akshu', 10);";
        String insertQuery2 = "INSERT INTO students (id, name, class) VALUES (2, 'cutie akshu', 12);";
        String selectQuery = "SELECT * FROM students;";
        String selectQuery2 = "SELECT id,name FROM students;";
        String updateQuery = "UPDATE students SET name=sexy akshu WHERE id=1";
        String updateQuery2 = "UPDATE students SET name=handsome akshu WHERE id=2";
        String insertQuery3 = "INSERT INTO students (id, name, class) VALUES (3, 'pb loves akshu', 9);";
        String deleteQuery = "DELETE FROM students WHERE id=1;";
        String deleteQuery2 = "DELETE FROM students;";

        TransactionMenu transaction = new TransactionMenu(database);


    }
}