package com.example.demo4.controllers;

import com.example.demo4.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class AssetsController {

    @FXML private TableView<AssetRow> tblAssets;
    @FXML private TableColumn<AssetRow,String> colName;
    @FXML private TableColumn<AssetRow,String> colType;
    @FXML private TableColumn<AssetRow,String> colStatus;
    @FXML private TableColumn<AssetRow,Void> colActions;
    @FXML private TableColumn<AssetRow,Integer> colQuantity;


    @FXML private TextField txtName;
    @FXML private TextField txtType;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtQuantity;

    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        cbStatus.setItems(FXCollections.observableArrayList(
                "Tốt",
                "Hư hỏng",
                "Đang sử dụng"
        ));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        addButtonToTable();

        loadAssets();
    }

    private void loadAssets() {
        ObservableList<AssetRow> list = FXCollections.observableArrayList();
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, type, quantity, status FROM assets")) {

            while (rs.next()) {
                list.add(new AssetRow(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }
            tblAssets.setItems(list);
        } catch (SQLException e) {
            lblMessage.setText("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }


    @FXML
    public void onAddAsset() {
        String name = txtName.getText();
        String type = txtType.getText();
        String status = cbStatus.getValue();
        String quantityStr = txtQuantity.getText();

        if(name.isEmpty() || type.isEmpty() || status == null || quantityStr.isEmpty()) {
            lblMessage.setText("Nhập đầy đủ thông tin!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            lblMessage.setText("Số lượng phải là số nguyên!");
            return;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO assets(name,type,quantity,status) VALUES(?,?,?,?)"
             )) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setInt(3, quantity);
            ps.setString(4, status);
            ps.executeUpdate();

            lblMessage.setText("Thêm tài sản thành công!");
            txtName.clear();
            txtType.clear();
            txtQuantity.clear();
            cbStatus.setValue(null);

            loadAssets();
        } catch (SQLException e) {
            lblMessage.setText("Lỗi: " + e.getMessage());
        }
    }


    private void addButtonToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                btnEdit.setOnAction(event -> {
                    AssetRow asset = getTableView().getItems().get(getIndex());
                    editAsset(asset);
                });
                btnDelete.setOnAction(event -> {
                    AssetRow asset = getTableView().getItems().get(getIndex());
                    deleteAsset(asset);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void editAsset(AssetRow asset) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Sửa tài sản");

        TextField tfName = new TextField(asset.getName());
        TextField tfType = new TextField(asset.getType());
        TextField tfQuantity = new TextField(String.valueOf(asset.getQuantity()));
        ComboBox<String> cbStatusEdit = new ComboBox<>();
        cbStatusEdit.getItems().addAll("Tốt", "Hư hỏng", "Đang sử dụng");
        cbStatusEdit.setValue(asset.getStatus());

        VBox vbox = new VBox(5, tfName, tfType, tfQuantity, cbStatusEdit);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Kiểm tra nút OK
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
                    lblMessage.setText("Số lượng phải là số nguyên!");
                }
            }
        });
    }


    private void deleteAsset(AssetRow asset) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM assets WHERE id=?")) {
            ps.setInt(1, asset.getId());
            ps.executeUpdate();
            loadAssets();
        } catch (SQLException e) {
            lblMessage.setText("Lỗi: " + e.getMessage());
        }
    }

    private void updateAssetInDB(AssetRow asset) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE assets SET name=?, type=?, quantity=?, status=? WHERE id=?"
             )) {
            ps.setString(1, asset.getName());
            ps.setString(2, asset.getType());
            ps.setInt(3, asset.getQuantity());
            ps.setString(4, asset.getStatus());
            ps.setInt(5, asset.getId());
            ps.executeUpdate();
            loadAssets();
        } catch (SQLException e) {
            lblMessage.setText("Lỗi: " + e.getMessage());
        }
    }

    public static class AssetRow {
        private final javafx.beans.property.SimpleIntegerProperty id;
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty type;
        private final javafx.beans.property.SimpleIntegerProperty quantity;
        private final javafx.beans.property.SimpleStringProperty status;

        public AssetRow(int id, String name, String type, int quantity, String status) {
            this.id = new javafx.beans.property.SimpleIntegerProperty(id);
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.type = new javafx.beans.property.SimpleStringProperty(type);
            this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
            this.status = new javafx.beans.property.SimpleStringProperty(status);
        }

        public int getId() { return id.get(); }

        public String getName() { return name.get(); }
        public void setName(String name) { this.name.set(name); }
        public javafx.beans.property.StringProperty nameProperty() { return name; }

        public String getType() { return type.get(); }
        public void setType(String type) { this.type.set(type); }
        public javafx.beans.property.StringProperty typeProperty() { return type; }

        public int getQuantity() { return quantity.get(); }
        public void setQuantity(int quantity) { this.quantity.set(quantity); }
        public javafx.beans.property.IntegerProperty quantityProperty() { return quantity; }

        public String getStatus() { return status.get(); }
        public void setStatus(String status) { this.status.set(status); }
        public javafx.beans.property.StringProperty statusProperty() { return status; }
    }

}
