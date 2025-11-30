package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.*;

public class HouseholdCustomerController {

    @FXML private TableView table;

    public void initialize() {
        loadMyHouseholds();
    }

    private void loadMyHouseholds() {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM households WHERE owner = ?"
            );
            ps.setString(1, Main.currentUser);
            ResultSet rs = ps.executeQuery();

            // TODO: map dữ liệu vào table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToMenu() throws Exception {
        Main.showMenu();
    }
}
