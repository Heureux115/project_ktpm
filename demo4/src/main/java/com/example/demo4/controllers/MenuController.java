package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.Session;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    public void goToManagement() throws Exception {
        if (!Session.isLoggedIn()) {
            Main.showLogin();
            return;
        }

        String role = Session.getCurrentRole();
        if ("ADMIN".equalsIgnoreCase(role)) {
            Main.showAdmin();
        } else {
            Main.showCustomer();
        }
    }

    @FXML
    public void goToCitizenManagement() throws Exception {
        if (!Session.isLoggedIn()) {
            Main.showLogin();
            return;
        }

        String role = Session.getCurrentRole();
        if ("ADMIN".equalsIgnoreCase(role)) {
            Main.showAdminHousehold();
        } else {
            Main.showCustomerHousehold();
        }
    }

    @FXML
    public void logout() throws Exception {
        Main.logout();   // đã clear Session bên trong
    }
}
