package ats.pages.guis.handlers.payments;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;

public class Transaction extends TablePage {
    String[] credentials;
    String blankID;


    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;

    public Transaction(App app, String blankID, boolean managerView) {
        this.blankID = blankID;
        credentials = app.getDBCredentials();

        backButton.addActionListener(e -> app.toBlank(blankID, managerView));
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
