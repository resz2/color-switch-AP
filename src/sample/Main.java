package sample;

import javafx.animation.KeyFrame;
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
        Scene scene = new Scene(canvas, 500, 500, Color.ALICEBLUE);
        Circle ball = new Circle(10, Color.CADETBLUE);
        ball.relocate(250, 400);

        canvas.getChildren().add(ball);

        stage.setTitle("Animated Ball");
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {
                    double velocity = 2; //Step on y

                    @Override
                    public void handle(ActionEvent t) {
                        ball.setLayoutY(ball.getLayoutY() + velocity);
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.SPACE) {

                try {
                    this.moveBall(ball,timeline);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void moveBall(Circle ball, Timeline gravityTimeline) throws InterruptedException{
        gravityTimeline.pause();
        ball.setLayoutY(ball.getLayoutY()-30);
        Thread.sleep(50);
        gravityTimeline.play();

    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
