package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;

public class ExchangeRate extends TablePage {
    private String currencyCode;

    private JPanel mainPanel;
    private JButton logoutButton;
    private JButton backButton;

    public ExchangeRate(App app, String currencyCode) {
        this.currencyCode = currencyCode;

        backButton.addActionListener(e -> app.toExchangeRates());
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    protected void populateTable() {

    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
