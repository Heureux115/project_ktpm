package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterController {
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
        roleBox.getItems().addAll("CUSTOMER");
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
            lblMessage.setText("Vui lòng nhập đầy đủ");
            return;
        }
        if (!p.equals(p2)) {
            lblMessage.setText("Mật khẩu không khớp");
            return;
        }
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users(username,password,role,fullname,email) VALUES(?,?,?,?,?)");
            ps.setString(1, u);
            ps.setString(2, p);
            ps.setString(3, role);
            ps.setString(4, name);
            ps.setString(5, email);
            ps.executeUpdate();
            ps.close();
            lblMessage.setText("Đăng ký thành công! Quay về login...");
            Thread.sleep(800);
            Main.showLogin();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMessage.setText("Lỗi: " + ex.getMessage());
        }
    }

    @FXML
    public void onBack(ActionEvent e) throws Exception {
        Main.showLogin();
    }
}
