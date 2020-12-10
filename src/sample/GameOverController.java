package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class GameOverController {
    @FXML
    private AnchorPane gameOverBG;

    public void exitToMainMenu() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        gameOverBG.getChildren().setAll(pane);
    }
    @FXML
    public void initialize()    {

//        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent t) {
//                        background.setOpacity(background.getOpacity()+0.0015);
//                        highScoreBanner.setOpacity(highScoreBanner.getOpacity()+0.01);
//                        highScoreBanner.setLayoutY(highScoreBanner.getLayoutY()+1);
//                        highScoreHeading.setOpacity(highScoreHeading.getOpacity()+0.01);
//                        highScoreHeading.setLayoutY(highScoreHeading.getLayoutY()+1);
//                        highScoreList.setOpacity(highScoreList.getOpacity()+0.01);
//                        difficultyImage.setOpacity(difficultyImage.getOpacity()+0.01);
//                        starImage.setOpacity(starImage.getOpacity()+0.01);
//                        backIcon.setOpacity(backIcon.getOpacity()+0.01);
//                        backIcon.setLayoutX(backIcon.getLayoutX()-2);
//                        backCircle.setOpacity(backIcon.getOpacity()+0.01);
//                        backCircle.setLayoutX(backCircle.getLayoutX()-2);
//                        backIcon.setRotate(backIcon.getRotate()+3.6);
//                    }
//                }));
//        enterTimeline.setCycleCount(100);
//        enterTimeline.play();

    }
    public void restartGame() {
    }

    public void continueGame() {
    }
}
