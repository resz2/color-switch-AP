package sample;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    Scene scene;
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
    int closestObsIndex=1;
    int newStarPosition=-600;
    Obstacle closestObstacle;
    Star closestStar;
    Label resumeButton=new Label(),saveButton= new Label(),homeButton = new Label();
    Rectangle overlay;
    public boolean cameraMoving = false;
    double velocity = 0;

    @Override
    public void start(Stage stage) throws Exception, InterruptedException{
        Parent root = FXMLLoader.load(getClass().getResource("titleScreen.fxml"));
        stage.setTitle("Color Switch");

        this.scene = new Scene(root, 450, 600);

        stage.setScene(this.scene);
        stage.show();

    }

    public void choosePlayer() {
        //display player list
    }

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
                            newGame();
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
    public void newGame() throws  Exception {

        Pane canvas = new Pane();
        InputStream stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\pause.png");
        Image image = new Image(stream);
        Label pauseButton = new Label();
        ImageView pauseIcon = new ImageView(image);
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setLayoutX(375);
        pauseButton.setLayoutY(30);
        pauseIcon.setFitHeight(50);
        pauseIcon.setFitWidth(50);
        pauseIcon.setPreserveRatio(true);

        canvas.getChildren().add(pauseButton);
        Ball ball = new Ball(225,550,3);
        ArrayList<Obstacle> circularObstacleArrayList = new ArrayList<Obstacle>();
        ArrayList<Star> StarArrayList = new ArrayList<Star>();
        for (int i=0;i<9;i++){
            if(i%3==0){
                circularObstacleArrayList.add(new CrossObstacle(300-300*(i),275));
            }
            else if (i%3==1){
                circularObstacleArrayList.add(new SquareObstacle(80,95,300-300*i,225));
            }
            else{
                circularObstacleArrayList.add(new CircularObstacle(80,95,300-300*(i),225));
            }
        }
        for(int i=0;i<circularObstacleArrayList.size();i++){
            circularObstacleArrayList.get(i).create();
            canvas.getChildren().add(circularObstacleArrayList.get(i).obstacle);
        }
        for(int i=0;i<3;i++){
            Star star = new Star(225,300-300*(i));
            StarArrayList.add(star);
            canvas.getChildren().add(StarArrayList.get(i).starBody);
        }
        for(int i=0;i<StarArrayList.size();i++){
            System.out.println(StarArrayList.get(i).Bbox.getBoundsInParent());
            System.out.println(ball.ballBody.getBoundsInParent());
        }
        closestObstacle = circularObstacleArrayList.get(closestObsIndex);
        closestStar = StarArrayList.get(0);
        canvas.getChildren().add(ball.ballBody);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        velocity+=0.35;
                        ball.setyCoordinate(velocity);

                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Timeline moveCameraTimeline = new Timeline(new KeyFrame(Duration.millis(2),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {

                        for(int i=0;i<circularObstacleArrayList.size();i++) {
                            circularObstacleArrayList.get(i).setyCoordinate(0.3);
                        }
                        for(int i=0;i<StarArrayList.size();i++){
                            StarArrayList.get(i).setyCoordinate(0.3);
                        }
                    }

                }));
        moveCameraTimeline.setCycleCount(100);
        Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(50),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        for(int i=0;i<circularObstacleArrayList.size();i++){
                            circularObstacleArrayList.get(i).setAngleOfRotation(2.5);
                        }

                    }
                }));
        rotateTimeline.setCycleCount(Timeline.INDEFINITE);
        rotateTimeline.play();
        canvas.setFocusTraversable(true);
        canvas.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE) {
                timeline.play();
                velocity=-5;
            }
        });
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(ball.getyCoordinate()<= 300 && !cameraMoving){
                    moveCamera(timeline,moveCameraTimeline);
                }
            }
        };
        timer.scheduleAtFixedRate(task,100,100);
        Timer collisionTimer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {

//                if(!closestObstacle.collidesInner(ball.Bbox) && closestObstacle.collidesOuter(ball.Bbox)){
//                    //System.out.println("Kaboom");
//                    if(!closestObstacle.colorMatch(2,ball.isInsideObstacle)){
//
//                        System.exit(0);
//                        //System.out.println("Game Over");
//                    }
//                }
//                else if (closestObstacle.collidesInner(ball.Bbox) && closestObstacle.collidesOuter(ball.Bbox)){
//                    ball.setInsideObstacle(true);
//                    //System.out.println("Inside");
//                }
//                else{
//                    if(ball.isInsideObstacle){
//                        System.out.println("Changing");
//                        closestObsIndex+=1;
//                        closestObstacle = circularObstacleArrayList.get(closestObsIndex);
//                        ball.setInsideObstacle(false);
//                    }
//
//                }
                if(closestStar.checkCollision(ball.ballBody)){
                    moveCameraTimeline.pause();
                    closestStar.showAnimation();
                    StarArrayList.remove(closestStar);
                    try {
                        Star newStar = new Star(225,newStarPosition);
                        newStarPosition-=300;
                        StarArrayList.add(newStar);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                canvas.getChildren().add(newStar.starBody);
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    closestStar = StarArrayList.get(0);
                    moveCameraTimeline.play();
                }
            }
        };
        collisionTimer.scheduleAtFixedRate(task1,100,10);
        menuBG.getChildren().setAll(canvas);
        EventHandler<MouseEvent> resumeHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Clicked Resume");
                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                overlay.setOpacity(overlay.getOpacity()-0.009);
                                resumeButton.setOpacity(resumeButton.getOpacity()-0.01);
                                resumeButton.setLayoutY(resumeButton.getLayoutY()+2);
                                saveButton.setOpacity(saveButton.getOpacity()-0.01);
                                saveButton.setLayoutY(saveButton.getLayoutY()+2);
                                homeButton.setOpacity(homeButton.getOpacity()-0.01);
                                homeButton.setLayoutY(homeButton.getLayoutY()-2);
                            }
                        }));
                enterTimeline.setCycleCount(100);
                enterTimeline.play();
                enterTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        timeline.play();
                        rotateTimeline.play();
                        moveCameraTimeline.play();
                    }
                });
            }
        };
        resumeButton.addEventFilter(MouseEvent.MOUSE_CLICKED,resumeHandler);
        EventHandler<MouseEvent> pauseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.pause();
                rotateTimeline.pause();
                moveCameraTimeline.pause();
                System.out.println("Clicked");
                overlay = new Rectangle(0,0,450,600);
                overlay.setFill(Color.BLACK);
                overlay.setOpacity(0);
                canvas.getChildren().add(overlay);
                InputStream stream = null;
                try {
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\resume.png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Image image = new Image(stream);
                ImageView resumeIcon = new ImageView(image);
                resumeIcon.setFitHeight(60);
                resumeIcon.setPreserveRatio(true);
                resumeButton.setGraphic(resumeIcon);
                resumeButton.setLayoutY(425);
                resumeButton.setLayoutX(195);
                try {
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\save.png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Image image1 = new Image(stream);
                ImageView saveIcon = new ImageView(image1);
                saveIcon.setFitHeight(60);
                saveIcon.setPreserveRatio(true);
                saveButton = new Label();
                saveButton.setGraphic(saveIcon);
                saveButton.setLayoutY(525);
                saveButton.setLayoutX(195);
                try {
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\home.png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Image image3 = new Image(stream);
                ImageView homeIcon = new ImageView(image3);
                saveIcon.setFitHeight(60);
                saveIcon.setPreserveRatio(true);
                homeButton = new Label();
                homeButton.setGraphic(homeIcon);
                homeIcon.setFitHeight(75);
                homeIcon.setPreserveRatio(true);
                homeButton.setLayoutY(-170);
                homeButton.setLayoutX(30);
                resumeButton.setOpacity(0);
                saveButton.setOpacity(0);
                homeButton.setOpacity(0);
                canvas.getChildren().add(resumeButton);
                canvas.getChildren().add(saveButton);
                canvas.getChildren().add(homeButton);
                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                overlay.setOpacity(overlay.getOpacity()+0.009);

                                resumeButton.setOpacity(resumeButton.getOpacity()+0.01);
                                resumeButton.setLayoutY(resumeButton.getLayoutY()-2);
                                saveButton.setOpacity(saveButton.getOpacity()+0.01);
                                saveButton.setLayoutY(saveButton.getLayoutY()-2);
                                homeButton.setOpacity(homeButton.getOpacity()+0.01);
                                homeButton.setLayoutY(homeButton.getLayoutY()+2);
                            }
                        }));
                enterTimeline.setCycleCount(100);
                enterTimeline.play();
            }
        };
        pauseButton.addEventFilter(MouseEvent.MOUSE_CLICKED,pauseEventHandler);


    }
    public void loadGame() throws Exception {
        exitAnimation(1);
    }

    public void displayHighScores() throws Exception {
        exitAnimation(2);

    }
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
    public void moveCamera(Timeline gravityTimeline, Timeline moveCameraTimeline) {
        cameraMoving = true;
        moveCameraTimeline.play();
        moveCameraTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cameraMoving = false;
            }
        });
    }
    public void exitGame() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("exitPopup.fxml"));
        menuBG.getChildren().setAll(pane);
    }
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
//Icons made by <a href="http://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>