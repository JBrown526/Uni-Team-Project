package ats.pages.guis.handlers.blanks;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class Blank extends TablePage implements Utilities {
    //================================================================================
    //region Properties
    //================================================================================
    private String blankID;
    private String[] credentials;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable blankTable;
    private JTextField staffIDField;
    private JButton reassignButton;
    private JPanel managerPanel;
    private JTextField dateField;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public Blank(App app, String blankID, boolean managerView) {
        //TODO: set blank to void
        //TODO: filter blank by status/type
        //TODO: search for blank
        this.blankID = blankID;
        credentials = app.getDBCredentials();

        populateTable();

        // hides the manager tools when viewing in sale mode
        if (!managerView) {
            managerPanel.remove(staffIDField);
            managerPanel.remove(reassignButton);
            mainPanel.remove(managerPanel);
        }

        //================================================================================
        //region Button Listeners
        //================================================================================
        reassignButton.addActionListener(e -> {
            int staffID = Integer.parseInt(staffIDField.getText());
            String date = dateField.getText();

            if (Utilities.staffExists(staffID, credentials)) {
                if (isValidDate(date)) {
                    if (isAvailable()) {
                        reassignBlank(staffID, date);
                    }
                } else {
                    dateField.setText("");
                    JOptionPane.showMessageDialog(null, "Invalid Date/Date Format supplied. Please use YYYY-MM-DD format");
                }
            } else {
                staffIDField.setText("");
                JOptionPane.showMessageDialog(null, "This Staff Member is not in the System");
            }
        });

        logoutButton.addActionListener(e -> app.logout());
        backButton.addActionListener(e -> app.toBlanks(managerView));
        //endregion

        //================================================================================
        //region Other Listeners
        //================================================================================
        // prevents invalid entries in the staffID field
        staffIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveInt(ke, staffIDField, "Staff ID");
            }
        });
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
    // populates the table for the given blank
    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM blank WHERE blank_id = ?")) {
                ps.setString(1, blankID);
                try (ResultSet rs = ps.executeQuery()) {
                    blankTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private boolean isAvailable() {
        boolean passesChecks = true;
        if (isStatus(credentials, blankID, "ASGN")) {
            int input = JOptionPane.showConfirmDialog(null,
                    "This blank is already assigned, do you wish to reassign it?", "", JOptionPane.YES_NO_OPTION);
            passesChecks = input == 0;
        }
        if (isStatus(credentials, blankID, "VOID")) {
            JOptionPane.showMessageDialog(null, "This blank has been voided, it cannot be reassigned");
            passesChecks = false;
        }
        if (isStatus(credentials, blankID, "SOLD")) {
            JOptionPane.showMessageDialog(null, "This blank has been sold, it cannot be reassigned");
            passesChecks = false;
        }
        return passesChecks;
    }

    private void reassignBlank(int staffID, String date) {
        //TODO: Make sure isn't assigned before received
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE ats.blank SET `staff_id` = ?, `date_assigned` = ?, `blank_status` = ? WHERE blank_id = ?;")) {
                ps.setInt(1, staffID);
                ps.setString(2, date);
                ps.setString(3, "ASGN");
                ps.setString(4, blankID);
                ps.executeUpdate();

                dateField.setText("");
                staffIDField.setText("");
                populateTable();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    //endregion
}
