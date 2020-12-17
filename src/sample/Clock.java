package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Clock extends GameElement {
    protected transient ImageView clockBody;

    public Clock(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.create();
    }
    public void create() {
        InputStream stream = this.getClass().getResourceAsStream("/fast.png");
        Image image = new Image(stream);
        clockBody = new ImageView();
        clockBody.setImage(image);
        clockBody.setX(this.xCoordinate-25);
        clockBody.setY(this.yCoordinate-25);
        clockBody.setFitWidth(50);
        clockBody.setPreserveRatio(true);
    }
    public void setyCoordinate(double val){

        this.clockBody.setLayoutY(this.clockBody.getLayoutY()+val);
        this.yCoordinate +=val;

    }
    public boolean checkCollision(Circle b){
        return clockBody.getBoundsInParent().intersects(b.getBoundsInParent());
    }
    public void showAnimation(Pane canvas){
        Timeline fadeTimeline = new Timeline(new KeyFrame(Duration.millis(2),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        clockBody.setOpacity(clockBody.getOpacity()-0.01);
                    }
                }));
        fadeTimeline.setCycleCount(100);
        fadeTimeline.play();
        ArrayList<ImageView> smallClocks = new ArrayList<>();
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/fast.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Random randomGen = new Random();
        Image image = new Image(stream);
        int offsetX,offsetY;
        for(int i=0;i<10;i++){
            if(i%4==0){
                offsetX = randomGen.nextInt(10);
                offsetY = randomGen.nextInt(10);
            }
            else if (i%4==1){
                offsetX = -1*randomGen.nextInt(10);
                offsetY = randomGen.nextInt(10);
            }
            else if (i%4==2){
                offsetX = -1*randomGen.nextInt(10);
                offsetY = -1*randomGen.nextInt(10);
            }
            else{
                offsetX = randomGen.nextInt(10);
                offsetY = -1*randomGen.nextInt(10);
            }
            ImageView clockBody = new ImageView();
            clockBody.setImage(image);
            clockBody.setX(this.xCoordinate+offsetX*3);
            clockBody.setY(this.yCoordinate+offsetY*4-12.5);
            clockBody.setFitWidth(10);
            clockBody.setOpacity(0);
            clockBody.setPreserveRatio(true);
            smallClocks.add(clockBody);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    canvas.getChildren().add(clockBody);
                }
            });

        }
        Timeline enlargeTimeline = new Timeline(new KeyFrame(Duration.millis(4),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        double opacityOffset=0.02;
                        for(int i=0;i<smallClocks.size();i++){
                            smallClocks.get(i).setOpacity(smallClocks.get(i).getOpacity()+opacityOffset);
                            smallClocks.get(i).setY(smallClocks.get(i).getY()-0.25);
                        }
                    }
                }));
        enlargeTimeline.setCycleCount(100);
        enlargeTimeline.play();
        enlargeTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                for(int i=0;i<smallClocks.size();i++){
                    ImageView clockBody = smallClocks.get(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            canvas.getChildren().remove(clockBody);
                        }
                    });
                }
            }
        });
    }
}