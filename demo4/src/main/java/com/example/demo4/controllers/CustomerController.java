package com.example.demo4.controllers;

import com.example.demo4.EventStatusUtil;
import com.example.demo4.Main;
import com.example.demo4.dao.EventDao;
import com.example.demo4.models.Event;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CustomerController extends BaseController {

    @FXML private TableView<EventRow> eventTable;
    @FXML private TableColumn<EventRow, String> colTitle;
    @FXML private TableColumn<EventRow, String> colDate;
    @FXML private TableColumn<EventRow, String> colStart;
    @FXML private TableColumn<EventRow, String> colEnd;
    @FXML private TableColumn<EventRow, String> colLocation;
    @FXML private TableColumn<EventRow, String> colStatus;

    private TextField titleFilterField;
    private TextField locationFilterField;
    private DatePicker dateFilterPicker;

    @FXML
    public void initialize() {
        // Binding các cột
        colTitle.setCellValueFactory(c -> c.getValue().titleProperty());
        colDate.setCellValueFactory(c -> c.getValue().dateProperty());
        colStart.setCellValueFactory(c -> c.getValue().startProperty());
        colEnd.setCellValueFactory(c -> c.getValue().endProperty());
        colLocation.setCellValueFactory(c -> c.getValue().locationProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        loadEvents();

        // Filter theo cột
        addColumnFilter(colTitle, "text");
        addColumnFilter(colLocation, "text");
        addColumnFilter(colDate, "date");
    }

    @FXML
    private void onResetTable() {
        if (titleFilterField != null) titleFilterField.clear();
        if (locationFilterField != null) locationFilterField.clear();
        if (dateFilterPicker != null) dateFilterPicker.setValue(null);

        eventTable.getSortOrder().clear();
        loadEvents();
    }

    @FXML
    private void onOpenUpdate(ActionEvent event) {
        EventRow row = eventTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showAlert("Chọn sự kiện", "Chọn sự kiện để cập nhật!", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/update_event.fxml")
            );
            Parent root = loader.load();

            UpdateEventController controller = loader.getController();
            controller.setEventData(
                    row.getId(),
                    row.titleProperty().get(),
                    LocalDate.parse(row.dateProperty().get()),
                    row.startProperty().get(),
                    row.endProperty().get(),
                    row.locationProperty().get(),
                    row.descriptionProperty().get(),
                    row.statusProperty().get()      // <-- thêm status
            );

            controller.setCustomerController(this);

            Stage stage = new Stage();
            stage.setTitle("Cập nhật sự kiện");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void loadEvents() {

        // 1. Tự động cập nhật status cho sự kiện đã qua
        EventStatusUtil.autoUpdatePastEvents();

        // 2. Chỉ lấy sự kiện từ (hôm nay - 30 ngày) trở đi
        LocalDate minDate = LocalDate.now().minusDays(30);

        ObservableList<EventRow> list = FXCollections.observableArrayList();
        try {
            // dùng EventDao thay cho JDBC trực tiếp
            for (Event e : EventDao.findUpcomingFrom(minDate)) {

                String start = null;
                String end   = null;
                try {
                    // nếu Event có getter startTime/endTime
                    start = e.getStartTime();
                    end   = e.getEndTime();
                } catch (NoSuchMethodError | Exception ignore) {
                    // nếu model chưa có, có thể bỏ đoạn này hoặc sửa lại cho khớp
                }

                if (start != null && start.length() >= 5) start = start.substring(0, 5);
                if (end   != null && end.length()   >= 5) end   = end.substring(0, 5);

                list.add(new EventRow(
                        e.getId(),
                        e.getTitle(),
                        e.getDate(),
                        start,
                        end,
                        e.getLocation(),
                        e.getDescription(),
                        e.getStatus()
                ));
            }

            eventTable.setItems(list);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Lỗi", "Lỗi tải dữ liệu: " + ex.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onAddEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/add_event.fxml"));
            Parent root = loader.load();

            AddEventController controller = loader.getController();
            controller.setCustomerController(this);

            Stage stage = new Stage();
            stage.setTitle("Tạo sự kiện mới");
            controller.setStage(stage);

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onLogout() throws Exception {
        Main.logout();
    }

    @FXML
    public void backToMenu() throws Exception {
        Main.showMenu();
    }

    // ==== Inner class hiển thị bảng ====
    public static class EventRow {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty date;
        private final SimpleStringProperty start;
        private final SimpleStringProperty end;
        private final SimpleStringProperty location;
        private final SimpleStringProperty description;
        private final SimpleStringProperty status;

        public EventRow(int id, String title, String date,
                        String start, String end,
                        String location, String description, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.title = new SimpleStringProperty(title);
            this.date = new SimpleStringProperty(date);
            this.start = new SimpleStringProperty(start == null ? "" : start);
            this.end = new SimpleStringProperty(end == null ? "" : end);
            this.location = new SimpleStringProperty(location);
            this.description = new SimpleStringProperty(description == null ? "" : description);
            this.status = new SimpleStringProperty(
                    status == null ? "ĐĂNG KÝ" : status
            );
        }

        public int getId() { return id.get(); }
        public SimpleStringProperty titleProperty() { return title; }
        public SimpleStringProperty dateProperty() { return date; }
        public SimpleStringProperty startProperty() { return start; }
        public SimpleStringProperty endProperty() { return end; }
        public SimpleStringProperty locationProperty() { return location; }
        public SimpleStringProperty descriptionProperty() { return description; }
        public SimpleStringProperty statusProperty() { return status; }
    }

    // ==== Filter header cho các cột ====
    private void addColumnFilter(TableColumn<CustomerController.EventRow, ?> column, String type) {
        if (type.equals("text")) {
            TextField filterField = new TextField();
            filterField.setPromptText("Lọc");

            if (column == colTitle) {
                titleFilterField = filterField;
            } else if (column == colLocation) {
                locationFilterField = filterField;
            }

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
            filterDate.setPromptText("Lọc");

            if (column == colDate) {
                dateFilterPicker = filterDate;
            }

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
