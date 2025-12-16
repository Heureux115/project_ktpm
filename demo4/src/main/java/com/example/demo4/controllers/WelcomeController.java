package com.example.demo4.controllers;

import com.example.demo4.Main;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class WelcomeController {

    @FXML private ImageView box, mario;
    @FXML private Text successText, scrollText;
    @FXML private Pane rootPane;
    @FXML private Button btnSkip;

    private SequentialTransition intro;

    @FXML
    public void initialize() {
        try {
            box.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/demo4/assets/box.jpg")));
            mario.setImage(new Image(getClass().getResourceAsStream(
                    "/com/example/demo4/assets/mario.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== FIX 1: căn Mario đứng ngay dưới box =====
        mario.setLayoutY(box.getLayoutY() + box.getFitHeight());

        playIntro();
    }

    private void playIntro() {
        intro = buildIntroAnimation();
        intro.setOnFinished(e -> goToMenu());
        intro.play();
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

        // ===== MARIO RUN TO BOX (FIX CHUẨN) =====
        TranslateTransition marioRun = new TranslateTransition(Duration.seconds(2), mario);
        marioRun.setToX(box.getLayoutX() - mario.getLayoutX());

        // ===== MARIO JUMP HIT BOX =====
        TranslateTransition marioJump = new TranslateTransition(Duration.millis(400), mario);
        marioJump.setByY(-box.getFitHeight());
        marioJump.setAutoReverse(true);
        marioJump.setCycleCount(2);

        SequentialTransition marioAnim = new SequentialTransition(marioRun, marioJump);

        // ===== SCROLL TEXT =====
        FadeTransition scrollFade = new FadeTransition(Duration.millis(400), scrollText);
        scrollFade.setToValue(1);

        TranslateTransition scrollMove = new TranslateTransition(Duration.seconds(5), scrollText);
        scrollMove.setFromX(0);
        scrollMove.setToX(-rootPane.getPrefWidth() - scrollText.getLayoutX());

        SequentialTransition scrollAnim = new SequentialTransition(
                new PauseTransition(Duration.seconds(1)),
                scrollFade,
                scrollMove
        );

        return new SequentialTransition(
                marioAnim,     // Mario chạy + nhảy
                boxBounce,    // Box rung
                textAppear,   // LOGIN SUCCESS bật
                scrollAnim,   // Welcome chạy
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
