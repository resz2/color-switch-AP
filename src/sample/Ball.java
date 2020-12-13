package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Ball {
    protected double xCoordinate,yCoordinate;
    protected Color ballColor;
    int color;
    protected Circle ballBody;
    public  Ball(double xCoordinate, double yCoordinate, int color){

        this.color = color;
        switch(color){
            case 1:
                ballColor = Color.web("fae100");
                break;
            case 2:
                ballColor = Color.web("ff0181");
                break;
            case 3:
                ballColor = Color.web("32dbf0");
                break;
            case 4:
                ballColor = Color.web("900dff");
                break;
            default:
                ballColor = Color.WHITE;
                break;
        }
        ballBody = new Circle(10, ballColor);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.ballBody.setLayoutY(yCoordinate);
        this.ballBody.setLayoutX(xCoordinate);
        this.ballBody.setTranslateZ(0);
    }
    public double setyCoordinate(double val) {
        if(this.ballBody.getLayoutY()+val<=300){
            return val;
        }
        else{
            this.ballBody.setLayoutY(this.ballBody.getLayoutY()+val);
            this.yCoordinate = this.ballBody.getLayoutY();
            return 0;
        }

    }
    public void setxCoordinate(double val) {
        this.ballBody.setLayoutX(this.ballBody.getLayoutX()+val);
        this.xCoordinate = this.ballBody.getLayoutX();
    }
    public double getyCoordinate(){
        return this.ballBody.getLayoutY();
    }
    public void changeColor(int color){
        this.color = color;
        switch(color){
            case 0:
                ballColor = Color.web("32dbf0");
                break;
            case 1:
                ballColor = Color.web("fae100");
                break;
            case 2:
                ballColor = Color.web("ff0181");
                break;
            case 3:
                ballColor = Color.web("900dff");
                break;
        }
        this.ballBody.setFill(ballColor);
    }


}
