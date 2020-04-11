package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;

public class ExchangeRates extends TablePage {
    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;

    public ExchangeRates(App app) {
        backButton.addActionListener(e -> app.toOfficeManager());
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
