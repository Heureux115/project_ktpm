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
    @FXML private TableColumn<Household, String> colHead;
    @FXML private TableColumn<Household, String> colAddress;
    @FXML private TableColumn<Household, String> colMembers;

    @FXML
    public void initialize() {
        colHead.setCellValueFactory(data -> {
            Integer cid = data.getValue().getHeadCitizenId();
            return new SimpleStringProperty(cid == null ? "(chưa có chủ hộ)" : "CID: " + cid);
        });

        colAddress.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAddress()));

        colMembers.setCellValueFactory(data -> {
            // Nếu chưa load members từ DB → tạm để trống
            List<?> m = data.getValue().getMembers();
            return new SimpleStringProperty(m == null ? "-" : String.valueOf(m.size()));
        });

        loadMyHouseholds();
    }

    private void loadMyHouseholds() {
        try {
            Integer userId = Session.getCurrentUserId();
            if (userId == null) {
                showWarning("Lỗi", "Bạn chưa đăng nhập!");
                return;
            }

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
