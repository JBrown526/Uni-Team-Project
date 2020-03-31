package ats.pages.guis.handlers.staff;

import ats.App;
import ats.common.Utilities;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StaffMemberAdd extends Page implements Utilities, StaffChanges {

    private JPanel mainPanel;
    private JButton applyButton;
    private JTextField roleField;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField postcodeField;
    private JTextField cityField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton backButton;
    private JButton logoutButton;
    private JTextField staffIDField;

    public StaffMemberAdd(App app) {
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        backButton.addActionListener(e -> app.toStaffMembers(true));
        logoutButton.addActionListener(e -> app.logout());

        staffIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void updateStaffMember() {

    }
}
