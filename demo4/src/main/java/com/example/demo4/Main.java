package com.example.demo4;

import com.example.demo4.controllers.WelcomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Quản lý Nhà Văn Hóa");
        showLogin();
        stage.show();
    }

    public static void showWelcome(String role) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo4/welcome.fxml"));
        Parent root = loader.load();

        WelcomeController controller = loader.getController();
        controller.setRole(role); // ✅ truyền role

        Stage stage = new Stage();
        stage.setTitle("Welcome!");
        stage.setScene(new Scene(root));
        stage.show();

        // Đóng cửa sổ login
        Stage curr = (Stage) Main.getPrimaryStage().getScene().getWindow();
        curr.close();
    }


    public static void showLogin() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("login.fxml"));
        primaryStage.setScene(new Scene(root, 800, 550));
    }

    public static void showRegister() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("register.fxml"));
        primaryStage.setScene(new Scene(root, 800, 550));
    }

    public static void showCustomer() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("customer.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void showStaff() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("staff.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void showAdmin() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("admin.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void main(String[]args) {
        launch(args);
    }
}
