package ats.pages.guis.reports;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockTurnover extends TablePage {
    private boolean managerView;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable stockTurnoverTable;
    private JPanel managerPanel;
    private JButton viewIndividualTurnoverButton;
    private JButton resetViewButton;
    private JComboBox staffMemberComboBox;

    public StockTurnover(App app, boolean managerView) {
        //TODO: this
        this.managerView = managerView;

        viewIndividualTurnoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        backButton.addActionListener(e -> {
            if (managerView) {
                app.toReports(true);
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
