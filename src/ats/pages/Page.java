package ats.pages;

import ats.common.Utilities;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.regex.Pattern;

public abstract class Page {

    public abstract JPanel getMainPanel();

    // only allows integer inputs in a field
    public static void restrictInputToPositiveInt(KeyEvent ke, JTextField field, String fieldName) {
        if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            field.setEditable(true);
        } else {
            field.setEditable(false);
            JOptionPane.showMessageDialog(null, fieldName + " must be a positive integer value");
        }
    }

    // only allows float input in a field
    public static void restrictInputToPositiveFloat(KeyEvent ke, JTextField field, String fieldName) {
        if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE
                || ke.getExtendedKeyCode() == KeyEvent.VK_PERIOD) {
            field.setEditable(true);
        } else {
            field.setEditable(false);
            JOptionPane.showMessageDialog(null, fieldName + " must be a positive float value");
        }
    }

    // checks if a string is a valid date
    public static boolean isValidDate(String d) {
        return DATE_PATTERN.matcher(d).matches();
    }

    // checks if the status of a blank fits the desired status
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

    // returns the sql for an insert person query based on the provided fields
    protected static String populatePersonInsertQuery(String table, String[] sqlFragments, JTextField phoneNumberField,
                                                      JTextField emailField, JTextField addressField, JTextField cityField,
                                                      JTextField postcodeField) {
        // creates a string of sql fields that need updating and their corresponding values
        // sqlFragments[0] and sqlFragments[1] hold the fields that are mandatory to update and their values to update
        String sqlFields = sqlFragments[0];
        String sqlUpdate = sqlFragments[1];

        // gets values of person information from their fields
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String postcode = postcodeField.getText();

        // adds the field to be updated if the field form was not empty
        // adds the value from the field to the update sql if it is not empty
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

        // rmoves the last character so there are no trailing commas
        sqlFields = Utilities.removeLastCharacter(sqlFields);
        sqlUpdate = Utilities.removeLastCharacter(sqlUpdate);

        String sql;
        // creates an sql query for inserting a person to the database
        sql = String.format("INSERT INTO %s (%s) VALUES (%s);", table, sqlFields, sqlUpdate);
        return sql;
    }

    // executes an update to the database from a prepared statement and resets the input of any populated fields to blank
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

    // date format matching Regex
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$"
    );
}
