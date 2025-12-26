package com.example.demo4.controllers;

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

public class CitizenController extends BaseController {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, String> colName;
    @FXML private TableColumn<Citizen, String> colRelation;
    @FXML private TableColumn<Citizen, String> colDob;
    @FXML private TableColumn<Citizen, String> colCCCD;
    @FXML private TableColumn<Citizen, String> colJob;
    @FXML private TextField searchField;

    // Lưu ID hộ khẩu hiện tại → kiểu Integer đúng theo Database
    private Integer currentHouseholdId = null;

    private final ObservableList<Citizen> citizenList = FXCollections.observableArrayList();

    public void setCurrentHouseholdId(int householdId) {
        this.currentHouseholdId = householdId;
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

        loadFromDB(); // nếu không set household trước → load all
    }

    @FXML
    private void addCitizen() {
        if (currentHouseholdId == null) {
            showWarning("Lỗi", "Cần mở từ màn hình hộ khẩu để thêm nhân khẩu!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo4/add_citizen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Thêm công dân");

            AddCitizenController controller = loader.getController();
            controller.setStage(stage);
            controller.setHouseholdId(currentHouseholdId); // kiểu int chuẩn
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
                    getClass().getResource("/com/example/demo4/edit_citizen.fxml"));
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
    private void backToMenu() throws Exception {
        Main.showMenu();
    }

    private void loadFromDB() {
        citizenList.clear();
        try {
            citizenList.addAll(
                    currentHouseholdId == null
                            ? CitizenDao.findAll()
                            : CitizenDao.findByHousehold(currentHouseholdId)
            );
        } catch (Exception e) {
            showError("Lỗi", "Không tải được công dân!");
        }
    }

    @FXML
    private void searchCitizen() {
        String key = searchField.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            citizenTable.setItems(citizenList);
            return;
        }

        ObservableList<Citizen> filtered = FXCollections.observableArrayList();
        for (Citizen c : citizenList) {
            if (c.getFullName().toLowerCase().contains(key)
                    || c.getCccd().toLowerCase().contains(key)) {
                filtered.add(c);
            }
        }
        citizenTable.setItems(filtered);
    }

    @FXML
    private void refreshList() {
        searchField.clear();
        citizenTable.setItems(citizenList);
        loadFromDB();
    }
}