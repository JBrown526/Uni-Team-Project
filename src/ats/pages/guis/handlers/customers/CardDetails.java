package ats.pages.guis.handlers.customers;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class CardDetails extends TablePage {
    String[] credentials;
    String customerAlias;

    private JButton applyButton;
    private JButton backButton;
    private JButton logoutButton;
    private JTable cardTable;
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField expiryDateField;
    private JPanel mainPanel;

    public CardDetails(App app, String customerAlias, boolean managerView) {
        this.customerAlias = customerAlias;
        credentials = app.getDBCredentials();

        populateTable();

        applyButton.addActionListener(e -> {
            if (requirementsMet()) {
                System.out.println("here");
                if (customerAlias == null) {
                    System.out.println("also here");
                    insertDetails();
                } else {
                    System.out.println("shouldn't be here");
                    updateDetails();
                }
            }
        });

        backButton.addActionListener(e -> app.toCustomer(customerAlias, managerView));
        logoutButton.addActionListener(e -> app.logout());

        cardNumberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveInt(ke, cardNumberField, "Card number");
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
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM card WHERE customer_alias = ?")) {
                ps.setString(1, customerAlias);
                try (ResultSet rs = ps.executeQuery()) {
                    cardTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private boolean requirementsMet() {
        if (!Utilities.isEmpty(cardNumberField.getText())) {
            if (!Utilities.isEmpty(cardHolderField.getText())) {
                if (!Utilities.isEmpty(expiryDateField.getText())) {
                    if (isValidDate(expiryDateField.getText())) {
                        return true;
                    }
                    JOptionPane.showMessageDialog(null, "Please input a valid expiry date");
                    return false;
                }
                JOptionPane.showMessageDialog(null, "Please input the expiry date of your card");
                return false;
            }
            JOptionPane.showMessageDialog(null, "Please input the name of the card holder");
            return false;
        }
        JOptionPane.showMessageDialog(null, "Please input a card number");
        return false;
    }

    private void insertDetails() {
        JTextField[] fields = {cardNumberField, cardHolderField, expiryDateField};
        String sql = String.format("INSERT INTO card VALUES ('%s', '%s', '%s', '%s')", customerAlias, cardNumberField.getText(),
                cardHolderField.getText(), expiryDateField.getText());

        commonInsertStatement(credentials, sql, fields);
        populateTable();
    }

    private void updateDetails() {

    }
}
