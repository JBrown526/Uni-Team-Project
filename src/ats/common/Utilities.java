package ats.common;

import javax.swing.*;
import java.sql.*;
import java.util.Optional;
import java.util.Vector;

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
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT staff_id FROM staff WHERE staff_id = ?;")) {
                ps.setInt(1, id);
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

    static boolean blankExists(String id, String[] credentials) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT blank_id FROM blank WHERE blank_id = ?;")) {
                ps.setString(1, id);
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

    static boolean customerExists(String id, String[] credentials) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT customer_alias FROM customer WHERE customer_alias = ?;")) {
                ps.setString(1, id);
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

    static void fillTypeDropdown(String[] credentials, JComboBox typeSelectBox, String table) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            String sql = String.format("SELECT * FROM %s", table);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    Vector<Integer> types = new Vector<>();
                    while (rs.next()) {
                        types.add(rs.getInt("blank_type"));
                    }
                    final DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>(types);
                    typeSelectBox.setModel(model);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    static void fillStaffDropdown(String[] credentials, JComboBox staffSelectBox) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT staff_id FROM staff")) {
                try (ResultSet rs = ps.executeQuery()) {
                    Vector<Integer> ids = new Vector<>();
                    while (rs.next()) {
                        ids.add(rs.getInt("staff_id"));
                    }
                    final DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>(ids);
                    staffSelectBox.setModel(model);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    static void fillCustomerStatusDropdown(String[] credentials, JComboBox statusBox) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT status_code FROM customer_status " +
                    "ORDER BY IF(status_code = 'RG', NULL, status_code)")) {
                try (ResultSet rs = ps.executeQuery()) {
                    Vector<String> ids = new Vector<>();
                    while (rs.next()) {
                        ids.add(rs.getString("status_code"));
                    }
                    final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(ids);
                    statusBox.setModel(model);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    static void fillCustomerDropdown(String[] credentials, JComboBox customerBox) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT customer_alias FROM customer " +
                    "ORDER BY IF(customer_alias = 'Anon', NULL, customer_alias)")) {
                try (ResultSet rs = ps.executeQuery()) {
                    Vector<String> aliases = new Vector<>();
                    while (rs.next()) {
                        aliases.add(rs.getString("customer_alias"));
                    }
                    final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(aliases);
                    customerBox.setModel(model);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    static void fillCurrencyDropdown(String[] credentials, JComboBox currencyBox) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT currency_code FROM exchange_rate")) {
                try (ResultSet rs = ps.executeQuery()) {
                    Vector<String> code = new Vector<>();
                    while (rs.next()) {
                        code.add(rs.getString("currency_code"));
                    }
                    final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(code);
                    currencyBox.setModel(model);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
