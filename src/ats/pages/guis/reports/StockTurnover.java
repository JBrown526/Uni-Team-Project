package ats.pages.guis.reports;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StockTurnover extends TablePage {
    private String[] credentials;
    private boolean managerView;
    private boolean displayAllStaff;
    private String reportEndDate;
    private String reportStartDate;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JPanel managerPanel;
    private JButton viewIndividualTurnoverButton;
    private JButton resetViewButton;
    private JComboBox staffMemberComboBox;
    private JButton generateReportButton;
    private JTextField dateField;

    private JTable newBlanksTable;
    private JTable assignedNewBlanksTable;
    private JTable assignedBlanksTable;
    private JTable soldBlanksTable;
    private JTable allAvailableBlanksTable;
    private JTable allAssignedBlanksTable;

    private JTable[] tables = {newBlanksTable, assignedNewBlanksTable, assignedBlanksTable,
            soldBlanksTable, allAvailableBlanksTable, allAssignedBlanksTable};
    private String[] sqlFull = {
            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANK NBRS', COUNT(blank_id) AS 'AMNT' FROM blank " +
                    "WHERE blank_status = ('AVBL' OR 'ASGN') AND date_received BETWEEN '2019-04-03' AND '2019-07-03' GROUP BY blank_type;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANKS NBRS', COUNT(blank_id) " +
                    "AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND date_received BETWEEN '2019-04-03' AND '2019-07-03' " +
                    "AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03' GROUP BY blank_type, staff_id;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'ASSIGNED (FROM/TO)', COUNT(blank_id) " +
                    "AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND date_received NOT BETWEEN '2019-04-03' AND '2019-07-03' " +
                    "AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03' GROUP BY blank_type, staff_id;",

            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'USED (FROM/TO)', COUNT(blank_id) AS 'AMNT' FROM blank " +
                    "WHERE blank_status = 'SOLD' AND date_sold BETWEEN '2019-04-03' AND '2019-07-03' GROUP BY blank_type;",

            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL AVAILABLE (FROM/TO)', COUNT(blank_id) AS 'AMNT' " +
                    "FROM blank WHERE blank_status = 'AVBL' GROUP BY blank_type;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL ASSIGNED (FROM/TO)', " +
                    "COUNT(blank_id) AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' GROUP BY blank_type, staff_id;"};
    private String[] sqlIndividual = {
            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANK NBRS', COUNT(blank_id) AS 'AMNT' FROM blank " +
                    "WHERE blank_status = ('AVBL' OR 'ASGN') AND date_received BETWEEN '2019-04-03' AND '2019-07-03' GROUP BY blank_type;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANKS NBRS', COUNT(blank_id) " +
                    "AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND date_received BETWEEN '2019-04-03' AND '2019-07-03' " +
                    "AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03' AND staff_id = ? GROUP BY blank_type, staff_id;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'ASSIGNED (FROM/TO)', COUNT(blank_id) " +
                    "AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND date_received NOT BETWEEN '2019-04-03' AND '2019-07-03' " +
                    "AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03' AND staff_id = ? GROUP BY blank_type, staff_id;",

            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'USED (FROM/TO)', COUNT(blank_id) AS 'AMNT' FROM blank " +
                    "WHERE blank_status = 'SOLD' AND date_sold BETWEEN '2019-04-03' AND '2019-07-03' GROUP BY blank_type;",

            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL AVAILABLE (FROM/TO)', COUNT(blank_id) AS 'AMNT' " +
                    "FROM blank WHERE blank_status = 'AVBL' GROUP BY blank_type;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL ASSIGNED (FROM/TO)', " +
                    "COUNT(blank_id) AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND staff_id = ? GROUP BY blank_type, staff_id;"};

    public StockTurnover(App app, boolean managerView) {
        //TODO: this
        this.managerView = managerView;
        credentials = app.getDBCredentials();

        populateTable();

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        viewIndividualTurnoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetViewButton.addActionListener(e -> app.toStockTurnover(managerView));

        backButton.addActionListener(e -> {
            if (managerView) {
                app.toReports(true);
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

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            for (int i = 0; i < tables.length; i++) {
                try (PreparedStatement ps = conn.prepareStatement(sqlFull[i])) {
                    try (ResultSet rs = ps.executeQuery()) {
                        tables[i].setModel(buildTableModel(rs));
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}

//    SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANK NBRS', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = ('AVBL' OR 'ASGN') AND date_received BETWEEN '2019-04-03' AND '2019-07-03'
//        GROUP BY blank_type;
//
//        SELECT staff_id AS 'CODE', CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANKS NBRS', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'ASGN' AND date_received BETWEEN '2019-04-03' AND '2019-07-03' AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03'
//        GROUP BY blank_type, staff_id;
//
//        SELECT staff_id AS 'CODE', CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'ASSIGNED (FROM/TO)', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'ASGN' AND date_received NOT BETWEEN '2019-04-03' AND '2019-07-03' AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03'
//        GROUP BY blank_type, staff_id;
//
//        SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'USED (FROM/TO)', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'SOLD' AND date_sold BETWEEN '2019-04-03' AND '2019-07-03'
//        GROUP BY blank_type;
//
//        SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL AVAILABLE (FROM/TO)', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'AVBL'
//        GROUP BY blank_type;
//
//        SELECT staff_id AS 'CODE', CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL ASSIGNED (FROM/TO)', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'ASGN'
//        GROUP BY blank_type, staff_id;