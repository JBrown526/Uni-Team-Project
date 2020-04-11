package ats.pages.guis.homepages;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Reports extends Page {
    private boolean managerView;
    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JButton stockTurnover;
    private JButton interlineSalesButton;
    private JButton domesticSalesButton;

    public Reports(App app, boolean managerView) {
        this.managerView = managerView;

        stockTurnover.addActionListener(e -> app.toStockTurnover(managerView));
        interlineSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make interline report page
            }
        });
        domesticSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make domestic reports page
            }
        });

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
}
