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
    private int mode, difficulty, nextObsIndex, prevObsIndex, newStarPosition, newClockPosition,
            newObstaclePosition, newColorChangerPosition, numStarsCollected, numRetry;
    private boolean over, played, keyLock, firstObstacle;
    private double xVelocity, yVelocity, xVelocityOffset, yVelocityOffset, dist, difficultyOffset;
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
    private transient Pane canvas;


    public GameState()  {
        mode = nextObsIndex = prevObsIndex = numStarsCollected = numRetry = difficulty = 0;
        xVelocity = yVelocity = dist = difficultyOffset = 0;
        xVelocityOffset = 0.25;
        yVelocityOffset = 0.35;
        newStarPosition = -200;
        newClockPosition = -300;
        newObstaclePosition = -500;
        newColorChangerPosition = -3550;
        over = played = keyLock = firstObstacle = false;
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

        scoreLabel = new Label("0");
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
    }

    public void addNewStar(ArrayList<Star> arr){
        //collisionTimeline.pause();
        try {
            Star newStar = new Star(225,newObstaclePosition+25*difficultyOffset);
            arr.add(newStar);
            canvas.getChildren().add(newStar.starBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //collisionTimeline.play();
    }
    public void addNewColorChanger(ArrayList<ColorChanger> arr){
        //collisionTimeline.pause();
        ColorChanger c = new ColorChanger(225,newColorChangerPosition+25*difficultyOffset);
        canvas.getChildren().add(c.colorChangerBody);
        arr.add(c);
        //collisionTimeline.play();
    }
    public void addNewObstacle(Timeline collisionTimeline,ArrayList<Obstacle> arr,double distanceTravelled){
        if(firstObstacle){
            firstObstacle=false;
            return;
        }
        collisionTimeline.pause();
        Random randGen = new Random();
        int obsNumber = randGen.nextInt(6);
        Obstacle removeThis = arr.remove(0);
        int offset=0;

        newObs = new CrossObstacle(newObstaclePosition, 275);

        //System.out.println(newObstaclePosition);
        switch (obsNumber) {
            case 0: newObs = new CrossObstacle(newObstaclePosition-offset, 275);
                    break;
            case 1: newObs = new SquareObstacle(90-10*difficultyOffset, 110-10*difficultyOffset, newObstaclePosition-offset+25*difficultyOffset, 225);
                    break;
            case 2: newObs = new ThornObstacle(80-10*difficultyOffset, newObstaclePosition-offset+25*difficultyOffset, 225, 1.2);
                    break;
            case 3: newObs = new BowObstacle(newObstaclePosition-offset+25*difficultyOffset, 225, 70-10*difficultyOffset, 90-10*difficultyOffset, 115);
                    break;
            case 4: newObs = new HalfBowObstacle(newObstaclePosition-offset+25*difficultyOffset, 225, 70-10*difficultyOffset, 90-10*difficultyOffset , 115);
                    break;
            case 5: newObs = new CircularObstacle(100-10*difficultyOffset, 120-10*difficultyOffset, newObstaclePosition-offset+25*difficultyOffset, 225);
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

        canvas = new Pane();

        // Setting up Game Screen
        gameScreenSetup();

        // Creating ball
        ball = new Ball(225,550,1);
        circularObstacleArrayList = new ArrayList<Obstacle>();
        StarArrayList = new ArrayList<>();
        ClockArrayList = new ArrayList<>();
        ColorChangerArraylist = new ArrayList<>();

        Random randGen = new Random();
        for (int i=0;i<3;i++){
            int obsNumber = 4;
            switch (obsNumber) {
                case 0: circularObstacleArrayList.add(new CrossObstacle(300 - 500 * (i), 275));
                        break;
                case 1: circularObstacleArrayList.add(new SquareObstacle(90-10*difficultyOffset, 110-10*difficultyOffset, 300 - 500 * i, 225));
                        break;
                case 2: circularObstacleArrayList.add(new ThornObstacle(80-10*difficultyOffset, 300 - 500 * i, 225, 1.2));
                        break;
                case 3: circularObstacleArrayList.add(new BowObstacle(300 - 500 * i, 225, 70-10*difficultyOffset, 90-10*difficultyOffset, 115));
                        break;
                case 4: circularObstacleArrayList.add(new HalfBowObstacle(300 - 500 * i, 225, 70-10*difficultyOffset, 90-10*difficultyOffset, 115));
                        break;
                case 5: circularObstacleArrayList.add(new CircularObstacle(100-10*difficultyOffset, 120-10*difficultyOffset, 300 - 500 * (i), 225));
                        break;
            }
        }

        // creating the obstacles to be used
        for (Obstacle obstacle : circularObstacleArrayList) {
            obstacle.create();
            //circularObstacleArrayList.get(i).obstacle.setOpacity(0);
            canvas.getChildren().add(obstacle.obstacle);
        }

        // adding initial 3 stars and colorchangers
        for(int i=0;i<3;i++){
            Star star = new Star(225,300-500*(i));
            StarArrayList.add(star);
            if(i<2){
                ColorChanger c = new ColorChanger(225,100-1200*(i));
                ColorChangerArraylist.add(c);
                canvas.getChildren().add(ColorChangerArraylist.get(i).colorChangerBody);
            }

            canvas.getChildren().add(StarArrayList.get(i).starBody);
            if(mode==1){
                Clock clock = new Clock(randGen.nextInt(390)+30,400-300*(i));
                ClockArrayList.add(clock);
                canvas.getChildren().add(ClockArrayList.get(i).clockBody);
            }
        }

        nextObstacle = circularObstacleArrayList.get(nextObsIndex);
        prevObstacle = circularObstacleArrayList.get(prevObsIndex);
        closestStar = StarArrayList.get(0);
        closestColorChanger = ColorChangerArraylist.get(0);
        if(mode==1){
            closestClock = ClockArrayList.get(0);
        }
        canvas.getChildren().add(ball.getBallBody());
        runGame(bgPane, canvas);
    }
    public void loadGame(AnchorPane bgPane) throws  Exception {

        canvas = new Pane();
        resumeButton = new Label();
        saveButton = new Label();
        homeButton = new Label();
        pauseButton = new Label();
        scoreLabel = new Label();
        restartButton = new Label();
        timeLabel = new Label();

        // Setting up Game Screen
        gameScreenSetup();

        // Creating ball
        ball.create();

        // creating the obstacles to be used
        for (Obstacle obstacle : circularObstacleArrayList) {
            obstacle.create();
            //circularObstacleArrayList.get(i).obstacle.setOpacity(0);
            canvas.getChildren().add(obstacle.obstacle);
        }

        // adding initial 3 stars and colorchangers
        for(Star s: StarArrayList)  {
            s.create();
            canvas.getChildren().add(s.starBody);
        }
        for(ColorChanger c: ColorChangerArraylist)  {
            c.create();
            canvas .getChildren().add(c.colorChangerBody);
        }
        if(mode==1) {
            for(Clock cl: ClockArrayList)   {
                cl.create();
                canvas.getChildren().add(cl.clockBody);
            }
        }

        nextObstacle.create();
        prevObstacle.create();
        closestStar.create();
        closestColorChanger.create();
        if(newObs!=null)    {
            newObs.create();
        }

        if(!canvas.getChildren().contains(nextObstacle.obstacle))
            canvas.getChildren().add(nextObstacle.obstacle);
        if(!canvas.getChildren().contains(prevObstacle.obstacle))
            canvas.getChildren().add(prevObstacle.obstacle);
        if(!canvas.getChildren().contains(closestStar.starBody))
            canvas.getChildren().add(closestStar.starBody);
        if(!canvas.getChildren().contains(closestColorChanger.colorChangerBody))
            canvas.getChildren().add(closestColorChanger.colorChangerBody);
        if(newObs!=null && !canvas.getChildren().contains(newObs.obstacle))
            canvas.getChildren().add(newObs.obstacle);

        if(mode==1){
            closestClock.create();
            if(!canvas.getChildren().contains(closestClock.clockBody))
                canvas.getChildren().add(closestClock.clockBody);
        }
        canvas.getChildren().add(ball.getBallBody());
        runGame(bgPane, canvas);
    }

    private void runGame(AnchorPane bgPane, Pane canvas)  {
        // not working
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
        gravityTimeline.setCycleCount(Timeline.INDEFINITE);

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

        if(mode==1){


            Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int timeLeft = Integer.parseInt(timeLabel.getText())-1;
                    timeLabel.setText(String.valueOf(timeLeft));
                    if(timeLeft==0){
                        try {
                            gameOver(ball,canvas,gravityTimeline, bgPane);
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
        canvas.addEventFilter(KeyEvent.KEY_RELEASED,event->{
            if(mode==0){
                if (event.getCode() == KeyCode.SPACE) {
                    keyLock=false;
                }
            }
        });

        canvas.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if(mode==0){
                if (event.getCode() == KeyCode.SPACE && !keyLock) {
                    bounceSound.play();
                    keyLock=true;
                    gravityTimeline.play();
                    yVelocity=-6;

                }
            }

            if(mode==1){
                if (event.getCode() == KeyCode.W) {
                    diagonalGravityLeftTimeline.pause();
                    diagonalGravityRightTimeline.pause();
                    gravityTimeline.play();
                    yVelocity=-6;

                }
                if (event.getCode() == KeyCode.A){
                    diagonalGravityLeftTimeline.play();
                    diagonalGravityRightTimeline.pause();
                    gravityTimeline.pause();
                    yVelocity=-6;
                    xVelocity=-4;
                }
                else if (event.getCode() ==KeyCode.D){
                    diagonalGravityLeftTimeline.pause();
                    diagonalGravityRightTimeline.play();
                    gravityTimeline.pause();
                    yVelocity=-6;
                    xVelocity=4;
                }
            }


        });

        Random randGen = new Random();

        collisionTimeline = new Timeline(new KeyFrame(Duration.millis(10),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        for(int i=0;i<circularObstacleArrayList.size();i++){
                            int val = circularObstacleArrayList.get(i).collides(ball.getBallBody(),ball.getColor());
                            if(val==0 && !over){
                                try {
                                    over = true;
                                    crashSound.play();
                                    gameOver(ball,canvas,gravityTimeline, bgPane);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(ball.getDistanceTravelled()>=500){
                                ball.setNumRoundsTravelled(ball.getNumRoundsTravelled()+1);
                                addNewObstacle(collisionTimeline,circularObstacleArrayList,ball.getDistanceTravelled());
                                addNewStar(StarArrayList);
                                if(ball.getNumRoundsTravelled()%2==0){
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                closestClock = ClockArrayList.get(0);
                            }
                        }
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

                pauseSetup();

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
                        gravityTimeline.play();
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

    public void setMode(int mode)   { this.mode = mode; }
    public int getMode()    { return this.mode; }

    public int getDifficulty() { return difficulty; }
    public int getNumStarsCollected()   { return numStarsCollected; }
    public void decreaseStars() {
        this.numStarsCollected -= 4;
    }
}
