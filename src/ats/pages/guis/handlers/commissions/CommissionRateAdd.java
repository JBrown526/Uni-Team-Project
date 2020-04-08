package ats.pages.guis.handlers.commissions;

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

public class CommissionRateAdd extends Page implements CommissionUtilities {
    int staffID;
    String[] credentials;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JComboBox typeSelectBox;
    private JTextField commissionRateField;
    private JButton setRateButton;
    private JTextField dateField;

    public CommissionRateAdd(App app, int staffID) {
        this.staffID = staffID;
        credentials = app.getDBCredentials();
        Utilities.fillTypeDropdown(credentials, typeSelectBox, "commission");

        setRateButton.addActionListener(e -> {
            int type = (int) typeSelectBox.getItemAt(typeSelectBox.getSelectedIndex());
            String rateString = commissionRateField.getText();
            String date = dateField.getText();
            try {
                float newCommissionRate = Float.parseFloat(rateString);
                if (isValidDate(date) && !Utilities.isEmpty(date)) {
                    updateRate(type, newCommissionRate, date);
                    JOptionPane.showMessageDialog(null, "Commission rate successfully added");
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
    public void updateRate(int type, float rate, String date) {
        String sqlUpdate = "'" + type + "', '" + staffID + "', '" + rate + "', '" + date + "'";

        String sql;
        sql = String.format("INSERT INTO ats.commission VALUES (%s);", sqlUpdate);

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.executeUpdate();

                commissionRateField.setText("");
                dateField.setText("");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
