package ats.pages.guis.handlers.payments;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.sql.*;

public class Transaction extends TablePage implements Utilities {
    private App app;
    String[] credentials;
    String blankID;
    boolean domesticSale;

    private JPanel mainPanel;
    private JPanel paymentDetailsPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable saleTable;
    private JComboBox aliasComboBox;
    private JComboBox currencyComboBox;
    private JTextField saleDateField;
    private JTextField priceField;
    private JTextField localTaxField;
    private JTextField otherTaxField;
    private JCheckBox cardPaymentCheckBox;
    private JCheckBox deferredPaymentCheckBox;
    private JButton makePaymentButton;

    public Transaction(App app, String blankID, boolean managerView) {
        this.app = app;
        this.blankID = blankID;
        credentials = app.getDBCredentials();
        Utilities.fillCustomerDropdown(credentials, aliasComboBox);
        Utilities.fillCurrencyDropdown(credentials, currencyComboBox);

        populateTable();
        if (saleMade()) {
            mainPanel.remove(paymentDetailsPanel);
        }

        makePaymentButton.addActionListener(e -> {
            int blankType = Integer.parseInt(blankID.substring(0, 3));
            domesticSale = blankType < 400;
            if (domesticSale) {
                domesticSale();
            } else {
                interlineSale();
            }
        });

        backButton.addActionListener(e -> app.toBlank(blankID, managerView));
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT sale.*, taxes.local_tax, taxes.other_tax " +
                    "FROM sale JOIN taxes ON sale.blank_id = taxes.blank_id WHERE sale.blank_id = ?")) {
                ps.setString(1, blankID);
                try (ResultSet rs = ps.executeQuery()) {
                    saleTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private boolean passesConditions() {
        if (domesticSale) {
            return generalConditions() && cardPaymentConditions() && deferredPaymentConditions();
        } else {
            return generalConditions() && interlineConditions() && cardPaymentConditions() && deferredPaymentConditions();
        }
    }

    private boolean generalConditions() {
        try {
            Float.parseFloat(priceField.getText());
            Float.parseFloat(localTaxField.getText());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Price and local tax must be in a positive float format");
            return false;
        }
        if (!Utilities.isEmpty(saleDateField.getText())) {
            if (isValidDate(saleDateField.getText())) {
                if (!Utilities.isEmpty(priceField.getText())) {
                    if (!Utilities.isEmpty(localTaxField.getText())) {
                        return true;
                    }
                    JOptionPane.showMessageDialog(null, "Please enter the local tax rate");
                    return false;
                }
                JOptionPane.showMessageDialog(null, "Please enter the price of the sale");
                return false;
            }
            JOptionPane.showMessageDialog(null, "Please enter a valid date");
            return false;
        }
        JOptionPane.showMessageDialog(null, "Please enter a date of sale");
        return false;
    }

    private boolean interlineConditions() {
        if (!Utilities.isEmpty(otherTaxField.getText())) {
            try {
                Float.parseFloat(otherTaxField.getText());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Other taxes must be in a positive float format");
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Please enter the miscelaneous tax rate");
        return false;
    }

    private boolean cardPaymentConditions() {
        if (cardPaymentCheckBox.isSelected()) {
            if (!isCasualCustomer(String.valueOf(aliasComboBox.getSelectedItem()))) {
                return true;
            }
            JOptionPane.showMessageDialog(null, "Casual customers cannot pay by card");
            return false;
        }
        return true;
    }

    private boolean deferredPaymentConditions() {
        if (deferredPaymentCheckBox.isSelected()) {
            if (isValuedCustomer(String.valueOf(aliasComboBox.getSelectedItem()))) {
                return true;
            }
            JOptionPane.showMessageDialog(null, "Only valued customers can make a deferred payment");
            return false;
        }
        return true;
    }

    private boolean isCasualCustomer(String alias) {
        return alias.equals("Anon");
    }

    private boolean isValuedCustomer(String alias) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT status_code FROM customer WHERE customer_alias = ?")) {
                ps.setString(1, alias);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getString("status_code").equals("VD");
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }

    private float getExchangeRate(String currencyCode) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT exchange_rate FROM exchange_rate WHERE currency_code = ?")) {
                ps.setString(1, currencyCode);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getFloat("exchange_rate");
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return 1.0f;
    }

    private float getCommissionRate() {
        float rate = 0;
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT commission FROM commission WHERE blank_type = ? AND staff_id = ?")) {
                ps.setInt(1, Integer.parseInt(blankID.substring(0, 3)));
                ps.setInt(2, app.getStaffID());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rate = rs.getFloat("commission");
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return rate;
    }

    private boolean saleMade() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT blank_id FROM sale WHERE blank_id = ?")) {
                ps.setString(1, blankID);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }

    private void insertSale() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO sale VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, blankID);
                ps.setString(2, String.valueOf(aliasComboBox.getSelectedItem()));
                ps.setString(3, String.valueOf(currencyComboBox.getSelectedItem()));
                ps.setString(4, saleDateField.getText());
                ps.setFloat(5, Float.parseFloat(priceField.getText()));
                ps.setFloat(6, getExchangeRate(String.valueOf(currencyComboBox.getSelectedItem())));
                ps.setFloat(7, getCommissionRate());
                ps.setInt(8, cardPaymentCheckBox.isSelected() ? 1 : 0);
                ps.setInt(9, deferredPaymentCheckBox.isSelected() ? 1 : 0);
                ps.setInt(10, 0);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateTable();
    }

    private void setBlankToSold() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE blank SET date_sold = ?, blank_status = 'SOLD' WHERE blank_id = ?")) {
                ps.setString(1, saleDateField.getText());
                ps.setString(2, blankID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void domesticSale() {
        if (passesConditions()) {
            insertSale();
            try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO taxes (blank_id, local_tax) VALUES (?, ?)")) {
                    ps.setString(1, blankID);
                    ps.setFloat(2, Float.parseFloat(localTaxField.getText()));

                    ps.executeUpdate();
                    setBlankToSold();
                    populateTable();
                    JOptionPane.showMessageDialog(null, "Domestic sale made!");
                    mainPanel.remove(paymentDetailsPanel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void interlineSale() {
        if (passesConditions()) {
            insertSale();
            try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO taxes VALUES (?, ?, ?)")) {
                    ps.setString(1, blankID);
                    ps.setFloat(2, Float.parseFloat(localTaxField.getText()));
                    ps.setFloat(3, Float.parseFloat(otherTaxField.getText()));

                    ps.executeUpdate();
                    setBlankToSold();
                    populateTable();
                    JOptionPane.showMessageDialog(null, "Interline sale made!");
                    mainPanel.remove(paymentDetailsPanel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
