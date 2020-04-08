package ats.pages.guis.handlers.commissions;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class CommissionRate extends TablePage implements Utilities, CommissionUtilities {
    private int blankType;
    private int staffID;
    private String[] credentials;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable commissionTable;
    private JTextField commissionRateField;
    private JTextField dateField;
    private JButton updateButton;

    public CommissionRate(App app, int staffID, int blankType) {
        this.blankType = blankType;
        this.staffID = staffID;
        credentials = app.getDBCredentials();

        populateTable();

        updateButton.addActionListener(e -> {
            String rateString = commissionRateField.getText();
            String date = dateField.getText();

            try {
                float newCommissionRate = Float.parseFloat(rateString);
                if (isValidDate(date) && !Utilities.isEmpty(date)) {
                    updateRate(blankType, newCommissionRate, date);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid date");
                    dateField.setText("");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Commission rates must be in the form of a valid float");
                commissionRateField.setText("");
            }
        });

        backButton.addActionListener(e -> app.toCommissionRates(staffID));
        logoutButton.addActionListener(e -> app.logout());

        commissionRateField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveFloat(ke, commissionRateField, "Commission Rate");
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
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM commission WHERE staff_id = ? AND blank_type = ?")) {
                ps.setInt(1, staffID);
                ps.setInt(2, blankType);
                try (ResultSet rs = ps.executeQuery()) {
                    commissionTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public void updateRate(int blankType, float rate, String date) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE ats.commission SET commission = ?, date_set = ? WHERE blank_type = ? AND staff_id = ?;")) {
                ps.setFloat(1, rate);
                ps.setString(2, date);
                ps.setInt(3, blankType);
                ps.setInt(4, staffID);
                ps.executeUpdate();
                commissionRateField.setText("");
                dateField.setText("");
                populateTable();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
