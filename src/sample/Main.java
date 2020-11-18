package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    int closestObsIndex=0;
    Obstacle closestObstacle;
    public boolean cameraMoving = false;
    double velocity = 0;
    @Override
    public void start(Stage stage) throws Exception, InterruptedException{
        Pane canvas = new Pane();
        Scene scene = new Scene(canvas, 800, 800, Color.BLACK);
        Ball ball = new Ball(400,785,3);
        ArrayList<CircularObstacle> circularObstacleArrayList = new ArrayList<CircularObstacle>();
        ArrayList<Star> StarArrayList = new ArrayList<Star>();
        for (int i=0;i<10;i++){
            CircularObstacle obs = new CircularObstacle(85,110,400,400);
            circularObstacleArrayList.add(obs);
        }

        for(int i=0;i<circularObstacleArrayList.size();i++){
            circularObstacleArrayList.get(i).create();
            circularObstacleArrayList.get(i).setyCoordinate(-400*i);
            canvas.getChildren().add(circularObstacleArrayList.get(i).obstacle);
        }
        for(int i=0;i<1;i++){
            Star star = new Star(400,400);
            StarArrayList.add(star);
            canvas.getChildren().add(StarArrayList.get(i).starBody);
        }
//        for(int i=0;i<StarArrayList.size();i++){
//            StarArrayList.get(i).setyCoordinate(-400*i);
//            System.out.println(StarArrayList.get(i).Bbox.getBoundsInLocal());
//        }
        closestObstacle = circularObstacleArrayList.get(closestObsIndex);

        canvas.getChildren().add(ball.ballBody);

        stage.setTitle("Animated Ball");

        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.67),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        velocity+=0.4;
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
                if(ball.getyCoordinate()<= 400 && !cameraMoving){
                    moveCamera(timeline,moveCameraTimeline);
                                            System.out.println(StarArrayList.get(0).Bbox.getBoundsInLocal());
                                            System.out.println();
                                            System.out.println();
//                    for(int i=0;i<StarArrayList.size();i++){
//                        System.out.println(StarArrayList.get(i).Bbox.getBoundsInParent());
//                    }

                }
            }
        };
        timer.scheduleAtFixedRate(task,100,250);
        Timer collisionTimer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {

                if(!closestObstacle.collidesInner(ball.Bbox) && closestObstacle.collidesOuter(ball.Bbox)){
                    if(!closestObstacle.colorMatch(2,ball.isInsideObstacle)){
//                        System.exit(0);
                        System.out.println("Game Over");
                    }
                }
                else if (closestObstacle.collidesInner(ball.Bbox) && closestObstacle.collidesOuter(ball.Bbox)){
                    ball.setInsideObstacle(true);
                }
                else if(!(closestObstacle.collidesInner(ball.Bbox) && closestObstacle.collidesOuter(ball.Bbox))){
                    if(ball.isInsideObstacle){
                        closestObsIndex+=1;
                        closestObstacle = circularObstacleArrayList.get(closestObsIndex);
                    }
                    ball.setInsideObstacle(false);
                }
//                        for(int i=0;i<StarArrayList.size();i++){
//                            if(StarArrayList.get(i).checkCollision(ball.Bbox)){
//                                System.out.println(StarArrayList.get(i).Bbox.getBoundsInParent() );
//                                System.out.println("Scoreee");
//                                StarArrayList.get(i).showAnimation();
//                                StarArrayList.remove(i);
//                            }
//                        }

            }
        };
        collisionTimer.scheduleAtFixedRate(task1,100,10);
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
