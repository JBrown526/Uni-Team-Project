package ats.pages.guis.handlers.customers;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;

public class Customers extends TablePage {
    private String[] credentials;
    private boolean managerView;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    public Customers(App app, boolean managerView) {
        this.managerView = managerView;
        credentials = app.getDBCredentials();

        backButton.addActionListener(e -> {
            if (managerView) {
                app.toOfficeManager();
            } else {
                app.toTravelAgent();
            }
        });
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
