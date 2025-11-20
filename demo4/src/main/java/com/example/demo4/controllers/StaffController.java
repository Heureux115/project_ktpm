package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class StaffController {
    @FXML private TableView<EventRow> tblEvents;
    @FXML private TextField txtTitle, txtDate, txtLocation;
    @FXML private TextArea txtDesc;
    @FXML private Label lblMessage;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnLogout;

    @FXML
    public void initialize() {
        tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldv, newv) -> {
            if (newv != null) {
                txtTitle.setText(newv.getTitle());
                txtDate.setText(newv.getDate());
                txtLocation.setText(newv.getLocation());
                txtDesc.setText(newv.getDescription());
            }
        });

        loadEvents();
    }


    private void loadEvents() {
        ObservableList<EventRow> list = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id, title, date, location, description FROM events")) {

            while (rs.next()) {
                list.add(new EventRow(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getString("description")
                ));
            }
            tblEvents.setItems(list);

        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Lỗi load dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    public void onAdd(ActionEvent e) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO events(title, date, location, description) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, txtTitle.getText());
            ps.setString(2, txtDate.getText());
            ps.setString(3, txtLocation.getText());
            ps.setString(4, txtDesc.getText());

            ps.executeUpdate();
            lblMessage.setText("✅ Thêm event thành công!");
            loadEvents();

        } catch (SQLException ex) {
            ex.printStackTrace();
            lblMessage.setText("❌ Lỗi thêm: " + ex.getMessage());
        }
    }


    @FXML
    public void onUpdate(ActionEvent e) {
        EventRow sel = tblEvents.getSelectionModel().getSelectedItem();
        if (sel==null) { lblMessage.setText("Chọn event để sửa"); return; }
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE events SET title=?,date=?,location=?,description=? WHERE id=?")) {
            ps.setString(1, txtTitle.getText());
            ps.setString(2, txtDate.getText());
            ps.setString(3, txtLocation.getText());
            ps.setString(4, txtDesc.getText());
            ps.setInt(5, sel.getId());
            ps.executeUpdate();
            lblMessage.setText("Cập nhật thành công");
            loadEvents();
        } catch (SQLException ex) { ex.printStackTrace(); lblMessage.setText("Lỗi: "+ex.getMessage()); }
    }

    @FXML
    public void onDelete(ActionEvent e) {
        EventRow sel = tblEvents.getSelectionModel().getSelectedItem();
        if (sel==null) { lblMessage.setText("Chọn event để xóa"); return; }
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM events WHERE id=?")) {
            ps.setInt(1, sel.getId());
            ps.executeUpdate();
            lblMessage.setText("Xóa thành công");
            loadEvents();
        } catch (SQLException ex) { ex.printStackTrace(); lblMessage.setText("Lỗi: "+ex.getMessage()); }
    }

    @FXML
    public void onLogout(ActionEvent e) throws Exception {
        Main.showLogin();
    }

    public static class EventRow {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty date;
        private final SimpleStringProperty location;
        private final SimpleStringProperty description;

        public EventRow(int id, String title, String date, String location, String description) {
            this.id = new SimpleIntegerProperty(id);
            this.title = new SimpleStringProperty(title);
            this.date = new SimpleStringProperty(date);
            this.location = new SimpleStringProperty(location);
            this.description = new SimpleStringProperty(description);
        }

        public int getId() { return id.get(); }
        public String getTitle() { return title.get(); }
        public String getDate() { return date.get(); }
        public String getLocation() { return location.get(); }
        public String getDescription() { return description.get(); }
    }

}

