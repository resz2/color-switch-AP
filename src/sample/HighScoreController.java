package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

public class HighScoreController {
    @FXML
    private ListView<String> highScoreList;
    @FXML
    private AnchorPane highScoreBG;
    @FXML
    private Circle backCircle;
    @FXML
    private ImageView background,backIcon,highScoreBanner,highScoreHeading,starImage,difficultyImage;


    @FXML
    public void initialize()    {
        // loads and displays the high scores
//        highScoreList.getItems().add("    10                        easy");
//        highScoreList.getItems().add("    9                         medium");
//        highScoreList.getItems().add("    8                         easy");
//        highScoreList.getItems().add("    7                         easy");
//        highScoreList.getItems().add("    6                         medium");
//        highScoreList.getItems().add("    5                         medium");
//        highScoreList.getItems().add("    4                         hard");
//        highScoreList.getItems().add("    3                         hard");
//        highScoreList.getItems().add("    2                         hard");

        if(Main.getCurrentPlayer().getHighScores().isEmpty())   {
            System.out.println("empty high");
        }
        else    {
            for(int[] pair: Main.getCurrentPlayer().getHighScores())
                    highScoreList.getItems().add("    " + pair[1] + "                         " + diffPrinter(pair[2]));
        }

        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        background.setOpacity(background.getOpacity()+0.0015);
                        highScoreBanner.setOpacity(highScoreBanner.getOpacity()+0.01);
                        highScoreBanner.setLayoutY(highScoreBanner.getLayoutY()+1);
                        highScoreHeading.setOpacity(highScoreHeading.getOpacity()+0.01);
                        highScoreHeading.setLayoutY(highScoreHeading.getLayoutY()+1);
                        highScoreList.setOpacity(highScoreList.getOpacity()+0.01);
                        difficultyImage.setOpacity(difficultyImage.getOpacity()+0.01);
                        starImage.setOpacity(starImage.getOpacity()+0.01);
                        backIcon.setOpacity(backIcon.getOpacity()+0.01);
                        backIcon.setLayoutX(backIcon.getLayoutX()-2);
                        backCircle.setOpacity(backIcon.getOpacity()+0.01);
                        backCircle.setLayoutX(backCircle.getLayoutX()-2);
                        backIcon.setRotate(backIcon.getRotate()+3.6);
                    }
                }));
        enterTimeline.setCycleCount(100);
        enterTimeline.play();
    }

    private String diffPrinter(int diff) {
        String s = "-";
        switch (diff)   {
            case 0: s = "easy";
                break;
            case 1: s = "medium";
                break;
            case 2: s = "hard";
                break;
        }
        return s;
    }

    public void goBack() throws Exception {
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        background.setOpacity(background.getOpacity()-0.0015);
                        highScoreBanner.setOpacity(highScoreBanner.getOpacity()-0.01);
                        highScoreBanner.setLayoutY(highScoreBanner.getLayoutY()-1);
                        highScoreHeading.setOpacity(highScoreHeading.getOpacity()-0.01);
                        highScoreHeading.setLayoutY(highScoreHeading.getLayoutY()-1);
                        highScoreList.setOpacity(highScoreList.getOpacity()-0.01);
                        difficultyImage.setOpacity(difficultyImage.getOpacity()-0.01);
                        starImage.setOpacity(starImage.getOpacity()-0.01);
                        backIcon.setOpacity(backIcon.getOpacity()-0.01);
                        backIcon.setLayoutX(backIcon.getLayoutX()+2);
                        backCircle.setOpacity(backIcon.getOpacity()-0.01);
                        backCircle.setLayoutX(backCircle.getLayoutX()+2);
                        backIcon.setRotate(backIcon.getRotate()-3.6);

                    }
                }));
        enterTimeline.setCycleCount(100);
        enterTimeline.play();
        enterTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane pane = null;
                try {
                    pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                highScoreBG.getChildren().setAll(pane);
            }
        });

    }
}