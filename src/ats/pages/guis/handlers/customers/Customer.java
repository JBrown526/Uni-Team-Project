package ats.pages.guis.handlers.customers;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;

public class Customer extends TablePage {
    private String[] credentials;
    private boolean managerView;
    private String customerAlias;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    public Customer(App app, String customerAlias, boolean managerView) {
        this.managerView = managerView;
        this.customerAlias = customerAlias;
        credentials = app.getDBCredentials();

        backButton.addActionListener(e -> app.toCustomers(managerView));
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
