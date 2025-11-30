package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import com.example.demo4.models.Household;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class HouseholdAdminController {

    @FXML private TableView<Household> table;
    @FXML private TableColumn<Household, String> colId;
    @FXML private TableColumn<Household, String> colOwner;
    @FXML private TableColumn<Household, String> colAddress;
    @FXML private TableColumn<Household, Integer> colMembers;
    @FXML private TableColumn<Household, String> colCreated;

    @FXML
    public void initialize() {
        // Thiết lập các cột TableView
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHouseholdId()));
        colOwner.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHeadName()));
        colAddress.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
        colMembers.setCellValueFactory(data -> new SimpleIntegerProperty(0).asObject()); // tạm thời 0
        colCreated.setCellValueFactory(data -> new SimpleStringProperty("")); // nếu chưa có ngày tạo

        // Load dữ liệu
        loadHouseholds();
    }

    private void loadHouseholds() {
        table.getItems().clear();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM households")) {

            while (rs.next()) {
                String id = rs.getString("household_id");
                String head = rs.getString("head_name");
                String address = rs.getString("address");

                table.getItems().add(new Household(id, head, address, null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải danh sách hộ khẩu!");
        }
    }

    @FXML
    private void backToMenu() throws Exception {
        Main.showMenu();
    }

    @FXML
    private void addHousehold() {
        if (!"ADMIN".equalsIgnoreCase(Main.currentRole)) {
            showAlert("Không thể thực hiện", "Chỉ ADMIN mới có quyền thêm hộ khẩu!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/add_household.fxml"));
            Parent root = loader.load();

            AddHouseholdController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.setOnAddSuccess(this::loadHouseholds); // reload TableView sau khi thêm

            stage.setTitle("Thêm Hộ khẩu");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm hộ khẩu!");
        }
    }

    @FXML
    private void editHousehold() {
        if (!"ADMIN".equalsIgnoreCase(Main.currentRole)) {
            showAlert("Không thể thực hiện", "Chỉ ADMIN mới có quyền sửa hộ khẩu!");
            return;
        }

        Household selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Hãy chọn một hộ khẩu để sửa!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/edit_household.fxml"));
            Parent root = loader.load();

            EditHouseholdController controller = loader.getController();
            controller.setHousehold(selected);
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.setOnEditSuccess(this::loadHouseholds); // reload TableView sau khi sửa

            stage.setTitle("Sửa Hộ khẩu");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form sửa hộ khẩu!");
        }
    }


    @FXML
    private void deleteHousehold() {
        if (!"ADMIN".equalsIgnoreCase(Main.currentRole)) {
            showAlert("Không thể thực hiện", "Chỉ ADMIN mới có quyền xóa hộ khẩu!");
            return;
        }

        Household selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Hãy chọn một hộ khẩu để xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn xóa hộ " + selected.getHouseholdId() + "?",
                ButtonType.YES, ButtonType.NO
        );
        if (alert.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM households WHERE household_id=?"
            );
            ps.setString(1, selected.getHouseholdId());
            ps.executeUpdate();

            showAlert("Thành công", "Đã xóa hộ khẩu!");
            loadHouseholds();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể xóa hộ khẩu!");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
