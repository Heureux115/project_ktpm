package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class AddCitizenController extends BaseController {

    
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

    @FXML private TextField tfPreviousAddress;
    @FXML private DatePicker dpRegisterDate;

    @FXML private CheckBox cbHouseholder;
    @FXML private TextField tfRelation;

    @FXML private Label lblMessage;

    
    private Stage stage;
    private Runnable onAddSuccess;
    private Integer householdId; 
    private Integer userId = null; 

    
    public void setStage(Stage stage) { this.stage = stage; }
    public void setOnAddSuccess(Runnable r) { this.onAddSuccess = r; }
    public void setHouseholdId(Integer householdId) { this.householdId = householdId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    @FXML
    public void initialize() {
        
        dpDob.setDayCellFactory(picker -> new DateCell() {
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        
        cbHouseholder.selectedProperty().addListener((obs, oldV, isSelected) -> {
            if (isSelected) {
                tfRelation.setText("Chủ hộ");
                tfRelation.setDisable(true);
            } else {
                tfRelation.clear();
                tfRelation.setDisable(false);
            }
        });

        
        dpRegisterDate.setValue(LocalDate.now());
    }

    @FXML
    private void handleAdd() {
        
        String fullName = blankToNull(tfFullName.getText());
        String alias = blankToNull(tfAlias.getText());
        LocalDate dob = dpDob.getValue();
        String cccd = blankToNull(tfCccd.getText());
        String job = blankToNull(tfJob.getText());
        String relation = blankToNull(tfRelation.getText());

        
        if (householdId == null) {
            setMsg("Lỗi hệ thống: Không xác định được hộ khẩu cần thêm!");
            return;
        }
        if (fullName == null) {
            setMsg("Vui lòng nhập Họ và tên!");
            tfFullName.requestFocus();
            return;
        }
        if (dob == null) {
            setMsg("Vui lòng chọn Ngày sinh!");
            dpDob.requestFocus();
            return;
        }
        if (cccd == null) {
            setMsg("Vui lòng nhập số CCCD!");
            tfCccd.requestFocus();
            return;
        }
        if (job == null) {
            setMsg("Vui lòng nhập Nghề nghiệp!");
            tfJob.requestFocus();
            return;
        }

        
        if (cbHouseholder.isSelected()) {
            relation = "Chủ hộ";
        } else {
            if (relation == null) {
                setMsg("Vui lòng nhập quan hệ với chủ hộ!");
                tfRelation.requestFocus();
                return;
            }
        }

        
        Citizen c = new Citizen();
        c.setHouseholdId(this.householdId);
        c.setFullName(fullName);
        c.setAlias(alias);
        c.setDob(Date.valueOf(dob).toLocalDate()); 

        c.setPlaceOfBirth(blankToNull(tfPlaceOfBirth.getText()));
        c.setHometown(blankToNull(tfHometown.getText()));
        c.setEthnicity(blankToNull(tfEthnicity.getText()));

        c.setCccd(cccd);
        if (dpCccdIssueDate.getValue() != null) {
            c.setCccdIssueDate(Date.valueOf(dpCccdIssueDate.getValue()).toLocalDate());
        }
        c.setCccdIssuePlace(blankToNull(tfCccdIssuePlace.getText()));

        c.setJob(job);
        c.setWorkplace(blankToNull(tfWorkplace.getText()));
        c.setPreviousAddress(blankToNull(tfPreviousAddress.getText()));

        if (dpRegisterDate.getValue() != null) {
            c.setRegisterDate(Date.valueOf(dpRegisterDate.getValue()).toLocalDate());
        }

        c.setRelation(relation);
        c.setRelation(cbHouseholder.isSelected() ? "Chủ hộ" : "Thành viên");

        
        
        c.setGender("Chưa xác định");


        
        try {
            CitizenDao dao = new CitizenDao(); 
            int newId = dao.insert(c);
            boolean success = newId > 0;

            if (success) {
                
                if (onAddSuccess != null) {
                    onAddSuccess.run();
                }
                close(); 
            } else {
                setMsg("Thêm thất bại! Có thể số CCCD đã tồn tại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setMsg("Lỗi Database: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    

    private void close() {
        if (stage != null) {
            stage.close();
        } else {
            
            Stage s = (Stage) tfFullName.getScene().getWindow();
            if (s != null) s.close();
        }
    }

    private void setMsg(String msg) {
        if (lblMessage != null) {
            lblMessage.setText(msg);
        } else {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        }
    }

    private String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}