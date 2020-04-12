package ats.pages.guis.handlers.customers;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardDetails extends TablePage {
    String[] credentials;
    String customerAlias;

    private JButton backButton;
    private JButton logoutButton;
    private JTable cardTable;
    private JTextField cardNumberField;
    private JButton applyButton;
    private JTextField cardHolderField;
    private JTextField expiryDateField;
    private JPanel mainPanel;

    public CardDetails(App app, String customerAlias, boolean managerView) {
        this.customerAlias = customerAlias;
        credentials = app.getDBCredentials();

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        backButton.addActionListener(e -> app.toCustomer(customerAlias, managerView));
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
