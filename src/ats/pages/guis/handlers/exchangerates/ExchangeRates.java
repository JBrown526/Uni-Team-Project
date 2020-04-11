package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ExchangeRates extends TablePage {
    String[] credentials;
    private String selectedExchangeRate;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable exchangeRatesTable;
    private JButton newRateButton;
    private JButton viewRateButton;

    public ExchangeRates(App app) {
        credentials = app.getDBCredentials();
        selectedExchangeRate = "";

        populateTable();

        newRateButton.addActionListener(e -> app.toExchangeRateAdd());
        viewRateButton.addActionListener(e -> {
            if (!selectedExchangeRate.equals("")) {
                app.toExchangeRate(selectedExchangeRate);
            } else {
                JOptionPane.showMessageDialog(null, "Please select an exchange rate");
            }
        });

        backButton.addActionListener(e -> app.toOfficeManager());
        logoutButton.addActionListener(e -> app.logout());

        exchangeRatesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = exchangeRatesTable.getSelectedRow();
                selectedExchangeRate = String.valueOf(exchangeRatesTable.getValueAt(row, 0));
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
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM exchange_rate")) {
                try (ResultSet rs = ps.executeQuery()) {
                    exchangeRatesTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
