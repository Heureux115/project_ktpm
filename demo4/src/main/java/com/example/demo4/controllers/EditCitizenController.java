package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditCitizenController extends BaseController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private DatePicker dpDob;
    @FXML private TextField tfCCCD;
    @FXML private TextField tfJob;

    private Stage stage;
    private Citizen citizen;
    private Runnable onEditSuccess;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;

        tfName.setText(citizen.getFullName());
        tfRelation.setText(citizen.getRelation());
        tfCCCD.setText(citizen.getCccd());
        tfJob.setText(citizen.getJob());

        // ⭐ chuyển String → LocalDate cho DatePicker
        if (citizen.getDob() != null && !citizen.getDob().isBlank()) {
            dpDob.setValue(LocalDate.parse(citizen.getDob()));
        }
    }

    public void setOnEditSuccess(Runnable onEditSuccess) {
        this.onEditSuccess = onEditSuccess;
    }

    @FXML
    private void handleSave() {

        if (citizen == null) return;

        if (tfName.getText().isBlank()
                || tfRelation.getText().isBlank()
                || dpDob.getValue() == null
                || tfCCCD.getText().isBlank()
                || tfJob.getText().isBlank()) {

            showWarning("Thiếu thông tin", "Nhập đầy đủ thông tin!");
            return;
        }

        try {
            citizen.setFullName(tfName.getText().trim());
            citizen.setRelation(tfRelation.getText().trim());
            citizen.setDob(dpDob.getValue().toString()); // ⭐ DatePicker
            citizen.setCccd(tfCCCD.getText().trim());
            citizen.setJob(tfJob.getText().trim());

            CitizenDao.update(citizen);

            if (onEditSuccess != null) onEditSuccess.run();
            if (stage != null) stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể cập nhật công dân!");
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }
}