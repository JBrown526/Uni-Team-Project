package ats.pages.guis.handlers.customers;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Customers extends TablePage {
    private String[] credentials;
    private boolean managerView;
    private String selectedCustomer;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable customersTable;
    private JButton addCustomerButton;
    private JButton viewCustomerButton;

    public Customers(App app, boolean managerView) {
        this.managerView = managerView;
        credentials = app.getDBCredentials();

        populateTable();

        addCustomerButton.addActionListener(e -> app.toCustomerAdd(managerView));
        viewCustomerButton.addActionListener(e -> {
            if (!selectedCustomer.equals("")) {
                app.toCustomer(selectedCustomer, managerView);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a customer");
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
        customersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customersTable.getSelectedRow();
                selectedCustomer = String.valueOf(customersTable.getValueAt(row, 0));
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer")) {
                try (ResultSet rs = ps.executeQuery()) {
                    customersTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
