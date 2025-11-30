package com.example.demo4.controllers;

import com.example.demo4.Database;
import com.example.demo4.Main;
import com.example.demo4.dao.CitizenDao;
import com.example.demo4.models.Citizen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CitizenController extends BaseController {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, String> colName;
    @FXML private TableColumn<Citizen, String> colRelation;
    @FXML private TableColumn<Citizen, String> colDob;
    @FXML private TableColumn<Citizen, String> colCCCD;
    @FXML private TableColumn<Citizen, String> colJob;
    @FXML private TextField searchField;

    // Danh sách nguồn cho TableView
    private final ObservableList<Citizen> citizenList = FXCollections.observableArrayList();

    // Hộ khẩu hiện tại (nếu xem nhân khẩu theo từng hộ)
    private String currentHouseholdId;

    public void setCurrentHouseholdId(String id) {
        this.currentHouseholdId = id;
        // Khi được set từ ngoài (vd: từ HouseholdController) thì load lại theo hộ đó
        loadFromDB();
    }

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        colRelation.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRelation()));
        colDob.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDob()));
        colCCCD.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCccd()));
        colJob.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getJob()));

        citizenTable.setItems(citizenList);

        // Nếu controller này được dùng độc lập (không setHouseholdId từ ngoài)
        // thì lúc khởi tạo sẽ load toàn bộ
        loadFromDB();
    }

    /**
     * Load dữ liệu từ DB:
     *  - Nếu currentHouseholdId != null: chỉ lấy nhân khẩu thuộc hộ đó
     *  - Nếu null: lấy tất cả công dân
     */
    private void loadFromDB() {
        citizenList.clear();
        try {
            if (currentHouseholdId == null || currentHouseholdId.isEmpty()) {
                // Lấy tất cả
                citizenList.addAll(CitizenDao.findAll());
            } else {
                // Lấy theo hộ khẩu
                citizenList.addAll(CitizenDao.findByHousehold(currentHouseholdId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể tải danh sách công dân!");
        }
    }

    @FXML
    private void addCitizen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/add_citizen.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Thêm công dân");

            AddCitizenController controller = loader.getController();
            controller.setStage(stage);
            controller.setHouseholdId(currentHouseholdId);
            controller.setOnAddSuccess(this::loadFromDB);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở màn thêm công dân!");
        }
    }

    @FXML
    private void editCitizen() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Lỗi", "Hãy chọn công dân để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/edit_citizen.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sửa công dân");

            EditCitizenController controller = loader.getController();
            controller.setStage(stage);
            controller.setCitizen(selected);
            controller.setOnEditSuccess(this::loadFromDB);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở màn sửa công dân!");
        }
    }

    @FXML
    private void deleteCitizen() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Lỗi", "Chọn công dân để xóa!");
            return;
        }

        if (!showConfirm("Xác nhận", "Bạn có chắc muốn xóa công dân này?")) return;

        try {
            CitizenDao.deleteById(selected.getId());
            loadFromDB();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể xóa công dân!");
        }
    }


    @FXML
    private void backToMenu() throws IOException {
        Main.showMenu();
    }

    @FXML
    private void searchCitizen() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            citizenTable.setItems(citizenList);
            return;
        }

        ObservableList<Citizen> filteredList = FXCollections.observableArrayList();
        for (Citizen c : citizenList) {
            if (c.getFullName().toLowerCase().contains(keyword)
                    || c.getCccd().toLowerCase().contains(keyword)) {
                filteredList.add(c);
            }
        }
        citizenTable.setItems(filteredList);
    }

    @FXML
    private void refreshList() {
        searchField.clear();
        citizenTable.setItems(citizenList);
        loadFromDB();
    }
}
