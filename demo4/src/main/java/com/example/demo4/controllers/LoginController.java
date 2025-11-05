package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Label lblMessage;

    @FXML
    private void initialize() {
    }

    @FXML
    public void onLogin(ActionEvent e) {
        String u = txtUsername.getText().trim();
        String p = txtPassword.getText().trim();
        if (u.isEmpty() || p.isEmpty()) {
            lblMessage.setText("Nhập đầy đủ username và password");
            return;
        }
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id,role FROM users WHERE username=? AND password=?");
            ps.setString(1, u);
            ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");

                lblMessage.setText("Đăng nhập thành công!");

                // ✅ chuyển sang màn welcome video và truyền role qua
                Main.showWelcome(role);

            } else {
                lblMessage.setText("Sai username hoặc password");
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMessage.setText("Lỗi kết nối DB: " + ex.getMessage());
        }
    }


    @FXML
    public void onShowRegister(ActionEvent e) throws Exception {
        Main.showRegister();
    }
}
