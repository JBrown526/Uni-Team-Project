package ats.pages.guis.reports;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import java.sql.*;
import java.util.Arrays;

public class StockTurnover extends TablePage {
    private String[] credentials;
    private String reportEndDate;
    private String reportStartDate;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JButton resetViewButton;
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
                    "WHERE blank_status = ('AVBL' OR 'ASGN') AND date_received BETWEEN ? AND ? GROUP BY blank_type;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANKS NBRS', COUNT(blank_id) " +
                    "AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND date_received BETWEEN '2019-04-03' AND '2019-07-03' " +
                    "AND date_assigned BETWEEN ? AND ? GROUP BY blank_type, staff_id;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'ASSIGNED (FROM/TO)', COUNT(blank_id) " +
                    "AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' AND date_received NOT BETWEEN '2019-04-03' AND '2019-07-03' " +
                    "AND date_assigned BETWEEN ? AND ? GROUP BY blank_type, staff_id;",

            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'USED (FROM/TO)', COUNT(blank_id) AS 'AMNT' FROM blank " +
                    "WHERE blank_status = 'SOLD' AND date_sold BETWEEN ? AND ? GROUP BY blank_type;",

            "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL AVAILABLE (FROM/TO)', COUNT(blank_id) AS 'AMNT' " +
                    "FROM blank WHERE blank_status = 'AVBL' GROUP BY blank_type;",

            "SELECT staff_id AS CODE, CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL ASSIGNED (FROM/TO)', " +
                    "COUNT(blank_id) AS 'AMNT' FROM blank WHERE blank_status = 'ASGN' GROUP BY blank_type, staff_id;"};

    public StockTurnover(App app, boolean managerView) {
        credentials = app.getDBCredentials();

        generateReportButton.addActionListener(e -> {
            setDates();
            populateTable();
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
                    if (i < 4) {
                        ps.setString(1, reportStartDate);
                        ps.setString(2, reportEndDate);
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        tables[i].setModel(buildTableModel(rs));
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void setDates() {
        String date = dateField.getText();
        if (isValidDate(date)) {
            reportEndDate = date;
            int[] splitDate = Arrays.stream(date.split("-"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            splitDate[1] = splitDate[1] <= 1 ? 12 : --splitDate[1];
            reportStartDate = String.format("%04d-%02d-%02d", splitDate[0], splitDate[1], splitDate[2]);

            while (!isValidDate(reportStartDate)) {
                splitDate[2]--;
                reportStartDate = String.format("%04d-%02d-%02d", splitDate[0], splitDate[1], splitDate[2]);
            }
        }
    }
}
