package ats.pages.guis.handlers.exchangerates;

import java.sql.*;

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

}
