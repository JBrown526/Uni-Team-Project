package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExchangeRates extends TablePage {
    String[] credentials;
    private String selectedExchangeRate = "";

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable exchangeRatesTable;
    private JButton newRateButton;
    private JButton viewRateButton;

    public ExchangeRates(App app) {
        credentials = app.getDBCredentials();

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
    protected void populateTable() {

    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
