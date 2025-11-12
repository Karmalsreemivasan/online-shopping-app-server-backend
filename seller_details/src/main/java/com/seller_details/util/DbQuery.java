package com.seller_details.util;

import java.sql.*;

public class DbQuery {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 3306;
        String user = "root";
        String password = "smce";
        String jdbc = String.format("jdbc:mysql://%s:%d/seller_detail?useSSL=false&serverTimezone=UTC", host, port);

        try (Connection c = DriverManager.getConnection(jdbc, user, password);
             Statement s = c.createStatement()) {
            System.out.println("Connected to seller_detail");

            System.out.println("\n-- SHOW CREATE TABLE seller_details --");
            try (ResultSet rs = s.executeQuery("SHOW CREATE TABLE seller_details")) {
                while (rs.next()) {
                    System.out.println(rs.getString(2));
                }
            } catch (SQLException ex) {
                System.out.println("Error showing seller_details: " + ex.getMessage());
            }

            System.out.println("\n-- SHOW CREATE TABLE status --");
            try (ResultSet rs = s.executeQuery("SHOW CREATE TABLE status")) {
                while (rs.next()) {
                    System.out.println(rs.getString(2));
                }
            } catch (SQLException ex) {
                System.out.println("Error showing status: " + ex.getMessage());
            }

            System.out.println("\n-- seller_details rows --");
            try (ResultSet rs = s.executeQuery("SELECT * FROM seller_details")) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= cols; i++) {
                        System.out.print(md.getColumnLabel(i) + "=" + rs.getObject(i) + "; ");
                    }
                    System.out.println();
                }
            } catch (SQLException ex) {
                System.out.println("Error selecting seller_details: " + ex.getMessage());
            }

            System.out.println("\n-- status rows --");
            try (ResultSet rs = s.executeQuery("SELECT * FROM status")) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= cols; i++) {
                        System.out.print(md.getColumnLabel(i) + "=" + rs.getObject(i) + "; ");
                    }
                    System.out.println();
                }
            } catch (SQLException ex) {
                System.out.println("Error selecting status: " + ex.getMessage());
            }
        }
    }
}
