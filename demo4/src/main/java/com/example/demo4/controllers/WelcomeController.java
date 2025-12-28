package com.example.demo4.controllers;

import com.example.demo4.Main;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.Node;

public class WelcomeController {

    @FXML private ImageView box, mario;
    @FXML private Text successText, scrollText;
    @FXML private Pane rootPane;
    @FXML private Button btnSkip;
    @FXML
    private ProgressIndicator loadingSpinner;

    private SequentialTransition intro;
    private Timeline loadingTimeline;
    private void centerHorizontally(Node node) {
        node.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                node.layoutXProperty().bind(
                        newScene.widthProperty()
                                .subtract(node.boundsInParentProperty().get().getWidth())
                                .divide(2)
                );
            }
        });
    }
    private void centerTextToBox(Text text, ImageView box) {
        Runnable align = () -> {
            double boxCenterX =
                    box.getLayoutX() + box.getBoundsInParent().getWidth() / 2;

            double textWidth = text.getBoundsInLocal().getWidth();

            text.setLayoutX(boxCenterX - textWidth / 2);
        };

        // Khi layout xong
        Platform.runLater(align);

        // Nếu text scale / zoom / đổi nội dung → tự căn lại
        text.boundsInLocalProperty().addListener((obs, o, n) -> align.run());
    }

    @FXML
    public void initialize() {
        try {
            box.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/demo4/assets/box.jpg")));
            mario.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/demo4/assets/mario.gif")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        centerHorizontally(box);
        centerTextToBox(successText, box);
        centerTextToBox(scrollText, box);
        // ===== FIX 1: căn Mario đứng ngay dưới box =====
        // Sử dụng Platform.runLater để đảm bảo layout đã load xong mới lấy tọa độ
        Platform.runLater(() -> {
            mario.setLayoutY(box.getLayoutY() + box.getFitHeight());
            playIntro();
        });
    }


    private void playIntro() {
        intro = buildIntroAnimation();
        intro.setOnFinished(e ->{
            stopLoadingDots();
            goToMenu();
        });
        intro.play();
    }
    private void startLoadingDots() {
        if (loadingTimeline != null) return;

        loadingTimeline = new Timeline(new KeyFrame(Duration.millis(400), e -> {
            String currentText = scrollText.getText();
            // Logic đơn giản:
            if (currentText.endsWith("...")) {
                scrollText.setText("Welcome to the system!\nLoading dashboard");
            } else {
                scrollText.setText(currentText + ".");
            }
        }));
        loadingTimeline.setCycleCount(Animation.INDEFINITE); // Chạy vô tận
        loadingTimeline.play();
    }
    private void stopLoadingDots() {
        if (loadingTimeline != null) {
            loadingTimeline.stop();
        }
    }

    private SequentialTransition buildIntroAnimation() {

        // ===== BOX BOUNCE =====
        TranslateTransition boxBounce = new TranslateTransition(Duration.millis(300), box);
        boxBounce.setByY(-30);
        boxBounce.setAutoReverse(true);
        boxBounce.setCycleCount(2);

        // ===== LOGIN SUCCESS TEXT =====
        ScaleTransition textZoom = new ScaleTransition(Duration.millis(400), successText);
        textZoom.setFromX(0);
        textZoom.setFromY(0);
        textZoom.setToX(1);
        textZoom.setToY(1);

        FadeTransition textFade = new FadeTransition(Duration.millis(400), successText);
        textFade.setToValue(1);

        ParallelTransition textAppear = new ParallelTransition(textZoom, textFade);

        // ===== MARIO RUN TO BOX (FIX CHUẨN CENTER) =====
        TranslateTransition marioRun = new TranslateTransition(Duration.seconds(2), mario);

        // 1. Tính toán tâm của Box và Mario
        // Lưu ý: Nếu trong FXML bạn chưa set fitWidth, hãy dùng box.getBoundsInParent().getWidth()
        double boxWidth = box.getFitWidth();
        double marioWidth = mario.getFitWidth();

        // Nếu fitWidth = 0 (do chưa load kịp hoặc không set trong FXML), ta lấy bounds
        if (boxWidth == 0) boxWidth = box.getBoundsInParent().getWidth();
        if (marioWidth == 0) marioWidth = mario.getBoundsInParent().getWidth();

        double boxCenterX = box.getLayoutX() + (boxWidth / 2);
        double marioCenterX = mario.getLayoutX() + (marioWidth / 2);

        // 2. Set khoảng cách di chuyển = Tâm Box - Tâm Mario
        marioRun.setToX(boxCenterX - marioCenterX);

        // ===== MARIO JUMP HIT BOX =====
        TranslateTransition marioJump = new TranslateTransition(Duration.millis(400), mario);
        // Nhảy lên đúng bằng chiều cao Box để cụng đầu vào đáy Box
        marioJump.setByY(-box.getFitHeight());
        marioJump.setAutoReverse(true);
        marioJump.setCycleCount(2);


        SequentialTransition marioAnim = new SequentialTransition(marioRun,marioJump);

        // ===== SCROLL TEXT =====
        FadeTransition showLoadingText = new FadeTransition(Duration.millis(500), scrollText);
        showLoadingText.setToValue(1);

        // Kích hoạt timeline dấu chấm ngay khi chữ bắt đầu hiện
        showLoadingText.setOnFinished(e -> startLoadingDots());

        TranslateTransition runAway = new TranslateTransition(Duration.seconds(2), mario);
        // Chạy ra khỏi màn hình (vượt qua chiều rộng pane + 100px dư)
        // Lưu ý: setToX tính từ vị trí gốc ban đầu của Mario
        runAway.setToX(rootPane.getPrefWidth() + 200);



        return new SequentialTransition(
                marioAnim,     // Mario chạy + nhảy
                boxBounce,    // Box rung
                textAppear,   // LOGIN SUCCESS bật
                showLoadingText,
                runAway,
                new PauseTransition(Duration.seconds(1))
        );
    }

    @FXML
    private void onSkip() {
        if (intro != null) {
            intro.stop();
        }
        goToMenu();
    }

    private void goToMenu() {
        try {
            Main.showMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}