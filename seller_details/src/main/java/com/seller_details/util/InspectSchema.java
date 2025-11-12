package com.seller_details.util;

import java.sql.*;

public class InspectSchema {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 3306;
        String user = "root";
        String password = "smce";
        String db = "seller_details";
        String jdbc = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC", host, port, db);

        try (Connection c = DriverManager.getConnection(jdbc, user, password);
             Statement s = c.createStatement()) {
            System.out.println("Connected to " + db);

            System.out.println("\n-- Columns for seller_details and status --");
            try (PreparedStatement ps = c.prepareStatement(
                        "SELECT table_name, column_name, column_type, is_nullable, column_key FROM information_schema.COLUMNS "
                        + "WHERE table_schema = ? AND table_name IN ('seller_details','seller_detail','status') ORDER BY table_name, ordinal_position")) {
                ps.setString(1, db);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("%s.%s : %s null=%s key=%s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                    }
                }
            }

            System.out.println("\n-- Foreign keys referencing status --");
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT constraint_name, table_name, column_name, referenced_table_name, referenced_column_name "
                    + "FROM information_schema.KEY_COLUMN_USAGE "
                    + "WHERE table_schema = ? AND referenced_table_name = 'status'")) {
                ps.setString(1, db);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("FK %s : %s.%s -> %s.%s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                    }
                }
            }

            System.out.println("\n-- SHOW CREATE TABLE seller_details --");
            try (ResultSet rs = s.executeQuery("SHOW CREATE TABLE seller_details")) {
                while (rs.next()) System.out.println(rs.getString(2));
            } catch (SQLException ex) { System.out.println("Error: " + ex.getMessage()); }

            System.out.println("\n-- SHOW CREATE TABLE status --");
            try (ResultSet rs = s.executeQuery("SHOW CREATE TABLE status")) {
                while (rs.next()) System.out.println(rs.getString(2));
            } catch (SQLException ex) { System.out.println("Error: " + ex.getMessage()); }
        }
    }
}
