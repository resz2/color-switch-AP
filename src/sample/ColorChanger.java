package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class ColorChanger {
    protected double xCoordinate,yCoordinate;
    protected Group colorChangerBody;
    public ColorChanger(double xCoordinate, double yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        colorChangerBody = create();
    }
    public void setyCoordinate(double val) {
        this.colorChangerBody.setLayoutY(this.colorChangerBody.getLayoutY()+val);
        this.yCoordinate = this.colorChangerBody.getLayoutY();
    }
    public double getyCoordinate(){
        return this.colorChangerBody.getLayoutY();
    }
    private Group create(){
        Group body = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate);
        moveTo.setY(this.yCoordinate);

        HLineTo line1 = new HLineTo();
        line1.setX(this.xCoordinate + 20);


        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(this.xCoordinate);
        arcToInner.setY(this.yCoordinate-20);
        arcToInner.setRadiusX(25);
        arcToInner.setRadiusY(25);


        VLineTo line2 = new VLineTo();
        line2.setY(this.yCoordinate);
        path.getElements().add(moveTo);
        path.getElements().add(line1);
        path.getElements().add(arcToInner);
        path.getElements().add(line2);

        body.getChildren().add(path);

        Path path2 = new Path();
        path2.setFill(Color.web("#900dff"));
        path2.setFillRule(FillRule.EVEN_ODD);
        path2.setLayoutY(path.getLayoutY()+20);
        path2.getElements().add(moveTo);
        path2.getElements().add(line1);
        path2.getElements().add(arcToInner);
        path2.getElements().add(line2);
        path2.setScaleX(1);
        path2.setScaleY(-1);
        body.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.web("#fae100"));
        path3.setFillRule(FillRule.EVEN_ODD);
        path3.setLayoutX(path.getLayoutY()-20);
        path3.getElements().add(moveTo);
        path3.getElements().add(line1);
        path3.getElements().add(arcToInner);
        path3.getElements().add(line2);
        path3.setScaleX(-1);
        path3.setScaleY(1);
        body.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.web("#32dbf0"));
        path4.setFillRule(FillRule.EVEN_ODD);
        path4.setLayoutX(path.getLayoutX()-20);
        path4.setLayoutY(path.getLayoutY()+20);
        path4.getElements().add(moveTo);
        path4.getElements().add(line1);
        path4.getElements().add(arcToInner);
        path4.getElements().add(line2);
        path4.setScaleX(-1);
        path4.setScaleY(-1);
        body.getChildren().add(path4);
        return body;
    }
    public boolean checkCollision(Circle b){
        return colorChangerBody.getBoundsInParent().intersects(b.getBoundsInParent());
    }
    public int showAnimation(Pane canvas){
        Timeline fadeTimeline = new Timeline(new KeyFrame(Duration.millis(2),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        colorChangerBody.setOpacity(colorChangerBody.getOpacity()-0.01);
                    }
                }));
        fadeTimeline.setCycleCount(100);
        fadeTimeline.play();
        Random randomGen = new Random();
        int newColor = randomGen.nextInt(4);
        ArrayList<ImageView> smallStars = new ArrayList<>();
        InputStream stream = null;
        try {
            switch(newColor){
                case 0:
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\shutter_blue.png");
                    break;
                case 1:
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\shutter_yellow.png");
                    break;
                case 2:
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\shutter_pink.png");
                    break;
                case 3:
                    stream = new FileInputStream("C:\\Users\\SAATVIK\\Desktop\\Semester3\\AP\\ColorSwitch\\color-switch-AP\\src\\assets\\shutter_purple.png");
                    break;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        ImageView confettiBody = new ImageView();
        confettiBody.setImage(image);
        confettiBody.setPreserveRatio(true);
        confettiBody.setScaleX(4);
        confettiBody.setScaleY(4);
        confettiBody.setPreserveRatio(true);
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    canvas.getChildren().add(confettiBody);
                }
            });

        Timeline enlargeTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        confettiBody.setScaleX(confettiBody.getScaleX()-0.04);
                        confettiBody.setScaleY(confettiBody.getScaleY()-0.04);

                    }
                }));
        enlargeTimeline.setCycleCount(100);
        enlargeTimeline.play();
        enlargeTimeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                Timeline enlargeTimeline = new Timeline(new KeyFrame(Duration.millis(3),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                confettiBody.setScaleX(confettiBody.getScaleX()+0.04);
                                confettiBody.setScaleY(confettiBody.getScaleY()+0.04);

                            }
                        }));
                enlargeTimeline.setCycleCount(100);
                enlargeTimeline.play();
                enlargeTimeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            canvas.getChildren().remove(confettiBody);
                        }
                    });
                    }
                });

            }
        });
        return newColor;
    }

}
