package ats.pages;

import javax.swing.*;
import java.awt.event.KeyEvent;
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

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$"
    );
}
