package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

public class TitleController {
    Timeline enterTimeline,exitTimeline = new Timeline();
    @FXML
    private ImageView infoLogo;
    @FXML
    private ImageView userLogo;
    @FXML
    private ImageView tcircle2;
    @FXML
    private ImageView tcircle1;
    @FXML
    private ImageView playButton;
    @FXML
    private AnchorPane titleBG;
    @FXML
    private Label playLabel;
    @FXML
    private Label heading;
    @FXML
    private ImageView background;
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
    }
    @FXML
    public void mainMenu() throws Exception {

        enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        background.setOpacity(background.getOpacity()-0.005);
                        heading.setOpacity(heading.getOpacity()-0.01);
                        heading.setLayoutY(heading.getLayoutY()-2.5);
                        playButton.setOpacity(playButton.getOpacity()-0.01);
                        playButton.setLayoutX(playButton.getLayoutX()-2);
                        playLabel.setOpacity(playLabel.getOpacity()-0.01);
                        playLabel.setLayoutX(playLabel.getLayoutX()+2);
                        userLogo.setOpacity(userLogo.getOpacity()-0.01);
                        userLogo.setLayoutY(userLogo.getLayoutY()+2);
                        infoLogo.setOpacity(infoLogo.getOpacity()-0.01);
                        infoLogo.setLayoutY(infoLogo.getLayoutY()+2);
                        tcircle1.setOpacity(heading.getOpacity()-0.01);
                        tcircle1.setLayoutY(tcircle1.getLayoutY()-2.5);
                        tcircle2.setOpacity(heading.getOpacity()-0.01);
                        tcircle2.setLayoutY(tcircle1.getLayoutY()-2.5);
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
                titleBG.getChildren().setAll(pane);
            }
        });

    }
    @FXML
    public void choosePlayer(){

    }
    public void enter(){
        if(exitTimeline.getStatus()!= Animation.Status.RUNNING){
            enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            if(playButton.getLayoutX()>=75){
                                playButton.setLayoutX(playButton.getLayoutX()-0.75);
//                                playLabel.setLayoutY(playLabel.getLayoutY()-0.6);
                                playLabel.setOpacity(playLabel.getOpacity()+0.01);
                            }

                        }
                    }));
            enterTimeline.setCycleCount(100);
            enterTimeline.play();
        }

    }
    public void exit(){
        if(enterTimeline.getStatus()!= Animation.Status.RUNNING){
            exitTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            if(playButton.getLayoutX()<=175)
                                playButton.setLayoutX(playButton.getLayoutX()+1);
//                            playLabel.setLayoutY(playLabel.getLayoutY()+0.6);
                           playLabel.setOpacity(playLabel.getOpacity()-0.01);
                        }
                    }));
            exitTimeline.setCycleCount(100);
            exitTimeline.play();
        }



    }

}
//Icons made by <a href="https://www.flaticon.com/authors/nikita-golubev" title="Nikita Golubev">Nikita Golubev</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>