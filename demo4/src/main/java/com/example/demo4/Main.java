package com.example.demo4;

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

    public static String currentRole;
    public static String currentUser;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Quản lý Nhà Văn Hóa");
        showLogin();
        stage.show();
    }

    public static void showWelcome() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo4/welcome.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showMenu() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/menu.fxml"));
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    public static void showAdminHousehold() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/household_admin.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void showCustomerHousehold() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/household_customer.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
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

    public static void showAdmin() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/admin.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
    }

    public static void main(String[]args) {
        launch(args);
    }
}
