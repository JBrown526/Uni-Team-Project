package ats.pages.guis.handlers.staff;

import java.sql.*;

public interface StaffChanges {
    static boolean validRole(String role, String[] credentials) {
        boolean validRole = false;

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT role_code FROM role WHERE role_code = ?;")) {
                ps.setString(1, role);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        validRole = true;
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return validRole;
    }

    void updateStaffMember();
}
