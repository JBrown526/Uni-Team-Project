package ats.pages.guis.reports;

import ats.App;
import ats.common.Utilities;
import ats.pages.TablePage;

import javax.swing.*;
import java.sql.*;
import java.util.Arrays;

public class IndividualReport extends TablePage {
    String[] credentials;
    private String reportStartDate;
    private String reportEndDate;
    boolean managerView;
    int staffID;

    private JPanel mainPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JTable individualReportTable;
    private JButton resetViewButton;
    private JButton generateReportButton;
    private JTextField dateField;
    private JCheckBox domesticReportCheckBox;
    private JComboBox staffComboBox;

    private String[] sqlFull = {
            "SELECT sale.blank_id, FORMAT((sale.amount * sale.exchange_rate), 2) AS USD, sale.currency, sale.exchange_rate, " +
                    "sale.amount, taxes.local_tax, taxes.other_tax, (sale.amount * (sale.commission_rate / 100)) " +
                    "AS 'Lost to commissions', FORMAT(sale.amount - taxes.local_tax - taxes.other_tax - (sale.amount * (sale.commission_rate / 100)), 2) " +
                    "AS Profit FROM sale JOIN taxes ON sale.blank_id = taxes.blank_id JOIN blank ON sale.blank_id = blank.blank_id " +
                    "WHERE sale.blank_id > 30000000000 AND blank.staff_id = ? AND sale.sale_date BETWEEN ? AND ?",

            "SELECT sale.blank_id, FORMAT((sale.amount * sale.exchange_rate), 2) AS USD, sale.currency, sale.exchange_rate, " +
                    "sale.amount, taxes.local_tax, (sale.amount * (sale.commission_rate / 100)) AS 'Lost to commissions',\n" +
                    "FORMAT(sale.amount - taxes.local_tax - (sale.amount * (sale.commission_rate / 100)), 2) AS Profit\n" +
                    "FROM sale JOIN taxes ON sale.blank_id = taxes.blank_id JOIN blank ON sale.blank_id = blank.blank_id \n" +
                    "WHERE sale.blank_id < 30000000000 AND blank.staff_id = ? AND sale.sale_date BETWEEN ? AND ?"};

    public IndividualReport(App app, boolean managerView) {
        this.managerView = managerView;
        credentials = app.getDBCredentials();
        staffID = app.getStaffID();
        Utilities.fillStaffDropdown(credentials, staffComboBox);

        if (!managerView) {
            mainPanel.remove(staffComboBox);
        }

        generateReportButton.addActionListener(e -> {
            if (managerView) {
                staffID = Integer.parseInt(String.valueOf(staffComboBox.getSelectedItem()));
            }
            setDates();
            populateTable();
        });
        resetViewButton.addActionListener(e -> app.toIndividualReport(managerView));

        backButton.addActionListener(e -> app.toReports(managerView));
        logoutButton.addActionListener(e -> app.logout());
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    protected void populateTable() {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sqlFull[domesticReportCheckBox.isSelected() ? 1 : 0])) {
                ps.setInt(1, staffID);
                ps.setString(2, reportStartDate);
                ps.setString(3, reportEndDate);
                try (ResultSet rs = ps.executeQuery()) {
                    individualReportTable.setModel(buildTableModel(rs));
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
            splitDate[0] = splitDate[1] <= 1 ? --splitDate[0] : splitDate[0];
            splitDate[1] = splitDate[1] <= 1 ? 12 : --splitDate[1];
            reportStartDate = String.format("%04d-%02d-%02d", splitDate[0], splitDate[1], splitDate[2]);

            while (!isValidDate(reportStartDate)) {
                splitDate[2]--;
                reportStartDate = String.format("%04d-%02d-%02d", splitDate[0], splitDate[1], splitDate[2]);
            }
        }
    }
}
