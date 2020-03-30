package ats.pages.guis;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class Blank extends TablePage {
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
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public Blank(App app, String blankID, boolean managerView) {
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
            int id = Integer.parseInt(staffIDField.getText());

            if (staffExists(id)) {
                // updates the blank to be assigned to the given staff member
                try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE ats.blank SET `staff_id` = ? WHERE blank_id = ?;")) {
                        ps.setInt(1, id);
                        ps.setString(2, blankID);
                        int affected = ps.executeUpdate();
                        System.out.println(affected);
                        populateTable();
                    }
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            } else {
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
                if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    staffIDField.setEditable(true);
                } else {
                    staffIDField.setEditable(false);
                    JOptionPane.showMessageDialog(null, "Staff ID must be numeric only");
                }
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
    private void populateTable() {
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

    // checks to see if selected staff member is in the system
    private boolean staffExists(int id) {
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
    //endregion
}
