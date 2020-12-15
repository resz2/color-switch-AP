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
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameState implements Serializable, Cloneable {
    private int mode, difficulty, nextObsIndex, prevObsIndex, newStarPosition, newClockPosition, newObstaclePosition, newColorChangerPosition, numStarsCollected, numRetry;;
    private boolean over, played, keyLock;
    private double xVelocity, yVelocity, xVelocityOffset, yVelocityOffset, dist;
    private Obstacle nextObstacle,prevObstacle, newObs;
    private Ball ball;
    private Star closestStar;
    private Clock closestClock;
    private ColorChanger closestColorChanger;
    private ArrayList<Obstacle> circularObstacleArrayList;
    private ArrayList<Star> StarArrayList;
    private ArrayList<ColorChanger> ColorChangerArraylist;
    private ArrayList<Clock> ClockArrayList;
    private transient Animation.Status moveCamTimelineStatus;
    private transient AudioClip crashSound,starSound,bounceSound,colorSound;
    private transient static AudioClip audio;
    private transient Label resumeButton, saveButton, homeButton, pauseButton, scoreLabel, restartButton, timeLabel;
    private transient Rectangle overlay;
    private transient Timeline collisionTimeline;
    private transient Pane gameOverCanvas;


    public GameState()  {
        mode = nextObsIndex = prevObsIndex = numStarsCollected = numRetry = 0;
        xVelocity = yVelocity = dist = 0;
        xVelocityOffset = 0.25;
        yVelocityOffset = 0.35;
        newStarPosition = -300;
        newClockPosition = -300;
        newObstaclePosition = -500;
        newColorChangerPosition = -3300;
        over = played = keyLock = false;
        resumeButton = new Label();
        saveButton = new Label();
        homeButton = new Label();
        pauseButton = new Label();
        scoreLabel = new Label();
        restartButton = new Label();
        timeLabel = new Label();
        circularObstacleArrayList = new ArrayList<Obstacle>();
        StarArrayList = new ArrayList<Star>();
        ColorChangerArraylist = new ArrayList<ColorChanger>();
        ClockArrayList = new ArrayList<Clock>();
    }

    @Override
    public String toString() {
        return "    " + this.getNumStarsCollected() + "                         " + diffPrinter(this.getDifficulty());
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

    public GameState deepClone()    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(this);

            GameState copy;
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            copy = (GameState)in.readObject();
            return copy;
        }
        catch (IOException | ClassNotFoundException e)  {
            e.printStackTrace();
            return null;
        }
    }

    public static void setAudio(AudioClip a){
        GameState.audio = a;
    }

    public void createAudioClips(){
        String path = "audio/bounce.mp3";
        bounceSound = new AudioClip(new File(path).toURI().toString());
        bounceSound.setVolume(1);
        bounceSound.setCycleCount(1);
        path = "audio/acid5.wav";
        crashSound = new AudioClip(new File(path).toURI().toString());
        crashSound.setVolume(1);
        crashSound.setCycleCount(1);
        path = "audio/color.mp3";
        starSound = new AudioClip(new File(path).toURI().toString());
        starSound.setVolume(0.75);
        starSound.setCycleCount(1);
        path = "audio/star.mp3";
        colorSound = new AudioClip(new File(path).toURI().toString());
        colorSound.setVolume(0.75);
        colorSound.setCycleCount(1);
    }

    private void gameScreenSetup(int mode) {
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

        if(mode==1) {
            timeLabel = new Label("15");
            timeLabel.setContentDisplay(ContentDisplay.CENTER);
            timeLabel.setTextAlignment(TextAlignment.CENTER);
            timeLabel.setLayoutY(100);
            timeLabel.setLayoutX(25);
            Font font1 = Font.font("Roboto", FontWeight.BOLD,
                    FontPosture.REGULAR, 25);
            timeLabel.setFont(font1);
            //Filling color to the label
            timeLabel.setTextFill(Color.RED);
        }
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
        gameScreenSetup(mode);
        canvas.getChildren().add(pauseButton);
        canvas.getChildren().add(scoreLabel);
        if(mode==1) { canvas.getChildren().add(timeLabel); }

        // Creating ball
        ball = new Ball(225,550,1);
        circularObstacleArrayList = new ArrayList<Obstacle>();
        StarArrayList = new ArrayList<>();
        ClockArrayList = new ArrayList<>();
        ColorChangerArraylist = new ArrayList<>();

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

        // adding initial 3 stars and colorchangers
        for(int i=0;i<3;i++){
            Star star = new Star(225,300-300*(i));
            StarArrayList.add(star);
            ColorChanger c = new ColorChanger(225,200-1000*(i));
            ColorChangerArraylist.add(c);
            canvas.getChildren().add(ColorChangerArraylist.get(i).colorChangerBody);
            canvas.getChildren().add(StarArrayList.get(i).starBody);
            if(mode==1) {
                Clock clock = new Clock(randGen.nextInt(390)+30,400-300*(i));
                ClockArrayList.add(clock);
                canvas.getChildren().add(ClockArrayList.get(i).clockBody);
            }
        }

        nextObstacle = circularObstacleArrayList.get(nextObsIndex);
        prevObstacle = circularObstacleArrayList.get(prevObsIndex);
        closestStar = StarArrayList.get(0);
        closestColorChanger = ColorChangerArraylist.get(0);
        if(mode==1) { closestClock = ClockArrayList.get(0); }

        canvas.getChildren().add(ball.getBallBody());
        runGame(bgPane, canvas);
    }

    public void loadGame(AnchorPane bgPane) throws  Exception {

        Pane canvas = new Pane();

        // Setting up Game Screen
        gameScreenSetup(mode);
        canvas.getChildren().add(pauseButton);
        canvas.getChildren().add(scoreLabel);
        if(mode==1) { canvas.getChildren().add(timeLabel); }

        // Creating ball
        this.ball = new Ball(225, this.ball.getStaticY(), this.ball.getColor());

        // creating the obstacles to be used
        for(int i=0;i<circularObstacleArrayList.size();i++){
            circularObstacleArrayList.get(i).create();
            //circularObstacleArrayList.get(i).obstacle.setOpacity(0);
            canvas.getChildren().add(circularObstacleArrayList.get(i).obstacle);
        }

        // adding initial stars and colorchangers
        for(Star s: StarArrayList)  {
            s.create();
            canvas.getChildren().add(s.starBody);
        }
        for(ColorChanger c: ColorChangerArraylist)  {
            c.create();
            canvas.getChildren().add(c.colorChangerBody);
        }
        if(mode==1) {
            for(Clock cl: ClockArrayList)  {
                cl.create();
                canvas.getChildren().add(cl.clockBody);
            }
        }

        nextObstacle = circularObstacleArrayList.get(nextObsIndex);
        prevObstacle = circularObstacleArrayList.get(prevObsIndex);
        closestStar = StarArrayList.get(0);
        closestColorChanger = ColorChangerArraylist.get(0);
        if(mode==1) { closestClock = ClockArrayList.get(0); }

        canvas.getChildren().add(ball.getBallBody());

        runGame(bgPane, canvas);
    }

    private void runGame(AnchorPane bgPane, Pane canvas)  {
        // not working
        //createAudioClips();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        yVelocity += yVelocityOffset;
                        double move = ball.setyCoordinate(yVelocity);
                        if(move!=0){
                            for (Obstacle obstacle : circularObstacleArrayList) {

                                obstacle.setyCoordinate(-move);
                            }
                            for (Star star : StarArrayList) {
                                star.setyCoordinate(-move);
                            }
                            for (ColorChanger colorChanger : ColorChangerArraylist) {
                                colorChanger.setyCoordinate(-move);
                            }
                            for(Clock clock: ClockArrayList){
                                clock.setyCoordinate(-move);
                            }
                        }
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Timeline diagonalGravityLeftTimeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        if(ball.xCoordinate<10){
                            ball.setxCoordinate(450);
                            xVelocityOffset=-0.25;
                        }
                        else if(ball.xCoordinate>440){
                            ball.setxCoordinate(0);
                            xVelocityOffset=0.25;
                        }
                        xVelocity+=xVelocityOffset;
                        yVelocity+=yVelocityOffset;
                        double move = ball.setyCoordinate(yVelocity);
                        ball.setxCoordinate(xVelocity);
                        if(move!=0){
                            for (Obstacle obstacle : circularObstacleArrayList) {

                                obstacle.setyCoordinate(-move);
                            }
                            for (Star star : StarArrayList) {
                                star.setyCoordinate(-move);
                            }
                            for (ColorChanger colorChanger : ColorChangerArraylist) {
                                colorChanger.setyCoordinate(-move);
                            }
                            for (Clock clock : ClockArrayList) {
                                clock.setyCoordinate(-move);
                            }
                        }
                    }
                }));
        diagonalGravityLeftTimeline.setCycleCount(Timeline.INDEFINITE);

        Timeline diagonalGravityRightTimeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        if(ball.xCoordinate<10){
                            ball.setxCoordinate(450);
                            xVelocityOffset=-0.25;
                        }
                        else if(ball.xCoordinate>440){
                            ball.setxCoordinate(0);
                            xVelocityOffset=0.25;
                        }
                        xVelocity-=xVelocityOffset;
                        yVelocity+=yVelocityOffset;
                        double move = ball.setyCoordinate(yVelocity);
                        ball.setxCoordinate(xVelocity);
                        if(move!=0){
                            for (Obstacle obstacle : circularObstacleArrayList) {

                                obstacle.setyCoordinate(-move);
                            }
                            for (Star star : StarArrayList) {
                                star.setyCoordinate(-move);
                            }
                            for (ColorChanger colorChanger : ColorChangerArraylist) {
                                colorChanger.setyCoordinate(-move);

                            }
                            for (Clock clock : ClockArrayList) {
                                clock.setyCoordinate(-move);
                            }
                        }
                    }
                }));
        diagonalGravityRightTimeline.setCycleCount(Timeline.INDEFINITE);

        if(mode==1){
            Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int timeLeft = Integer.parseInt(timeLabel.getText())-1;
                    timeLabel.setText(String.valueOf(timeLeft));
                    if(timeLeft==0){
                        try {
                            gameOver(ball,canvas,timeline, bgPane);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }));
            timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();
        }

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

        canvas.addEventFilter(KeyEvent.KEY_PRESSED, event-> {
            if(mode==0){
                if (event.getCode() == KeyCode.SPACE) {
                    timeline.play();
                    yVelocity=-6;
                }
            }

            if(mode==1){
                if (event.getCode() == KeyCode.W) {
                    diagonalGravityLeftTimeline.pause();
                    diagonalGravityRightTimeline.pause();
                    timeline.play();
                    yVelocity=-6;
                }
                if (event.getCode() == KeyCode.A){
                    diagonalGravityLeftTimeline.play();
                    diagonalGravityRightTimeline.pause();
                    timeline.pause();
                    yVelocity=-6;
                    xVelocity=-4;
                }
                else if (event.getCode() ==KeyCode.D){
                    diagonalGravityLeftTimeline.pause();
                    diagonalGravityRightTimeline.play();
                    timeline.pause();
                    yVelocity=-6;
                    xVelocity=4;
                }
            }
        });

        Random randGen = new Random();
        Timer collisionTimer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {

                if( (nextObstacle.collides(ball.getBallBody(),ball.getColor())==0 || prevObstacle.collides(ball.getBallBody(),ball.getColor())==0) && !over){
                        try {
                            over = true;
                            gameOver(ball,canvas,timeline, bgPane);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (nextObstacle.collides(ball.getBallBody(),ball.getColor())==1){
                        if(prevObsIndex==2 && nextObsIndex==3){
                            System.out.println("Adding new");
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
                            }
                            newObs.create();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    canvas.getChildren().add(newObs.obstacle);
                                }
                            });

                            circularObstacleArrayList.add(newObs);

                        }
                        if(nextObsIndex!=prevObsIndex){
                            prevObsIndex++;
                        }
                        nextObsIndex++;
                        nextObstacle = circularObstacleArrayList.get(nextObsIndex);
                        prevObstacle = circularObstacleArrayList.get(prevObsIndex);
                    }
                if(mode==1){
                    if(closestClock.checkCollision(ball.getBallBody())){
                        closestClock.showAnimation(canvas);
                        ClockArrayList.remove(closestClock);
                        try {
                            Clock newClock = new Clock(225,newClockPosition);
                            newStarPosition-=300;
                            ClockArrayList.add(newClock);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    canvas.getChildren().remove(closestClock);
                                    canvas.getChildren().add(newClock.clockBody);
                                    timeLabel.setText(String.valueOf(10));
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        closestClock = ClockArrayList.get(0);
                    }
                }
                if(closestStar.checkCollision(ball.getBallBody())){

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
                }
                if(closestColorChanger.checkCollision(ball.getBallBody())){
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
                overlay = new Rectangle(0,0,450,600);
                overlay.setFill(Color.BLACK);
                overlay.setOpacity(0);
                canvas.getChildren().add(overlay);
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
                resumeButton.setCursor(Cursor.HAND);
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
                saveButton.setCursor(Cursor.HAND);
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
                restartButton.setCursor(Cursor.HAND);
                try {
                    stream = this.getClass().getResourceAsStream("/home.png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Image image4 = new Image(stream);
                ImageView homeIcon = new ImageView(image4);
                homeIcon.setFitHeight(75);
                homeIcon.setPreserveRatio(true);
                homeButton.setGraphic(homeIcon);
                homeButton.setLayoutY(-170);
                homeButton.setLayoutX(30);
                homeButton.setCursor(Cursor.HAND);

                resumeButton.setOpacity(0);
                saveButton.setOpacity(0);
                homeButton.setOpacity(0);
                restartButton.setOpacity(0);
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
                            //moveCameraTimeline.play();
                        }
                    }
                });
            }
        };

        EventHandler<MouseEvent> saveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Player p = Main.getCurrentPlayer();
                GameState state = deepClone();
                p.getSavedGames().add(state);

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

    public void gameOver(Ball ball,Pane canvas,Timeline gravityTimeline, AnchorPane bgPane) throws Exception{

        gravityTimeline.pause();
        Timeline enlargeTimeline = new Timeline(new KeyFrame(Duration.millis(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        ball.getBallBody().setScaleX(ball.getBallBody().getScaleX()+0.015);
                        ball.getBallBody().setScaleY(ball.getBallBody().getScaleY()+0.015);
                    }
                }));
        enlargeTimeline.setCycleCount(100);

        enlargeTimeline.play();

        enlargeTimeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                ball.getBallBody().setOpacity(0);
                System.out.println("hello");

                ArrayList<Circle> smallBalls = new ArrayList<Circle>();
                ArrayList<PathTransition> paths = new ArrayList<>();
                Random randomGen = new Random();
                for(int i=0;i<24;i++){
                    if(i%4==0){
                        Circle newCircle = new Circle();
                        newCircle.setRadius(2+randomGen.nextInt(8));
                        newCircle.setLayoutX(225);
                        newCircle.setLayoutY(ball.yCoordinate);
                        newCircle.setFill(Color.web("fae100"));
                        smallBalls.add(newCircle);
                        int offset = (i/4)*2;
                        canvas.getChildren().add(newCircle);
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(2.5),
                                new EventHandler<ActionEvent>() {
                                    int xVelocity = 4+offset,yVelocity = 3+ offset;
                                    @Override
                                    public void handle(ActionEvent t) {
                                        newCircle.setLayoutX(newCircle.getLayoutX()+ xVelocity);
                                        newCircle.setLayoutY(newCircle.getLayoutY()+ yVelocity);
                                        Bounds bounds = canvas.getBoundsInLocal();

                                        //If the ball reaches the left or right border make the step negative
                                        if(newCircle.getLayoutX() <= (bounds.getMinX() + newCircle.getRadius()) ||
                                                newCircle.getLayoutX() >= (bounds.getMaxX() - newCircle.getRadius()) ){

                                            xVelocity = -xVelocity;

                                        }

                                        //If the ball reaches the bottom or top border make the step negative
                                        if((newCircle.getLayoutY() >= (bounds.getMaxY() - newCircle.getRadius())) ||
                                                (newCircle.getLayoutY() <= (bounds.getMinY() + newCircle.getRadius()))){

                                            yVelocity = -yVelocity;

                                        }
                                    }
                                }));
                        moveTimeline.setCycleCount(200);
                        moveTimeline.play();
                    }
                    else if (i%4==1){
                        Circle newCircle = new Circle();
                        newCircle.setRadius(2+randomGen.nextInt(8));
                        newCircle.setLayoutX(225);
                        newCircle.setLayoutY(ball.yCoordinate);
                        newCircle.setFill(Color.web("ff0181"));
                        smallBalls.add(newCircle);
                        int offset = (i/4)*2;
                        canvas.getChildren().add(newCircle);
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(2.5),
                                new EventHandler<ActionEvent>() {
                                    int xVelocity = -4 -offset,yVelocity = 3 + offset;
                                    @Override
                                    public void handle(ActionEvent t) {
                                        newCircle.setLayoutX(newCircle.getLayoutX()+ xVelocity);
                                        newCircle.setLayoutY(newCircle.getLayoutY()+ yVelocity);
                                        Bounds bounds = canvas.getBoundsInLocal();

                                        //If the ball reaches the left or right border make the step negative
                                        if(newCircle.getLayoutX() <= (bounds.getMinX() + newCircle.getRadius()) ||
                                                newCircle.getLayoutX() >= (bounds.getMaxX() - newCircle.getRadius()) ){

                                            xVelocity = -xVelocity;

                                        }

                                        //If the ball reaches the bottom or top border make the step negative
                                        if((newCircle.getLayoutY() >= (bounds.getMaxY() - newCircle.getRadius())) ||
                                                (newCircle.getLayoutY() <= (bounds.getMinY() + newCircle.getRadius()))){

                                            yVelocity = -yVelocity;

                                        }
                                    }
                                }));
                        moveTimeline.setCycleCount(200);
                        moveTimeline.play();
                    }
                    else if (i%4==2){
                        Circle newCircle = new Circle();
                        newCircle.setRadius(5);
                        newCircle.setLayoutX(2+randomGen.nextInt(8));
                        newCircle.setLayoutY(ball.yCoordinate);
                        newCircle.setFill(Color.web("32dbf0"));
                        smallBalls.add(newCircle);
                        int offset = (i/4)*2;
                        canvas.getChildren().add(newCircle);
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(2.5),
                                new EventHandler<ActionEvent>() {
                                    int xVelocity = 4 + offset,yVelocity = -3 + offset;
                                    @Override
                                    public void handle(ActionEvent t) {
                                        newCircle.setLayoutX(newCircle.getLayoutX()+ xVelocity);
                                        newCircle.setLayoutY(newCircle.getLayoutY()+ yVelocity);
                                        Bounds bounds = canvas.getBoundsInLocal();

                                        //If the ball reaches the left or right border make the step negative
                                        if(newCircle.getLayoutX() <= (bounds.getMinX() + newCircle.getRadius()) ||
                                                newCircle.getLayoutX() >= (bounds.getMaxX() - newCircle.getRadius()) ){

                                            xVelocity = -xVelocity;

                                        }

                                        //If the ball reaches the bottom or top border make the step negative
                                        if((newCircle.getLayoutY() >= (bounds.getMaxY() - newCircle.getRadius())) ||
                                                (newCircle.getLayoutY() <= (bounds.getMinY() + newCircle.getRadius()))){

                                            yVelocity = -yVelocity;

                                        }
                                    }
                                }));
                        moveTimeline.setCycleCount(200);
                        moveTimeline.play();

                    }
                    else if (i%4==3){
                        Circle newCircle = new Circle();
                        newCircle.setRadius(2+randomGen.nextInt(8));
                        newCircle.setLayoutX(225);
                        newCircle.setLayoutY(ball.yCoordinate);
                        newCircle.setFill(Color.web("900dff"));
                        smallBalls.add(newCircle);
                        int offset = (i/4)*2;
                        canvas.getChildren().add(newCircle);
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(2.5),
                                new EventHandler<ActionEvent>() {
                                    int xVelocity = -4 + offset,yVelocity = -3 + offset;
                                    @Override
                                    public void handle(ActionEvent t) {
                                        newCircle.setLayoutX(newCircle.getLayoutX()+ xVelocity);
                                        newCircle.setLayoutY(newCircle.getLayoutY()+ yVelocity);
                                        Bounds bounds = canvas.getBoundsInLocal();

                                        //If the ball reaches the left or right border make the step negative
                                        if(newCircle.getLayoutX() <= (bounds.getMinX() + newCircle.getRadius()) ||
                                                newCircle.getLayoutX() >= (bounds.getMaxX() - newCircle.getRadius()) ){

                                            xVelocity = -xVelocity;

                                        }

                                        //If the ball reaches the bottom or top border make the step negative
                                        if((newCircle.getLayoutY() >= (bounds.getMaxY() - newCircle.getRadius())) ||
                                                (newCircle.getLayoutY() <= (bounds.getMinY() + newCircle.getRadius()))){

                                            yVelocity = -yVelocity;

                                        }
                                    }
                                }));
                        moveTimeline.setCycleCount(200);
                        moveTimeline.play();
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

    public void setMode(int mode)   { this.mode = mode;}

    public int getDifficulty() { return difficulty; }

    public int getNumStarsCollected()   { return numStarsCollected; }

    public void decreaseStars() {
        this.numStarsCollected -= 4;
    }

    public Pane getGameOverCanvas() { return gameOverCanvas; }
}
