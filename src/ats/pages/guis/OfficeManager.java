package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OfficeManager extends Page {
    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton saleViewButton;
    private JButton viewStaffButton;
    private JButton exchangeRatesButton;
    private JButton commissionRatesButton;
    private JButton viewBlanksButton;
    private JButton generateReportsButton;

    public OfficeManager(App app) {
        saleViewButton.addActionListener(e -> app.toTravelAgent());
        viewBlanksButton.addActionListener(e -> app.toBlanks(true));

        viewStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        generateReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        commissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        exchangeRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        backButton.addActionListener(e -> app.logout());
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
