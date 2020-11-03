package sample;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    Button button;
    boolean started=false;
    @Override
    public void start(Stage stage) throws Exception, InterruptedException{
        Pane canvas = new Pane();
        Scene scene = new Scene(canvas, 800, 800, Color.ALICEBLUE);
        Circle ball = new Circle(15, Color.CADETBLUE);
        ball.relocate(350, 770);
        canvas.getChildren().add(ball);
        Group obs = new Group();
        SVGPath slice1 = new SVGPath();

        String slice1String = "M396.075,163.653L396.075,163.653l27.648-16.128C363.903,44.761,234.966,5.503,128.027,57.494V18.15h-32v96h96v-32 h-40.88C241.665,43.753,346.612,78.676,396.075,163.653z";
        slice1.setContent(slice1String);
        slice1.setScaleX(0.5);
        slice1.setScaleY(0.5);
        slice1.setScaleZ(0.5);
        slice1.relocate(200,225);
        slice1.setFill(Color.YELLOW);
        obs.getChildren().add(slice1);

        SVGPath slice2 = new SVGPath();
        String slice2String = "M104.027,412.965C29.84,352.714,10.314,247.543,57.931,164.678l-27.744-16c-54.311,94.575-33.761,214.399,48.96,285.472 h-39.12v32h96v-96h-32V412.965z";
        slice2.setContent(slice2String);
        slice2.setScaleX(0.5);
        slice2.setScaleY(0.5);
        slice2.setScaleZ(0.5);
        slice2.relocate(200,250);
        slice2.setFill(Color.GREEN);
        obs.getChildren().add(slice2);

        SVGPath slice3 = new SVGPath();
        String slice3String = "M439.451,219.238l-67.872,67.888l22.624,22.624l27.2-27.2c-10.669,99.13-94.202,174.364-193.904,174.64v32 c118.79-0.312,217.334-91.989,226.208-210.448l31.008,31.008l22.624-22.624L439.451,219.238z";
        slice3.setContent(slice3String);
        slice3.setScaleX(0.5);
        slice3.setScaleY(0.5);
        slice3.setScaleZ(0.5);
        slice3.setFill(Color.RED);
        slice3.relocate(275,300);
        obs.getChildren().add(slice3);
        canvas.getChildren().add(obs);
        stage.setTitle("Animated Ball");
        stage.setScene(scene);
        stage.show();

        //scene.getStylesheets().add("./styles.css");

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {
                    double velocity = 3; //Step on y
                    @Override
                    public void handle(ActionEvent t) {
                        ball.setLayoutY(ball.getLayoutY() + velocity);
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
        Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(50),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        obs.setRotate(obs.getRotate()+5);
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

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
