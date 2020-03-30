package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TravelAgent extends Page {
    //================================================================================
    //region Properties
    //================================================================================
    private JButton blanksButton;
    private JButton transactionButton;
    private JButton customersButton;
    private JButton backButton;
    private JPanel mainPanel;
    private JButton logoutButton;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public TravelAgent(App app) {
        //================================================================================
        //region Button Listeners
        //================================================================================
        blanksButton.addActionListener(e -> app.toBlanks(false));

        customersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: View customers page(s)
            }
        });
        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Make transaction page(s)
                // NOTE: May move to blank page
            }
        });

        backButton.addActionListener(e -> {
            if (app.getStaffRole().equals("OM")) {
                app.toOfficeManager();
            } else if (app.getStaffRole().equals("SA")) {
                app.toSystemAdministrator();
            } else {
                app.logout();
            }
        });
        logoutButton.addActionListener(e -> app.logout());
        //endregion
    }
    //endregion

    //region Accessors
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
    //endregion
}
