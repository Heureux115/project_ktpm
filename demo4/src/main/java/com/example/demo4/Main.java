package com.example.demo4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/demo4/assets/icon.jpg")));
        stage.setTitle("Hệ thống quản lý");
        showLogin();
        stage.setOnShown(event -> {
            stage.centerOnScreen();
        });
        stage.show();
    }

    public static void logout() throws IOException {
        Session.logout();
        showLogin();
    }

    public static void showWelcome() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo4/welcome.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void showMenu() throws IOException {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/com/example/demo4/menu.fxml")
        );

        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/menu.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    public static void showAdminHousehold() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/household_admin.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.centerOnScreen();
    }

    public static void showCustomerHousehold() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/household_customer.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.centerOnScreen();
    }

    public static void showLogin() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/example/demo4/login.fxml"));
        Scene scene = new Scene(root, 600, 650);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/login.css").toExternalForm()
        );
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showRegister() throws IOException {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/com/example/demo4/register.fxml")
        );

        Scene scene = new Scene(root, 800, 750);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/register.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    public static void showCustomer() throws IOException {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/com/example/demo4/customer.fxml")
        );

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/customer.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    public static void showAdmin() throws IOException {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/com/example/demo4/admin.fxml")
        );

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/admin.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    public static void showStatistics() throws IOException {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/com/example/demo4/statistics.fxml")
        );

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/statistics.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }


    public static void showTemporaryRecords() throws IOException {
        Parent root = FXMLLoader.load(
                Main.class.getResource("/com/example/demo4/temporary_records.fxml")
        );

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(
                Main.class.getResource("/com/example/demo4/css/temporary_records.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }



    public static void main(String[]args) {
        launch(args);
    }
}