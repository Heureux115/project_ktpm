package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.Database;
import com.example.demo4.models.Citizen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

import java.io.IOException;
import java.sql.*;

public class CitizenController {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, String> colName;
    @FXML private TableColumn<Citizen, String> colRelation;
    @FXML private TableColumn<Citizen, String> colDob;
    @FXML private TableColumn<Citizen, String> colCCCD;
    @FXML private TableColumn<Citizen, String> colJob;
    @FXML private TextField searchField;

    private ObservableList<Citizen> citizenList = FXCollections.observableArrayList();

    private boolean fromHousehold = false;

    private String currentHouseholdId;

    public void setCurrentHouseholdId(String id) {
        this.currentHouseholdId = id;
    }

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        colRelation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRelation()));
        colDob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDob()));
        colCCCD.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCccd()));
        colJob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getJob()));

        citizenTable.setItems(citizenList);
        loadFromDB();
    }

    private void loadFromDB() {
        citizenList.clear();
        try (Connection conn = Database.getConnection()) {
            PreparedStatement st;

            if (currentHouseholdId != null && !currentHouseholdId.isEmpty()) {
                // Trường hợp đang xem thành viên của một hộ cụ thể
                st = conn.prepareStatement("SELECT * FROM citizens WHERE household_id = ?");
                st.setString(1, currentHouseholdId);
            } else {
                // Trường hợp xem toàn bộ công dân (màn quản lý chung)
                st = conn.prepareStatement("SELECT * FROM citizens");
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                citizenList.add(new Citizen(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("relation"),
                        rs.getString("dob"),
                        rs.getString("cccd"),
                        rs.getString("job")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========= ADD ==========
    @FXML
    private void addCitizen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/add_citizen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.setTitle("Thêm công dân");

            AddCitizenController controller = loader.getController();
            controller.setStage(stage);
            controller.setHouseholdId(currentHouseholdId);
            controller.setOnAddSuccess(this::loadFromDB); // reload danh sách sau khi thêm

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ========= EDIT ==========
    @FXML
    private void editCitizen() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Hãy chọn công dân để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/edit_citizen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.setTitle("Sửa công dân");

            EditCitizenController controller = loader.getController();
            controller.setStage(stage);
            controller.setCitizen(selected);
            controller.setOnEditSuccess(this::loadFromDB);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========= DELETE ==========
    @FXML
    private void deleteCitizen() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Chọn công dân để xóa!");
            return;
        }

        try {
            Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM citizens WHERE id=?");
            st.setInt(1, selected.getId());
            st.executeUpdate();

            loadFromDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========= MENU ==========
    @FXML
    private void backToMenu() throws IOException {
        Main.showMenu();
    }

    @FXML
    private void searchCitizen() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            citizenTable.setItems(citizenList);
            return;
        }

        ObservableList<Citizen> filteredList = FXCollections.observableArrayList();
        for (Citizen c : citizenList) {
            if (c.getFullName().toLowerCase().contains(keyword) || c.getCccd().toLowerCase().contains(keyword)) {
                filteredList.add(c);
            }
        }
        citizenTable.setItems(filteredList);
    }

    @FXML
    private void refreshList() {
        searchField.clear();
        loadFromDB();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
