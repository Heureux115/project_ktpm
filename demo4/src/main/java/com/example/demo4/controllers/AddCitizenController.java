package com.example.demo4.controllers;

import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class AddCitizenController extends BaseController {

    // === KHAI BÁO CÁC CONTROL TRONG FXML ===
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

    // === CÁC BIẾN HỖ TRỢ LOGIC ===
    private Stage stage;
    private Runnable onAddSuccess;
    private Integer householdId; // ID hộ khẩu (Bắt buộc phải set từ màn hình trước)
    private Integer userId = null; // ID người tạo (Optional)

    // === SETTER ĐỂ TRUYỀN DỮ LIỆU TỪ BÊN NGOÀI VÀO ===
    public void setStage(Stage stage) { this.stage = stage; }
    public void setOnAddSuccess(Runnable r) { this.onAddSuccess = r; }
    public void setHouseholdId(Integer householdId) { this.householdId = householdId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    @FXML
    public void initialize() {
        // 1. Validate Ngày sinh: Không được chọn ngày tương lai
        dpDob.setDayCellFactory(picker -> new DateCell() {
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        // 2. Logic Checkbox Chủ hộ
        cbHouseholder.selectedProperty().addListener((obs, oldV, isSelected) -> {
            if (isSelected) {
                tfRelation.setText("Chủ hộ");
                tfRelation.setDisable(true);
            } else {
                tfRelation.clear();
                tfRelation.setDisable(false);
            }
        });

        // Mặc định ngày đăng ký là ngày hiện tại nếu chưa chọn
        dpRegisterDate.setValue(LocalDate.now());
    }

    @FXML
    private void handleAdd() {
        // --- BƯỚC 1: LẤY DỮ LIỆU ---
        String fullName = blankToNull(tfFullName.getText());
        String alias = blankToNull(tfAlias.getText());
        LocalDate dob = dpDob.getValue();
        String cccd = blankToNull(tfCccd.getText());
        String job = blankToNull(tfJob.getText());
        String relation = blankToNull(tfRelation.getText());

        // --- BƯỚC 2: VALIDATE DỮ LIỆU (Dựa trên dấu * ở FXML) ---
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

        // Validate logic quan hệ
        if (cbHouseholder.isSelected()) {
            relation = "Chủ hộ";
        } else {
            if (relation == null) {
                setMsg("Vui lòng nhập quan hệ với chủ hộ!");
                tfRelation.requestFocus();
                return;
            }
        }

        // --- BƯỚC 3: TẠO ĐỐI TƯỢNG MODEL ---
        Citizen c = new Citizen();
        c.setHouseholdId(this.householdId);
        c.setFullName(fullName);
        c.setAlias(alias);
        c.setDob(Date.valueOf(dob).toLocalDate()); // LocalDate -> SQL Date

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

        // CẢNH BÁO: FXML CỦA BẠN ĐANG THIẾU TRƯỜNG GIỚI TÍNH (GENDER)
        // Tôi tạm set null hoặc mặc định, bạn cần bổ sung ComboBox vào FXML nếu DB bắt buộc
        c.setGender("Chưa xác định");


        // --- BƯỚC 4: GỌI DAO ĐỂ LƯU ---
        try {
            CitizenDao dao = new CitizenDao(); // Hoặc getInstance() tùy code DAO của bạn
            int newId = dao.insert(c);
            boolean success = newId > 0;

            if (success) {
                // Gọi callback để màn hình cha (Danh sách) load lại dữ liệu
                if (onAddSuccess != null) {
                    onAddSuccess.run();
                }
                close(); // Đóng cửa sổ
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

    // --- CÁC HÀM HỖ TRỢ ---

    private void close() {
        if (stage != null) {
            stage.close();
        } else {
            // Fallback nếu quên setStage
            Stage s = (Stage) tfFullName.getScene().getWindow();
            if (s != null) s.close();
        }
    }

    private void setMsg(String msg) {
        if (lblMessage != null) {
            lblMessage.setText(msg);
        } else {
            // Nếu không có label thì hiện alert
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