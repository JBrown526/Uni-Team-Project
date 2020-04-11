package ats.pages.guis.handlers.exchangerates;

import ats.common.Utilities;
import ats.pages.Page;

import javax.swing.*;
import java.sql.*;
import java.util.regex.Pattern;

public interface ExchangeRateUtilities {

    static boolean exchangeRateExists(String currencyCode, String[] credentials) {
        boolean exchangeRateExists = false;

        try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT currency_code FROM exchange_rate WHERE currency_code = ?;")) {
                ps.setString(1, currencyCode);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        exchangeRateExists = true;
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return exchangeRateExists;
    }

    static boolean conditionsMet(String currencyCode, String date, String[] credentials) {
        if (Pattern.matches("[A-Z][A-Z][A-Z]", currencyCode)) {
            if (Page.isValidDate(date) && !Utilities.isEmpty(date)) {
                JOptionPane.showMessageDialog(null, "Exchange rate successfully added");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid date");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please provide a valid currency code.\n" +
                    "Please note: codes should be 3 letters long and must be capitalised");
        }
        return false;
    }
}
