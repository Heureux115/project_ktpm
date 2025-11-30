package com.example.demo4.controllers;

import com.example.demo4.Session;
import com.example.demo4.dao.HouseholdDao;
import com.example.demo4.models.Household;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddHouseholdController extends BaseController {

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
        String id      = tfId.getText().trim();
        String head    = tfHead.getText().trim();
        String address = tfAddress.getText().trim();

        // Xác định owner: ưu tiên nhập tay, rồi tới user hiện tại, cuối cùng fallback "ADMIN"
        String ownerInput = tfOwner.getText().trim();
        String owner;
        if (!ownerInput.isEmpty()) {
            owner = ownerInput;
        } else if (Session.getCurrentUserId() != null) {
            owner = String.valueOf(Session.getCurrentUserId());
        } else {
            owner = "ADMIN";
        }

        if (id.isEmpty() || head.isEmpty() || address.isEmpty()) {
            showWarning("Lỗi", "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        try {
            // Tạo model
            Household household = new Household(id, head, address, owner);

            // Lưu qua DAO
            HouseholdDao.insert(household);

            if (onAddSuccess != null) onAddSuccess.run();
            if (stage != null) stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể thêm hộ khẩu!\n" + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }
}
