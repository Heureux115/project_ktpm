package com.example.demo4.controllers;

import com.example.demo4.dao.AssetDao;
import com.example.demo4.models.assets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AssetsController extends BaseController {

    @FXML private TableView<assets> tblAssets;
    @FXML private TableColumn<assets,String> colName;
    @FXML private TableColumn<assets,String> colType;
    @FXML private TableColumn<assets,String> colStatus;
    @FXML private TableColumn<assets,Void>   colActions;
    @FXML private TableColumn<assets,Integer> colQuantity;

    @FXML private TextField txtName;
    @FXML private TextField txtType;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtQuantity;

    @FXML private Label lblMessage;

    private final ObservableList<assets> assetList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind cột với property trong model `assets`
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        cbStatus.setItems(FXCollections.observableArrayList(
                "Tốt",
                "Hư hỏng",
                "Đang sử dụng"
        ));

        tblAssets.setItems(assetList);

        addButtonToTable();
        loadAssets();
    }

    private void loadAssets() {
        assetList.clear();
        try {
            assetList.addAll(AssetDao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    public void onAddAsset() {
        if (!requireAdmin()) return;

        String name       = txtName.getText().trim();
        String type       = txtType.getText().trim();
        String status     = cbStatus.getValue();
        String quantityStr= txtQuantity.getText().trim();

        if (name.isEmpty() || type.isEmpty() || status == null || quantityStr.isEmpty()) {
            showWarning("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin tài sản!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            showWarning("Lỗi", "Số lượng phải là số nguyên!");
            return;
        }

        try {
            assets a = new assets(0, name, type, quantity, status); // id=0, DB tự tăng
            AssetDao.insert(a);

            lblMessage.setText("Thêm tài sản thành công!");
            txtName.clear();
            txtType.clear();
            txtQuantity.clear();
            cbStatus.setValue(null);

            loadAssets();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể thêm tài sản: " + e.getMessage());
        }
    }

    private void addButtonToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit   = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");
            private final HBox pane        = new HBox(5, btnEdit, btnDelete);

            {
                btnEdit.setOnAction(event -> {
                    assets asset = getTableView().getItems().get(getIndex());
                    editAsset(asset);
                });
                btnDelete.setOnAction(event -> {
                    assets asset = getTableView().getItems().get(getIndex());
                    deleteAsset(asset);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void editAsset(assets asset) {
        if (!requireAdmin()) return;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Sửa tài sản");

        TextField tfName      = new TextField(asset.getName());
        TextField tfType      = new TextField(asset.getType());
        TextField tfQuantity  = new TextField(String.valueOf(asset.getQuantity()));
        ComboBox<String> cbStatusEdit = new ComboBox<>();
        cbStatusEdit.getItems().addAll("Tốt", "Hư hỏng", "Đang sử dụng");
        cbStatusEdit.setValue(asset.getStatus());

        VBox vbox = new VBox(5, tfName, tfType, tfQuantity, cbStatusEdit);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    int quantity = Integer.parseInt(tfQuantity.getText());

                    asset.setName(tfName.getText());
                    asset.setType(tfType.getText());
                    asset.setQuantity(quantity);
                    asset.setStatus(cbStatusEdit.getValue());

                    updateAssetInDB(asset);
                } catch (NumberFormatException e) {
                    showWarning("Lỗi", "Số lượng phải là số nguyên!");
                }
            }
        });
    }

    private void deleteAsset(assets asset) {
        if (!requireAdmin()) return;

        if (!showConfirm("Xác nhận", "Bạn có chắc muốn xóa tài sản này?")) {
            return;
        }

        try {
            AssetDao.deleteById(asset.getId());
            loadAssets();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", e.getMessage());
        }
    }

    private void updateAssetInDB(assets asset) {
        try {
            AssetDao.update(asset);
            loadAssets();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", e.getMessage());
        }
    }
}
