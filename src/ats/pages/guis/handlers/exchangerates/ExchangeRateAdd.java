package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.Page;

import javax.swing.*;

public class ExchangeRateAdd extends Page {
    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;

    public ExchangeRateAdd(App app) {
        backButton.addActionListener(e -> app.toExchangeRates());
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
