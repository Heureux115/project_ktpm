package com.example.demo4.controllers;

import com.example.demo4.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddEventController {

    @FXML private TextField txtTitle;
    @FXML private TextArea txtDesc;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cbStartHour, cbStartMinute, cbEndHour, cbEndMinute;
    @FXML private ComboBox<String> txtLocation;
    @FXML private Label lblMessage;

    private Stage stage;
    private CustomerController customerController;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCustomerController(CustomerController controller) {
        this.customerController = controller;
    }

    @FXML
    public void initialize() {
        // Địa điểm
        txtLocation.getItems().addAll(
                "Hội trường rộng tầng 1",
                "Phòng chức năng tầng 2"
        );

        // Giờ / phút
        for (int h = 0; h < 24; h++) {
            cbStartHour.getItems().add(String.format("%02d", h));
            cbEndHour.getItems().add(String.format("%02d", h));
        }
        for (int m = 0; m < 60; m += 5) {
            cbStartMinute.getItems().add(String.format("%02d", m));
            cbEndMinute.getItems().add(String.format("%02d", m));
        }
    }

    @FXML
    public void onSave() {
        String title = txtTitle.getText().trim();
        LocalDate date = datePicker.getValue();
        String sh = cbStartHour.getValue();
        String sm = cbStartMinute.getValue();
        String eh = cbEndHour.getValue();
        String em = cbEndMinute.getValue();
        String location = txtLocation.getValue();
        String desc = txtDesc.getText().trim();

        // Check thiếu
        if (title.isEmpty() || date == null ||
                sh == null || sm == null || eh == null || em == null ||
                location == null) {
            showAlert("Lỗi nhập liệu", "⚠️ Nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        String start = sh + ":" + sm;
        String end   = eh + ":" + em;

        LocalTime startTime, endTime;
        try {
            startTime = LocalTime.parse(start);
            endTime = LocalTime.parse(end);
        } catch (DateTimeParseException ex) {
            showAlert("Lỗi giờ", "Định dạng giờ không hợp lệ (HH:mm)!", Alert.AlertType.WARNING);
            return;
        }

        // Check logic giờ
        if (!startTime.isBefore(endTime)) {
            showAlert("Lỗi giờ", "Giờ bắt đầu phải nhỏ hơn giờ kết thúc!", Alert.AlertType.WARNING);
            return;
        }

        // Không cho tạo sự kiện quá khứ
        if (date.isBefore(LocalDate.now())) {
            showAlert("Ngày không hợp lệ", "Không thể tạo sự kiện trong quá khứ!", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = Database.getConnection()) {
            // Kiểm tra trùng giờ trong cùng ngày
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM events " +
                            "WHERE date = ? " +
                            "AND ((start_time <= ? AND end_time > ?) " +
                            "OR (start_time < ? AND end_time >= ?))"
            );
            checkStmt.setString(1, date.toString());
            checkStmt.setString(2, start);
            checkStmt.setString(3, start);
            checkStmt.setString(4, end);
            checkStmt.setString(5, end);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                showAlert("Trùng giờ", "Sự kiện này trùng giờ với sự kiện khác!", Alert.AlertType.ERROR);
                return;
            }

            // Insert
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO events(title, date, start_time, end_time, location, description, status) " +
                            "VALUES(?,?,?,?,?,?,?)"
            );
            ps.setString(1, title);
            ps.setString(2, date.toString());
            ps.setString(3, start);
            ps.setString(4, end);
            ps.setString(5, location);
            ps.setString(6, desc);
            ps.setString(7, com.example.demo4.models.Event.STATUS_REGISTERED);
            ps.executeUpdate();


            showAlert("Thông báo", "✅ Tạo sự kiện thành công!", Alert.AlertType.INFORMATION);

            // Reload bảng ở CustomerController nếu có
            if (customerController != null) {
                customerController.loadEvents();
            }

            // Đóng cửa sổ
            if (stage != null) stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("❌ Lỗi: " + e.getMessage());
        }
    }

    @FXML
    public void onCancel() {
        if (stage != null) stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
