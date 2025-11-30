package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddHouseholdController {

    @FXML private TextField tfId;
    @FXML private TextField tfHead;
    @FXML private TextField tfAddress;
    @FXML private TextField tfOwner;

    private Stage stage;
    private Runnable onAddSuccess;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnAddSuccess(Runnable r) {
        this.onAddSuccess = r;
    }

    @FXML
    private void handleAdd() {
        String id = tfId.getText().trim();
        String head = tfHead.getText().trim();
        String address = tfAddress.getText().trim();
        String owner = tfOwner.getText().trim().isEmpty()
                ? (Main.currentUser != null ? Main.currentUser : "ADMIN")
                : tfOwner.getText().trim();

        if (id.isEmpty() || head.isEmpty() || address.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO households (household_id, head_name, address, owner) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, id);
            ps.setString(2, head);
            ps.setString(3, address);
            ps.setString(4, owner);
            ps.executeUpdate();

            if (onAddSuccess != null) onAddSuccess.run();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể thêm hộ khẩu!");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }
}
