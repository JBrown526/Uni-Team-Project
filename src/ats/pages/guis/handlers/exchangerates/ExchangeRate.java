package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ExchangeRate extends TablePage {
    private String[] credentials;
    private String currencyCode;

    private JPanel mainPanel;
    private JButton logoutButton;
    private JButton backButton;
    private JTable exchangeRateTable;
    private JTextField exchangeRateField;
    private JTextField dateField;
    private JButton updateButton;

    public ExchangeRate(App app, String currencyCode) {
        this.currencyCode = currencyCode;
        credentials = app.getDBCredentials();

        populateTable();

        updateButton.addActionListener(e -> {
            String rateString = exchangeRateField.getText();
            String date = dateField.getText();
            try {
                float newExchangeRate = Float.parseFloat(rateString);
                if (ExchangeRateUtilities.conditionsMet(currencyCode, date)) {
                    updateRate(currencyCode, newExchangeRate, date);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Exchange rates must be in the form of a valid float");
                exchangeRateField.setText("");
            }
        });

        backButton.addActionListener(e -> app.toExchangeRates());
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM exchange_rate WHERE currency_code = ?")) {
                ps.setString(1, currencyCode);
                try (ResultSet rs = ps.executeQuery()) {
                    exchangeRateTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void updateRate(String currencyCode, float rate, String date) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE ats.exchange_rate SET exchange_rate = ?, date_set = ? WHERE currency_code = ?")) {
                ps.setFloat(1, rate);
                ps.setString(2, date);
                ps.setString(3, currencyCode);
                ps.executeUpdate();

                exchangeRateField.setText("");
                dateField.setText("");
                populateTable();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
