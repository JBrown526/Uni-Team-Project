package ats.pages.guis.handlers.blanks;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Blanks extends TablePage implements Utilities {

    //================================================================================
    //region Properties
    //================================================================================
    private App app;
    private boolean managerView;
    private String selectedBlank;

    private boolean searching;
    private String blankID;
    private int conditionCount;
    private String query;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable blankTable;
    private JButton generateBlanksButton;
    private JPanel managerPanel;
    private JButton viewBlankButtonManager;
    private JPanel staffPanel;
    private JButton viewBlankButtonStaff;
    private JCheckBox hideVoidedCheckBox;
    private JTextField blankIDField;
    private JButton searchButton;
    private JCheckBox hideSoldCheckBox;
    private JCheckBox hideAvailableCheckBox;
    private JCheckBox hideAssignedCheckBox;
    private JPanel filterPanel;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public Blanks(App app, boolean managerView) {
        this.app = app;
        this.managerView = managerView;

        // hides manager tools in sales mode and sales tools in manager mode
        if (managerView) {
            query = "SELECT * FROM blank";
            conditionCount = 0;

            staffPanel.remove(viewBlankButtonStaff);
            mainPanel.remove(staffPanel);
        } else {
            query = "SELECT * FROM blank WHERE staff_id = ?";
            conditionCount = 1;

            filterPanel.remove(hideAvailableCheckBox);
            managerPanel.remove(generateBlanksButton);
            managerPanel.remove(viewBlankButtonManager);
            mainPanel.remove(managerPanel);
        }

        populateTable();

        //================================================================================
        //region Button Listeners
        //================================================================================
        generateBlanksButton.addActionListener(e -> app.toBlankAdd());

        viewBlankButtonStaff.addActionListener(e -> {
            if (selectedBlank != null) {
                app.toBlank(selectedBlank, false);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a blank");
            }
        });

        viewBlankButtonManager.addActionListener(e -> {
            if (selectedBlank != null) {
                app.toBlank(selectedBlank, true);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a blank");
            }
        });

        searchButton.addActionListener(e -> search());

        logoutButton.addActionListener(e -> app.logout());
        backButton.addActionListener(e -> {
            if (managerView) {
                app.toOfficeManager();
            } else {
                app.toTravelAgent();
            }
        });
        //endregion

        //================================================================================
        //region Other Listeners
        //================================================================================
        hideVoidedCheckBox.addItemListener(e -> checkBoxSelected(hideVoidedCheckBox, "VOID"));
        hideSoldCheckBox.addItemListener(e -> checkBoxSelected(hideSoldCheckBox, "SOLD"));
        hideAvailableCheckBox.addItemListener(e -> checkBoxSelected(hideAvailableCheckBox, "AVBL"));
        hideAssignedCheckBox.addItemListener(e -> checkBoxSelected(hideAssignedCheckBox, "ASGN"));

        blankTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = blankTable.getSelectedRow();
                selectedBlank = String.valueOf(blankTable.getValueAt(row, 1));
            }
        });
        blankIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToPositiveInt(ke, blankIDField, "Blank ID");
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
    // populates the table with a specified selection of blanks
    @Override
    protected void populateTable() {
        //TODO: show staff name instead of id
        String[] credentials = app.getDBCredentials();

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                if (!managerView) {
                    ps.setInt(1, app.getStaffID());
                    if (searching) {
                        ps.setString(2, blankID);
                    }
                } else {
                    if (searching) {
                        ps.setString(1, blankID);
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    blankTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void checkBoxSelected(JCheckBox cb, String status) {
        if (cb.isSelected()) {
            if (conditionCount > 0) {
                query = query + " AND NOT blank_status = '" + status + "'";
            } else {
                query = query + " WHERE NOT blank_status = '" + status + "'";
            }
            conditionCount++;
        } else {
            query = query.replace(" NOT blank_status = '" + status + "'", "");
            query = query.replace("AND AND", "AND");
            query = query.replace("WHERE AND", "WHERE");
            query = query.replace("AND ", "temp");
            query = query.replace(" AND", "");
            query = query.replace("temp", "AND ");
            conditionCount--;

            if (conditionCount <= 1) {
                query = query.replace(" AND", "");
            }
            if (conditionCount <= 0) {
                query = query.replace(" WHERE", "");
            }
        }
        populateTable();
    }

    private void search() {
        blankID = blankIDField.getText();
        if (Utilities.blankExists(blankID, app.getDBCredentials())) {
            hideAssignedCheckBox.setSelected(false);
            hideAvailableCheckBox.setSelected(false);
            hideSoldCheckBox.setSelected(false);
            hideVoidedCheckBox.setSelected(false);

            query = "SELECT * FROM blank WHERE blank_id = ?";
            if (!managerView) {
                query = "SELECT * FROM blank WHERE staff_id = ? AND blank_id = ?";
            }

            conditionCount++;
            searching = true;
            populateTable();
        } else {
            JOptionPane.showMessageDialog(null, "That blank does not exist, please search for a valid blank");
        }
    }
    //endregion
}
