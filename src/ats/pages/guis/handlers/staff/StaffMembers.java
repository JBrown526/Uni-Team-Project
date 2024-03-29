package ats.pages.guis.handlers.staff;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class StaffMembers extends TablePage {
    //================================================================================
    //region Properties
    //================================================================================
    private App app;
    private int selectedStaffMember = -1;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable staffTable;
    private JPanel adminPanel;
    private JButton newUserButton;
    private JButton viewStaffMemberButtonAdmin;
    private JPanel managerPanel;
    private JButton viewStaffMemberButtonManager;
    //endregion

    //region Constructor
    public StaffMembers(App app, boolean adminView) {
        this.app = app;

        populateTable();

        // hides admin tools from managers and vice versa
        if (adminView) {
            managerPanel.remove(viewStaffMemberButtonManager);
            mainPanel.remove(managerPanel);
        } else {
            adminPanel.remove(newUserButton);
            adminPanel.remove(viewStaffMemberButtonAdmin);
            mainPanel.remove(adminPanel);
        }

        //================================================================================
        //region Button Listeners
        //================================================================================
        newUserButton.addActionListener(e -> app.toStaffMemberAdd());

        viewStaffMemberButtonAdmin.addActionListener(e -> {
            if (selectedStaffMember != -1) {
                app.toStaffMember(selectedStaffMember, true);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a staff member");
            }
        });

        viewStaffMemberButtonManager.addActionListener(e -> {
            if (selectedStaffMember != -1) {
                app.toStaffMember(selectedStaffMember, false);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a staff member");
            }
        });

        logoutButton.addActionListener(e -> app.logout());
        backButton.addActionListener(e -> {
            if (adminView) {
                app.toSystemAdministrator();
            } else {
                app.toOfficeManager();
            }
        });
        //endregion

        //================================================================================
        //region Other Listeners
        //================================================================================
        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = staffTable.getSelectedRow();
                selectedStaffMember = Integer.parseInt(String.valueOf(staffTable.getValueAt(row, 0)));
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
    @Override
    protected void populateTable() {
        String[] credentials = app.getDBCredentials();

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM staff")) {
                try (ResultSet rs = ps.executeQuery()) {
                    staffTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    //endregion
}
