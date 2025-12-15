package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddHomelessCitizenController extends BaseController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private TextField tfDob;
    @FXML private TextField tfCCCD;
    @FXML private TextField tfJob;
    @FXML private Label lblMessage;

    private HomelessCitizenController parent;

    public void setParent(HomelessCitizenController parent) {
        this.parent = parent;
    }

    @FXML
    private void save() {
        try {
            if (tfName.getText().isBlank() || tfCCCD.getText().isBlank()) {
                lblMessage.setText("Tên và CCCD không được trống!");
                return;
            }

            Citizen c = new Citizen(
                    0,
                    tfName.getText().trim(),
                    tfRelation.getText().trim(),
                    tfDob.getText().trim(),
                    tfCCCD.getText().trim(),
                    tfJob.getText().trim(),
                    null,   // household_id
                    null    // user_id
            );

            CitizenDao.insert(c);
            showInfo("Thành công", "Đã thêm công dân!");

            if (parent != null) parent.reload();

            close();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Lỗi thêm công dân!");
        }
    }

    @FXML
    private void cancel() {
        close();
    }

    private void close() {
        Stage s = (Stage) tfName.getScene().getWindow();
        s.close();
    }
}
