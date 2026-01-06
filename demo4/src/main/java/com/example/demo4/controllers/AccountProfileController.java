package com.example.demo4.controllers;

import com.example.demo4.Session;

import com.example.demo4.dao.UserDao;

import com.example.demo4.dao.CitizenDao;

import com.example.demo4.models.Citizen;

import com.example.demo4.models.User;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;



import java.sql.SQLException;



public class AccountProfileController extends BaseController {



    @FXML private TextField txtUsername;

    @FXML private PasswordField txtPassword;

    @FXML private TextField txtCccd;

    @FXML private Label lblCitizenFullname;

    @FXML private Label lblCitizenCccd;

    @FXML private Label lblMessage;



    private User currentUser;

    private Citizen currentCitizen;

    @FXML

    public void initialize() {

        try {

            currentUser = UserDao.findById(Session.getCurrentUserId());



            txtUsername.setText(currentUser.getUsername());



            currentCitizen = CitizenDao.findByUserId(currentUser.getId());

            if (currentCitizen != null) {

                lblCitizenFullname.setText("Họ tên: " + currentCitizen.getFullName());

                if(currentCitizen.getCccd() != null) {

                    lblCitizenCccd.setText("CCCD: " + currentCitizen.getCccd());

                    txtCccd.setVisible(false);

                    txtCccd.setManaged(false);

                }else{

                    lblCitizenCccd.setText("Chưa cập nhật số CCCD");

                    txtCccd.setVisible(true);

                    txtCccd.setManaged(true);

                }

            } else {

                lblCitizenFullname.setText("⚠ Chưa gán công dân cho tài khoản!");

            }



        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    @FXML

    private void updateAccount() throws SQLException {

        String newUsername = txtUsername.getText().trim();

        String newPassword = txtPassword.getText();

        boolean changed = false;



        try {

            if (!newUsername.equals(currentUser.getUsername())) {

                if (UserDao.findByUsername(newUsername) != null) {

                    showWarning("Trùng username",

                            "Tên đăng nhập đã tồn tại!");

                    return;

                }

                currentUser.setUsername(newUsername);

                changed = true;

            }



            if (!newPassword.isBlank()) {

                currentUser.setPassword(newPassword);

                changed = true;

            }



            if (newUsername.isBlank()) {

                showWarning("Lỗi", "Username không được để trống!");

                return;

            }



            if(txtCccd.isVisible()) {

                String newCccd = txtCccd.getText().trim();

                if(newCccd.isEmpty()) {

                    showWarning("Lỗi", "Vui lòng nhập CCCD để định danh cư dân ");

                    return;

                }

                if (!newCccd.matches("\\d{12}")) { 

                    showWarning("Lỗi", "Số CCCD không hợp lệ (phải là 12 chữ số)!");

                    return;

                }

                if (currentCitizen != null) {



                    currentCitizen.setCccd(newCccd);

                    CitizenDao.update(currentCitizen); 

                    changed = true;

                }

                else {





                    Citizen citizenToLink = CitizenDao.findByCccd(newCccd);



                    if (citizenToLink != null) {

                        CitizenDao.assignUser(citizenToLink.getId(), currentUser.getId());

                        currentCitizen = citizenToLink; 

                        changed = true;

                    } else {

                        showWarning("Lỗi", "Không tìm thấy công dân nào có số CCCD này trong hệ thống!");

                        return; 

                    }

                }

            }





            if (!changed) {

                showWarning("Thông báo", "Bạn chưa thay đổi thông tin nào!");

                return;

            }



            UserDao.update(currentUser);

            showInfo("Thành công", "Cập nhật tài khoản thành công!");

            initialize();



        } catch (Exception e) {

            e.printStackTrace();

            lblMessage.setText("Lỗi cập nhật tài khoản!");

        }

    }

}