package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.dao.UserDao;
import com.example.demo4.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController extends BaseController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPassword2;
    @FXML private TextField txtFullname;
    @FXML private TextField txtEmail;
    @FXML private Button btnRegister;
    @FXML private Label lblMessage;
    @FXML private ChoiceBox<String> roleBox;

    @FXML
    public void initialize() {
        // Tạm thời chỉ cho đăng ký CUSTOMER
        roleBox.getItems().addAll("CUSTOMER");
        roleBox.setValue("CUSTOMER");
    }

    @FXML
    public void onRegister(ActionEvent e) {
        String u     = txtUsername.getText().trim();
        String p     = txtPassword.getText();
        String p2    = txtPassword2.getText();
        String name  = txtFullname.getText().trim();
        String email = txtEmail.getText().trim();
        String role  = roleBox.getValue();

        if (u.isEmpty() || p.isEmpty() || p2.isEmpty()) {
            lblMessage.setText("Vui lòng nhập đầy đủ");
            showWarning("Thiếu thông tin", "Vui lòng nhập đầy đủ username và password!");
            return;
        }
        if (!p.equals(p2)) {
            lblMessage.setText("Mật khẩu không khớp");
            showWarning("Sai mật khẩu", "Mật khẩu nhập lại không khớp!");
            return;
        }

        try {
            // 1. Kiểm tra username đã tồn tại chưa
            if (UserDao.findByUsername(u) != null) {
                lblMessage.setText("Username đã tồn tại, hãy chọn tên khác");
                showWarning("Trùng username", "Tên đăng nhập này đã có người dùng, vui lòng chọn tên khác!");
                return;
            }

            // 2. Tạo User và lưu qua DAO
            // TODO: về lâu dài nên hash password trước khi lưu
            User user = new User(u, p, role, name, email);
            UserDao.insert(user);

            lblMessage.setText("Đăng ký thành công! Quay về login...");
            showInfo("Thành công", "Đăng ký thành công! Nhấn OK để quay lại màn đăng nhập.");
            Main.showLogin();

        } catch (Exception ex) {
            ex.printStackTrace();
            lblMessage.setText("Lỗi: " + ex.getMessage());
            showError("Lỗi", "Không thể đăng ký tài khoản: " + ex.getMessage());
        }
    }

    @FXML
    public void onBack(ActionEvent e) throws Exception {
        Main.showLogin();
    }
}
