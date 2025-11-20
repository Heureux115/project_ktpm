package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class CustomerController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cbStartHour, cbStartMinute, cbEndHour, cbEndMinute;

    @FXML private TableView<EventRow> eventTable;
    @FXML private TableColumn<EventRow, String> colTitle;
    @FXML private TableColumn<EventRow, String> colDate;
    @FXML private TableColumn<EventRow, String> colStart;
    @FXML private TableColumn<EventRow, String> colEnd;
    @FXML private TableColumn<EventRow, String> colLocation;

    @FXML private TextField txtTitle;
    @FXML private TextArea txtDesc;
    @FXML private ComboBox<String> txtLocation;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(c -> c.getValue().titleProperty());
        colDate.setCellValueFactory(c -> c.getValue().dateProperty());
        colStart.setCellValueFactory(c -> c.getValue().startProperty());
        colEnd.setCellValueFactory(c -> c.getValue().endProperty());
        colLocation.setCellValueFactory(c -> c.getValue().locationProperty());
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

        loadEvents();
    }

    @FXML
    public void loadEvents() {
        ObservableList<EventRow> list = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, title, date, start_time, end_time, location FROM events")) {

            while (rs.next()) {
                String start = rs.getString("start_time");
                String end = rs.getString("end_time");

                // ✅ Bỏ giây nếu có HH:mm:ss => HH:mm
                if (start != null && start.length() >= 5) start = start.substring(0, 5);
                if (end != null && end.length() >= 5) end = end.substring(0, 5);

                list.add(new EventRow(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        start,
                        end,
                        rs.getString("location")
                ));
            }
            eventTable.setItems(list);
        } catch (SQLException e) {
            showAlert("Lỗi","Lỗi tải dữ liệu: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onOpenUpdate() {
        EventRow row = eventTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showAlert("Chọn sự kiện", "Chọn sự kiện để cập nhật!", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/update_event.fxml"));
            Parent root = loader.load();

            UpdateEventController controller = loader.getController();
            controller.setEventData(
                    row.getId(),
                    row.titleProperty().get(),
                    LocalDate.parse(row.dateProperty().get()),
                    row.startProperty().get(),
                    row.endProperty().get(),
                    row.locationProperty().get(),
                    "" // mô tả
            );
            controller.setCustomerController(this);

            Stage stage = new Stage();
            stage.setTitle("Cập nhật sự kiện");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // in ra console để debug
            showAlert("Lỗi", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void onAddEvent() {
        String title = txtTitle.getText();
        LocalDate date = datePicker.getValue();
        String start = cbStartHour.getValue() + ":" + cbStartMinute.getValue();
        String end = cbEndHour.getValue() + ":" + cbEndMinute.getValue();
        String location = txtLocation.getValue(); // dùng getValue() thay vì getText()
        String desc = txtDesc.getText();

        if (title.isEmpty() || date == null || start == null || end == null || location == null) {
            showAlert("Lỗi nhập liệu","⚠️ Nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        java.time.LocalTime startTime = java.time.LocalTime.parse(start);
        java.time.LocalTime endTime = java.time.LocalTime.parse(end);

        if (!startTime.isBefore(endTime)) {
            showAlert("Lỗi giờ", "Giờ bắt đầu phải nhỏ hơn giờ kết thúc!", Alert.AlertType.WARNING);
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert("Ngày không hợp lệ", "Không thể tạo sự kiện trong quá khứ!", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = Database.getConnection()) {
            // Kiểm tra trùng giờ
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

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO events(title, date, start_time, end_time, location, description) VALUES(?,?,?,?,?,?)"
            );
            ps.setString(1, title);
            ps.setString(2, date.toString());
            ps.setString(3, start);
            ps.setString(4, end);
            ps.setString(5, location);
            ps.setString(6, desc);
            ps.executeUpdate();

            showAlert("thông báo","✅ Tạo sự kiện thành công!", Alert.AlertType.INFORMATION);
            clearFields();
            loadEvents();

        } catch (SQLException e) {
            lblMessage.setText("❌ Lỗi: " + e.getMessage());
        }
    }


    @FXML
    public void onLogout() throws Exception {
        Main.showLogin();
    }

    @FXML
    public void backToMenu() throws Exception {
        Main.showMenu();
    }

    private void clearFields() {
        txtTitle.clear();
        datePicker.setValue(null);
        cbStartHour.setValue(null);
        cbStartMinute.setValue(null);
        cbEndHour.setValue(null);
        cbEndMinute.setValue(null);
        txtLocation.setValue(null); // ComboBox
        txtDesc.clear();
    }

    // ✅ Inner class hiển thị bảng
    public static class EventRow {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty title, date, start, end, location;

        public EventRow(int id, String title, String date, String start, String end, String location) {
            this.id = new SimpleIntegerProperty(id);
            this.title = new SimpleStringProperty(title);
            this.date = new SimpleStringProperty(date);
            this.start = new SimpleStringProperty(start);
            this.end = new SimpleStringProperty(end);
            this.location = new SimpleStringProperty(location);
        }

        public int getId() { return id.get(); }
        public SimpleStringProperty titleProperty() { return title; }
        public SimpleStringProperty dateProperty() { return date; }
        public SimpleStringProperty startProperty() { return start; }
        public SimpleStringProperty endProperty() { return end; }
        public SimpleStringProperty locationProperty() { return location; }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addColumnFilter(TableColumn<CustomerController.EventRow, ?> column, String type) {
        if (type.equals("text")) {
            TextField filterField = new TextField();
            filterField.setPromptText("Lọc");
            VBox header = new VBox(new Label(column.getText()), filterField);
            column.setGraphic(header);

            filterField.textProperty().addListener((obs, oldVal, newVal) -> {
                FilteredList<EventRow> filtered = new FilteredList<>(eventTable.getItems(), row ->
                        row.titleProperty().get().toLowerCase().contains(newVal.toLowerCase()) ||
                                row.locationProperty().get().toLowerCase().contains(newVal.toLowerCase())
                );
                SortedList<EventRow> sorted = new SortedList<>(filtered);
                sorted.comparatorProperty().bind(eventTable.comparatorProperty());
                eventTable.setItems(sorted);
            });

        } else if (type.equals("date")) {
            DatePicker filterDate = new DatePicker();
            VBox header = new VBox(new Label(column.getText()), filterDate);
            column.setGraphic(header);

            filterDate.valueProperty().addListener((obs, oldVal, newVal) -> {
                FilteredList<EventRow> filtered = new FilteredList<>(eventTable.getItems(), row ->
                        newVal == null || row.dateProperty().get().equals(newVal.toString())
                );
                SortedList<EventRow> sorted = new SortedList<>(filtered);
                sorted.comparatorProperty().bind(eventTable.comparatorProperty());
                eventTable.setItems(sorted);
            });
        }
    }

}
