package com.example.demo4.controllers;

import com.example.demo4.Main;
import com.example.demo4.dao.HouseholdDao;
import com.example.demo4.models.Household;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HouseholdAdminController extends BaseController {

    @FXML private TableView<Household> table;
    @FXML private TableColumn<Household, String>  colId;
    @FXML private TableColumn<Household, String>  colOwner;
    @FXML private TableColumn<Household, String>  colAddress;
    @FXML private TableColumn<Household, Integer> colMembers;
    @FXML private TableColumn<Household, String>  colCreated;

    @FXML
    public void initialize() {
        // Thiết lập các cột TableView
        colId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHouseholdId()));
        colOwner.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHeadName()));
        colAddress.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAddress()));

        // Chưa có cột members/created trong DB → tạm hard-code
        colMembers.setCellValueFactory(data ->
                new SimpleIntegerProperty(0).asObject());
        colCreated.setCellValueFactory(data ->
                new SimpleStringProperty(""));

        loadHouseholds();
    }

    private void loadHouseholds() {
        try {
            table.getItems().setAll(HouseholdDao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể tải danh sách hộ khẩu!");
        }
    }

    @FXML
    private void backToMenu() throws Exception {
        Main.showMenu();
    }

    @FXML
    private void addHousehold() {
        if (!requireAdmin()) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/add_household.fxml"));
            Parent root = loader.load();

            AddHouseholdController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.setOnAddSuccess(this::loadHouseholds); // reload TableView sau khi thêm

            stage.setTitle("Thêm Hộ khẩu");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form thêm hộ khẩu!\n" + e.getMessage());
        }
    }

    @FXML
    private void editHousehold() {
        if (!requireAdmin()) return;

        Household selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Lỗi", "Hãy chọn một hộ khẩu để sửa!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/edit_household.fxml"));
            Parent root = loader.load();

            EditHouseholdController controller = loader.getController();
            controller.setHousehold(selected);
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.setOnEditSuccess(this::loadHouseholds);

            stage.setTitle("Sửa Hộ khẩu");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form sửa hộ khẩu!\n" + e.getMessage());
        }
    }

    @FXML
    private void deleteHousehold() {
        if (!requireAdmin()) return;

        Household selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Lỗi", "Hãy chọn một hộ khẩu để xóa!");
            return;
        }

        boolean ok = showConfirm(
                "Xác nhận",
                "Bạn có chắc chắn muốn xóa hộ " + selected.getHouseholdId() + "?"
        );
        if (!ok) return;

        try {
            // dùng DAO cho sạch
            HouseholdDao.deleteById(selected.getHouseholdId());

            showInfo("Thành công", "Đã xóa hộ khẩu!");
            loadHouseholds();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể xóa hộ khẩu!\n" + e.getMessage());
        }
    }
}
