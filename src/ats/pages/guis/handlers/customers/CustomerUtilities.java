package ats.pages.guis.handlers.customers;

import ats.common.Utilities;

import javax.swing.*;

public interface CustomerUtilities {
    static boolean commonRequirementsMet(boolean managerView, JTextField discountRateField, JComboBox statusComboBox, JTextField nameField) {
        try {
            if (!Utilities.isEmpty(discountRateField.getText())) {
                Float.parseFloat(discountRateField.getText());
            }
            String status = String.valueOf(statusComboBox.getItemAt(statusComboBox.getSelectedIndex()));
            if (!status.equals("RG") && !managerView) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Valued customer status requires Office Manager approval, select yes if you have Office Manager approval", "", JOptionPane.YES_NO_OPTION);
                if (input != 0) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Discount rates must be in the form of a valid float");
        }
        return false;
    }
}
