package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        Stop[] stops = new Stop[] { new Stop(0, Color.DARKGREY), new Stop(1, Color.BLACK)};
        LinearGradient linear = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        Pane canvas = new Pane();
        Scene scene = new Scene(canvas, 800, 800, Color.BLACK);
        scene.setFill(linear);
        obstacle = this.obstacleMaker();
        obstacle2 = this.obstacleMaker();
        obstacle3 = this.obstacleMaker();
        obstacle4 = this.obstacleMaker();
        obstacle5 = this.obstacleMaker();
        obstacle6 = this.obstacleMaker();
        obstacle7 = this.obstacleMaker();
        Circle ball = new Circle(15, Color.YELLOW);
        ball.relocate(385, 770);

        obstacle.setLayoutX(-100) ;
                obstacle.setLayoutY(0);
                obstacle2.setLayoutX(100);
                obstacle2.setLayoutY(0);
                obstacle3.setLayoutY(-500);
        obstacle4.setLayoutY(-1000);
        obstacle5.setLayoutY(-1500);
        obstacle6.setLayoutY(-2000);
        obstacle7.setLayoutY(-2500);


        canvas.getChildren().add(ball);
        canvas.getChildren().add(obstacle);
        canvas.getChildren().add(obstacle2);
        canvas.getChildren().add(obstacle3);
        canvas.getChildren().add(obstacle4);
        canvas.getChildren().add(obstacle5);canvas.getChildren().add(obstacle6);
        canvas.getChildren().add(obstacle7);

        stage.setTitle("Animated Ball");
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {
                    double velocity = 4; //Step on y
                    @Override
                    public void handle(ActionEvent t) {
                        ball.setLayoutY(ball.getLayoutY() + velocity);
//                        camera.setOnBall(ball);
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        Timeline moveTimeline = new Timeline(new KeyFrame(Duration.millis(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        ball.setLayoutY(ball.getLayoutY() - 1);
                    }
                }));
        moveTimeline.setCycleCount(60);
        Timeline moveCameraTimeline = new Timeline(new KeyFrame(Duration.millis(4),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        obstacle.setLayoutY(obstacle.getLayoutY()+1);
                        obstacle2.setLayoutY(obstacle2.getLayoutY()+1);
                        obstacle3.setLayoutY(obstacle3.getLayoutY()+1);
                        obstacle4.setLayoutY(obstacle4.getLayoutY()+1);
                        obstacle5.setLayoutY(obstacle5.getLayoutY()+1);
                        obstacle6.setLayoutY(obstacle6.getLayoutY()+1);
                        obstacle7.setLayoutY(obstacle7.getLayoutY()+1);
                        ball.setLayoutY(ball.getLayoutY()+1);
                    }
                }));
        moveCameraTimeline.setCycleCount(100);
        Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(50),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        obstacle.setRotate(obstacle.getRotate()+5);
                        obstacle2.setRotate(obstacle2.getRotate()-4);
                        obstacle3.setRotate(obstacle3.getRotate()+5);
                        obstacle4.setRotate(obstacle4.getRotate()+5);
                        obstacle5.setRotate(obstacle5.getRotate()+5);
                        obstacle6.setRotate(obstacle6.getRotate()+5);
                        obstacle7.setRotate(obstacle7.getRotate()+5);

                    }
                }));
        rotateTimeline.setCycleCount(Timeline.INDEFINITE);
        rotateTimeline.play();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE) {

                try {
                    this.moveBall(ball,timeline, moveTimeline);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(ball.getLayoutY()<= 450){
                    System.out.println("Kaboom baby");
                    moveCamera(timeline,moveCameraTimeline);
                }
            }
        };
        timer.scheduleAtFixedRate(task,100,500);
    }
    public void moveBall(Circle ball, Timeline gravityTimeline, Timeline moveTimeline) throws InterruptedException{
        gravityTimeline.pause();
        moveTimeline.play();
        moveTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                gravityTimeline.play();
            }
        });
    }
    public void moveCamera(Timeline gravityTimeline, Timeline moveCameraTimeline) {
        gravityTimeline.pause();
        moveCameraTimeline.play();
        moveCameraTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gravityTimeline.play();
            }
        });
    }
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
