package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;

public class SystemAdministrator extends Page {
    //================================================================================
    //region Properties
    //================================================================================
    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public SystemAdministrator(App app) {
        //================================================================================
        //region Button Listeners
        //================================================================================
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
