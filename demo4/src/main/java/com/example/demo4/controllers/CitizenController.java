package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.Database;
import com.example.demo4.models.Citizen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;

public class CitizenController {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, String> colName;
    @FXML private TableColumn<Citizen, String> colRelation;
    @FXML private TableColumn<Citizen, String> colDob;
    @FXML private TableColumn<Citizen, String> colCCCD;
    @FXML private TableColumn<Citizen, String> colJob;
    @FXML private TextField searchField;

    private ObservableList<Citizen> citizenList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        colRelation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRelation()));
        colDob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDob()));
        colCCCD.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCccd()));
        colJob.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getJob()));

        loadFromDB();
    }

    private void loadFromDB() {
        citizenList.clear();
        try {
            Connection conn = Database.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM citizens");

            while (rs.next()) {
                citizenList.add(new Citizen(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("relation"),
                        rs.getString("dob"),
                        rs.getString("cccd"),
                        rs.getString("job")
                ));
            }

            citizenTable.setItems(citizenList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========= ADD ==========
    @FXML
    private void addCitizen() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thêm công dân");
        dialog.setHeaderText("Nhập theo format:");
        dialog.setContentText("Họ tên;Quan hệ;Ngày sinh;CCCD;Nghề:");

        dialog.showAndWait().ifPresent(data -> {
            String[] parts = data.split(";");
            if (parts.length != 5) {
                showAlert("Sai format!", "Hãy nhập 5 trường cách nhau dấu ;");
                return;
            }

            try {
                Connection conn = Database.getConnection();
                PreparedStatement st = conn.prepareStatement(
                        "INSERT INTO citizens (full_name, relation, dob, cccd, job) VALUES (?, ?, ?, ?, ?)"
                );
                st.setString(1, parts[0]);
                st.setString(2, parts[1]);
                st.setString(3, parts[2]);
                st.setString(4, parts[3]);
                st.setString(5, parts[4]);
                st.executeUpdate();

                loadFromDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ========= EDIT ==========
    @FXML
    private void editCitizen() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Hãy chọn công dân để sửa.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(
                selected.getFullName() + ";" +
                        selected.getRelation() + ";" +
                        selected.getDob() + ";" +
                        selected.getCccd() + ";" +
                        selected.getJob()
        );
        dialog.setTitle("Sửa công dân");
        dialog.setHeaderText("Chỉnh lại thông tin:");
        dialog.setContentText("Họ tên;Quan hệ;Ngày sinh;CCCD;Nghề:");

        dialog.showAndWait().ifPresent(data -> {
            String[] p = data.split(";");
            if (p.length != 5) return;

            try {
                Connection conn = Database.getConnection();
                PreparedStatement st = conn.prepareStatement(
                        "UPDATE citizens SET full_name=?, relation=?, dob=?, cccd=?, job=? WHERE id=?"
                );
                st.setString(1, p[0]);
                st.setString(2, p[1]);
                st.setString(3, p[2]);
                st.setString(4, p[3]);
                st.setString(5, p[4]);
                st.setInt(6, selected.getId());
                st.executeUpdate();

                loadFromDB();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ========= DELETE ==========
    @FXML
    private void deleteCitizen() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lỗi", "Chọn công dân để xóa!");
            return;
        }

        try {
            Connection conn = Database.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM citizens WHERE id=?");
            st.setInt(1, selected.getId());
            st.executeUpdate();

            loadFromDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========= MENU ==========
    @FXML
    private void backToMenu() throws IOException {
        Main.showMenu();
    }

    @FXML
    private void searchCitizen() throws IOException {
        Main.showMenu();
    }

    @FXML
    private void refreshList() throws IOException {
        Main.showMenu();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
