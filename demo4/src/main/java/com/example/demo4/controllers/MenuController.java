package com.example.demo4.controllers;

import com.example.demo4.Main;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    public void goToManagement() throws Exception {
        if ("ADMIN".equalsIgnoreCase(Main.currentRole)) {
            Main.showAdmin();
        } else {
            Main.showCustomer();
        }
    }

    @FXML
    public void goToCitizenManagement() throws Exception {
        if ("ADMIN".equalsIgnoreCase(Main.currentRole)) {
            Main.showAdminHousehold();
        } else {
            Main.showCustomerHousehold();
        }
    }

    @FXML
    public void logout() throws Exception {
        Main.showLogin();
    }
}


