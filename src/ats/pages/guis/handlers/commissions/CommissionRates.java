package ats.pages.guis.handlers.commissions;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class CommissionRates extends TablePage {
    private String[] credentials;
    private int staffID;
    private int selectedCommissionRate = -1;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable commissionTable;
    private JButton newRateButton;
    private JButton viewRateButton;

    public CommissionRates(App app, int staffID) {
        this.staffID = staffID;
        credentials = app.getDBCredentials();

        populateTable();

        newRateButton.addActionListener(e -> app.toCommissionRateAdd(staffID));

        viewRateButton.addActionListener(e -> {
            if (selectedCommissionRate != -1) {
                app.toCommissionRate(staffID, selectedCommissionRate);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a commission rate");
            }
        });

        backButton.addActionListener(e -> app.toStaffMember(staffID, false));
        logoutButton.addActionListener(e -> app.logout());

        commissionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = commissionTable.getSelectedRow();
                selectedCommissionRate = Integer.parseInt(String.valueOf(commissionTable.getValueAt(row, 0)));
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM commission WHERE staff_id = ?")) {
                ps.setInt(1, staffID);
                try (ResultSet rs = ps.executeQuery()) {
                    commissionTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
