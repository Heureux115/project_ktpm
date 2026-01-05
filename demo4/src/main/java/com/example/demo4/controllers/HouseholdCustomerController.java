package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.Session;
import com.example.demo4.dao.CitizenDao;
import com.example.demo4.dao.HouseholdDao;
import com.example.demo4.models.Citizen;
import com.example.demo4.models.Household;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class HouseholdCustomerController extends BaseController {

    @FXML private TableView<Citizen> table;

    @FXML private TableColumn<Citizen, String> colName;
    @FXML private TableColumn<Citizen, String> colCccd;
    @FXML private TableColumn<Citizen, String> colDob;
    @FXML private TableColumn<Citizen, String> colRelation;
    @FXML private TableColumn<Citizen, String> colJob;

    @FXML
    public void initialize() {

        colName.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFullName())
        );

        colCccd.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCccd())
        );

        colDob.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDob() == null ? "" : c.getValue().getDob().toString()
                )
        );

        colRelation.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getRelation())
        );

        colJob.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getJob())
        );

        loadMyCitizens();
    }

    private void loadMyCitizens() {
        try {
            Integer userId = Session.getCurrentUserId();
            if (userId == null) {
                showWarning("Lỗi", "Bạn chưa đăng nhập!");
                return;
            }

            // 1️⃣ user → citizen
            Citizen me = CitizenDao.findByUserId(userId);
            if (me == null) {
                showWarning("Lỗi", "Tài khoản chưa được gán với công dân!");
                return;
            }

            Integer householdId = me.getHouseholdId();
            if (householdId == null) {
                showWarning("Thông báo", "Bạn chưa thuộc hộ khẩu nào!");
                table.getItems().clear();
                return;
            }

            // 2️⃣ household → citizens
            List<Citizen> citizens =
                    CitizenDao.findByHouseholdId(householdId);

            table.getItems().setAll(citizens);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể tải danh sách nhân khẩu");
        }
    }

    @FXML
    private void backToMenu() throws Exception {
        Main.showMenu();
    }
}