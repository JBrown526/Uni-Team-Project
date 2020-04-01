package ats.pages.guis.handlers.commissions;

import ats.App;
import ats.common.Utilities;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommissionRateAdd extends Page {
    int staffID;
    String[] credentials;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JComboBox typeSelectBox;
    private JTextField commissionRateField;
    private JButton setRateButton;
    private JTextField dateField;

    //TODO: Create This

    public CommissionRateAdd(App app, int staffID) {
        this.staffID = staffID;
        credentials = app.getDBCredentials();
        Utilities.fillTypeDropdown(credentials, typeSelectBox, "commission");

        setRateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        backButton.addActionListener(e -> app.toCommissionRates(staffID));
        logoutButton.addActionListener(e -> app.logout());

        commissionRateField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveFloat(ke, commissionRateField, "Commission Rate");
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
