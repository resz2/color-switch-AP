package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameState implements Serializable {
    private int difficulty, nextObsIndex, prevObsIndex, newStarPosition, newObstaclePosition, newColorChangerPosition, numStarsCollected, numRetry;;
    private boolean over, played;
    private double velocity;
    private Obstacle nextObstacle,prevObstacle, newObs;
    private Star closestStar;
    private Ball ball;
    private ColorChanger closestColorChanger;
    private ArrayList<Obstacle> circularObstacleArrayList;
    private ArrayList<Star> StarArrayList;
    private ArrayList<ColorChanger> ColorChangerArraylist;
    private transient Animation.Status moveCamTimelineStatus;
    @FXML
    private transient Label resumeButton, saveButton, homeButton, pauseButton, scoreLabel, restartButton;
    private transient Rectangle overlay;
    private transient Pane gameOverCanvas;
    public boolean cameraMoving;

    public GameState()  {
        nextObsIndex = prevObsIndex = numStarsCollected = numRetry = 0;
        velocity = 0;
        newStarPosition = -600;
        newObstaclePosition = -1000;
        newColorChangerPosition = -2800;
        over = cameraMoving = played = false;
        resumeButton = new Label();
        saveButton = new Label();
        homeButton = new Label();
        pauseButton = new Label();
        scoreLabel = new Label();
        restartButton = new Label();
        circularObstacleArrayList = new ArrayList<Obstacle>();
        StarArrayList = new ArrayList<Star>();
        ColorChangerArraylist = new ArrayList<ColorChanger>();
    }

    @Override
    public String toString() {
        return "    " + this.getNumStarsCollected() + "                         " + this.getDifficulty();
    }

    private void gameScreenSetup() {
        //pause icon setup start
        InputStream stream = this.getClass().getResourceAsStream("/pause.png");
        Image image = new Image(stream);
        pauseButton = new Label();
        ImageView pauseIcon = new ImageView(image);
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setLayoutX(375);
        pauseButton.setLayoutY(30);
        pauseIcon.setFitHeight(50);
        pauseIcon.setFitWidth(50);
        pauseIcon.setPreserveRatio(true);
        //pause icon setup end

        //score setup start
        try {
            stream = this.getClass().getResourceAsStream("/silverStar.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image2 = new Image(stream);
        ImageView scoreIcon = new ImageView(image2);
        scoreLabel = new Label("0");
        scoreLabel.setContentDisplay(ContentDisplay.CENTER);
        scoreLabel.setTextAlignment(TextAlignment.CENTER);
        scoreIcon.setFitHeight(75);
        scoreIcon.setPreserveRatio(true);
        scoreLabel.setGraphic(scoreIcon);
        scoreLabel.setLayoutY(30);
        scoreLabel.setLayoutX(25);
        Font font = Font.font("Roboto", FontWeight.BOLD,
                FontPosture.REGULAR, 25);
        scoreLabel.setFont(font);
        //Filling color to the label
        scoreLabel.setTextFill(Color.web("272727"));
        //score setup end
    }

    private void pauseSetup()   {
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/resume.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        ImageView resumeIcon = new ImageView(image);
        resumeIcon.setFitHeight(60);
        resumeIcon.setPreserveRatio(true);
        resumeButton.setGraphic(resumeIcon);
        resumeButton.setLayoutY(375);
        resumeButton.setLayoutX(195);
        try {
            stream = this.getClass().getResourceAsStream("/save.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image1 = new Image(stream);
        ImageView saveIcon = new ImageView(image1);
        saveIcon.setFitHeight(60);
        saveIcon.setPreserveRatio(true);
        saveButton.setGraphic(saveIcon);
        saveButton.setLayoutY(475);
        saveButton.setLayoutX(195);
        try {
            stream = this.getClass().getResourceAsStream("/restart.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image3 = new Image(stream);
        ImageView restartIcon = new ImageView(image3);
        restartIcon.setFitHeight(60);
        restartIcon.setPreserveRatio(true);
        restartButton.setGraphic(restartIcon);
        restartButton.setLayoutY(575);
        restartButton.setLayoutX(195);
        try {
            stream = this.getClass().getResourceAsStream("/home.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image4 = new Image(stream);
        ImageView homeIcon = new ImageView(image4);
        saveIcon.setFitHeight(60);
        saveIcon.setPreserveRatio(true);
        homeButton.setGraphic(homeIcon);
        homeIcon.setFitHeight(75);
        homeIcon.setPreserveRatio(true);
        homeButton.setLayoutY(-170);
        homeButton.setLayoutX(30);
        resumeButton.setOpacity(0);
        saveButton.setOpacity(0);
        homeButton.setOpacity(0);
        restartButton.setOpacity(0);
    }

    public void newGame(int diff, AnchorPane bgPane) throws  Exception {

        this.difficulty = diff;

        Pane canvas = new Pane();

        // Setting up Game Screen
        gameScreenSetup();
        canvas.getChildren().add(pauseButton);
        canvas.getChildren().add(scoreLabel);

        // Creating ball
        ball = new Ball(225,550,1);

        Random randGen = new Random();
        for (int i=0;i<5;i++){
            int obsNumber = randGen.nextInt(6);
            switch (obsNumber) {
                case 0: circularObstacleArrayList.add(new CrossObstacle(300 - 500 * (i), 275));
                        break;
                case 1: circularObstacleArrayList.add(new SquareObstacle(80, 95, 300 - 500 * i, 225));
                        break;
                case 2: circularObstacleArrayList.add(new ThornObstacle(100, 300 - 500 * i, 225, 1.3));
                        break;
                case 3: circularObstacleArrayList.add(new BowObstacle(300 - 500 * i, 225, 75, 90, 125));
                        break;
                case 4: circularObstacleArrayList.add(new HalfBowObstacle(300 - 500 * i, 225, 75, 90, 125));
                        break;
                case 5: circularObstacleArrayList.add(new CircularObstacle(80, 95, 300 - 500 * (i), 225));
                        break;
                default:break;
            }
        }

        // creating the obstacles to be used
        for(int i=0;i<circularObstacleArrayList.size();i++){
            circularObstacleArrayList.get(i).create();
            //circularObstacleArrayList.get(i).obstacle.setOpacity(0);
            canvas.getChildren().add(circularObstacleArrayList.get(i).obstacle);
        }

        // adding initial 3 stars ad colorchangers
        for(int i=0;i<3;i++){
            Star star = new Star(225,300-300*(i));
            StarArrayList.add(star);
            ColorChanger c = new ColorChanger(225,200-1000*(i));
            ColorChangerArraylist.add(c);
            canvas.getChildren().add(ColorChangerArraylist.get(i).colorChangerBody);
            canvas.getChildren().add(StarArrayList.get(i).starBody);
        }

        nextObstacle = circularObstacleArrayList.get(nextObsIndex);
        prevObstacle = circularObstacleArrayList.get(prevObsIndex);
        closestStar = StarArrayList.get(0);
        closestColorChanger = ColorChangerArraylist.get(0);
        canvas.getChildren().add(ball.ballBody);

        runGame(bgPane, canvas);
    }

    public void loadGame(AnchorPane bgPane) throws  Exception {

        GameState cur = Main.getCurrentPlayer().getCurrentState();
        Pane canvas = new Pane();

        // Setting up Game Screen
        gameScreenSetup();
        canvas.getChildren().add(pauseButton);
        canvas.getChildren().add(scoreLabel);

        // Creating ball
        ball = new Ball(225, ball.yCoordinate, ball.getColor());

        // creating the obstacles to be used
        for(int i=0;i<circularObstacleArrayList.size();i++){
            circularObstacleArrayList.get(i).create();
            //circularObstacleArrayList.get(i).obstacle.setOpacity(0);
            canvas.getChildren().add(circularObstacleArrayList.get(i).obstacle);
        }

        // adding the 3 present stars and colorchangers
        for(int i=0;i<3;i++){
            Star star = new Star(225, StarArrayList.get(i).yCoordinate);
            StarArrayList.add(star);
            ColorChanger c = new ColorChanger(225,ColorChangerArraylist.get(i).yCoordinate);
            ColorChangerArraylist.add(c);
            canvas.getChildren().add(ColorChangerArraylist.get(i).colorChangerBody);
            canvas.getChildren().add(StarArrayList.get(i).starBody);
        }

        nextObstacle = circularObstacleArrayList.get(nextObsIndex);
        prevObstacle = circularObstacleArrayList.get(prevObsIndex);
        closestStar = StarArrayList.get(0);
        closestColorChanger = ColorChangerArraylist.get(0);
        canvas.getChildren().add(ball.ballBody);

        runGame(bgPane, canvas);
    }

    private void runGame(AnchorPane bgPane, Pane canvas)  {
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
                        for(int i=0;i<ColorChangerArraylist.size();i++){
                            ColorChangerArraylist.get(i).setyCoordinate(0.3);
                        }
                    }

                }));
        moveCameraTimeline.setCycleCount(100);

        Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(50),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        for(int i=0;i<circularObstacleArrayList.size();i++) {
                            circularObstacleArrayList.get(i).setAngleOfRotation(2.5);
                        }
                    }
                }));

        rotateTimeline.setCycleCount(Timeline.INDEFINITE);
        rotateTimeline.play();
        canvas.setFocusTraversable(true);

        canvas.addEventFilter(KeyEvent.KEY_PRESSED, event-> {
            if (event.getCode() == KeyCode.SPACE) {
                if(!played){
                    timeline.play();
                    played =true;
                }

                velocity=-6;
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

        Random randGen = new Random();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                if( (nextObstacle.collides(ball.ballBody,ball.getColor())==0 || prevObstacle.collides(ball.ballBody,ball.getColor())==0) && !over){
                    try {
                        over = true;
                        gameOver(ball,canvas,timeline, bgPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (nextObstacle.collides(ball.ballBody,ball.getColor())==1){
                    if(prevObsIndex==2 && nextObsIndex==3){
                        System.out.println("Adding new");
//                            rotateTimeline.pause();
//                            moveCameraTimeline.pause();

                        circularObstacleArrayList.remove(0);
                        nextObsIndex--;
                        prevObsIndex--;
                        int obsNumber = randGen.nextInt(6);
                        newObs = new CrossObstacle(newObstaclePosition, 275);;
                        switch (obsNumber) {
                            case 0: newObs = new CrossObstacle(newObstaclePosition, 275);
                                    break;
                            case 1: newObs = new SquareObstacle(80, 95, newObstaclePosition, 225);
                                    break;
                            case 2: newObs = new ThornObstacle(50, newObstaclePosition, 225, 1.5);
                                    break;
                            case 3: newObs = new BowObstacle(newObstaclePosition, 225, 75, 90, 125);
                                    break;
                            case 4: newObs = new HalfBowObstacle(newObstaclePosition, 225, 75, 90, 125);
                                    break;
                            case 5: newObs = new CircularObstacle(80, 95, newObstaclePosition, 225);
                                    break;
                            default:break;
                        }
                        newObs.create();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                canvas.getChildren().add(newObs.obstacle);
                            }
                        });

                        circularObstacleArrayList.add(newObs);

//                            moveCameraTimeline.play();
//                            rotateTimeline.play();
                    }
                    if(nextObsIndex!=prevObsIndex){
                        prevObsIndex++;
                    }
                    nextObsIndex++;
                    nextObstacle = circularObstacleArrayList.get(nextObsIndex);
                    prevObstacle = circularObstacleArrayList.get(prevObsIndex);
                }

                if(closestStar.checkCollision(ball.ballBody)){
                    moveCameraTimeline.pause();
                    closestStar.showAnimation(canvas);
                    StarArrayList.remove(closestStar);
                    numStarsCollected = Integer.parseInt(scoreLabel.getText())+1;


                    try {
                        Star newStar = new Star(225,newStarPosition);
                        newStarPosition-=300;
                        StarArrayList.add(newStar);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                canvas.getChildren().remove(closestStar);
                                canvas.getChildren().add(newStar.starBody);
                                scoreLabel.setText(String.valueOf(numStarsCollected));
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    closestStar = StarArrayList.get(0);
//                    moveCameraTimeline.play();
                }
                if(closestColorChanger.checkCollision(ball.ballBody)){
                    moveCameraTimeline.pause();
                    int newColor = closestColorChanger.showAnimation(canvas);
                    System.out.println(newColor);
                    ball.changeColor(newColor);
                    ColorChangerArraylist.remove(closestColorChanger);
                    canvas.getChildren().remove(closestColorChanger);
                    ColorChanger c = new ColorChanger(225,newColorChangerPosition);
                    newColorChangerPosition-=300;
                    ColorChangerArraylist.add(c);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            canvas.getChildren().add(c.colorChangerBody);
                        }
                    });

                    closestColorChanger = ColorChangerArraylist.get(0);
                    moveCameraTimeline.play();
                }
            }
        };

        collisionTimer.scheduleAtFixedRate(task1,100,10);

        EventHandler<MouseEvent> homeHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goBack(bgPane);
            }
        };

        EventHandler<MouseEvent> pauseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.pause();
                rotateTimeline.pause();
//                moveCamTimelineStatus = moveCameraTimeline.getStatus();
//                moveCameraTimeline.pause();
                overlay = new Rectangle(0,0,450,600);
                overlay.setFill(Color.BLACK);
                overlay.setOpacity(0);
                canvas.getChildren().add(overlay);

                pauseSetup();

                canvas.getChildren().add(resumeButton);
                canvas.getChildren().add(saveButton);
                canvas.getChildren().add(homeButton);
                canvas.getChildren().add(restartButton);
                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                overlay.setOpacity(overlay.getOpacity()+0.009);

                                resumeButton.setOpacity(resumeButton.getOpacity()+0.01);
                                resumeButton.setLayoutY(resumeButton.getLayoutY()-2);
                                restartButton.setOpacity(restartButton.getOpacity()+0.01);
                                restartButton.setLayoutY(restartButton.getLayoutY()-2);
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

        EventHandler<MouseEvent> restartHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                AnchorPane pane = null;
                try {
                    pane = FXMLLoader.load(getClass().getResource("newGameScreen.fxml"));
                    bgPane.getChildren().setAll(pane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        EventHandler<MouseEvent> resumeHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                overlay.setOpacity(overlay.getOpacity()-0.009);
                                resumeButton.setOpacity(resumeButton.getOpacity()-0.01);
                                resumeButton.setLayoutY(resumeButton.getLayoutY()+2);
                                saveButton.setOpacity(saveButton.getOpacity()-0.01);
                                saveButton.setLayoutY(saveButton.getLayoutY()+2);
                                restartButton.setOpacity(restartButton.getOpacity()-0.01);
                                restartButton.setLayoutY(restartButton.getLayoutY()+2);
                                homeButton.setOpacity(homeButton.getOpacity()-0.01);
                                homeButton.setLayoutY(homeButton.getLayoutY()-2);

                            }
                        }));
                enterTimeline.setCycleCount(100);
                enterTimeline.play();
                enterTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        canvas.getChildren().remove(resumeButton);
                        canvas.getChildren().remove(homeButton);
                        canvas.getChildren().remove(saveButton);
                        canvas.getChildren().remove(restartButton);
                        canvas.getChildren().remove(overlay);
                        timeline.play();
                        rotateTimeline.play();
                        if(moveCamTimelineStatus== Animation.Status.RUNNING){
                            moveCameraTimeline.play();
                        }
                    }
                });
            }
        };

        EventHandler<MouseEvent> saveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("test");
                Player p = Main.getCurrentPlayer();
                p.getSavedGames().add(p.getCurrentState());

                try {
                    Database.serialize(Main.getDB());
                    System.out.println("Game saved");
                }
                catch (IOException e)   {
                    System.out.println("could not save gamestate");
                }
            }
        };

        resumeButton.addEventFilter(MouseEvent.MOUSE_CLICKED,resumeHandler);
        restartButton.addEventFilter(MouseEvent.MOUSE_CLICKED,restartHandler);
        saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED,saveHandler);
        pauseButton.addEventFilter(MouseEvent.MOUSE_CLICKED,pauseEventHandler);
        homeButton.addEventFilter(MouseEvent.MOUSE_CLICKED,homeHandler);
        bgPane.getChildren().setAll(canvas);
    }

    private void goBack(AnchorPane pausePane) {
        AnchorPane pane=null;
        try {
            pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
        pausePane.getChildren().setAll(pane);
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

    public void gameOver(Ball ball,Pane canvas,Timeline gravityTimeline, AnchorPane bgPane) throws Exception{

        // not sure about this
        this.gameOverCanvas = canvas;

        gravityTimeline.pause();
        Timeline enlargeTimeline = new Timeline(new KeyFrame(Duration.millis(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        ball.ballBody.setScaleX(ball.ballBody.getScaleX()+0.015);
                        ball.ballBody.setScaleY(ball.ballBody.getScaleY()+0.015);
                    }
                }));
        enlargeTimeline.setCycleCount(100);

        enlargeTimeline.play();

        enlargeTimeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                ball.ballBody.setOpacity(0);
                System.out.println("hello");

                ArrayList<Circle> smallBalls = new ArrayList<Circle>();
                ArrayList<PathTransition> paths = new ArrayList<>();
                Random randomGen = new Random();

                for(int i=0;i<24;i++)   {
                    Circle newCircle = new Circle();
                    final int offset;
                    Timeline moveTimeline;
                    if(i%4==0)      { newCircle.setFill(Color.web("fae100")); }
                    else if(i%4==1) { newCircle.setFill(Color.web("ff0181")); }
                    else if(i%4==2) { newCircle.setFill(Color.web("32dbf0")); }
                    else if(i%4==3) { newCircle.setFill(Color.web("900dff")); }

                    newCircle.setRadius(2+randomGen.nextInt(8));
                    newCircle.setLayoutX(225);
                    newCircle.setLayoutY(ball.yCoordinate);
                    smallBalls.add(newCircle);
                    offset = 1;
                    canvas.getChildren().add(newCircle);
                    moveTimeline = new Timeline(new KeyFrame(Duration.millis(2.5), new EventHandler<ActionEvent>() {
                        int xVelocity = 4+ offset,yVelocity = 3+ offset;
                        @Override
                        public void handle(ActionEvent t) {
                            newCircle.setLayoutX(newCircle.getLayoutX()+ xVelocity);
                            newCircle.setLayoutY(newCircle.getLayoutY()+ yVelocity);
                            Bounds bounds = canvas.getBoundsInLocal();

                            //If the ball reaches the left or right border make the step negative
                            if(newCircle.getLayoutX() <= (bounds.getMinX() + newCircle.getRadius()) ||
                                    newCircle.getLayoutX() >= (bounds.getMaxX() - newCircle.getRadius()) )  {
                                xVelocity = -xVelocity;
                            }

                            //If the ball reaches the bottom or top border make the step negative
                            if((newCircle.getLayoutY() >= (bounds.getMaxY() - newCircle.getRadius())) ||
                                    (newCircle.getLayoutY() <= (bounds.getMinY() + newCircle.getRadius()))) {
                                yVelocity = -yVelocity;
                            }
                        }
                    }));
                    moveTimeline.setCycleCount(200);
                    moveTimeline.play();

                    if (i%4==3)    {
                        moveTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                AnchorPane pane = null;
                                try {
                                    pane = FXMLLoader.load(getClass().getResource("gameOverScreen.fxml"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                bgPane.getChildren().setAll(pane);
                            }
                        });
                    }
                }

            }
        });
    }

    public int getDifficulty() { return difficulty; }

    public int getNumStarsCollected()   { return numStarsCollected; }

    public Pane getGameOverCanvas() { return gameOverCanvas; }
}
