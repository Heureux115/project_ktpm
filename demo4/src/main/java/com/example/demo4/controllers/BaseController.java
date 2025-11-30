package com.example.demo4.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public abstract class BaseController {

    protected void showInfo(String title, String msg) {
        showAlert(title, msg, Alert.AlertType.INFORMATION);
    }

    protected void showWarning(String title, String msg) {
        showAlert(title, msg, Alert.AlertType.WARNING);
    }

    protected void showError(String title, String msg) {
        showAlert(title, msg, Alert.AlertType.ERROR);
    }

    protected void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Hộp thoại xác nhận, trả về true nếu user chọn OK.
     */
    protected boolean showConfirm(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
