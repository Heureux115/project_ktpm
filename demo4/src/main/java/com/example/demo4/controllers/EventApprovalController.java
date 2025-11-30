package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.models.Event;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class EventApprovalController {

    @FXML private TableView<EventRow> tblEvents;
    @FXML private TableColumn<EventRow, String> colTitle;
    @FXML private TableColumn<EventRow, String> colDate;
    @FXML private TableColumn<EventRow, String> colLocation;
    @FXML private TableColumn<EventRow, String> colStatus;

    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(c -> c.getValue().titleProperty());
        colDate.setCellValueFactory(c -> c.getValue().dateProperty());
        colLocation.setCellValueFactory(c -> c.getValue().locationProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        loadEvents();
    }

    private void loadEvents() {
        ObservableList<EventRow> list = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id, title, date, location, status FROM events"
             )) {

            while (rs.next()) {
                list.add(new EventRow(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getString("status")
                ));
            }
            tblEvents.setItems(list);

        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void onMarkPaid() {
        changeStatus(Event.STATUS_PAID);
    }

    @FXML
    private void onMarkConfirmed() {
        changeStatus(Event.STATUS_CONFIRMED);
    }

    @FXML
    private void onMarkCancelled() {
        changeStatus(Event.STATUS_CANCELLED);
    }

    private void changeStatus(String newStatus) {
        EventRow sel = tblEvents.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("Chọn sự kiện", "Hãy chọn sự kiện để cập nhật trạng thái!");
            return;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE events SET status=? WHERE id=?"
             )) {
            ps.setString(1, newStatus);
            ps.setInt(2, sel.getId());
            ps.executeUpdate();

            showAlert("Thành công", "Cập nhật trạng thái thành công!");
            loadEvents();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể cập nhật trạng thái!");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class EventRow {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty date;
        private final SimpleStringProperty location;
        private final SimpleStringProperty status;

        public EventRow(int id, String title, String date, String location, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.title = new SimpleStringProperty(title);
            this.date = new SimpleStringProperty(date);
            this.location = new SimpleStringProperty(location);
            this.status = new SimpleStringProperty(status);
        }

        public int getId() { return id.get(); }
        public SimpleStringProperty titleProperty() { return title; }
        public SimpleStringProperty dateProperty() { return date; }
        public SimpleStringProperty locationProperty() { return location; }
        public SimpleStringProperty statusProperty() { return status; }
    }
}
