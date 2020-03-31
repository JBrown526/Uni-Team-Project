package ats.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public interface Utilities {

    static boolean isEmpty(String fieldValue) {
        return fieldValue.equals("");
    }

    static String removeLastCharacter(String str) {
        return Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
    }

    static int setStatementValue(PreparedStatement ps, String str, int i) throws SQLException {
        if (!isEmpty(str)) {
            ps.setString(i, str);
            i++;
        }
        return i;
    }
}
