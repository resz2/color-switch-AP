package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

public class NewGameController {

    @FXML
    private Circle diffBackIconCircle;
    @FXML
    private Label easyLabel, mediumLabel, hardLabel;
    @FXML
    private ImageView diffBackgroundImage, diffBackIcon, chooseImage, easyImage, mediumImage, hardImage;
    @FXML
    private AnchorPane newGameBG;

    @FXML
    public void initialize()    {
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        diffBackgroundImage.setOpacity(diffBackgroundImage.getOpacity()+0.004);
                        easyLabel.setOpacity(easyLabel.getOpacity()+0.01);
                        easyLabel.setLayoutX(easyLabel.getLayoutX()+1.25);
                        easyImage.setOpacity(easyImage.getOpacity()+0.01);
                        easyImage.setLayoutX(easyImage.getLayoutX()+1.77);
                        mediumLabel.setLayoutX(mediumLabel.getLayoutX()-1.25);
                        mediumLabel.setOpacity(mediumLabel.getOpacity()+0.01);
                        mediumImage.setOpacity(mediumLabel.getOpacity()+0.01);
                        mediumImage.setLayoutX(mediumImage.getLayoutX()-1.25);
                        hardLabel.setLayoutX(hardLabel.getLayoutX()+1.25);
                        hardLabel.setOpacity(hardLabel.getOpacity()+0.01);
                        hardImage.setOpacity(hardImage.getOpacity()+0.01);
                        hardImage.setLayoutX(hardImage.getLayoutX()+1.77);
                        diffBackIcon.setOpacity(diffBackIcon.getOpacity()+0.01);
                        diffBackIconCircle.setOpacity(diffBackIconCircle.getOpacity()+0.01);
                        chooseImage.setLayoutY(chooseImage.getLayoutY()+0.7);
                        chooseImage.setOpacity(chooseImage.getOpacity()+0.01);
                    }
                }));
        enterTimeline.setCycleCount(100);
        enterTimeline.play();
    }

    public void goBack() {
        AnchorPane pane=null;
        try {
            pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
        newGameBG.getChildren().setAll(pane);
    }

    public void setDifficultyToEasy() {
        Main.getCurrentPlayer().setCurrentState(new GameState());
        try {
            Main.getCurrentPlayer().getCurrentState().newGame(0, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDifficultyToMedium() {
        Main.getCurrentPlayer().setCurrentState(new GameState());
        try {
            Main.getCurrentPlayer().getCurrentState().newGame(1, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDifficultyToHard() {
        Main.getCurrentPlayer().setCurrentState(new GameState());
        try {
            Main.getCurrentPlayer().getCurrentState().newGame(2, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
