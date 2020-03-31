package ats.pages.guis.handlers.staff;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.sql.*;

public class StaffMember extends TablePage implements Utilities, StaffChanges {
    //================================================================================
    //region Properties
    //================================================================================
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
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public StaffMember(App app, int staffID, boolean adminView) {
        this.staffID = staffID;
        credentials = app.getDBCredentials();

        populateTable();

        // TODO: Manager tools
        // TODO: Delete staff

        // hides admin tools when in manager view
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

        //================================================================================
        //region Button Listeners
        //================================================================================
        applyButton.addActionListener(e -> {
            if (requirementsMet()) {
                updateStaffMember();
            }
        });

        backButton.addActionListener(e -> app.toStaffMembers(adminView));
        logoutButton.addActionListener(e -> app.logout());
        //endregion
    }
    //endregion

    //================================================================================
    //region Accessors
    //================================================================================
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
    //endregion

    //================================================================================
    //region Methods
    //================================================================================
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

    @Override
    public boolean requirementsMet() {
        if (Utilities.isEmpty(roleField.getText()) || StaffChanges.validRole(roleField.getText(), credentials)) {
            // ensures at least one field has been filled
            if (!Utilities.isEmpty(roleField.getText()) || !Utilities.isEmpty(nameField.getText())
                    || !Utilities.isEmpty(String.valueOf(passwordField.getPassword()))
                    || !Utilities.isEmpty(phoneNumberField.getText()) || !Utilities.isEmpty((emailField.getText()))
                    || !Utilities.isEmpty(addressField.getText()) || !Utilities.isEmpty(cityField.getText())
                    || !Utilities.isEmpty(postcodeField.getText())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in new details");
            }
        } else {
            JOptionPane.showMessageDialog(null, "That is an invalid user role");
        }
        return false;
    }

    @Override
    public void updateStaffMember() {
        String newRole = roleField.getText().toUpperCase();
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

        // adds relevant information the the sql string if the field has been filled in
        sqlUpdate += Utilities.isEmpty(newRole) ? "" : " role_code = ?,";
        sqlUpdate += Utilities.isEmpty(newName) ? "" : " name = ?,";
        sqlUpdate += Utilities.isEmpty(newPassword) ? "" : " password = ?,";
        sqlUpdate += Utilities.isEmpty(newPhoneNumber) ? "" : " phone_number = ?,";
        sqlUpdate += Utilities.isEmpty(newEmail) ? "" : " email = ?,";
        sqlUpdate += Utilities.isEmpty(newAddress) ? "" : " address = ?,";
        sqlUpdate += Utilities.isEmpty(newCity) ? "" : " city = ?,";
        sqlUpdate += Utilities.isEmpty(newPostCode) ? "" : " postcode = ?,";

        // trims the last ',' from the update portion of the string and makes the full query
        sqlUpdate = Utilities.removeLastCharacter(sqlUpdate);
        String sql = String.format("UPDATE ats.staff SET%s WHERE staff_id = ?;", sqlUpdate);

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int insertPosition = 1;

                // adds the new information to the statement and increments the insert position index if the field is not empty
                for (String newField : newFields) {
                    insertPosition = Utilities.setStatementValue(ps, newField, insertPosition);
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
    //endregion
}
