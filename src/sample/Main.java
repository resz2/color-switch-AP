package sample;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.*;
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
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
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
    private AnchorPane menuBG,newGameBG,gameOverBG;
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
    @FXML
    private ImageView diffBackgroundImage;
    @FXML
    private ImageView easyImage;
    @FXML
    private ImageView mediumImage;
    @FXML
    private ImageView hardImage;
    @FXML
    private ImageView chooseImage;
    @FXML
    private Label easyLabel,mediumLabel,hardLabel;
    @FXML
    private ImageView diffBackIcon;
    Boolean played = false;
    @FXML
            private Circle diffBackIconCircle;
    int nextObsIndex=0,prevObsIndex=0;
    int newStarPosition=-300,newClockPosition=-300;
    double newObstaclePosition=-500;
    int difficulty;
    int newColorChangerPosition=-3300;
    Animation.Status moveCamTimelineStatus;
    Obstacle nextObstacle,prevObstacle,newObs;
    Boolean keyLock=false,over=false;
    Star closestStar;
    Clock closestClock;
    ColorChanger closestColorChanger;
    AudioClip crashSound,starSound,bounceSound,colorSound;
    static AudioClip audio;
    Label resumeButton=new Label(),saveButton= new Label(),homeButton = new Label(),pauseButton= new Label(),scoreLabel = new Label(),restartButton=new Label(),timeLabel = new Label();
    Rectangle overlay;
    double xVelocity = 0,yVelocity=0,xVelocityOffset=0.25,yVelocityOffset=0.35,dist=0;
    Pane canvas;
    Timeline collisionTimeline;
    int numStarsCollected=0;
    int numRetry = 0;
    @Override
    public void start(Stage stage) throws Exception, InterruptedException{
        Parent root = FXMLLoader.load(getClass().getResource("titleScreen.fxml"));
        stage.setTitle("Color Switch");

        this.scene = new Scene(root, 450, 600);

        stage.setScene(this.scene);
        stage.show();
    }
    static void setAudio(AudioClip a){
        Main.audio = a;
    }

    public void choosePlayer() {
        //display player list
    }
    public void createAudioClips(){
        String path = "bounce.mp3";
        bounceSound = new AudioClip(new File(path).toURI().toString());
        bounceSound.setVolume(1);
        bounceSound.setCycleCount(1);
        path = "acid5.wav";
        crashSound = new AudioClip(new File(path).toURI().toString());
        crashSound.setVolume(1);
        crashSound.setCycleCount(1);
        path = "color.mp3";
        starSound = new AudioClip(new File(path).toURI().toString());
        starSound.setVolume(0.75);
        starSound.setCycleCount(1);
        path = "star.mp3";
        colorSound = new AudioClip(new File(path).toURI().toString());
        colorSound.setVolume(0.75);
        colorSound.setCycleCount(1);
    }
    public void initialize(){
        createAudioClips();
        if(tcircle1!=null){
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
        else if(diffBackgroundImage!=null){
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
        else{

        }

    }
    public void exitAnimation(int buttonClicked){
        Timeline exitTimeline;
        if(menuBG!=null) {
            exitTimeline = new Timeline(new KeyFrame(Duration.millis(2.5),
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
                            backgroundImage.setOpacity(backgroundImage.getOpacity() - 0.004);
                            heading.setOpacity(heading.getOpacity() - 0.01);
                            heading.setLayoutY(heading.getLayoutY() - 1);
                            tcircle1.setOpacity(tcircle1.getOpacity() - 0.01);
                            tcircle1.setLayoutY(tcircle1.getLayoutY() - 1);
                            tcircle2.setOpacity(tcircle2.getOpacity() - 0.01);
                            tcircle2.setLayoutY(tcircle2.getLayoutY() - 1);
                            newGameButton.setOpacity(newGameButton.getOpacity() - 0.01);
                            newGameButton.setLayoutX(newGameButton.getLayoutX() - 2);
                            loadGameButton.setOpacity(loadGameButton.getOpacity() - 0.01);
                            loadGameButton.setLayoutX(loadGameButton.getLayoutX() + 2);
                            highScoreButton.setOpacity(highScoreButton.getOpacity() - 0.01);
                            highScoreButton.setLayoutX(highScoreButton.getLayoutX() - 2);
                            exitButton.setOpacity(exitButton.getOpacity() - 0.01);
                            exitButton.setLayoutX(exitButton.getLayoutX() + 2);
                        }
                    }));
            exitTimeline.setCycleCount(200);
        }
        else{
            exitTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            overlay.setOpacity(overlay.getOpacity()-0.009);
                            resumeButton.setOpacity(resumeButton.getOpacity()-0.01);
                            resumeButton.setLayoutY(resumeButton.getLayoutY()+2);
                            restartButton.setOpacity(restartButton.getOpacity()-0.01);
                            restartButton.setLayoutY(restartButton.getLayoutY()+2);
                            saveButton.setOpacity(saveButton.getOpacity()-0.01);
                            saveButton.setLayoutY(saveButton.getLayoutY()+2);
                            homeButton.setOpacity(homeButton.getOpacity()-0.01);
                            homeButton.setLayoutY(homeButton.getLayoutY()-2);
                        }
                    }));
            exitTimeline.setCycleCount(100);
        }

            exitTimeline.play();
            exitTimeline.setOnFinished(new EventHandler<ActionEvent>() {
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
                            if(menuBG!=null){
                                menuBG.getChildren().setAll(pane);
                            }
                            else{
                                newGameBG.getChildren().setAll(pane);
                            }
                            break;
                        case 2:

                            try {
                                pane = FXMLLoader.load(getClass().getResource("highScoreScreen.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(menuBG!=null){
                                menuBG.getChildren().setAll(pane);
                            }
                            else{
                                newGameBG.getChildren().setAll(pane);
                            }
                            break;
                        case 3:
                            try {
                                pane = FXMLLoader.load(getClass().getResource("newGameScreen.fxml"));
//                            newGame();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(menuBG!=null){
                                menuBG.getChildren().setAll(pane);
                            }
                            else{
                                newGameBG.getChildren().setAll(pane);
                            }

                            break;
                    }
                }
            });
    }
    public void goBack(){
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
    public void setModeToFrenzy() throws Exception {
        newGame(1);
    }
    public void setDifficultyToEasy() throws Exception {
        difficulty=0;
        newGame(1);
    }
    public void setDifficultyToMedium() throws Exception {
        difficulty=1;
        newGame(0);
    }
    public void setDifficultyToHard() throws Exception {
        difficulty=2;
        newGame(0);
    }
    public void newGameAuxiliary(){
        exitAnimation(3);
    }
    public void addNewStar(ArrayList<Star> arr){
        //collisionTimeline.pause();
        try {
            Star newStar = new Star(225,newObstaclePosition);
            arr.add(newStar);
            canvas.getChildren().add(newStar.starBody);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //collisionTimeline.play();
    }
    public void addNewColorChanger(ArrayList<ColorChanger> arr){
        //collisionTimeline.pause();
        ColorChanger c = new ColorChanger(225,newColorChangerPosition);
        canvas.getChildren().add(c.colorChangerBody);
        arr.add(c);
        //collisionTimeline.play();
    }
    public void addNewObstacle(Timeline collisionTimeline,ArrayList<Obstacle> arr,double distanceTravelled){
        collisionTimeline.pause();
        Random randGen = new Random();
        int obsNumber = randGen.nextInt(6);
        System.out.println(obsNumber);
        Obstacle removeThis = arr.remove(0);
        newObs = new CrossObstacle(newObstaclePosition, 275);;

        //System.out.println(newObstaclePosition);
        switch (obsNumber) {
            case 0 -> newObs = new CrossObstacle(newObstaclePosition, 275);
            case 1 -> newObs = new SquareObstacle(80, 95, newObstaclePosition, 225);
            case 2 -> newObs = new ThornObstacle(50, newObstaclePosition, 225, 1.5);
            case 3 -> newObs = new BowObstacle(newObstaclePosition, 225, 75, 90, 125);
            case 4 -> newObs = new HalfBowObstacle(newObstaclePosition, 225, 75, 90, 125);
            case 5 -> newObs = new CircularObstacle(80, 95, newObstaclePosition, 225);
        }
        newObs.create();
        canvas.getChildren().add(newObs.obstacle);
        canvas.getChildren().remove(removeThis.obstacle);
        arr.add(newObs);
        collisionTimeline.play();
    }
    public void newGame(int mode) throws  Exception {
        canvas = new Pane();
        //pause icon setup start
        InputStream stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\pause.png");
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
        try {
            stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\silverStar.png");
        } catch (FileNotFoundException e) {
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
        if(mode==1){
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
        Ball ball = new Ball(225,550,1);

        ArrayList<Obstacle> circularObstacleArrayList = new ArrayList<Obstacle>();
        ArrayList<Star> StarArrayList = new ArrayList<>();
        ArrayList<Clock> ClockArrayList = new ArrayList<>();
        ArrayList<ColorChanger> ColorChangerArraylist = new ArrayList<>();
        Random randGen = new Random();
        for (int i=0;i<3;i++){
            int obsNumber = randGen.nextInt(6);
            switch (obsNumber) {
                case 0 -> circularObstacleArrayList.add(new CrossObstacle(300 - 500 * (i), 275));
                case 1 -> circularObstacleArrayList.add(new SquareObstacle(80, 95, 300 - 500 * i, 225));
                case 2 -> circularObstacleArrayList.add(new ThornObstacle(100, 300 - 500 * i, 225, 1.3));
                case 3 -> circularObstacleArrayList.add(new BowObstacle(300 - 500 * i, 225, 75, 90, 125));
                case 4 -> circularObstacleArrayList.add(new HalfBowObstacle(300 - 500 * i, 225, 75, 90, 125));
                case 5 -> circularObstacleArrayList.add(new CircularObstacle(80, 95, 300 - 500 * (i), 225));
            }
        }
        for (Obstacle obstacle : circularObstacleArrayList) {
            obstacle.create();
            //circularObstacleArrayList.get(i).obstacle.setOpacity(0);
            canvas.getChildren().add(obstacle.obstacle);
        }
        for(int i=0;i<3;i++){
            Star star = new Star(225,300-500*(i));
            StarArrayList.add(star);
            ColorChanger c = new ColorChanger(225,100-1000*(i));
            ColorChangerArraylist.add(c);
            canvas.getChildren().add(ColorChangerArraylist.get(i).colorChangerBody);
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

        canvas.getChildren().add(ball.ballBody);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        yVelocity+=yVelocityOffset;
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
//        if(mode==1){
//
//
//            Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    int timeLeft = Integer.parseInt(timeLabel.getText())-1;
//                    timeLabel.setText(String.valueOf(timeLeft));
//                    if(timeLeft==0){
//                        try {
//                            gameOver(ball,canvas,timeline);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }));
//            timer.setCycleCount(Timeline.INDEFINITE);
//            timer.play();
//        }


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

        Timer collisionTimer = new Timer();

        collisionTimeline = new Timeline(new KeyFrame(Duration.millis(10),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        for(int i=0;i<circularObstacleArrayList.size();i++){
                             int val = circularObstacleArrayList.get(i).collides(ball.ballBody,ball.color);
                            if(val==0 && !over){
                                try {
                                over = true;
                                crashSound.play();
                                gameOver(ball,canvas,timeline);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(ball.distanceTravelled>=500){
                                addNewObstacle(collisionTimeline,circularObstacleArrayList,ball.distanceTravelled);
                                addNewStar(StarArrayList);
                                addNewColorChanger(ColorChangerArraylist);
                                ball.distanceTravelled=0;
                            }
                        }
                        if(mode==1){
                            if(closestClock.checkCollision(ball.ballBody)){
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
                        if(closestStar.checkCollision(ball.ballBody)){
                            closestStar.showAnimation(canvas);
                            StarArrayList.remove(closestStar);
                            numStarsCollected = Integer.parseInt(scoreLabel.getText())+1;
                            canvas.getChildren().remove(closestStar);
                            scoreLabel.setText(String.valueOf(numStarsCollected));
                            closestStar = StarArrayList.get(0);
                        }
                        if(closestColorChanger.checkCollision(ball.ballBody)){
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
                goBack();
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
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\resume.png");
                } catch (FileNotFoundException e) {
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
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\save.png");
                } catch (FileNotFoundException e) {
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
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\restart.png");
                } catch (FileNotFoundException e) {
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
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\home.png");
                } catch (FileNotFoundException e) {
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
                try {
                    newGameAuxiliary();
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
                System.out.println();
            }
        };
        resumeButton.addEventFilter(MouseEvent.MOUSE_CLICKED,resumeHandler);
        restartButton.addEventFilter(MouseEvent.MOUSE_CLICKED,restartHandler);
        saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED,saveHandler);
        pauseButton.addEventFilter(MouseEvent.MOUSE_CLICKED,pauseEventHandler);
        homeButton.addEventFilter(MouseEvent.MOUSE_CLICKED,homeHandler);
        newGameBG .getChildren().setAll(canvas);

    }
    public void loadGame() throws Exception {
        exitAnimation(1);
    }

    public void displayHighScores() throws Exception {
        exitAnimation(2);
    }
    public void exitToMainMenu() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        gameOverBG.getChildren().setAll(pane);
    }
    public void restartGame() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("newGameScreen.fxml"));
        gameOverBG.getChildren().setAll(pane);
    }

    public void continueGame() {

    }
    public void gameOver(Ball ball,Pane canvas,Timeline gravityTimeline) throws Exception{

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
                                    if(menuBG !=null){
                                        System.out.println("Menu fucker");
                                        menuBG.getChildren().setAll(pane);
                                    }
                                    else{
                                        newGameBG.getChildren().setAll(pane);
                                    }

                                }
                            });
                        }
                    }

                }
            });
    }
    public void revive(){
        gameOverBG.getChildren().setAll(canvas);
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
    public void exitGame() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("exitPopup.fxml"));
        menuBG.getChildren().setAll(pane);
    }
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
//Icons made by <a href="http://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>