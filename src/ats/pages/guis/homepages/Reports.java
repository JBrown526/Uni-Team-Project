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
    private JButton individualSalesButton;
    private JButton globalSalesButton;

    public Reports(App app, boolean managerView) {
        this.managerView = managerView;

        stockTurnover.addActionListener(e -> app.toStockTurnover(managerView));
        individualSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.toIndividualReport(managerView);
            }
        });
        globalSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make global reports page
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
