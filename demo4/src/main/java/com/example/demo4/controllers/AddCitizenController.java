package com.example.demo4.controllers;

import com.example.demo4.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCitizenController extends BaseController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private TextField tfDob;
    @FXML private TextField tfCCCD;
    @FXML private TextField tfJob;

    private Stage stage;
    private Runnable onAddSuccess;
    private String householdId; // id của hộ khẩu

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnAddSuccess(Runnable r) {
        this.onAddSuccess = r;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    @FXML
    private void handleAdd() {
        String name = tfName.getText().trim();
        String relation = tfRelation.getText().trim();
        String dob = tfDob.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String job = tfJob.getText().trim();

        if (name.isEmpty() || relation.isEmpty() || dob.isEmpty() || cccd.isEmpty() || job.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin công dân!", Alert.AlertType.ERROR);
            return;
        }


        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(
                     "INSERT INTO citizens (full_name, relation, dob, cccd, job, household_id) VALUES (?, ?, ?, ?, ?, ?)")) {

            st.setString(1, name);
            st.setString(2, relation);
            st.setString(3, dob);
            st.setString(4, cccd);
            st.setString(5, job);
            st.setString(6, householdId); // gán tự động theo hộ khẩu
            st.executeUpdate();

            if (onAddSuccess != null) onAddSuccess.run();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể cập nhật dữ liệu, vui lòng thử lại sau!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }

}
