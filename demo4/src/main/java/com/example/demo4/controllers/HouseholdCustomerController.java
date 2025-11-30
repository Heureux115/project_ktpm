package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.Session;
import com.example.demo4.dao.HouseholdDao;
import com.example.demo4.models.Household;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class HouseholdCustomerController extends BaseController {

    @FXML private TableView<Household> table;
    @FXML private TableColumn<Household, String> colId;
    @FXML private TableColumn<Household, String> colOwner;
    @FXML private TableColumn<Household, String> colAddress;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHouseholdId()));
        colOwner.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHeadName()));
        colAddress.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAddress()));

        loadMyHouseholds();
    }

    private void loadMyHouseholds() {
        try {
            Integer userId = Session.getCurrentUserId();
            if (userId == null) {
                showWarning("Lỗi", "Chưa đăng nhập!");
                return;
            }

            // Gọi DAO dạng static
            List<Household> list = HouseholdDao.findByOwner(userId);
            table.getItems().setAll(list);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể tải hộ khẩu: " + e.getMessage());
        }
    }

    @FXML
    private void backToMenu() throws Exception {
        Main.showMenu();
    }
}
