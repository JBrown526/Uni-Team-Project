package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemAdministrator extends Page {
    //================================================================================
    //region Properties
    //================================================================================
    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton saleViewButton;
    private JButton viewStaffButton;
    private JButton databaseToolsButton;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public SystemAdministrator(App app) {
        //================================================================================
        //region Button Listeners
        //================================================================================
        saleViewButton.addActionListener(e -> app.toTravelAgent());

        viewStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        databaseToolsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
