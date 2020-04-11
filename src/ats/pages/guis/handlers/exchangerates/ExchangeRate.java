package ats.pages.guis.handlers.exchangerates;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExchangeRate extends TablePage {
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

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    }
}
