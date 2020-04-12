package ats.pages.guis.handlers.customers;

import ats.App;
import ats.common.Utilities;
import ats.pages.Page;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerAdd extends Page {
    private String[] credentials;
    private boolean managerView;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton applyButton;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField postcodeField;
    private JTextField cityField;
    private JTextField emailField;
    private JTextField discountRateField;
    private JTextField aliasField;
    private JComboBox statusComboBox;

    public CustomerAdd(App app, boolean managerView) {
        this.managerView = managerView;
        credentials = app.getDBCredentials();
        Utilities.fillCustomerStatusDropdown(credentials, statusComboBox);

        backButton.addActionListener(e -> app.toCustomers(managerView));
        logoutButton.addActionListener(e -> app.logout());
        applyButton.addActionListener(e -> {
            if (requirementsMet()) {
                updateCustomer();
            } else {
                JOptionPane.showMessageDialog(null, "A new customer must have a valid alias and name");
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private boolean requirementsMet() {
        if (!Utilities.isEmpty(aliasField.getText())) {
            if (!Utilities.customerExists(aliasField.getText(), credentials)) {
                try {
                    if (!Utilities.isEmpty(discountRateField.getText())) {
                        Float.parseFloat(discountRateField.getText());
                    }
                    String status = String.valueOf(statusComboBox.getItemAt(statusComboBox.getSelectedIndex()));
                    if (!status.equals("RG") && !managerView) {
                        int input = JOptionPane.showConfirmDialog(null,
                                "Valued customer status requires Office Manager approval, select yes if you have Office Manager approval", "", JOptionPane.YES_NO_OPTION);
                        if (input != 0) {
                            return false;
                        }
                    }
                    return !Utilities.isEmpty(nameField.getText());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Discount rates must be in the form of a valid float");
                    return false;
                }
            }
            JOptionPane.showMessageDialog(null, "This alias is already in use, please select another one");
        }
        return false;
    }

    private void updateCustomer() {
        String alias = aliasField.getText();
        String statusCode = String.valueOf(statusComboBox.getItemAt(statusComboBox.getSelectedIndex()));
        String name = nameField.getText();
        String discountRateString = discountRateField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String postcode = postcodeField.getText();

        JTextField[] fields = {aliasField, nameField, discountRateField, phoneNumberField,
                emailField, addressField, cityField, postcodeField};

        String sqlFields = "customer_alias, status_code, name,";
        String sqlUpdate = "'" + alias + "', '" + statusCode + "', '" + name + "',";


        sqlFields += Utilities.isEmpty(discountRateString) ? "" : " discount_rate,";
        sqlUpdate += Utilities.isEmpty(discountRateString) ? "" : Float.parseFloat(discountRateString) + ",";
        sqlFields += Utilities.isEmpty(phoneNumber) ? "" : " phone_number,";
        sqlUpdate += Utilities.isEmpty(phoneNumber) ? "" : " '" + phoneNumber + "',";
        sqlFields += Utilities.isEmpty(phoneNumber) ? "" : " phone_number,";
        sqlUpdate += Utilities.isEmpty(phoneNumber) ? "" : " '" + phoneNumber + "',";
        sqlFields += Utilities.isEmpty(email) ? "" : " email,";
        sqlUpdate += Utilities.isEmpty(email) ? "" : " '" + email + "',";
        sqlFields += Utilities.isEmpty(address) ? "" : " address,";
        sqlUpdate += Utilities.isEmpty(address) ? "" : " '" + address + "',";
        sqlFields += Utilities.isEmpty(address) ? "" : " city,";
        sqlUpdate += Utilities.isEmpty(address) ? "" : " '" + address + "',";
        sqlFields += Utilities.isEmpty(postcode) ? "" : " postcode,";
        sqlUpdate += Utilities.isEmpty(postcode) ? "" : " '" + postcode + "',";

        sqlFields = Utilities.removeLastCharacter(sqlFields);
        sqlUpdate = Utilities.removeLastCharacter(sqlUpdate);

        String sql;
        sql = String.format("INSERT INTO ats.customer (%s) VALUES (%s);", sqlFields, sqlUpdate);
        System.out.println(sql);

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.executeUpdate();

                for (JTextField field : fields) {
                    field.setText("");
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
