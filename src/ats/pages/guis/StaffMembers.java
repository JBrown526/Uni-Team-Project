package ats.pages.guis;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class StaffMembers extends TablePage {
    private App app;
    private int selectedStaffMember;
    private boolean adminView;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable staffTable;
    private JPanel adminPanel;
    private JButton newUserButton;
    private JButton viewStaffMemberButtonAdmin;
    private JPanel managerPanel;
    private JButton viewStaffMemberButtonManager;

    public StaffMembers(App app, boolean adminView) {
        this.app = app;
        this.adminView = adminView;

        populateTable();

        if (adminView) {
            managerPanel.remove(viewStaffMemberButtonManager);
            mainPanel.remove(managerPanel);
        } else {
            adminPanel.remove(newUserButton);
            adminPanel.remove(viewStaffMemberButtonAdmin);
            mainPanel.remove(adminPanel);
        }

        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewStaffMemberButtonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewStaffMemberButtonManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = staffTable.getSelectedRow();
                selectedStaffMember = Integer.parseInt(String.valueOf(staffTable.getValueAt(row, 0)));
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void populateTable() {
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

}
