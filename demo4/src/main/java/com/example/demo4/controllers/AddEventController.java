package com.example.demo4.controllers;

import com.example.demo4.dao.EventDao;
import com.example.demo4.models.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddEventController extends BaseController {

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
        txtLocation.getItems().addAll(
                "Hội trường rộng tầng 1",
                "Phòng chức năng tầng 2"
        );

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
        String title    = txtTitle.getText().trim();
        LocalDate date  = datePicker.getValue();
        String sh       = cbStartHour.getValue();
        String sm       = cbStartMinute.getValue();
        String eh       = cbEndHour.getValue();
        String em       = cbEndMinute.getValue();
        String location = txtLocation.getValue();
        String desc     = txtDesc.getText().trim();

        // 1. Check thiếu dữ liệu
        if (title.isEmpty() || date == null ||
                sh == null || sm == null || eh == null || em == null ||
                location == null) {
            showWarning("Lỗi nhập liệu", "⚠️ Nhập đầy đủ thông tin!");
            return;
        }

        String start = sh + ":" + sm;
        String end   = eh + ":" + em;

        // 2. Validate format giờ
        LocalTime startTime, endTime;
        try {
            startTime = LocalTime.parse(start);
            endTime   = LocalTime.parse(end);
        } catch (DateTimeParseException ex) {
            showWarning("Lỗi giờ", "Định dạng giờ không hợp lệ (HH:mm)!");
            return;
        }

        // 3. Start < End
        if (!startTime.isBefore(endTime)) {
            showWarning("Lỗi giờ", "Giờ bắt đầu phải nhỏ hơn giờ kết thúc!");
            return;
        }

        // 4. Ngày không ở quá khứ
        if (date.isBefore(LocalDate.now())) {
            showWarning("Ngày không hợp lệ", "Không thể tạo sự kiện trong quá khứ!");
            return;
        }

        try {
            // 5. Kiểm tra trùng giờ dùng EventDao
            boolean conflict = EventDao.hasTimeConflict(date.toString(), start, end);
            if (conflict) {
                showError("Trùng giờ", "Sự kiện này trùng giờ với sự kiện khác!");
                return;
            }

            // 6. Tạo Event model
            Event event = new Event(
                    -1,                         // id tạm, DB tự tăng
                    title,
                    date.toString(),
                    start,
                    end,
                    location,
                    desc,
                    Event.STATUS_REGISTERED
            );

            // 7. Lưu DB qua DAO
            EventDao.insert(event);

            showInfo("Thông báo", "✅ Tạo sự kiện thành công!");

            if (customerController != null) {
                customerController.loadEvents(); // reload list
            }

            if (stage != null) stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("❌ Lỗi: " + e.getMessage());
        }
    }

    @FXML
    public void onCancel() {
        if (stage != null) stage.close();
    }
}
