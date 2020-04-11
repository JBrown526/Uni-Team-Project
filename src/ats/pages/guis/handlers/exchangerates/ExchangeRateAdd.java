package ats.pages.guis.handlers.exchangerates;

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

public class ExchangeRateAdd extends Page implements Utilities, ExchangeRateUtilities {
    private String[] credentials;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JButton setRateButton;
    private JTextField dateField;
    private JTextField currencyCodeField;
    private JTextField exchangeRateField;

    public ExchangeRateAdd(App app) {
        credentials = app.getDBCredentials();

        setRateButton.addActionListener(e -> {
            String currencyCode = currencyCodeField.getText();
            String rateString = exchangeRateField.getText();
            String date = dateField.getText();
            try {
                float newExchangeRate = Float.parseFloat(rateString);
                if (!ExchangeRateUtilities.exchangeRateExists(currencyCode, credentials)) {
                    if (ExchangeRateUtilities.conditionsMet(currencyCode, date, credentials)) {
                        updateRate(currencyCode, newExchangeRate, date);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Exchange rate is already in the system, please update it instead.");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Exchange rates must be in the form of a valid float");
                exchangeRateField.setText("");
            }
        });

        backButton.addActionListener(e -> app.toExchangeRates());
        logoutButton.addActionListener(e -> app.logout());

        exchangeRateField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveFloat(ke, exchangeRateField, "Exchange Rate");
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateRate(String currencyCode, float exchangeRate, String dateSet) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO ats.exchange_rate VALUES (?, ?, ?)")) {
                ps.setString(1, currencyCode);
                ps.setFloat(2, exchangeRate);
                ps.setString(3, dateSet);
                ps.executeUpdate();

                currencyCodeField.setText("");
                exchangeRateField.setText("");
                dateField.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
