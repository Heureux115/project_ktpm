package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import com.example.demo4.models.Citizen;
import com.example.demo4.models.Household;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseholdController {

    @FXML private TableView<Household> householdTable;
    @FXML private TableColumn<Household, String> colId;
    @FXML private TableColumn<Household, String> colHead;
    @FXML private TableColumn<Household, String> colAddress;

    private ObservableList<Household> householdList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHouseholdId()));
        colHead.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHeadName()));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));

        loadHouseholds();
    }

    private void loadHouseholds() {
        householdList.clear();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM households")) {

            while (rs.next()) {
                String id = rs.getString("household_id");
                String head = rs.getString("head_name");
                String address = rs.getString("address");

                // Load members
                List<Citizen> members = new ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM citizens WHERE household_id=?")) {
                    ps.setString(1, id);
                    ResultSet rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        members.add(new Citizen(
                                rs2.getInt("id"),
                                rs2.getString("full_name"),
                                rs2.getString("relation"),
                                rs2.getString("dob"),
                                rs2.getString("cccd"),
                                rs2.getString("job")
                        ));
                    }
                }

                householdList.add(new Household(id, head, address, members));
            }

            householdTable.setItems(householdList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogout() throws Exception {
        Main.showLogin();
    }

    @FXML
    public void backToMenu() throws Exception {
        Main.showMenu();
    }

    @FXML
    private void viewMembers() {
        Household selected = householdTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Chọn một hộ khẩu để xem thành viên!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo4/citizen.fxml"));
            Parent root = loader.load();

            CitizenController controller = loader.getController();
            controller.setCurrentHouseholdId(selected.getHouseholdId());
            controller.setMembers(selected.getMembers());

            Main.getPrimaryStage().setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
