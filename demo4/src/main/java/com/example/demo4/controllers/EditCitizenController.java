package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditCitizenController extends BaseController {

    @FXML private TextField tfFullName;
    @FXML private TextField tfAlias;
    @FXML private DatePicker dpDob;
    @FXML private TextField tfPlaceOfBirth;
    @FXML private TextField tfHometown;
    @FXML private TextField tfEthnicity;

    @FXML private TextField tfCccd;
    @FXML private DatePicker dpCccdIssueDate;
    @FXML private TextField tfCccdIssuePlace;

    @FXML private TextField tfJob;
    @FXML private TextField tfWorkplace;
    @FXML private ChoiceBox<String> cbStatus;
    @FXML private TextField tfPreviousAddress;
    @FXML private DatePicker dpRegisterDate;

    @FXML private CheckBox cbHouseholder;
    @FXML private TextField tfRelation;

    @FXML private Label lblMessage;

    private Stage stage;
    private Citizen citizen;
    private Runnable onEditSuccess;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setOnEditSuccess(Runnable r) { this.onEditSuccess = r; }

    public void setCitizen(Citizen c) {
        this.citizen = c;

        tfFullName.setText(c.getFullName());
        tfAlias.setText(nvl(c.getAlias()));
        if (c.getDob() != null) dpDob.setValue(c.getDob());

        tfPlaceOfBirth.setText(nvl(c.getPlaceOfBirth()));
        tfHometown.setText(nvl(c.getHometown()));
        tfEthnicity.setText(nvl(c.getEthnicity()));

        tfCccd.setText(nvl(c.getCccd()));
        if (c.getCccdIssueDate() != null) {
            dpCccdIssueDate.setValue(c.getCccdIssueDate());
        }
        tfCccdIssuePlace.setText(nvl(c.getCccdIssuePlace()));

        tfJob.setText(nvl(c.getJob()));
        tfWorkplace.setText(nvl(c.getWorkplace()));

        tfPreviousAddress.setText(nvl(c.getPreviousAddress()));
        if (c.getRegisterDate() != null) {
            dpRegisterDate.setValue(c.getRegisterDate());
        }

        if (c.getStatus() != null && !c.getStatus().isEmpty()) {
            cbStatus.setValue(c.getStatus());
        } else {
            cbStatus.setValue("Thường trú"); // Giá trị mặc định
        }

        cbHouseholder.setSelected(Boolean.TRUE.equals(c.getHouseholder()));
        if (cbHouseholder.isSelected()) {
            tfRelation.clear();
            tfRelation.setDisable(true);
        } else {
            tfRelation.setDisable(false);
            tfRelation.setText(nvl(c.getRelation()));
        }
    }

    @FXML
    public void initialize() {
        cbStatus.setItems(FXCollections.observableArrayList(
                "Thường trú",
                "Tạm trú",
                "Tạm vắng",
                "Mới sinh",
                "Đã qua đời"
        ));
        cbHouseholder.selectedProperty().addListener((obs, oldV, isSelected) -> {
            if (isSelected) {
                tfRelation.clear();
                tfRelation.setDisable(true);
            } else {
                tfRelation.setDisable(false);
            }
        });
        Platform.runLater(() -> {
            Stage s = (Stage) tfFullName.getScene().getWindow();
            s.setWidth(900);
            s.setHeight(600);
            s.setResizable(false); // nếu muốn
            s.centerOnScreen();
        });
    }

    @FXML
    private void handleSave() {
        if (citizen == null) return;

        if (tfFullName.getText().isBlank()
                || dpDob.getValue() == null
                || tfCccd.getText().isBlank()
                || tfJob.getText().isBlank()
                || cbStatus.getValue() == null) {
            setMsg("Thiếu thông tin bắt buộc: Họ tên, ngày sinh, CCCD, nghề nghiệp, tình trạng!");
            return;
        }

        if (!tfCccd.getText().trim().matches("\\d{12}")) {
            setMsg("CCCD phải đúng 12 chữ số!");
            return;
        }

        if (!cbHouseholder.isSelected() && tfRelation.getText().isBlank()) {
            setMsg("Không phải chủ hộ thì phải nhập quan hệ!");
            return;
        }

        try {
            citizen.setFullName(tfFullName.getText().trim());
            citizen.setAlias(blankToNull(tfAlias.getText()));
            citizen.setDob(dpDob.getValue());
            citizen.setPlaceOfBirth(blankToNull(tfPlaceOfBirth.getText()));
            citizen.setHometown(blankToNull(tfHometown.getText()));
            citizen.setEthnicity(blankToNull(tfEthnicity.getText()));

            citizen.setCccd(tfCccd.getText().trim());
            citizen.setCccdIssueDate(dpCccdIssueDate.getValue());
            citizen.setCccdIssuePlace(blankToNull(tfCccdIssuePlace.getText()));

            citizen.setJob(tfJob.getText().trim());
            citizen.setWorkplace(blankToNull(tfWorkplace.getText()));

            citizen.setPreviousAddress(blankToNull(tfPreviousAddress.getText()));
            citizen.setRegisterDate(dpRegisterDate.getValue());

            citizen.setStatus(cbStatus.getValue());

            citizen.setHouseholder(cbHouseholder.isSelected());
            citizen.setRelation(cbHouseholder.isSelected() ? null : tfRelation.getText().trim());

            CitizenDao.update(citizen);

            if (onEditSuccess != null) onEditSuccess.run();
            close();

        } catch (Exception e) {
            e.printStackTrace();
            setMsg("Lỗi cập nhật công dân!");
        }
    }

    @FXML
    private void handleCancel() { close(); }

    private void close() {
        if (stage != null) stage.close();
        else {
            Stage s = (Stage) tfFullName.getScene().getWindow();
            s.close();
        }
    }

    private void setMsg(String msg) {
        if (lblMessage != null) lblMessage.setText(msg);
        else showWarning("Thông báo", msg);
    }

    private String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String nvl(String s) { return s == null ? "" : s; }


}
