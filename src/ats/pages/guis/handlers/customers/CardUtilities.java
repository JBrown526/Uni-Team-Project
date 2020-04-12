package ats.pages.guis.handlers.customers;

import java.sql.*;

public interface CardUtilities {

    static boolean customerHasCard(String alias, String[] credentials) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT customer_alias FROM card WHERE customer_alias = ?;")) {
                ps.setString(1, alias);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }
}
