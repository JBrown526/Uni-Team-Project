package ats;

import javax.swing.*;

import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class App {

    //================================================================================
    //region Properties
    //================================================================================
    final JFrame window = new JFrame("Air Ticket Sales");

    private String url = "jdbc:mysql://127.0.0.1:3306/ats?autoReconnect=true&useSSL=false";
    private String user = "root";
    private String password = "JBMySQLAdmin01";
    private String[] credentials = {url, user, password};

    private Page currentPage;
    private int staffID;
    private String staffRole;
    //endregion

    //================================================================================
    //region Constructor
    //================================================================================
    public App() {
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLocationByPlatform(true);
        window.setResizable(true);

        Login login = new Login(this);
        window.add(login.getMainPanel());
        currentPage = login;

        window.pack();
        window.setVisible(true);
    }
    //endregion

    //================================================================================
    //region Accessors
    //================================================================================
    public String[] getDBCredentials() {
        return credentials;
    }

    public int getStaffID() {
        return staffID;
    }

    public String getStaffRole() {
        return staffRole;
    }
    //endregion

    //================================================================================
    //region Window Navigation
    //================================================================================
    public void login(int id, String role) {
        staffID = id;
        staffRole = role;

        if (staffRole.equals("OM")) {
            toOfficeManager();
        } else if (staffRole.equals("SA")) {
            toSystemAdministrator();
        } else {
            toTravelAgent();
        }
    }

    public void toTravelAgent() {
        changeWindow(new TravelAgent(this));
    }

    public void toOfficeManager() {
        changeWindow(new OfficeManager(this));
    }

    public void toSystemAdministrator() {
        changeWindow(new SystemAdministrator(this));
    }

    public void toBlanks(boolean managerView) {
        changeWindow(new Blanks(this, managerView));
    }

    public void toBlank(String blankID, boolean managerView) {
        changeWindow(new Blank(this, blankID, managerView));
    }

    public void logout() {
        staffID = 0;
        staffRole = null;
        changeWindow(new Login(this));
    }
    //endregion

    private void changeWindow(Page page) {
        Dimension d = window.getSize();
        window.remove(currentPage.getMainPanel());
        window.add(page.getMainPanel());
        currentPage = page;

        window.pack();
        window.setSize(d);
    }

    public static void main(String[] args) {
        new App();
    }
}
