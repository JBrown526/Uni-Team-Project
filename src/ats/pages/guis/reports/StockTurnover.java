package ats.pages.guis.reports;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;

public class StockTurnover extends TablePage {
    private boolean managerView;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;

    public StockTurnover(App app, boolean managerView) {
        //TODO: this
        this.managerView = managerView;

        backButton.addActionListener(e -> {
            if (managerView) {
                //TODO: make manager reports homepage
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
