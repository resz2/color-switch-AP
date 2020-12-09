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
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Star extends GameElement {
    private double angle = Math.PI / 5;
    protected transient Rectangle Bbox;
    protected transient ImageView starBody;
    public Star(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.Bbox = new Rectangle(this.xCoordinate-20,this.yCoordinate-20,40,40);
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
        this.Bbox = new Rectangle(this.xCoordinate-20,this.Bbox.getLayoutY()+val,20,20);
        this.starBody.setLayoutY(this.starBody.getLayoutY()+val);
        this.yCoordinate = this.starBody.getLayoutY();

    }
    public boolean checkCollision(Circle b){
        return starBody.getBoundsInParent().intersects(b.getBoundsInParent());
    }
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
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/star.png");
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
            ImageView starBody = new ImageView();
            starBody.setImage(image);
            starBody.setX(this.xCoordinate+offsetX*4);
            starBody.setY(this.yCoordinate+offsetY*4-12.5);
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
//        this.starBody = null;
//        Path smallStar = new Path();
//        ArrayList<Path> smallStars = new ArrayList<>();
//        for(int j=0;j<10;j++){
//            for (int i = 0; i < 10; i++) {
//                double r = (i & 1) == 0 ? 10 : 5;
//                Point2D p = new Point2D(
//                        this.xCoordinate-20 + Math.cos(i * angle) * r,
//                        this.yCoordinate-(j-2)*15 + Math.sin(i * angle) * r);
//                if (i == 0) {
//                    smallStar.getElements().add(new MoveTo(p.getX(), p.getY()));
//                }
//                else {
//                    smallStar.getElements().add(new LineTo(p.getX(), p.getY()));
//                }
//                smallStars.add(smallStar);
//            }
//        }
//        return smallStars;
    }
}
