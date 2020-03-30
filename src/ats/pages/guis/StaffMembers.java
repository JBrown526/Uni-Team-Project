package ats.pages.guis;

import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StaffMembers extends TablePage {
    private int selectedStaffMember;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable staffTable;
    private JPanel adminPanel;
    private JButton newUserButton;
    private JButton viewStaffMemberButtonAdmin;
    private JPanel managerPanel;
    private JButton viewStaffMemberButtonManager;

    public StaffMembers() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewStaffMemberButtonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        viewStaffMemberButtonManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = staffTable.getSelectedRow();
                selectedStaffMember = Integer.parseInt(String.valueOf(staffTable.getValueAt(row, 0)));
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

}
