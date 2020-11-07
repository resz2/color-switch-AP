package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    Group obstacle;
    Group obstacle2;
    Group obstacle3;
    Group obstacle4;
    Group obstacle5;
    Group obstacle6;
    Group obstacle7;
    public boolean cameraMoving = false;
    double velocity = 0; //Step on y
    BoundingBox ballBounds = new BoundingBox(375,755,30,30);
    public Group obstacleMaker(){
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.RED);
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(400 + 75);
        moveTo.setY(400);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(400);
        arcToInner.setY(400-75);
        arcToInner.setRadiusX(75);
        arcToInner.setRadiusY(75);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(400 + 75);
        moveTo2.setY(400);

        HLineTo hLineToRightLeg = new HLineTo();
        hLineToRightLeg.setX(400 + 100);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(400);
        arcTo.setY(400-100);
        arcTo.setRadiusX(100);
        arcTo.setRadiusY(100);

        VLineTo hLineToLeftLeg = new VLineTo();
        hLineToLeftLeg.setY(400-75);
        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);
        path.getElements().add(moveTo2);
        path.getElements().add(hLineToRightLeg);
        path.getElements().add(arcTo);
        path.getElements().add(hLineToLeftLeg);

        obstacle.getChildren().add(path);
        Path path2 = new Path();
        path2.setFill(Color.GREEN);
        path2.setFillRule(FillRule.EVEN_ODD);
        path2.setLayoutY(path.getLayoutY()+100);
        path2.getElements().add(moveTo);
        path2.getElements().add(arcToInner);
        path2.getElements().add(moveTo2);
        path2.getElements().add(hLineToRightLeg);
        path2.getElements().add(arcTo);
        path2.getElements().add(hLineToLeftLeg);
        path2.setScaleX(1);
        path2.setScaleY(-1);
        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.YELLOW);
        path3.setFillRule(FillRule.EVEN_ODD);
        path3.setLayoutX(path.getLayoutY()-100);
        path3.getElements().add(moveTo);
        path3.getElements().add(arcToInner);
        path3.getElements().add(moveTo2);
        path3.getElements().add(hLineToRightLeg);
        path3.getElements().add(arcTo);
        path3.getElements().add(hLineToLeftLeg);
        path3.setScaleX(-1);
        path3.setScaleY(1);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.BLUE);
        path4.setFillRule(FillRule.EVEN_ODD);
        path4.setLayoutX(path.getLayoutX()-100);
        path4.setLayoutY(path.getLayoutY()+100);
        path4.getElements().add(moveTo);
        path4.getElements().add(arcToInner);
        path4.getElements().add(moveTo2);
        path4.getElements().add(hLineToRightLeg);
        path4.getElements().add(arcTo);
        path4.getElements().add(hLineToLeftLeg);
        path4.setScaleX(-1);
        path4.setScaleY(-1);
        obstacle.getChildren().add(path4);



        return obstacle;
    }
    @Override
    public void start(Stage stage) throws Exception, InterruptedException{
        Pane canvas = new Pane();
        Scene scene = new Scene(canvas, 800, 800, Color.BLACK);
        ArrayList<CircularObstacle> circularObstacleArrayList = new ArrayList<CircularObstacle>();
        for (int i=0;i<5;i++){
            CircularObstacle obs = new CircularObstacle(85,110,400,400);
            circularObstacleArrayList.add(obs);
        }
        for(int i=0;i<circularObstacleArrayList.size();i++){
            circularObstacleArrayList.get(i).create();
            circularObstacleArrayList.get(i).setyCoordinate(-400*i);
            canvas.getChildren().add(circularObstacleArrayList.get(i).obstacle);
        }
        Circle ball = new Circle(15, Color.YELLOW);
        ball.relocate(385, 770);
        canvas.getChildren().add(ball);

        stage.setTitle("Animated Ball");

        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        velocity+=0.4;
                        ball.setLayoutY(ball.getLayoutY() + velocity);
                        ballBounds = new BoundingBox(375,ball.getLayoutY(),30,30);
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Timeline moveCameraTimeline = new Timeline(new KeyFrame(Duration.millis(2),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        for(int i=0;i<circularObstacleArrayList.size();i++) {
                            circularObstacleArrayList.get(i).setyCoordinate(0.1);
                        }
                    }

                }));
        moveCameraTimeline.setCycleCount(100);
         Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(50),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        for(int i=0;i<circularObstacleArrayList.size();i++){
                            circularObstacleArrayList.get(i).setAngleOfRotation(5);
                        }
                    }
                }));
        rotateTimeline.setCycleCount(Timeline.INDEFINITE);
        rotateTimeline.play();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE) {
                timeline.play();
                velocity=-6;
            }
        });
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(ball.getLayoutY()<= 400 && !cameraMoving){
                    moveCamera(timeline,moveCameraTimeline);
                }
            }
        };
        timer.scheduleAtFixedRate(task,100,250);
        Timer collisionTimer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {

                for(int i=0;i<circularObstacleArrayList.size();i++){
                    if(circularObstacleArrayList.get(i).collidesInner(ballBounds) || circularObstacleArrayList.get(i).collidesOuter(ballBounds)){
                        System.out.println("Kaboom ?");
                    }
                }
            }
        };
        collisionTimer.scheduleAtFixedRate(task1,100,100);
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
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
