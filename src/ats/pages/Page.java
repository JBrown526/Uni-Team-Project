package ats.pages;

import ats.common.Utilities;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.regex.Pattern;

public abstract class Page {

    public abstract JPanel getMainPanel();

    public static void restrictInputToPositiveInt(KeyEvent ke, JTextField field, String fieldName) {
        if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            field.setEditable(true);
        } else {
            field.setEditable(false);
            JOptionPane.showMessageDialog(null, fieldName + " must be a positive integer value");
        }
    }

    public static void restrictInputToPositiveFloat(KeyEvent ke, JTextField field, String fieldName) {
        if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE
                || ke.getExtendedKeyCode() == KeyEvent.VK_PERIOD) {
            field.setEditable(true);
        } else {
            field.setEditable(false);
            JOptionPane.showMessageDialog(null, fieldName + " must be a positive float value");
        }
    }

    public static boolean isValidDate(String d) {
        return DATE_PATTERN.matcher(d).matches();
    }

    public static boolean isStatus(String[] credentials, String blankID, String desiredStatus) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT blank_status FROM blank WHERE blank_id = ?")) {
                ps.setString(1, blankID);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getString("blank_status").equals(desiredStatus);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected static String populatePersonInsertQuery(String table, String[] sqlFragments, JTextField phoneNumberField,
                                                      JTextField emailField, JTextField addressField, JTextField cityField,
                                                      JTextField postcodeField) {
        String sqlFields = sqlFragments[0];
        String sqlUpdate = sqlFragments[1];

        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String postcode = postcodeField.getText();

        sqlFields += Utilities.isEmpty(phoneNumber) ? "" : " phone_number,";
        sqlUpdate += Utilities.isEmpty(phoneNumber) ? "" : " '" + phoneNumber + "',";
        sqlFields += Utilities.isEmpty(email) ? "" : " email,";
        sqlUpdate += Utilities.isEmpty(email) ? "" : " '" + email + "',";
        sqlFields += Utilities.isEmpty(address) ? "" : " address,";
        sqlUpdate += Utilities.isEmpty(address) ? "" : " '" + address + "',";
        sqlFields += Utilities.isEmpty(city) ? "" : " city,";
        sqlUpdate += Utilities.isEmpty(city) ? "" : " '" + city + "',";
        sqlFields += Utilities.isEmpty(postcode) ? "" : " postcode,";
        sqlUpdate += Utilities.isEmpty(postcode) ? "" : " '" + postcode + "',";

        sqlFields = Utilities.removeLastCharacter(sqlFields);
        sqlUpdate = Utilities.removeLastCharacter(sqlUpdate);

        String sql;
        sql = String.format("INSERT INTO %s (%s) VALUES (%s);", table, sqlFields, sqlUpdate);
        return sql;
    }

    protected static void commonInsertStatement(String[] credentials, String sql, JTextField[] fields) {
        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.executeUpdate();

                for (JTextField field : fields) {
                    field.setText("");
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$"
    );
}
