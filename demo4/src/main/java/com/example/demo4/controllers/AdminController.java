package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.dao.UserDao;
import com.example.demo4.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class AdminController extends BaseController {

    @FXML private TableView<UserRow> tblUsers;
    @FXML private TableColumn<UserRow,String> colUsername;
    @FXML private TableColumn<UserRow,String> colRole;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(c -> c.getValue().usernameProperty());
        colRole.setCellValueFactory(c -> c.getValue().roleProperty());
        loadUsers();
    }

    private void loadUsers() {
        ObservableList<UserRow> list = FXCollections.observableArrayList();
        try {
            List<User> users = UserDao.findAll();
            for (User u : users) {
                list.add(new UserRow(u.getId(), u.getUsername(), u.getRole()));
            }
            tblUsers.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể tải danh sách user: " + e.getMessage());
        }
    }

    @FXML
    public void onDelete(ActionEvent e) {
        if (!requireAdmin()) return;

        UserRow sel = tblUsers.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showWarning("Thiếu chọn", "Chọn user để xóa");
            return;
        }
        try {
            UserDao.deleteById(sel.getId());
            showInfo("Thành công", "Xóa user thành công");
            loadUsers();
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Lỗi", "Lỗi: " + ex.getMessage());
        }
    }

    @FXML
    public void onLogout(ActionEvent e) throws Exception {
        Main.logout();
    }

    public static class UserRow {
        private final javafx.beans.property.SimpleIntegerProperty id;
        private final javafx.beans.property.SimpleStringProperty username;
        private final javafx.beans.property.SimpleStringProperty role;
        public UserRow(int id, String username, String role) {
            this.id = new javafx.beans.property.SimpleIntegerProperty(id);
            this.username = new javafx.beans.property.SimpleStringProperty(username);
            this.role = new javafx.beans.property.SimpleStringProperty(role);
        }
        public int getId(){return id.get();}
        public javafx.beans.property.StringProperty usernameProperty(){return username;}
        public javafx.beans.property.StringProperty roleProperty(){return role;}
    }

    @FXML
    public void backToMenu() throws Exception {
        Main.showMenu();
    }

    @FXML
    public void onViewPastEvents() {
        if (!requireAdmin()) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/past_events.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Sự kiện đã quá 30 ngày");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Lỗi mở danh sách sự kiện cũ: " + e.getMessage());
        }
    }

    @FXML
    public void onApproveEvents() {
        if (!requireAdmin()) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/event_approval.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Duyệt sự kiện");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Lỗi mở màn hình duyệt sự kiện: " + e.getMessage());
        }
    }

    @FXML
    public void onManageAssets() {
        if (!requireAdmin()) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/assets.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Quản lý cơ sở vật chất");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Lỗi mở quản lý cơ sở vật chất: " + e.getMessage());
        }
    }
}
