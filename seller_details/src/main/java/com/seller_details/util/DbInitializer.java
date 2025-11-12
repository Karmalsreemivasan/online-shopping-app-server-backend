package com.seller_details.util;

import java.sql.*;

public class DbInitializer {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 3306;
        String user = "root";
        String password = "smce";

        String jdbcNoDb = String.format("jdbc:mysql://%s:%d/?useSSL=false&serverTimezone=UTC", host, port);
        String jdbcWithDb = String.format("jdbc:mysql://%s:%d/seller_detail?useSSL=false&serverTimezone=UTC", host, port);

        // 1) create database if not exists
        try (Connection c = DriverManager.getConnection(jdbcNoDb, user, password);
             Statement s = c.createStatement()) {
            System.out.println("Creating database if not exists...");
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS seller_detail CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }

        // 2) connect to the database
        try (Connection c = DriverManager.getConnection(jdbcWithDb, user, password)) {
            c.setAutoCommit(false);
            try (Statement s = c.createStatement()) {
                System.out.println("Connected to seller_detail");

                // 3) drop FK from seller_details if it references status (to allow altering types)
                String findFk = "SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'seller_details' " +
                        "AND REFERENCED_TABLE_NAME = 'status'";
                try (ResultSet rs = s.executeQuery(findFk)) {
                    while (rs.next()) {
                        String fk = rs.getString(1);
                        System.out.println("Dropping foreign key: " + fk);
                        s.executeUpdate("ALTER TABLE seller_details DROP FOREIGN KEY `" + fk + "`");
                    }
                }

                // 4) ensure status table exists with BIGINT id
                s.executeUpdate("CREATE TABLE IF NOT EXISTS status (" +
                        "id BIGINT NOT NULL AUTO_INCREMENT, " +
                        "status_name VARCHAR(255), " +
                        "description VARCHAR(255), " +
                        "PRIMARY KEY (id)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

                // 5) ensure seller_details table exists with status_id BIGINT
                s.executeUpdate("CREATE TABLE IF NOT EXISTS seller_details (" +
                        "id BIGINT NOT NULL, " +
                        "name VARCHAR(255), " +
                        "address VARCHAR(1024), " +
                        "email VARCHAR(255), " +
                        "password VARCHAR(255), " +
                        "phone VARCHAR(50), " +
                        "status_id BIGINT, " +
                        "PRIMARY KEY (id)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

                // 6) Alter columns to BIGINT to harmonize types
                try {
                    s.executeUpdate("ALTER TABLE status MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT");
                    System.out.println("Ensured status.id is BIGINT AUTO_INCREMENT");
                } catch (SQLException ex) {
                    System.out.println("Warning altering status.id: " + ex.getMessage());
                }

                try {
                    s.executeUpdate("ALTER TABLE seller_details MODIFY COLUMN status_id BIGINT");
                    System.out.println("Ensured seller_details.status_id is BIGINT");
                } catch (SQLException ex) {
                    System.out.println("Warning altering seller_details.status_id: " + ex.getMessage());
                }

                // 7) re-add FK constraint
                try {
                    s.executeUpdate("ALTER TABLE seller_details ADD CONSTRAINT status_seller_id_FK FOREIGN KEY (status_id) REFERENCES status(id)");
                    System.out.println("Added foreign key status_seller_id_FK");
                } catch (SQLException ex) {
                    System.out.println("Warning adding FK: " + ex.getMessage());
                }

                // 8) Ensure status rows 1 & 2 exist
                s.executeUpdate("INSERT INTO status (id, status_name, description) VALUES (1, 'Active', 'Active status') ON DUPLICATE KEY UPDATE status_name=VALUES(status_name), description=VALUES(description)");
                s.executeUpdate("INSERT INTO status (id, status_name, description) VALUES (2, 'Inactive', 'Inactive status') ON DUPLICATE KEY UPDATE status_name=VALUES(status_name), description=VALUES(description)");

                // 9) Upsert seller rows
                s.executeUpdate("INSERT INTO seller_details (id, name, address, email, password, phone, status_id) VALUES " +
                        "(1, 'Best Plastics Pvt Ltd', 'dsfsdfs', 'efesffffs@gmail.com', '12340', '9087654312', 1)," +
                        "(2, 'QuickFoods Pvt Ltd', 'shasj', 'rasdasfas@gmail.com', '56786', '7890654321', 2)," +
                        "(3, 'Indian Electronics Pvt Ltd', 'csnckjs', 'dsfgsdvsd@gmail.com', '132456', '9087654672', 1) " +
                        "ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), email=VALUES(email), password=VALUES(password), phone=VALUES(phone), status_id=VALUES(status_id)");

                // 10) verify
                try (ResultSet rs = s.executeQuery("SELECT id, name, status_id FROM seller_details WHERE id IN (1,2,3)")) {
                    System.out.println("Inserted / existing sellers:");
                    while (rs.next()) {
                        System.out.println("id=" + rs.getLong(1) + ", name=" + rs.getString(2) + ", status_id=" + rs.getObject(3));
                    }
                }

                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            }
        }

        System.out.println("Done.");
    }

}
