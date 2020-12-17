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
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Star extends GameElement {
    protected transient ImageView starBody;

    public Star(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.create();
    }

    public void create() {
        InputStream stream = this.getClass().getResourceAsStream("/star.png");
        Image image = new Image(stream);
        starBody = new ImageView();
        starBody.setImage(image);
        starBody.setX(this.xCoordinate-20);
        starBody.setY(this.yCoordinate-20);
        starBody.setFitWidth(40);
        starBody.setPreserveRatio(true);
    }

    public void setyCoordinate(double val){
        this.starBody.setLayoutY(this.starBody.getLayoutY()+val);
        this.yCoordinate +=val;
    }

    public boolean checkCollision(SVGPath b){
        return starBody.getBoundsInParent().intersects(b.getBoundsInParent());
    }



    // ANIMATION

    public void showAnimation(Pane canvas){
        Timeline fadeTimeline = new Timeline(new KeyFrame(Duration.millis(2),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        starBody.setOpacity(starBody.getOpacity()-0.01);
                    }
                }));
        fadeTimeline.setCycleCount(100);
        fadeTimeline.play();
        ArrayList<ImageView> smallStars = new ArrayList<>();
        InputStream stream = this.getClass().getResourceAsStream("/star.png");

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
            ImageView starBody = new ImageView();
            starBody.setImage(image);
            starBody.setX(this.xCoordinate+offsetX*4);
            starBody.setY(300+offsetY*4-12.5);
            starBody.setFitWidth(10);
            starBody.setOpacity(0);
            starBody.setPreserveRatio(true);
            smallStars.add(starBody);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    canvas.getChildren().add(starBody);
                }
            });

        }
        Timeline enlargeTimeline = new Timeline(new KeyFrame(Duration.millis(4),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        double opacityOffset=0.02;
                        for(int i=0;i<smallStars.size();i++){
                            smallStars.get(i).setOpacity(smallStars.get(i).getOpacity()+opacityOffset);
                            smallStars.get(i).setY(smallStars.get(i).getY()-0.25);
                        }
                    }
                }));
        enlargeTimeline.setCycleCount(100);
        enlargeTimeline.play();
        enlargeTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                for(int i=0;i<smallStars.size();i++){
                    ImageView starBody = smallStars.get(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            canvas.getChildren().remove(starBody);
                        }
                    });
                }
            }
        });
    }
}
