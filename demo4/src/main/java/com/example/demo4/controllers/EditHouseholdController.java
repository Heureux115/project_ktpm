package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.models.Household;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditHouseholdController extends BaseController {

    @FXML private TextField tfHead;
    @FXML private TextField tfAddress;

    private Stage stage;
    private Household household;
    private Runnable onEditSuccess;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHousehold(Household h) {
        this.household = h;
        tfHead.setText(h.getHeadName());
        tfAddress.setText(h.getAddress());
    }

    public void setOnEditSuccess(Runnable r) {
        this.onEditSuccess = r;
    }

    @FXML
    private void handleSave() {
        String head = tfHead.getText().trim();
        String address = tfAddress.getText().trim();
        if (head.isEmpty() || address.isEmpty()) return;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE households SET head_name=?, address=? WHERE household_id=?")) {

            ps.setString(1, head);
            ps.setString(2, address);
            ps.setString(3, household.getHouseholdId());
            ps.executeUpdate();

            if (onEditSuccess != null) onEditSuccess.run();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể cập nhật hộ khẩu, vui lòng thử lại!");
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }
}
