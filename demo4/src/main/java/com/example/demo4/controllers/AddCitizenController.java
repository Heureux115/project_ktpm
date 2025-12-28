package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddCitizenController extends BaseController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private DatePicker dpDob;
    @FXML private TextField tfCCCD;
    @FXML private TextField tfJob;

    private Stage stage;
    private Runnable onAddSuccess;
    private Integer householdId;

    @FXML
    public void initialize() {
        dpDob.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnAddSuccess(Runnable r) {
        this.onAddSuccess = r;
    }

    public void setHouseholdId(Integer householdId) {
        this.householdId = householdId;
    }

    @FXML
    private void handleAdd() {

        if (tfName.getText().isBlank()
                || tfRelation.getText().isBlank()
                || tfCCCD.getText().isBlank()
                || tfJob.getText().isBlank()
                || dpDob.getValue() == null) {

            showWarning("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!isValidCCCD(tfCCCD.getText().trim())) {
            showWarning("Sai CCCD", "CCCD phải gồm đúng 12 chữ số!");
            return;
        }

        if (!isValidDob(dpDob.getValue())) {
            showWarning("Sai ngày sinh", "Ngày sinh không hợp lệ!");
            return;
        }

        try {
            Citizen citizen = new Citizen(
                    0,
                    tfName.getText().trim(),
                    tfRelation.getText().trim(),
                    dpDob.getValue().toString(), // ⭐ DatePicker → String
                    tfCCCD.getText().trim(),
                    tfJob.getText().trim(),
                    householdId,
                    null
            );

            CitizenDao.insert(citizen);

            if (onAddSuccess != null) onAddSuccess.run();
            closeStage();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể thêm công dân!");
        }
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        if (stage != null) stage.close();
    }

    // ===== VALIDATE =====
    private boolean isValidCCCD(String cccd) {
        return cccd.matches("\\d{12}");
    }

    private boolean isValidDob(LocalDate dob) {
        return !dob.isAfter(LocalDate.now());
    }
}
