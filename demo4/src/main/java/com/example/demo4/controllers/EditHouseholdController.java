package com.example.demo4.controllers;

import com.example.demo4.dao.HouseholdDao;
import com.example.demo4.models.Household;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        String head    = tfHead.getText().trim();
        String address = tfAddress.getText().trim();

        if (head.isEmpty() || address.isEmpty()) {
            showWarning("Thiếu thông tin", "Vui lòng nhập đầy đủ chủ hộ và địa chỉ!");
            return;
        }

        try {
            // cập nhật object trong bộ nhớ
            household.setHeadName(head);
            household.setAddress(address);

            // ghi xuống DB qua DAO
            HouseholdDao.update(household);

            if (onEditSuccess != null) onEditSuccess.run();
            if (stage != null) stage.close();
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
