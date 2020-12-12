package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Ball extends GameElement {
    protected transient Color ballColor;
    private int color;
    protected transient Circle ballBody;

    public Ball(double xCoordinate, double yCoordinate, int color)  {
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
    @Override
    public void setyCoordinate(double val) {
        this.ballBody.setLayoutY(this.ballBody.getLayoutY()+val);
        this.yCoordinate = this.ballBody.getLayoutY();
    }
    @Override
    public double getyCoordinate(){
        return this.ballBody.getLayoutY();
    }

    public int getColor()   { return this.color; }

    public void changeColor(int color){
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
