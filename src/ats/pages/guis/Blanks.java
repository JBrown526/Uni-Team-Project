package ats.pages.guis;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Blanks extends TablePage {

    //================================================================================
    //region Properties
    //================================================================================
    private App app;
    private boolean managerView;
    private String selectedBlank;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable blankTable;
    private JButton generateBlanksButton;
    private JPanel managerPanel;
    private JButton viewBlankButtonManager;
    private JPanel staffPanel;
    private JButton viewBlankButtonStaff;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public Blanks(App app, boolean managerView) {
        this.app = app;
        this.managerView = managerView;

        populateTable();

        // hides manager tools in sales mode and sales tools in manager mode
        if (managerView) {
            staffPanel.remove(viewBlankButtonStaff);
            mainPanel.remove(staffPanel);
        } else {
            managerPanel.remove(generateBlanksButton);
            mainPanel.remove(viewBlankButtonManager);
            mainPanel.remove(managerPanel);
        }

        //================================================================================
        //region Button Listeners
        //================================================================================
        generateBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

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
        blankTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = blankTable.getSelectedRow();
                selectedBlank = String.valueOf(blankTable.getValueAt(row, 1));
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
    private void populateTable() {
        String[] credentials = app.getDBCredentials();
        String sql;
        // shows all blanks if in manager mode or staff members assigned blanks if in sales mode
        if (managerView) {
            sql = "SELECT * FROM blank";
        } else {
            sql = "SELECT * FROM blank WHERE staff_id = ?";
        }

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (!managerView) {
                    ps.setInt(1, app.getStaffID());
                }
                try (ResultSet rs = ps.executeQuery()) {
                    blankTable.setModel(buildTableModel(rs));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    //endregion
}
