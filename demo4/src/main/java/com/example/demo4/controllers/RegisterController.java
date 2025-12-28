package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.dao.UserDao;
import com.example.demo4.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;

public class RegisterController extends BaseController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPassword2;
    @FXML private TextField txtFullname;
    @FXML private TextField txtEmail;
    @FXML private ChoiceBox<String> roleBox;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        roleBox.getItems().add("CUSTOMER");
        roleBox.setValue("CUSTOMER");
    }

    @FXML
    public void onRegister(ActionEvent e) {
        String u = txtUsername.getText().trim();
        String p = txtPassword.getText();
        String p2 = txtPassword2.getText();
        String name = txtFullname.getText().trim();
        String email = txtEmail.getText().trim();
        String role = roleBox.getValue();

        if (u.isEmpty() || p.isEmpty() || p2.isEmpty()) {
            showWarning("Thiếu thông tin", "Vui lòng nhập đầy đủ username và password!");
            return;
        }
        if (!p.equals(p2)) {
            showWarning("Sai mật khẩu", "Mật khẩu nhập lại không khớp!");
            return;
        }

        try {
            // check username
            if (UserDao.findByUsername(u) != null) {
                showWarning("Trùng username", "Tên đăng nhập đã tồn tại!");
                return;
            }

            // 1. Tạo Citizen
            Citizen citizen = new Citizen(
                    0,
                    name,
                    "Chủ hộ",
                    "",
                    "",
                    "",
                    null,   // householdId
                    null    // userId (chưa có)
            );

            int citizenId = CitizenDao.insert(citizen);

            // 2. Tạo User
            User user = new User(u, p, role, name, email);
            int userId = UserDao.insert(user);

            // 3. Gán user vào citizen
            CitizenDao.assignUser(citizenId, userId);

            showInfo("Thành công", "Đăng ký thành công!");
            Main.showLogin();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Lỗi", "Không thể đăng ký tài khoản: " + ex.getMessage());
        }
    }

    @FXML
    public void onBack(ActionEvent e) throws Exception {
        Main.showLogin();
    }
}
