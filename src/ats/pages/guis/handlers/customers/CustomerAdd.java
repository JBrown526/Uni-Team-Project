package ats.pages.guis.handlers.customers;

import ats.App;
import ats.pages.Page;

import javax.swing.*;

public class CustomerAdd extends Page {
    private String[] credentials;
    private boolean managerView;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    public CustomerAdd(App app, boolean managerView){
        this.managerView = managerView;
        credentials = app.getDBCredentials();

        backButton.addActionListener(e -> app.toCustomers(managerView));
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
