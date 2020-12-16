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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

public class NewGameController {
    @FXML
    private Line line;
    @FXML
    private Text frenzyText, gravityText, standardText;
    @FXML
    private Circle diffBackIconCircle, gravityCircle, frenzyCircle;
    @FXML
    private Label easyLabel, mediumLabel, hardLabel;
    @FXML
    private ImageView diffBackgroundImage, diffBackIcon, easyImage, mediumImage, hardImage, gravityIcon, frenzyIcon;
    @FXML
    private AnchorPane newGameBG;

    @FXML
    public void initialize()    {
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        diffBackgroundImage.setOpacity(diffBackgroundImage.getOpacity()+0.0015);
                        easyLabel.setOpacity(easyLabel.getOpacity()+0.01);
                        easyImage.setOpacity(easyImage.getOpacity()+0.01);
                        mediumLabel.setOpacity(mediumLabel.getOpacity()+0.01);
                        mediumImage.setOpacity(mediumLabel.getOpacity()+0.01);
                        hardLabel.setOpacity(hardLabel.getOpacity()+0.01);
                        hardImage.setOpacity(hardImage.getOpacity()+0.01);
                        diffBackIcon.setOpacity(diffBackIcon.getOpacity()+0.01);
                        diffBackIconCircle.setOpacity(diffBackIconCircle.getOpacity()+0.01);
                        gravityCircle.setOpacity(gravityCircle.getOpacity()+0.01);
                        gravityIcon.setOpacity(gravityIcon.getOpacity()+0.01);
                        frenzyCircle.setOpacity(frenzyCircle.getOpacity()+0.01);
                        frenzyIcon.setOpacity(frenzyIcon.getOpacity()+0.01);
                        gravityText.setOpacity(gravityText.getOpacity()+0.01);
                        frenzyText.setOpacity(frenzyText.getOpacity()+0.01);
                        standardText.setOpacity(standardText.getOpacity()+0.01);
                        line.setOpacity(line.getOpacity()+0.01);
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

    public void setModeToFrenzy() {
        Player p = Main.getCurrentPlayer();
        p.setCurrentState(new GameState());
        p.getCurrentState().setMode(1);
        try {
            p.getCurrentState().newGame(0, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeToGravity() {
        Player p = Main.getCurrentPlayer();
        p.setCurrentState(new GameState());
        p.getCurrentState().setMode(2);
        try {
            p.getCurrentState().newGame(0, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDifficultyToEasy() {
        Player p = Main.getCurrentPlayer();
        p.setCurrentState(new GameState());
        p.getCurrentState().setMode(0);
        try {
            p.getCurrentState().newGame(0, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDifficultyToMedium() {
        Player p = Main.getCurrentPlayer();
        p.setCurrentState(new GameState());
        p.getCurrentState().setMode(0);
        try {
            p.getCurrentState().newGame(1, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDifficultyToHard() {
        Player p = Main.getCurrentPlayer();
        p.setCurrentState(new GameState());
        p.getCurrentState().setMode(0);
        try {
            p.getCurrentState().newGame(2, newGameBG);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
