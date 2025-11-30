package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCitizenController extends BaseController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private TextField tfDob;
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
        tfDob.setText(citizen.getDob());
        tfCCCD.setText(citizen.getCccd());
        tfJob.setText(citizen.getJob());
    }

    public void setOnEditSuccess(Runnable onEditSuccess) {
        this.onEditSuccess = onEditSuccess;
    }

    @FXML
    private void handleSave() {
        String name     = tfName.getText().trim();
        String relation = tfRelation.getText().trim();
        String dob      = tfDob.getText().trim();
        String cccd     = tfCCCD.getText().trim();
        String job      = tfJob.getText().trim();

        if (name.isEmpty() || relation.isEmpty() || dob.isEmpty()
                || cccd.isEmpty() || job.isEmpty()) {
            showAlert("Thiếu thông tin",
                    "Vui lòng nhập đầy đủ thông tin công dân!",
                    Alert.AlertType.WARNING);
            return;
        }

        try {
            // Cập nhật lại object Citizen trong memory
            citizen.setFullName(name);
            citizen.setRelation(relation);
            citizen.setDob(dob);
            citizen.setCccd(cccd);
            citizen.setJob(job);

            // Ghi xuống DB qua DAO
            CitizenDao.update(citizen);

            if (onEditSuccess != null) onEditSuccess.run();
            if (stage != null) stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể cập nhật thông tin công dân, vui lòng thử lại!");
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }
}
