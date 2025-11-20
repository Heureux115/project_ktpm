package com.example.demo4.controllers;

import com.example.demo4.Main;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MenuController {

    private String role;

    public void setRole(String role) {
        this.role = role;
    }

    @FXML
    public void goToManagement() throws Exception {
        if ("ADMIN".equalsIgnoreCase(role)) {
            Main.showAdmin();
        } else {
            Main.showCustomer();
        }
    }

    @FXML
    public void goToCitizenManagement() throws Exception {
        Main.showHouseholdList();
    }

    @FXML
    public void logout() throws Exception {
        Main.showLogin();
    }
}


