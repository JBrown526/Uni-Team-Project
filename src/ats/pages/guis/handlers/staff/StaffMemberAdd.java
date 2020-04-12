package ats.pages.guis.handlers.staff;

import ats.App;
import ats.common.Utilities;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StaffMemberAdd extends Page implements Utilities, StaffUtilities {
    String[] credentials;

    private JPanel mainPanel;
    private JButton applyButton;
    private JTextField staffIDField;
    private JTextField roleField;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField postcodeField;
    private JTextField cityField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton backButton;
    private JButton logoutButton;

    public StaffMemberAdd(App app) {
        credentials = app.getDBCredentials();

        applyButton.addActionListener(e -> {
            if (requirementsMet()) {
                updateStaffMember();
            } else {
                JOptionPane.showMessageDialog(null, "A new user must have a valid Staff ID, Role, name and password");
            }
        });

        backButton.addActionListener(e -> app.toStaffMembers(true));
        logoutButton.addActionListener(e -> app.logout());

        staffIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveInt(ke, staffIDField, "Staff ID");
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public boolean requirementsMet() {
        if (!Utilities.isEmpty(staffIDField.getText())) {
            if (!Utilities.staffExists(Integer.parseInt(staffIDField.getText()), credentials)) {
                if (!Utilities.isEmpty(roleField.getText()) || StaffUtilities.validRole(roleField.getText(), credentials)) {
                    if (!Utilities.isEmpty(nameField.getText())) {
                        return !Utilities.isEmpty(String.valueOf(passwordField.getPassword()));
                    }
                    return false;
                }
                JOptionPane.showMessageDialog(null, "Invalid role chosen, please select a valid role");
                return false;
            }
            JOptionPane.showMessageDialog(null, "This Staff ID is already in use, select another ID");
            return false;
        }
        return false;
    }

    @Override
    public void updateStaffMember() {
        String newStaffID = staffIDField.getText();
        String newRole = roleField.getText().toUpperCase();
        String newName = nameField.getText();
        String newPassword = String.valueOf(passwordField.getPassword());
        String newPhoneNumber = phoneNumberField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();
        String newCity = cityField.getText();
        String newPostCode = postcodeField.getText();

        JTextField[] fields = {staffIDField, roleField, nameField, passwordField, phoneNumberField,
                emailField, addressField, cityField, postcodeField};

        String sqlFields = "staff_id, role_code, name, password,";
        String sqlUpdate = "'" + newStaffID + "', '" + newRole + "', '" + newName + "', '" + newPassword + "',";

        // adds relevant information the the sql string if the field has been filled in
        sqlFields += Utilities.isEmpty(newPhoneNumber) ? "" : " phone_number,";
        sqlUpdate += Utilities.isEmpty(newPhoneNumber) ? "" : " '" + newPhoneNumber + "',";
        sqlFields += Utilities.isEmpty(newEmail) ? "" : " email,";
        sqlUpdate += Utilities.isEmpty(newEmail) ? "" : " '" + newEmail + "',";
        sqlFields += Utilities.isEmpty(newAddress) ? "" : " address,";
        sqlUpdate += Utilities.isEmpty(newAddress) ? "" : " '" + newAddress + "',";
        sqlFields += Utilities.isEmpty(newCity) ? "" : " city,";
        sqlUpdate += Utilities.isEmpty(newCity) ? "" : " '" + newCity + "',";
        sqlFields += Utilities.isEmpty(newPostCode) ? "" : " postcode,";
        sqlUpdate += Utilities.isEmpty(newPostCode) ? "" : " '" + newPostCode + "',";

        sqlFields = Utilities.removeLastCharacter(sqlFields);
        sqlUpdate = Utilities.removeLastCharacter(sqlUpdate);
        String sql;
        sql = String.format("INSERT INTO ats.staff (%s) VALUES (%s);", sqlFields, sqlUpdate);

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.executeUpdate();

                for (JTextField field : fields) {
                    field.setText("");
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
