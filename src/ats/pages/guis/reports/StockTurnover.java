package ats.pages.guis.reports;

import ats.App;
import ats.pages.TablePage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class StockTurnover extends TablePage {
    private String[] credentials;
    private boolean managerView;

    Vector<String> columnNames;
    Vector<Vector<Object>> data;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable stockTurnoverTable;
    private JPanel managerPanel;
    private JButton viewIndividualTurnoverButton;
    private JButton resetViewButton;
    private JComboBox staffMemberComboBox;
    private JButton generateReportButton;
    private JTextField dateField;

    public StockTurnover(App app, boolean managerView) {
        //TODO: this
        this.managerView = managerView;
        credentials = app.getDBCredentials();

        columnNames = new Vector<>();
        data = new Vector<>();

        populateTable();
        showResults();

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
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANK NBRS', COUNT(blank_id) AS 'AMNT' " +
                            "FROM blank WHERE blank_status = 'AVBL' AND date_received BETWEEN '2019-04-03' AND '2019-07-03' " +
                            "GROUP BY blank_type;\n")) {
                try (ResultSet rs = ps.executeQuery()) {
                    makeTable(rs);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT staff_id AS 'CODE', CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANKS NBRS', COUNT(blank_id) AS 'AMNT'\n" +
                            "        FROM blank WHERE blank_status = 'ASGN' AND date_received BETWEEN '2019-04-03' AND '2019-07-03' AND date_assigned BETWEEN '2019-04-03' AND '2019-07-03'\n" +
                            "        GROUP BY blank_type, staff_id;")) {
                try (ResultSet rs = ps.executeQuery()) {
                    makeTable(rs);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void makeTable(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
    }

    private void showResults() {
        DefaultTableModel dtm = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        stockTurnoverTable.setModel(dtm);
    }
}

//    SELECT CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'FROM/TO BLANK NBRS', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'AVBL' AND date_received BETWEEN '2019-04-03' AND '2019-07-03'
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
//        SELECT 'CODE', CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL AVAILABLE (FROM/TO)', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'AVBL'
//        GROUP BY blank_type;
//
//        SELECT staff_id AS 'CODE', CONCAT(MIN(blank_id), '-', MAX(blank_id)) AS 'TOTAL ASSIGNED (FROM/TO)', COUNT(blank_id) AS 'AMNT'
//        FROM blank WHERE blank_status = 'ASGN'
//        GROUP BY blank_type, staff_id;