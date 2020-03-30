package ats.pages.guis;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
}
