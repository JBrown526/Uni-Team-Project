package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;

public class SystemAdministrator extends Page {
    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    public SystemAdministrator(App app) {
        backButton.addActionListener(e -> app.logout());
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
