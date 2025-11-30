package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCitizenController extends BaseController {

    @FXML private TextField tfName;
    @FXML private TextField tfRelation;
    @FXML private TextField tfDob;
    @FXML private TextField tfCCCD;
    @FXML private TextField tfJob;

    private Stage stage;
    private Runnable onAddSuccess;
    private String householdId;

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
        String name     = tfName.getText().trim();
        String relation = tfRelation.getText().trim();
        String dob      = tfDob.getText().trim();
        String cccd     = tfCCCD.getText().trim();
        String job      = tfJob.getText().trim();

        if (name.isEmpty() || relation.isEmpty() || dob.isEmpty()
                || cccd.isEmpty() || job.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin công dân!", Alert.AlertType.ERROR);
            return;
        }

        if (householdId == null || householdId.isEmpty()) {
            showAlert("Lỗi", "Không xác định được hộ khẩu cho công dân này!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // id = 0 vì DB auto-increment, model chỉ cần đủ dữ liệu logic
            Citizen citizen = new Citizen(
                    0,          // id tạm, DB sẽ tự gán
                    name,
                    relation,
                    dob,
                    cccd,
                    job
            );

            // Dùng DAO mới
            CitizenDao.insert(citizen, householdId);

            if (onAddSuccess != null) {
                onAddSuccess.run();   // reload list bên CitizenController
            }
            if (stage != null) {
                stage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể lưu công dân, vui lòng thử lại sau!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }
}
