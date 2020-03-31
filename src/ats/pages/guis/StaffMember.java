package ats.pages.guis;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.sql.*;
import java.util.Optional;

public class StaffMember extends TablePage {
    int staffID;
    String[] credentials;

    private JButton backButton;
    private JButton logoutButton;
    private JTable blankTable;
    private JPanel adminPanel;
    private JButton applyButton;
    private JTextField roleField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField postcodeField;
    private JPanel mainPanel;

    public StaffMember(App app, int staffID, boolean adminView) {
        this.staffID = staffID;
        credentials = app.getDBCredentials();

        populateTable();

        if (!adminView) {
            adminPanel.remove(roleField);
            adminPanel.remove(nameField);
            adminPanel.remove(passwordField);
            adminPanel.remove(phoneNumberField);
            adminPanel.remove(emailField);
            adminPanel.remove(addressField);
            adminPanel.remove(cityField);
            adminPanel.remove(postcodeField);
            mainPanel.remove(adminPanel);
        }

        applyButton.addActionListener(e -> {
            if (isEmpty(roleField.getText()) || validRole(roleField.getText())) {
                // ensures at least one field has been filled
                if (!isEmpty(roleField.getText()) || !isEmpty(nameField.getText())
                        || !isEmpty(String.valueOf(passwordField.getPassword()))
                        || !isEmpty(phoneNumberField.getText()) || !isEmpty((emailField.getText()))
                        || !isEmpty(addressField.getText()) || !isEmpty(cityField.getText())
                        || !isEmpty(postcodeField.getText())) {
                    updateStaffMember();
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in new details");
                }
            } else {
                JOptionPane.showMessageDialog(null, "That is an invalid user role");
            }
        });

        backButton.addActionListener(e -> app.toStaffMembers(adminView));
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM staff WHERE staff_id = ?")) {
                ps.setInt(1, staffID);
                try (ResultSet rs = ps.executeQuery()) {
                    blankTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private static boolean isEmpty(String fieldValue) {
        return fieldValue.equals("");
    }

    private boolean validRole(String role) {
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

    private static String removeLastCharacter(String str) {
        return Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
    }

    private static int setStatementValue(PreparedStatement ps, String str, int i) throws SQLException {
        if (!isEmpty(str)) {
            ps.setString(i, str);
            i++;
        }
        return i;
    }

    private void updateStaffMember() {
        String newRole = roleField.getText();
        String newName = nameField.getText();
        String newPassword = String.valueOf(passwordField.getPassword());
        String newPhoneNumber = phoneNumberField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();
        String newCity = cityField.getText();
        String newPostCode = postcodeField.getText();
        JTextField[] fields = {roleField, nameField, passwordField, phoneNumberField,
                emailField, addressField, cityField, postcodeField};
        String[] newFields = {newRole, newName, newPassword, newPhoneNumber,
                newEmail, newAddress, newCity, newPostCode};

        String sqlUpdate = "";

        sqlUpdate += isEmpty(newRole) ? "" : " role_code = ?,";
        sqlUpdate += isEmpty(newName) ? "" : " name = ?,";
        sqlUpdate += isEmpty(newPassword) ? "" : " password = ?,";
        sqlUpdate += isEmpty(newPhoneNumber) ? "" : " phone_number = ?,";
        sqlUpdate += isEmpty(newEmail) ? "" : " email = ?,";
        sqlUpdate += isEmpty(newAddress) ? "" : " address = ?,";
        sqlUpdate += isEmpty(newCity) ? "" : " city = ?,";
        sqlUpdate += isEmpty(newPostCode) ? "" : " postcode = ?,";
        sqlUpdate = removeLastCharacter(sqlUpdate);

        String sql = String.format("UPDATE ats.staff SET%s WHERE staff_id = ?;", sqlUpdate);

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int insertPosition = 1;

                for (String newField : newFields) {
                    insertPosition = setStatementValue(ps, newField, insertPosition);
                }

                ps.setInt(insertPosition, staffID);
                ps.executeUpdate();

                for (JTextField field : fields) {
                    field.setText("");
                }

                populateTable();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
