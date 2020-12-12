package sample;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    Scene scene;
    private static Database db;
    private static Player currentPlayer;

    @FXML
    private ImageView tcircle2;
    @FXML
    private ImageView tcircle1;
    @FXML
    private Circle yellowBall2;
    @FXML
    private Circle yellowBall1;
    @FXML
    private Circle blueBall1;
    @FXML
    private Circle blueBall2;
    @FXML
    private Circle pinkBall1;
    @FXML
    private Circle pinkBall2;
    @FXML
    private Circle purpleBall1;
    @FXML
    private Circle purpleBall2;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private AnchorPane menuBG;
    @FXML
    private Label newGameButton;
    @FXML
    private Label loadGameButton;
    @FXML
    private Label highScoreButton;
    @FXML
    private Label exitButton;
    @FXML
    private Label heading;

    @Override
    public void start(Stage stage) throws Exception, InterruptedException{
        Main.db = Database.deserialize();
        Parent root = FXMLLoader.load(getClass().getResource("titleScreen.fxml"));
        stage.setTitle("Color Switch");

        this.scene = new Scene(root, 450, 600);

        stage.setScene(this.scene);
        stage.show();

    }

    @FXML
    public void initialize(){
        Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(50),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        tcircle1.setRotate(tcircle1.getRotate()+5);
                        tcircle2.setRotate(tcircle2.getRotate()+5);
                    }
                }));
        rotateTimeline.setCycleCount(Timeline.INDEFINITE);
        rotateTimeline.play();
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        backgroundImage.setOpacity(backgroundImage.getOpacity()+0.004);
                        heading.setOpacity(heading.getOpacity()+0.01);
                        heading.setLayoutY(heading.getLayoutY()+1);
                        tcircle1.setOpacity(tcircle1.getOpacity()+0.01);
                        tcircle1.setLayoutY(tcircle1.getLayoutY()+1);
                        tcircle2.setOpacity(tcircle2.getOpacity()+0.01);
                        tcircle2.setLayoutY(tcircle2.getLayoutY()+1);
                        newGameButton.setOpacity(newGameButton.getOpacity()+0.01);
                        newGameButton.setLayoutX(newGameButton.getLayoutX()+2);
                        loadGameButton.setOpacity(loadGameButton.getOpacity()+0.01);
                        loadGameButton.setLayoutX(loadGameButton.getLayoutX()-2);
                        highScoreButton.setOpacity(highScoreButton.getOpacity()+0.01);
                        highScoreButton.setLayoutX(highScoreButton.getLayoutX()+2);
                        exitButton.setOpacity(exitButton.getOpacity()+0.01);
                        exitButton.setLayoutX(exitButton.getLayoutX()-2);
                    }
                }));
        enterTimeline.setCycleCount(100);
        enterTimeline.play();
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player player) {
        Main.currentPlayer = player;
    }

    public static Database getDB() {
        return Main.db;
    }

    public void exitAnimation(int buttonClicked){
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(2.5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        yellowBall1.setOpacity(0);
                        yellowBall2.setOpacity(0);
                        pinkBall1.setOpacity(0);
                        pinkBall2.setOpacity(0);
                        blueBall1.setOpacity(0);
                        blueBall2.setOpacity(0);
                        purpleBall1.setOpacity(0);
                        purpleBall2.setOpacity(0);
                        backgroundImage.setOpacity(backgroundImage.getOpacity()-0.004);
                        heading.setOpacity(heading.getOpacity()-0.01);
                        heading.setLayoutY(heading.getLayoutY()-1);
                        tcircle1.setOpacity(tcircle1.getOpacity()-0.01);
                        tcircle1.setLayoutY(tcircle1.getLayoutY()-1);
                        tcircle2.setOpacity(tcircle2.getOpacity()-0.01);
                        tcircle2.setLayoutY(tcircle2.getLayoutY()-1);
                        newGameButton.setOpacity(newGameButton.getOpacity()-0.01);
                        newGameButton.setLayoutX(newGameButton.getLayoutX()-2);
                        loadGameButton.setOpacity(loadGameButton.getOpacity()-0.01);
                        loadGameButton.setLayoutX(loadGameButton.getLayoutX()+2);
                        highScoreButton.setOpacity(highScoreButton.getOpacity()-0.01);
                        highScoreButton.setLayoutX(highScoreButton.getLayoutX()-2);
                        exitButton.setOpacity(exitButton.getOpacity()-0.01);
                        exitButton.setLayoutX(exitButton.getLayoutX()+2);
                    }
                }));
        enterTimeline.setCycleCount(200);
        enterTimeline.play();
        enterTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AnchorPane pane = null;
                switch(buttonClicked){
                    case 1:

                        try {
                            pane = FXMLLoader.load(getClass().getResource("loadScreen.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        menuBG.getChildren().setAll(pane);
                        break;
                    case 2:

                        try {
                            pane = FXMLLoader.load(getClass().getResource("highScoreScreen.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        menuBG.getChildren().setAll(pane);
                        break;
                    case 3:
                        try {
                            currentPlayer.setCurrentState(new GameState());
                            currentPlayer.getCurrentState().newGame(menuBG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }
    public void newGameAuxiliary(){
        exitAnimation(3);
    }

    public void loadGame() throws Exception {
        exitAnimation(1);
    }

    public void displayHighScores() throws Exception {
        exitAnimation(2);
    }

    public void exitGame() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("exitPopup.fxml"));
        menuBG.getChildren().setAll(pane);
    }
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }




    // ANIMATION

    public void enterYellow(){
        if(newGameButton.getLayoutX()<=149){
            Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            yellowBall1.setLayoutX(yellowBall1.getLayoutX()- 0.5);
                            yellowBall1.setOpacity(yellowBall1.getOpacity()+0.01);
                            yellowBall2.setLayoutX(yellowBall2.getLayoutX() + 0.5);
                            yellowBall2.setOpacity(yellowBall2.getOpacity()+0.01);
                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }
    public void exitYellow(){
        if(newGameButton.getLayoutX()<=149){
            Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            yellowBall1.setLayoutX(yellowBall1.getLayoutX()+0.5);
                            yellowBall1.setOpacity(yellowBall1.getOpacity()-0.01);
                            yellowBall2.setLayoutX(yellowBall2.getLayoutX() - 0.5);
                            yellowBall2.setOpacity(yellowBall2.getOpacity()-0.01);
                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }
    public void enterBlue(){
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        blueBall1.setLayoutX(blueBall1.getLayoutX()- 0.5);
                        blueBall1.setOpacity(blueBall1.getOpacity()+0.01);
                        blueBall2.setLayoutX(blueBall2.getLayoutX() + 0.5);
                        blueBall2.setOpacity(blueBall2.getOpacity()+0.01);
                    }
                }));
        enterTimeline.setCycleCount(100);
        enterTimeline.play();

    }
    public void exitBlue(){
        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        blueBall1.setLayoutX(blueBall1.getLayoutX()+ 0.5);
                        blueBall1.setOpacity(blueBall1.getOpacity()-0.01);
                        blueBall2.setLayoutX(blueBall2.getLayoutX() - 0.5);
                        blueBall2.setOpacity(blueBall2.getOpacity()-0.01);
                    }
                }));
        enterTimeline.setCycleCount(100);
        enterTimeline.play();

    }
    public void enterPink(){
        if(highScoreButton.getLayoutX()<=149){
            Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            pinkBall1.setLayoutX(pinkBall1.getLayoutX()- 0.5);
                            pinkBall1.setOpacity(pinkBall1.getOpacity()+0.01);
                            pinkBall2.setLayoutX(pinkBall2.getLayoutX() + 0.5);
                            pinkBall2.setOpacity(pinkBall2.getOpacity()+0.01);
                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }
    public void exitPink(){
        if(highScoreButton.getLayoutX()<=149){
            Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            pinkBall1.setLayoutX(pinkBall1.getLayoutX()+ 0.5);
                            pinkBall1.setOpacity(pinkBall1.getOpacity()-0.01);
                            pinkBall2.setLayoutX(pinkBall2.getLayoutX() - 0.5);
                            pinkBall2.setOpacity(pinkBall2.getOpacity()-0.01);
                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }
    public void enterPurple(){
        if(exitButton.getLayoutX()<=149){
            Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            purpleBall1.setLayoutX(purpleBall1.getLayoutX()- 0.5);
                            purpleBall1.setOpacity(purpleBall1.getOpacity()+0.01);
                            purpleBall2.setLayoutX(purpleBall2.getLayoutX() + 0.5);
                            purpleBall2.setOpacity(purpleBall2.getOpacity()+0.01);
                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }

    public void exitPurple(){
        if(exitButton.getLayoutX()<=149){
            Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            purpleBall1.setLayoutX(purpleBall1.getLayoutX()+ 0.5);
                            purpleBall1.setOpacity(purpleBall1.getOpacity()-0.01);
                            purpleBall2.setLayoutX(purpleBall2.getLayoutX() - 0.5);
                            purpleBall2.setOpacity(purpleBall2.getOpacity()-0.01);
                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }
}
//Icons made by <a href="http://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>