package ats.pages.guis.handlers.customers;

import ats.App;
import ats.common.Utilities;
import ats.pages.Page;

import javax.swing.*;

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
                return CustomerUtilities.commonRequirementsMet(managerView, discountRateField, statusComboBox, nameField);
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

        JTextField[] fields = {aliasField, nameField, discountRateField, phoneNumberField,
                emailField, addressField, cityField, postcodeField};

        String sqlFields = "customer_alias, status_code, name,";
        String sqlUpdate = "'" + alias + "', '" + statusCode + "', '" + name + "',";


        sqlFields += Utilities.isEmpty(discountRateString) ? "" : " discount_rate,";
        sqlUpdate += Utilities.isEmpty(discountRateString) ? "" : Float.parseFloat(discountRateString) + ",";

        String[] sqlFragments = {sqlFields, sqlUpdate};
        String sql = populatePersonInsertQuery("ats.customer", sqlFragments, phoneNumberField, emailField, addressField, cityField, postcodeField);

        commonInsertStatement(credentials, sql, fields);
    }
}
