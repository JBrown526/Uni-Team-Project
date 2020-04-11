package ats.pages.guis.homepages;

import ats.App;
import ats.pages.Page;

import javax.swing.*;

public class OfficeManager extends Page {
    //================================================================================
    //region Properties
    //================================================================================
    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton saleViewButton;
    private JButton viewStaffButton;
    private JButton exchangeRatesButton;
    private JButton viewBlanksButton;
    private JButton reportsButton;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public OfficeManager(App app) {
        //================================================================================
        //region Button Listeners
        //================================================================================
        saleViewButton.addActionListener(e -> app.toTravelAgent());
        viewBlanksButton.addActionListener(e -> app.toBlanks(true));
        viewStaffButton.addActionListener(e -> app.toStaffMembers(false));
        reportsButton.addActionListener(e -> app.toReports(true));
        exchangeRatesButton.addActionListener(e -> app.toExchangeRates());

        backButton.addActionListener(e -> app.logout());
        logoutButton.addActionListener(e -> app.logout());
        //endregion
    }
    //endregion

    //================================================================================
    //region Accessors
    //================================================================================
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
    //endregion
}
