package ats.pages.guis.handlers.customers;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.sql.*;

public class Customer extends TablePage {
    private String[] credentials;
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
        this.customerAlias = customerAlias;
        credentials = app.getDBCredentials();
        Utilities.fillCustomerStatusDropdown(credentials, statusComboBox);

        populateTable();

        applyButton.addActionListener(e -> {
            if (CustomerUtilities.commonRequirementsMet(managerView, discountRateField, statusComboBox, nameField)) {
                updateCustomer();
                JOptionPane.showMessageDialog(null, "Customer successfully updated!");
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

    private void updateCustomer() {
        String statusCode = String.valueOf(statusComboBox.getItemAt(statusComboBox.getSelectedIndex()));
        String name = nameField.getText();
        String discountRateString = discountRateField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String postcode = postcodeField.getText();

        JTextField[] fields = {nameField, discountRateField, phoneNumberField,
                emailField, addressField, cityField, postcodeField};
        String[] newValues = {statusCode, name, discountRateString, phoneNumber,
                email, address, city, postcode};

        String sqlUpdate = "";

        sqlUpdate += Utilities.isEmpty(statusCode) ? "" : " status_code = ?,";
        sqlUpdate += Utilities.isEmpty(name) ? "" : " name = ?,";
        sqlUpdate += Utilities.isEmpty(discountRateString) ? "" : " discount_rate = ?,";
        sqlUpdate += Utilities.isEmpty(phoneNumber) ? "" : " phone_number = ?,";
        sqlUpdate += Utilities.isEmpty(email) ? "" : " email = ?,";
        sqlUpdate += Utilities.isEmpty(address) ? "" : " address = ?,";
        sqlUpdate += Utilities.isEmpty(city) ? "" : " city = ?,";
        sqlUpdate += Utilities.isEmpty(postcode) ? "" : " postcode = ?,";

        sqlUpdate = Utilities.removeLastCharacter(sqlUpdate);
        String sql = String.format("UPDATE ats.customer SET%s WHERE customer_alias = ?;", sqlUpdate);

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int insertPosition = 1;

                // adds the new information to the statement and increments the insert position index if the field is not empty
                for (String newValue : newValues) {
                    insertPosition = Utilities.setStatementValue(ps, newValue, insertPosition);
                }

                ps.setString(insertPosition, customerAlias);
                ps.executeUpdate();

                for (JTextField field : fields) {
                    field.setText("");
                }

                populateTable();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
