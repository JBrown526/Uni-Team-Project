package ats;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class Login extends Page {
    private String[] credentials;
    private JTextField staffIDField;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JPanel mainPanel;

    public Login(App app) {
        credentials = app.getDBCredentials();

        submitButton.addActionListener(e -> {
            int id = Integer.parseInt(staffIDField.getText());
            String pwd = String.valueOf(passwordField.getPassword());

            // Connects to the database
            try (Connection conn = DriverManager.getConnection(credentials[0], credentials[1], credentials[2])) {
                // Query message
                try (PreparedStatement ps = conn.prepareStatement("SELECT staff_id, role_code, password FROM staff WHERE staff_id = ? AND password = ? ")) {
                    ps.setInt(1, id);
                    ps.setString(2, pwd);
                    try (ResultSet rs = ps.executeQuery()) {
                        boolean credentialsCorrect = false;

                        // logs user in if correct credentials supplied
                        while (rs.next()) {
                            credentialsCorrect = true;
                            app.login(rs.getInt("staff_id"), rs.getString("role_code"));
                        }
                        // informs user that incorrect credentials were supplied
                        if (!credentialsCorrect) {
                            JOptionPane.showMessageDialog(null, "Invalid Credentials Supplied");
                        }
                    }
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        });

        // prevents invalid entries in the staffID field
        staffIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    staffIDField.setEditable(true);
                } else {
                    staffIDField.setEditable(false);
                    JOptionPane.showMessageDialog(null, "Staff ID must be numeric only");
                }
            }
        });
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
