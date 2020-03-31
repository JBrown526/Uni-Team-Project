package ats.pages.guis.handlers.blanks;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Vector;

public class BlankGenerator extends Page {
    //================================================================================
    //region Properties
    //================================================================================
    private String[] credentials;

    private JPanel mainPanel;
    private JButton logoutButton;
    private JButton backButton;
    private JComboBox typeSelectBox;
    private JTextField numberOfField;
    private JButton generateButton;
    private JTextField dateField;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public BlankGenerator(App app) {
        credentials = app.getDBCredentials();
        fillTypeDropdown();

        //================================================================================
        //region Button Listeners
        //================================================================================
        generateButton.addActionListener(e -> {
            int numberToGenerate = Integer.parseInt(numberOfField.getText());
            if (numberToGenerate > 0) {
                int type = (int) typeSelectBox.getItemAt(typeSelectBox.getSelectedIndex());
                long nextAvailable = findNextAvailableID(type);
                String date = dateField.getText();
                if (isValidDate(date)) {
                    for (long i = 0; i < numberToGenerate; i++) {
                        generateBlank(type, nextAvailable + i, date);
                    }
                    JOptionPane.showMessageDialog(null, "Blanks Successfully Generated");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Date/Date Format supplied. Please use YYYY-MM-DD format");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please Specify a Number to Generate Greater than 0");
            }
        });

        backButton.addActionListener(e -> app.toBlanks(true));
        logoutButton.addActionListener(e -> app.logout());
        //endregion

        //================================================================================
        //region Other Listeners
        //================================================================================
        numberOfField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToNums(ke, numberOfField);
            }
        });
        //endregion
    }
    //endregion

    //================================================================================
    //region Accessors
    //================================================================================
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
    //endregion

    //================================================================================
    //region Methods
    //================================================================================
    private void fillTypeDropdown() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM blank_type")) {
                try (ResultSet rs = ps.executeQuery()) {
                    Vector<Integer> types = new Vector<>();
                    while (rs.next()) {
                        types.add(rs.getInt("blank_type"));
                    }
                    final DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>(types);
                    typeSelectBox.setModel(model);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private long findNextAvailableID(int type) {
        long nextAvailableID = 0;
        boolean existingBlanks = false;
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT max(blank_id) AS maximum FROM ats.blank WHERE blank_type = ?;")) {
                ps.setInt(1, type);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String blankID = rs.getString("maximum");
                        if (blankID != null) {
                            existingBlanks = true;
                            nextAvailableID = Long.parseLong(blankID);
                            nextAvailableID++;
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        if (!existingBlanks) {
            String typePrefix = String.valueOf(type);
            String blankID = typePrefix + "00000001";
            nextAvailableID = Long.parseLong(blankID);
        }

        return nextAvailableID;
    }

    private void generateBlank(int type, long id, String date) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO ats.blank (blank_type, blank_id, date_received, void) VALUES (?, ?, ?, 0)")) {
                ps.setInt(1, type);
                ps.setString(2, String.valueOf(id));
                ps.setString(3, date);
                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    //endregion
}
