package ats.common;

import java.sql.*;
import java.util.Optional;

public interface Utilities {

    static boolean isEmpty(String fieldValue) {
        return fieldValue.equals("");
    }

    static String removeLastCharacter(String str) {
        return Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
    }

    static int setStatementValue(PreparedStatement ps, String str, int i) throws SQLException {
        if (!isEmpty(str)) {
            ps.setString(i, str);
            i++;
        }
        return i;
    }

    static boolean staffExists(int id, String[] credentials) {
        boolean staffExists = false;

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT staff_id FROM staff WHERE staff_id = ?;")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        staffExists = true;
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return staffExists;
    }
}