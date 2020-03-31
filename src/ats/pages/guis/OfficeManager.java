package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton commissionRatesButton;
    private JButton viewBlanksButton;
    private JButton generateReportsButton;
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

        generateReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Generate reports page
            }
        });
        commissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Remove this
            }
        });
        exchangeRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: View/Set exchange rate page
            }
        });

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
