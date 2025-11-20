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
        controller.setRole(role);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showMenu() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/menu.fxml"));
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    public static void showCitizenManagement() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/citizen.fxml"));
        primaryStage.setScene(new Scene(root));
    }

    public static void showHouseholdList() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/householdList.fxml"));
        primaryStage.setScene(new Scene(root, 900, 600)); // chỉnh kích thước hợp lý
    }

    public static void showLogin() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/login.fxml"));
        primaryStage.setScene(new Scene(root, 800, 550));
    }

    public static void showRegister() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/register.fxml"));
        primaryStage.setScene(new Scene(root, 800, 550));
    }

    public static void showCustomer() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/customer.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void showStaff() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/staff.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void showAdmin() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/admin.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void main(String[]args) {
        launch(args);
    }
}
