    package com.example.demo4.controllers;

    import com.example.demo4.Database;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.stage.Stage;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import java.time.LocalDate;

    public class UpdateEventController {

        private CustomerController customerController;

        public void setCustomerController(CustomerController controller) {
            this.customerController = controller;
        }


        @FXML private DatePicker datePicker;
        @FXML private ComboBox<String> cbStartHour, cbStartMinute, cbEndHour, cbEndMinute;
        @FXML private TextField txtTitle;
        @FXML private TextArea txtDesc;
        @FXML private ComboBox<String> txtLocation;

        private int eventId;

        public void setEventData(int id, String title, LocalDate date, String start, String end, String location, String desc) {
            this.eventId = id;
            txtTitle.setText(title);
            datePicker.setValue(date);
            txtLocation.setValue(location);
            txtDesc.setText(desc);

            String[] startSplit = start.split(":");
            String[] endSplit = end.split(":");

            cbStartHour.setValue(startSplit[0]);
            cbStartMinute.setValue(startSplit[1]);
            cbEndHour.setValue(endSplit[0]);
            cbEndMinute.setValue(endSplit[1]);
        }

        @FXML
        public void initialize() {
            // populate giờ/phút
            for (int h = 0; h < 24; h++) {
                cbStartHour.getItems().add(String.format("%02d", h));
                cbEndHour.getItems().add(String.format("%02d", h));
            }
            for (int m = 0; m < 60; m += 5) {
                cbStartMinute.getItems().add(String.format("%02d", m));
                cbEndMinute.getItems().add(String.format("%02d", m));
            }

            // populate địa điểm
            txtLocation.getItems().addAll(
                    "Hội trường rộng tầng 1",
                    "Phòng chức năng tầng 2"
            );
        }


        @FXML
        public void onSave() {
            String title = txtTitle.getText();
            LocalDate date = datePicker.getValue();
            String start = cbStartHour.getValue() + ":" + cbStartMinute.getValue();
            String end = cbEndHour.getValue() + ":" + cbEndMinute.getValue();
            String location = txtLocation.getValue();
            String desc = txtDesc.getText();

            if (title.isEmpty() || date == null || start.isEmpty() || end.isEmpty()) {
                showAlert("Nhập thiếu", "Nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
                return;
            }

            if (!java.time.LocalTime.parse(start).isBefore(java.time.LocalTime.parse(end))) {
                showAlert("Lỗi giờ", "Giờ bắt đầu phải nhỏ hơn giờ kết thúc!", Alert.AlertType.WARNING);
                return;
            }

            try (Connection conn = Database.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE events SET title=?, date=?, start_time=?, end_time=?, location=?, description=? WHERE id=?"
                );
                ps.setString(1, title);
                ps.setString(2, date.toString());
                ps.setString(3, start);
                ps.setString(4, end);
                ps.setString(5, location);
                ps.setString(6, desc);
                ps.setInt(7, eventId);
                ps.executeUpdate();

                showAlert("Thành công", "Cập nhật sự kiện thành công!", Alert.AlertType.INFORMATION);
                if (customerController != null) {
                    customerController.loadEvents(); // reload bảng ở CustomerController
                }
                ((Stage) txtTitle.getScene().getWindow()).close();

            } catch (SQLException e) {
                showAlert("Lỗi", e.getMessage(), Alert.AlertType.ERROR);
            }
        }

        @FXML
        public void onCancel() {
            ((Stage) txtTitle.getScene().getWindow()).close();
        }

        private void showAlert(String title, String message, Alert.AlertType type) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
