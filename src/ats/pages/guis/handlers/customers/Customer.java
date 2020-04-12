package ats.pages.guis.handlers.customers;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Customer extends TablePage {
    private String[] credentials;
    private boolean managerView;
    private String customerAlias;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable customerTable;
    private JButton applyButton;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField postcodeField;
    private JTextField cityField;
    private JTextField emailField;
    private JComboBox statusComboBox;
    private JTextField discountRateField;

    public Customer(App app, String customerAlias, boolean managerView) {
        this.managerView = managerView;
        this.customerAlias = customerAlias;
        credentials = app.getDBCredentials();
        Utilities.fillCustomerStatusDropdown(credentials, statusComboBox);

        populateTable();

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CustomerUtilities.commonRequirementsMet(managerView, discountRateField, statusComboBox, nameField)) {

                }
            }
        });

        backButton.addActionListener(e -> app.toCustomers(managerView));
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer WHERE customer_alias = ?")) {
                ps.setString(1, customerAlias);
                try (ResultSet rs = ps.executeQuery()) {
                    customerTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
