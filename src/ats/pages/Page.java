package ats.pages;

import javax.swing.*;
import java.awt.event.KeyEvent;

public abstract class Page {

    public abstract JPanel getMainPanel();

    public static void restrictInputToNums(KeyEvent ke, JTextField field) {
        if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
            field.setEditable(true);
        } else {
            field.setEditable(false);
            JOptionPane.showMessageDialog(null, "Staff ID must be numeric only");
        }
    }
}
