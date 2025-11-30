package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.EventStatusUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

public class PastEventsController {

    @FXML private TableView<EventRow> tblPastEvents;
    @FXML private TableColumn<EventRow, String> colTitle;
    @FXML private TableColumn<EventRow, String> colDate;
    @FXML private TableColumn<EventRow, String> colLocation;
    @FXML private TableColumn<EventRow, String> colStatus;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {

        // Cập nhật status ĐÃ DIỄN RA trước cho chắc
        EventStatusUtil.autoUpdatePastEvents();

        colTitle.setCellValueFactory(c -> c.getValue().titleProperty());
        colDate.setCellValueFactory(c -> c.getValue().dateProperty());
        colLocation.setCellValueFactory(c -> c.getValue().locationProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        loadPastEvents();
    }

    private void loadPastEvents() {
        ObservableList<EventRow> list = FXCollections.observableArrayList();
        LocalDate limitDate = LocalDate.now().minusDays(30);
        String limitStr = limitDate.toString();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, title, date, location, status " +
                             "FROM events " +
                             "WHERE date < ? " +
                             "ORDER BY date DESC"
             )) {

            ps.setString(1, limitStr);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new EventRow(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getString("status")
                ));
            }
            tblPastEvents.setItems(list);
            lblMessage.setText("Tổng số sự kiện cũ: " + list.size());

        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Lỗi tải dữ liệu: " + e.getMessage());
        }
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
