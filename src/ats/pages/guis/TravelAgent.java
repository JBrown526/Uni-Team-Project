package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TravelAgent extends Page {
    private JButton blanksButton;
    private JButton transactionButton;
    private JButton customersButton;
    private JButton backButton;
    private JPanel mainPanel;
    private JButton logoutButton;

    public TravelAgent(App app) {
        blanksButton.addActionListener(e -> app.toBlanks(false));

        customersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
