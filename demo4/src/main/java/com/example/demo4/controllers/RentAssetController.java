package com.example.demo4.controllers;

import com.example.demo4.dao.BookingAssetDao;
import com.example.demo4.models.assets;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RentAssetController extends BaseController {

    @FXML private ComboBox<assets> cbAsset;
    @FXML private TextField tfQuantity;
    @FXML private TextArea taConditionOut;

    private int bookingId;

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    @FXML
    public void initialize() {
        try {
            cbAsset.getItems().addAll(
                    com.example.demo4.dao.AssetDao.findAllAvailable()
            );
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không tải được danh sách tài sản!");
        }
    }

    @FXML
    private void handleRent() {

        if (cbAsset.getValue() == null) {
            showWarning("Thiếu thông tin", "Chọn tài sản!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(tfQuantity.getText().trim());
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showWarning("Sai số lượng", "Số lượng phải là số dương!");
            return;
        }

        if (taConditionOut.getText().isBlank()) {
            showWarning("Thiếu thông tin", "Nhập tình trạng khi xuất!");
            return;
        }

        try {
            // 🔥 GỌI DAO CHUẨN (có trừ quantity + check)
            BookingAssetDao.rentAsset(
                    bookingId,
                    cbAsset.getValue().getId(),
                    quantity,
                    taConditionOut.getText().trim()
            );

            showInfo("Thành công", "Thuê tài sản thành công!");
            closeStage();

        } catch (Exception e) {
            showError("Không thể thuê", e.getMessage());
        }
    }

    private void closeStage() {
        cbAsset.getScene().getWindow().hide();
    }
}
