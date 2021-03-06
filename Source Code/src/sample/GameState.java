package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GameState implements Serializable, Cloneable {
    private int mode, difficulty, newClockPosition=0, newObstaclePosition, newColorChangerPosition,
            numStarsCollected, numRetry, timeLeft, stateID;
    private boolean over, keyLock, firstObstacle;
    private double xVelocity, yVelocity, xVelocityOffset, yVelocityOffset, difficultyOffset;
    private Obstacle newObs;
    private Ball ball;
    private Star closestStar;
    private ColorChanger closestColorChanger;
    private ArrayList<Obstacle> circularObstacleArrayList;
    private ArrayList<Star> StarArrayList;
    private ArrayList<ColorChanger> ColorChangerArraylist;
    private ArrayList<Clock> ClockArrayList;
    private transient AudioClip crashSound,starSound,bounceSound,colorSound;
    private transient Label resumeButton, saveButton, homeButton, pauseButton, scoreLabel, restartButton, timeLabel, savedLabel;
    private transient ImageView savedIcon;
    private transient Rectangle overlay, overlayLoad;
    private transient Timeline collisionTimeline;
    private transient Pane canvas;
    private transient GameState gameOverState;
    private transient Timeline diagonalGravityLeftTimeline,diagonalGravityRightTimeline;


    public GameState()  {
        mode = numStarsCollected = numRetry = difficulty = stateID = 0;
        xVelocity = yVelocity = difficultyOffset = 0;
        xVelocityOffset = 0.25;
        yVelocityOffset = 0.35;
        newObstaclePosition = -300;
        newColorChangerPosition = -3550;
        over = keyLock =  false;
        timeLeft = 20;
        firstObstacle =true;
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
        canvas.getChildren().add(pauseButton);
        //pause icon setup end

        //score setup start

        scoreLabel = new Label(String.valueOf(getNumStarsCollected()));
        scoreLabel.setContentDisplay(ContentDisplay.CENTER);
        scoreLabel.setTextAlignment(TextAlignment.CENTER);
        scoreLabel.setLayoutY(30);
        scoreLabel.setLayoutX(25);
        Font font = Font.font("Roboto", FontWeight.BOLD,
                FontPosture.REGULAR, 40);
        scoreLabel.setFont(font);
        //Filling color to the label
        scoreLabel.setTextFill(Color.WHITE);
        if (mode == 1) {
            scoreLabel.setLayoutY(75);
            scoreLabel.setLayoutX(50);
            timeLabel = new Label(String.valueOf(timeLeft));
            Image img = new Image("assets/clock.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(40);
            view.setPreserveRatio(true);
            timeLabel.setGraphic(view);
            timeLabel.setLayoutY(30);
            timeLabel.setLayoutX(25);
            Font font1 = Font.font("Roboto", FontWeight.BOLD,
                    FontPosture.REGULAR, 25);
            timeLabel.setFont(font1);
            //Filling color to the label
            timeLabel.setTextFill(Color.LIGHTBLUE);
            canvas.getChildren().add(timeLabel);
        }
        canvas.getChildren().add(scoreLabel);
        //score setup end
    }

    private void pauseSetup()   {
        InputStream stream = null;

        overlay = new Rectangle(0,0,450,600);
        overlay.setFill(Color.BLACK);
        overlay.setOpacity(0);
        canvas.getChildren().add(overlay);

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

        if(this.mode==0)    {
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
        }

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
        if(mode==0) {
            saveButton.setOpacity(0);
        }
        homeButton.setOpacity(0);
        restartButton.setOpacity(0);
        canvas.getChildren().add(resumeButton);
        if(mode==0) {
            canvas.getChildren().add(saveButton);
            try {
                stream = this.getClass().getResourceAsStream("/tick.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Image i= new Image(stream);
            savedIcon = new ImageView(i);
            savedIcon.setFitHeight(50);
            savedIcon.setFitWidth(50);
            savedIcon.setPreserveRatio(true);
            savedIcon.setOpacity(0);
            savedIcon.setLayoutY(475);
            savedIcon.setLayoutX(125);
            savedLabel = new Label();
            savedLabel.setText("Saved");
            savedLabel.setOpacity(0);
            savedLabel.setLayoutX(200);
            savedLabel.setLayoutY(475);
            Font font = Font.font("Roboto", FontWeight.BOLD,
                    FontPosture.REGULAR, 40);
            savedLabel.setFont(font);
            savedLabel.setTextFill(Color.YELLOW);
            canvas.getChildren().add(savedIcon);
            canvas.getChildren().add(savedLabel);
        }
        canvas.getChildren().add(homeButton);
        canvas.getChildren().add(restartButton);
    }

    public void addNewStar(ArrayList<Star> arr){
        collisionTimeline.pause();
        try {
            Star newStar = new Star(225,newObstaclePosition+25*difficultyOffset);
            arr.add(newStar);
            canvas.getChildren().add(newStar.starBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collisionTimeline.play();
    }
    public void addNewColorChanger(ArrayList<ColorChanger> arr){
        collisionTimeline.pause();
        ColorChanger c = new ColorChanger(225,newColorChangerPosition+25*difficultyOffset);
        arr.add(c);
        canvas.getChildren().add(c.colorChangerBody);
        collisionTimeline.play();
    }
    public void addNewClock(ArrayList<Clock> arr){
        collisionTimeline.pause();
        Clock newClock;
        try {
            if(newClockPosition%3==1){
                newClock = new Clock(75,newObstaclePosition+25*difficultyOffset);
                newClockPosition++;
            }
            else if (newClockPosition%3==2){
                newClock = new Clock(225,newObstaclePosition+25*difficultyOffset);
                newClockPosition++;
            }
            else{
                newClock = new Clock(375,newObstaclePosition+25*difficultyOffset);
                newClockPosition++;
            }
            arr.add(newClock);
            canvas.getChildren().add(newClock.clockBody);
            System.out.println(newClockPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collisionTimeline.play();
    }
    public void addNewObstacle(ArrayList<Obstacle> arr,double distanceTravelled){
        if(firstObstacle){
            firstObstacle=false;
            return;
        }
        collisionTimeline.pause();
        Random randGen = new Random();
        int obsNumber = randGen.nextInt(6);
        Obstacle removeThis = arr.remove(0);
        int offset=0;

        newObs = new CrossObstacle(newObstaclePosition, 275,0);

        //System.out.println(newObstaclePosition);
        switch (obsNumber) {
            case 0: newObs = new CrossObstacle(newObstaclePosition-offset, 275,0);
                    break;
            case 1: newObs = new SquareObstacle(90-10*difficultyOffset, 110-10*difficultyOffset, newObstaclePosition-offset+25*difficultyOffset, 225,0);
                    break;
            case 2: newObs = new ThornObstacle(80-10*difficultyOffset, newObstaclePosition-offset+25*difficultyOffset, 225, 1.2,0);
                    break;
            case 3: newObs = new BowObstacle(newObstaclePosition-offset+25*difficultyOffset, 225, 70-10*difficultyOffset, 90-10*difficultyOffset, 115,0);
                    break;
            case 4: newObs = new HalfBowObstacle(newObstaclePosition-offset+25*difficultyOffset, 225, 70-10*difficultyOffset, 90-10*difficultyOffset , 115,0);
                    break;
            case 5: newObs = new CircularObstacle(100-10*difficultyOffset, 120-10*difficultyOffset, newObstaclePosition-offset+25*difficultyOffset, 225,0);
                    break;
        }
        newObs.create();
        canvas.getChildren().add(newObs.obstacle);
        canvas.getChildren().remove(removeThis.obstacle);
        arr.add(newObs);
        collisionTimeline.play();
    }

    public void newGame(int diff, AnchorPane bgPane) throws  Exception {

        this.difficulty = diff;
        if(diff==0) {
            this.difficultyOffset = 0;
        }
        else if(diff==1)    {
            this.difficultyOffset = 0.5;
        }
        else if(diff==2)    {
            this.difficultyOffset = 1;
        }

        if(mode==1) {
            numRetry = -1;
        }

        canvas = new Pane();

        // Setting up Game Screen
        gameScreenSetup();

        // Creating ball
        ball = new Ball(225,550,1, Main.getCurrentPlayer().getCurrentBall());
        circularObstacleArrayList = new ArrayList<Obstacle>();
        StarArrayList = new ArrayList<>();
        ClockArrayList = new ArrayList<>();
        ColorChangerArraylist = new ArrayList<>();

        Random randGen = new Random();
        for (int i=0;i<3;i++){
            int obsNumber = randGen.nextInt(6);
            switch (obsNumber) {
                case 0: circularObstacleArrayList.add(new CrossObstacle(300 - 500 * (i), 275,0));
                        break;
                case 1: circularObstacleArrayList.add(new SquareObstacle(90-10*difficultyOffset, 110-10*difficultyOffset, 300 - 500 * i, 225,0));
                        break;
                case 2: circularObstacleArrayList.add(new ThornObstacle(80-10*difficultyOffset, 300 - 500 * i, 225, 1.2,0));
                        break;
                case 3: circularObstacleArrayList.add(new BowObstacle(300 - 500 * i, 225, 70-10*difficultyOffset, 90-10*difficultyOffset, 115,0));
                        break;
                case 4: circularObstacleArrayList.add(new HalfBowObstacle(300 - 500 * i, 225, 70-10*difficultyOffset, 90-10*difficultyOffset, 115,0));
                        break;
                case 5: circularObstacleArrayList.add(new CircularObstacle(100-10*difficultyOffset, 120-10*difficultyOffset, 300 - 500 * (i), 225,04));
                        break;
            }
        }

        // creating the obstacles to be used
        for (Obstacle obstacle : circularObstacleArrayList) {
            obstacle.create();
            canvas.getChildren().add(obstacle.obstacle);
        }

        // adding initial 3 stars and colorchangers
        for(int i=0;i<3;i++){
            if(mode!=1){
                Star star = new Star(225,300-500*(i));
                StarArrayList.add(star);
                canvas.getChildren().add(StarArrayList.get(i).starBody);
            }

            if(i<2){
                ColorChanger c = new ColorChanger(225,100-1200*(i));
                ColorChangerArraylist.add(c);
                canvas.getChildren().add(ColorChangerArraylist.get(i).colorChangerBody);
            }

            if(mode==1){
                Clock clock;
                if(newClockPosition%3==0){
                    clock = new Clock(225,300-500*(i));
                    newClockPosition++;
                }
                else if (newClockPosition%3==1){
                    clock = new Clock(75,300-500*(i));
                    newClockPosition++;
                }
                else{
                    clock = new Clock(375,300-500*(i));
                    newClockPosition++;
                }
                ClockArrayList.add(clock);
                canvas.getChildren().add(ClockArrayList.get(i).clockBody);
            }
        }

        if(mode !=1){
            closestStar = StarArrayList.get(0);
            closestColorChanger = ColorChangerArraylist.get(0);
        }

        canvas.getChildren().add(ball.getBallBody());
        runGame(bgPane, canvas);
    }
    public void loadGame(AnchorPane bgPane) throws  Exception {

        canvas = new Pane();
        overlay = new Rectangle();
        resumeButton = new Label();
        saveButton = new Label();
        homeButton = new Label();
        pauseButton = new Label();
        scoreLabel = new Label();
        restartButton = new Label();
        timeLabel = new Label();

        // Setting up Game Screen
        gameScreenSetup();

        overlayLoad = new Rectangle(0, 0, 450, 600);
        overlayLoad.setFill(Color.BLACK);
        overlayLoad.setOpacity(0);

        Label countDown = new Label("3");
        Font f1 = Font.font("Roboto", FontWeight.BOLD,
                FontPosture.REGULAR, 120);
        countDown.setFont(f1);
        countDown.setTextFill(Color.web("fae100"));
        countDown.setLayoutY(200);
        countDown.setLayoutX(200);
        countDown.setTranslateZ(-100);
        countDown.setOpacity(1);

        //bgPane.getChildren().add(overlayLoad);
        bgPane.getChildren().add(countDown);
        Timeline fadeTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        overlayLoad.setOpacity(overlayLoad.getOpacity()+0.008);
                        if(overlayLoad.getOpacity()>=0.75){
                            countDown.setOpacity(1);
                        }
                    }
                }));
        fadeTimeline.setCycleCount(100);
        fadeTimeline.play();
        fadeTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            int countDownTime=3;
            @Override
            public void handle(ActionEvent actionEvent) {
                Timeline reviveTimeline = new Timeline(new KeyFrame(Duration.millis(1000),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                countDownTime-=1;
                                countDown.setText(String.valueOf(countDownTime));
                            }
                        }));
                reviveTimeline.setCycleCount(3);
                reviveTimeline.play();
                reviveTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Timeline fadeTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent t) {
                                        overlayLoad.setOpacity(overlayLoad.getOpacity()-0.008);
                                        countDown.setOpacity(countDown.getOpacity()-0.01);
                                    }
                                }));
                        fadeTimeline.setCycleCount(100);
                        fadeTimeline.play();
                        fadeTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                bgPane.getChildren().remove(overlayLoad);
                                bgPane.getChildren().remove(countDown);
                                runGame(bgPane, canvas);
                            }
                        });

                    }
                });

            }
        });

        // Creating ball
        ball.create();

        // creating the obstacles to be used
        for (Obstacle obstacle : circularObstacleArrayList) {
            obstacle.create();
            canvas.getChildren().add(obstacle.obstacle);
        }

        // adding initial 3 stars and colorchangers
        if(mode!=1){
            for(Star s: StarArrayList)  {
                s.create();
                canvas.getChildren().add(s.starBody);
            }
        }
        for(ColorChanger c: ColorChangerArraylist)  {
            c.create();
            canvas.getChildren().add(c.colorChangerBody);
        }
        if(mode==1) {
            for(Clock cl: ClockArrayList)   {
                cl.create();
                canvas.getChildren().add(cl.clockBody);
            }
        }

        canvas.getChildren().add(ball.getBallBody());
    }

    private void runGame(AnchorPane bgPane, Pane canvas)  {
        createAudioClips();
        Timeline gravityTimeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        yVelocity += yVelocityOffset;
                        double move = ball.setyCoordinate(yVelocity);
                        if(move!=0){
                            for (Obstacle obstacle : circularObstacleArrayList) {

                                obstacle.setyCoordinate(-move);
                            }
                            if(mode!=1){
                                for (Star star : StarArrayList) {
                                    star.setyCoordinate(-move);
                                }
                            }
                            else{
                                for(Clock clock: ClockArrayList){
                                    clock.setyCoordinate(-move);
                                }
                            }
                            for (ColorChanger colorChanger : ColorChangerArraylist) {
                                colorChanger.setyCoordinate(-move);
                            }
                        }
                    }
                }));
        gravityTimeline.setCycleCount(Timeline.INDEFINITE);

        if(mode==1) {
            diagonalGravityLeftTimeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            if (ball.xCoordinate < 10) {
                                ball.setxCoordinate(450);
                                xVelocityOffset = -0.25;
                            } else if (ball.xCoordinate > 440) {
                                ball.setxCoordinate(0);
                                xVelocityOffset = 0.25;
                            }
                            xVelocity += xVelocityOffset;
                            yVelocity += yVelocityOffset;
                            double move = ball.setyCoordinate(yVelocity);
                            ball.setxCoordinate(xVelocity);
                            if (move != 0) {
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

            diagonalGravityRightTimeline = new Timeline(new KeyFrame(Duration.millis(16.67),
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
                                for (int i=0;i< StarArrayList.size();i++) {
                                    StarArrayList.get(i).setyCoordinate(-move);
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

            Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    timeLeft = Integer.parseInt(timeLabel.getText())-1;
                    timeLabel.setText(String.valueOf(timeLeft));
                    if(timeLeft==0){
                        try {
                            canvas.getChildren().remove(timeLabel);
                            gameOver(gravityTimeline, bgPane);
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
                            circularObstacleArrayList.get(i).setAngleOfRotation(2.5+difficultyOffset);
                        }

                    }
                }));
        rotateTimeline.setCycleCount(Timeline.INDEFINITE);
        rotateTimeline.play();
        canvas.setFocusTraversable(true);
        EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                    if(mode==0){
                        if (keyEvent.getCode() == KeyCode.SPACE) {
                            keyLock=false;
                        }
                    }
            }
        };
        canvas.addEventFilter(KeyEvent.KEY_RELEASED,keyReleased);
        EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if(mode==0){
                    if (keyEvent.getCode() == KeyCode.SPACE && !keyLock) {
                        bounceSound.play();
                        keyLock=true;
                        gravityTimeline.play();
                        yVelocity=-6;

                    }
                }

                if(mode==1){
                    if (keyEvent.getCode() == KeyCode.W) {
                        bounceSound.play();
                        diagonalGravityLeftTimeline.pause();
                        diagonalGravityRightTimeline.pause();
                        gravityTimeline.play();
                        yVelocity=-6;

                    }
                    if (keyEvent.getCode() == KeyCode.A){
                        bounceSound.play();
                        diagonalGravityLeftTimeline.play();
                        diagonalGravityRightTimeline.pause();
                        gravityTimeline.pause();
                        yVelocity=-6;
                        xVelocity=-4;
                    }
                    else if (keyEvent.getCode() ==KeyCode.D){
                        bounceSound.play();
                        diagonalGravityLeftTimeline.pause();
                        diagonalGravityRightTimeline.play();
                        gravityTimeline.pause();
                        yVelocity=-6;
                        xVelocity=4;
                    }
                }
            }
        };
        canvas.addEventFilter(KeyEvent.KEY_PRESSED, keyPressed);

        Random randGen = new Random();

        collisionTimeline = new Timeline(new KeyFrame(Duration.millis(10),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        if(ball.getyCoordinate()>600){
                            try {
                                numRetry = -1;
                                gameOver(gravityTimeline, bgPane);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        for(int i=0;i<circularObstacleArrayList.size();i++){
                            int val = circularObstacleArrayList.get(i).collides(ball.getBallBody(),ball.getColor());
                            if(val==0 && !over){
                                try {
                                    gravityTimeline.pause();
                                    rotateTimeline.pause();
                                    canvas.removeEventFilter(KeyEvent.KEY_RELEASED,keyReleased);
                                    canvas.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressed);
                                    canvas.getChildren().remove(circularObstacleArrayList.remove(i).obstacle);
                                    gameOverState = deepClone();
                                    over = true;
                                    crashSound.play();
                                    gameOver(gravityTimeline, bgPane);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if(ball.getDistanceTravelled()>=500){
                                ball.setNumRoundsTravelled(ball.getNumRoundsTravelled()+1);
                                addNewObstacle(circularObstacleArrayList,ball.getDistanceTravelled());
                                if(mode!=1){
                                    addNewStar(StarArrayList);
                                }
                                else{
                                    addNewClock(ClockArrayList);
                                }
                                if(ball.getNumRoundsTravelled()%2==0 || ColorChangerArraylist.size()==1){
                                    if(ball.getNumRoundsTravelled()%4==0 &&  difficultyOffset<2.5){
                                        System.out.println("Changing gears");
                                        difficultyOffset+=0.5;
                                    }
                                    addNewColorChanger(ColorChangerArraylist);
                                }
                                ball.setDistanceTravelled(0);
                            }
                        }
                        if(mode==1){
                            for(int i=0;i<ClockArrayList.size();i++){
                                if(ClockArrayList.get(i).checkCollision(ball.getBallBody())){
                                    starSound.play();
                                    ClockArrayList.get(i).showAnimation(canvas);
                                    numStarsCollected = Integer.parseInt(scoreLabel.getText())+1;
                                    if(difficultyOffset<=1){
                                        timeLeft=20;
                                    }
                                    else if (difficultyOffset>1 && difficultyOffset<=2){
                                        timeLeft=15;
                                    }
                                    else{
                                        timeLeft=10;
                                    }
                                    timeLabel.setText(String.valueOf(timeLeft));
                                    canvas.getChildren().remove(ClockArrayList.get(i));
                                    scoreLabel.setText(String.valueOf(numStarsCollected));
                                    ClockArrayList.remove(i);
                                }
                            }
                            for(int i=0;i<ColorChangerArraylist.size();i++){
                                if(ColorChangerArraylist.get(i).checkCollision(ball.getBallBody())){
                                    colorSound.play();
                                    int newColor = ColorChangerArraylist.get(i).showAnimation(canvas);
                                    ball.changeColor(newColor);
                                    canvas.getChildren().remove(ColorChangerArraylist.get(i));
                                    ColorChangerArraylist.remove(i);
                                }
                            }
                        }
                        if(mode==0){
                            if(closestStar.checkCollision(ball.getBallBody())){

                                closestStar.showAnimation(canvas);
                                starSound.play();
                                StarArrayList.remove(closestStar);
                                numStarsCollected = Integer.parseInt(scoreLabel.getText())+1;
                                canvas.getChildren().remove(closestStar);
                                scoreLabel.setText(String.valueOf(numStarsCollected));
                                closestStar = StarArrayList.get(0);
                            }
                            if(closestColorChanger.checkCollision(ball.getBallBody())){
                                colorSound.play();
                                int newColor = closestColorChanger.showAnimation(canvas);
                                ball.changeColor(newColor);
                                ColorChangerArraylist.remove(closestColorChanger);
                                canvas.getChildren().remove(closestColorChanger);
                                closestColorChanger = ColorChangerArraylist.get(0);
                            }
                        }
                    }
                }));
        collisionTimeline.setCycleCount(Timeline.INDEFINITE);
        collisionTimeline.play();


        EventHandler<MouseEvent> homeHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goBack(bgPane);
            }
        };

        EventHandler<MouseEvent> pauseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                gravityTimeline.pause();
                rotateTimeline.pause();
                canvas.removeEventFilter(KeyEvent.KEY_RELEASED,keyReleased);
                canvas.removeEventFilter(KeyEvent.KEY_PRESSED,keyPressed);
                pauseSetup();

                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                overlay.setOpacity(overlay.getOpacity()+0.009);
                                resumeButton.setOpacity(resumeButton.getOpacity()+0.01);
                                restartButton.setOpacity(restartButton.getOpacity()+0.01);
                                if(mode==0) {
                                    resumeButton.setLayoutY(resumeButton.getLayoutY()-2);
                                    restartButton.setLayoutY(restartButton.getLayoutY()-2);
                                    saveButton.setOpacity(saveButton.getOpacity()+0.01);
                                    saveButton.setLayoutY(saveButton.getLayoutY()-2);
                                }
                                else{
                                    resumeButton.setLayoutY(resumeButton.getLayoutY()-1.5);
                                    restartButton.setLayoutY(restartButton.getLayoutY()-2.5);
                                }
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
                canvas.getChildren().remove(savedIcon);
                canvas.getChildren().remove(savedLabel);
                canvas.addEventFilter(KeyEvent.KEY_RELEASED,keyReleased);
                canvas.addEventFilter(KeyEvent.KEY_PRESSED,keyPressed);

                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                overlay.setOpacity(overlay.getOpacity()-0.009);
                                resumeButton.setOpacity(resumeButton.getOpacity()-0.01);
                                resumeButton.setLayoutY(resumeButton.getLayoutY()+2);
                                if(mode==0) {
                                    saveButton.setOpacity(saveButton.getOpacity()-0.01);
                                    saveButton.setLayoutY(saveButton.getLayoutY()+2);
                                }
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
                        if(mode==0) {
                            canvas.getChildren().remove(saveButton);
                        }
                        canvas.getChildren().remove(restartButton);
                        canvas.getChildren().remove(overlay);
                        gravityTimeline.play();
                        rotateTimeline.play();

                    }
                });
            }
        };

        EventHandler<MouseEvent> saveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Player p = Main.getCurrentPlayer();
                if(stateID==0)  {
                    do {
                        stateID = randGen.nextInt(1000000) + 1;
                    }
                    while (p.getGameStateIDs().contains(stateID));
                }
                GameState state = deepClone();
                p.getSavedGames().add(state);

                Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(2),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                savedIcon.setOpacity(savedIcon.getOpacity()+0.01);
                                savedLabel.setOpacity(savedLabel.getOpacity()+0.01);
                            }
                        }));
                enterTimeline.setCycleCount(100);
                enterTimeline.play();
                enterTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
                                new EventHandler<ActionEvent>() {
                                    double scaleOffset=0.002;
                                    @Override
                                    public void handle(ActionEvent t) {
                                        if(savedIcon.getScaleX()>=1.2){
                                            scaleOffset=-0.002;
                                        }
                                        else if(savedIcon.getScaleX()<=1){
                                            scaleOffset=0.002;
                                        }
                                        savedIcon.setScaleX(savedIcon.getScaleX()+scaleOffset);
                                        savedLabel.setScaleX(savedLabel.getScaleX()+scaleOffset);
                                        savedIcon.setScaleY(savedIcon.getScaleX()+scaleOffset);
                                        savedLabel.setScaleY(savedLabel.getScaleX()+scaleOffset);
                                    }
                                }));
                        enterTimeline.setCycleCount(Timeline.INDEFINITE);
                        enterTimeline.play();
                    }
                });

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
        if(mode==0) {
            saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED,saveHandler);
        }
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

    public void gameOver(Timeline gravityTimeline, AnchorPane bgPane) throws Exception{
        gravityTimeline.pause();
        collisionTimeline.stop();
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

                ArrayList<Circle> smallBalls = new ArrayList<Circle>();

                Random randomGen = new Random();
                for(int i=0;i<24;i++){

                    if(i%4==0){
                        Circle newCircle = new Circle();
                        newCircle.setRadius(2+randomGen.nextInt(8));
                        newCircle.setLayoutX(225);
                        newCircle.setLayoutY(ball.yCoordinate);
                        newCircle.setFill(Color.web("fae100"));

                        int offset = (i/4)*2;
                        canvas.getChildren().add(newCircle);
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(5),
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
                        moveTimeline.setCycleCount(300);
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
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(5),
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
                        moveTimeline.setCycleCount(500);
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
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(3),
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
                        moveTimeline.setCycleCount(500);
                        moveTimeline.play();

                    }
                    else    {
                        Circle newCircle = new Circle();
                        newCircle.setRadius(2+randomGen.nextInt(8));
                        newCircle.setLayoutX(225);
                        newCircle.setLayoutY(ball.yCoordinate);
                        newCircle.setFill(Color.web("900dff"));
                        smallBalls.add(newCircle);
                        int offset = (i/4)*2;
                        canvas.getChildren().add(newCircle);
                        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(5),
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
                        moveTimeline.setCycleCount(300);
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

    public void setMode(int mode)   { this.mode = mode; }
    public int getMode()    { return this.mode; }

    public Ball getBall()   { return ball; }
    public int getDifficulty() { return difficulty; }
    public int getNumStarsCollected()   { return numStarsCollected; }
    public void decreaseStars() { this.numStarsCollected -= 4*numRetry; }
    public int getNumRetry() { return numRetry; }
    public int getStateID() { return stateID; }
    public void incrementNumRetry() { this.numRetry += 1; }
    public GameState getGameOverState() { return gameOverState; }
}
