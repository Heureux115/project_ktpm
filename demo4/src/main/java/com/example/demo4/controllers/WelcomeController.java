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
    @FXML private Button btnSkip;   // 👉 thêm nút skip

    // Giữ animation để có thể stop khi skip
    private SequentialTransition intro;

    @FXML
    public void initialize() {
        try {
            box.setImage(new Image(getClass().getResourceAsStream("/com/example/demo4/assets/box.jpg")));
            mario.setImage(new Image(getClass().getResourceAsStream("/com/example/demo4/assets/mario.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        playIntro();
    }

    private SequentialTransition buildIntroAnimation() {

        TranslateTransition boxBounce = new TranslateTransition(Duration.millis(300), box);
        boxBounce.setByY(-30);
        boxBounce.setAutoReverse(true);
        boxBounce.setCycleCount(2);

        ScaleTransition textZoom = new ScaleTransition(Duration.millis(400), successText);
        textZoom.setFromX(0);
        textZoom.setFromY(0);
        textZoom.setToX(1);
        textZoom.setToY(1);

        FadeTransition textFade = new FadeTransition(Duration.millis(400), successText);
        textFade.setToValue(1);

        ParallelTransition textAppear = new ParallelTransition(textZoom, textFade);

        TranslateTransition marioRun = new TranslateTransition(Duration.seconds(2), mario);
        marioRun.setByX(550);

        TranslateTransition marioJump = new TranslateTransition(Duration.millis(400), mario);
        marioJump.setByY(-120);
        marioJump.setAutoReverse(true);
        marioJump.setCycleCount(2);

        SequentialTransition marioAnim = new SequentialTransition(marioRun, marioJump);

        FadeTransition scrollFade = new FadeTransition(Duration.millis(400), scrollText);
        scrollFade.setToValue(1);

        TranslateTransition scrollMove = new TranslateTransition(Duration.seconds(3), scrollText);
        scrollMove.setToX(-900);

        SequentialTransition scrollAnim = new SequentialTransition(
                new PauseTransition(Duration.seconds(1)),
                scrollFade, scrollMove
        );

        return new SequentialTransition(
                boxBounce,
                textAppear,
                marioAnim,
                scrollAnim,
                new PauseTransition(Duration.seconds(1))
        );
    }

    private void playIntro() {
        intro = buildIntroAnimation();
        intro.setOnFinished(e -> goToMenu());
        intro.play();
    }

    private void goToMenu() {
        try {
            // dùng Main.showMenu() cho gọn nếu em đã có
            Main.showMenu();

            // Hoặc nếu muốn giữ như code cũ:
            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
            */

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onSkip() {
        // stop animation nếu đang chạy
        if (intro != null) {
            intro.stop();
        }
        goToMenu();
    }
}
