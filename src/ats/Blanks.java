package ats;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Blanks extends TablePage {

    private App app;
    private boolean managerView;

    private JButton backButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable blankTable;
    private JButton assignBlanksButton;
    private JButton generateBlanksButton;
    private JPanel managerPanel;

    public Blanks(App app, boolean managerView) {
        this.app = app;
        this.managerView = managerView;

        populateTable();

        if (!managerView) {
            managerPanel.remove(assignBlanksButton);
            managerPanel.remove(generateBlanksButton);
            mainPanel.remove(managerPanel);
        }

        assignBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        generateBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void populateTable() {
        String[] credentials = app.getDBCredentials();
        String sql;
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
}
