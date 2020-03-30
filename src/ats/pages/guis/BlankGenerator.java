package ats.pages.guis;

import ats.App;
import ats.pages.Page;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BlankGenerator extends Page {
    //================================================================================
    //region Properties
    //================================================================================
    private JPanel mainPanel;
    private JButton logoutButton;
    private JButton backButton;
    private JComboBox typeSelectBox;
    private JTextField numberOfField;
    private JButton generateButton;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public BlankGenerator(App app) {
        //================================================================================
        //region Button Listeners
        //================================================================================
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        backButton.addActionListener(e -> app.toBlanks(true));
        logoutButton.addActionListener(e -> app.logout());
        //endregion

        //================================================================================
        //region Other Listeners
        //================================================================================
        numberOfField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                restrictInputToNums(ke, numberOfField);
            }
        });
        //endregion
    }
    //endregion

    //================================================================================
    //region Accessors
    //================================================================================
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
    //endregion
}
