package com.seller_details.util;

import java.sql.*;

public class FixSchema {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 3306;
        String user = "root";
        String password = "smce";
        String db = "seller_details"; // matches application.properties
        String jdbc = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC", host, port, db);

        try (Connection c = DriverManager.getConnection(jdbc, user, password);
             Statement s = c.createStatement()) {
            System.out.println("Connected to " + db);

            // Disable foreign key checks to allow altering columns when constraints are incompatible
            System.out.println("Disabling FOREIGN_KEY_CHECKS to allow schema changes...");
            try {
                s.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            } catch (SQLException ex) {
                System.out.println("Warning: could not disable FOREIGN_KEY_CHECKS: " + ex.getMessage());
            }

            // Find and drop any foreign keys on seller_details (any referenced table) so we can alter columns
            System.out.println("Listing foreign keys on seller_details and dropping them (if any)...");
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT constraint_name, referenced_table_name FROM information_schema.KEY_COLUMN_USAGE "
                    + "WHERE table_schema = ? AND table_name = 'seller_details' AND referenced_table_name IS NOT NULL")) {
                ps.setString(1, db);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String fk = rs.getString(1);
                        String ref = rs.getString(2);
                        System.out.println("Found FK: " + fk + " -> " + ref + "  - dropping");
                        try {
                            s.executeUpdate("ALTER TABLE seller_details DROP FOREIGN KEY `" + fk + "`");
                        } catch (SQLException ex) {
                            System.out.println("Could not drop FK " + fk + ": " + ex.getMessage());
                        }
                    }
                }
            }

            // Alter columns to BIGINT and set AUTO_INCREMENT on ids
            System.out.println("Altering status.id to BIGINT NOT NULL AUTO_INCREMENT");
            try {
                s.executeUpdate("ALTER TABLE status MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT");
            } catch (SQLException ex) {
                System.out.println("Warning/Info: could not modify status.id: " + ex.getMessage());
            }

            // Try dropping commonly named constraints explicitly in case the automated lookup missed them
            String[] candidateFks = new String[]{"seller_details_status_FK", "status_seller_id_FK", "status_sellerid_FK", "seller_detail_status_fk"};
            for (String fkName : candidateFks) {
                try {
                    System.out.println("Attempting to drop FK by name: " + fkName);
                    s.executeUpdate("ALTER TABLE seller_detail DROP FOREIGN KEY `" + fkName + "`");
                } catch (SQLException ex) {
                    // ignore
                }
                try {
                    s.executeUpdate("ALTER TABLE seller_details DROP FOREIGN KEY `" + fkName + "`");
                } catch (SQLException ex) {
                    // ignore
                }
            }

            System.out.println("Altering seller_details.id to BIGINT NOT NULL AUTO_INCREMENT");
            try {
                s.executeUpdate("ALTER TABLE seller_details MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT");
            } catch (SQLException ex) {
                System.out.println("Warning/Info: could not modify seller_details.id: " + ex.getMessage());
            }

            System.out.println("Altering seller_details.status_id to BIGINT");
            try {
                s.executeUpdate("ALTER TABLE seller_details MODIFY COLUMN status_id BIGINT");
            } catch (SQLException ex) {
                System.out.println("Warning/Info: could not modify seller_details.status_id: " + ex.getMessage());
            }

            // Recreate FK constraint

            // Re-enable foreign key checks before attempting to add FK
            try {
                s.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
            } catch (SQLException ex) {
                System.out.println("Warning: could not enable FOREIGN_KEY_CHECKS: " + ex.getMessage());
            }

            System.out.println("Adding foreign key seller_details_status_FK (seller_details.status_id -> status.id)");
            try {
                s.executeUpdate("ALTER TABLE seller_details ADD CONSTRAINT seller_details_status_FK FOREIGN KEY (status_id) REFERENCES status(id)");
            } catch (SQLException ex) {
                System.out.println("Warning/Info: could not add FK: " + ex.getMessage());
            }

            System.out.println("Schema fix completed.");
        }
    }
}
