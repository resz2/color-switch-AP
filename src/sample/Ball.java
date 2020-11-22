package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Ball {
    protected double xCoordinate,yCoordinate;
    boolean isInsideObstacle;
    int color;
    protected Circle ballBody;
    protected Rectangle Bbox;
    public  Ball(double xCoordinate, double yCoordinate, int color){
        Color ballColor;
        switch(color){
            case 1:
                ballColor = Color.GREEN;
                break;
            case 2:
                ballColor = Color.RED;
                break;
            case 3:
                ballColor = Color.YELLOW;
                break;
            case 4:
                ballColor = Color.BLUE;
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
        this.Bbox = new Rectangle(this.xCoordinate-15,this.yCoordinate-15,20,20);
    }
    public void setyCoordinate(double val) {
        this.ballBody.setLayoutY(this.ballBody.getLayoutY()+val);
        this.yCoordinate = this.ballBody.getLayoutY();
        this.Bbox = new Rectangle(this.xCoordinate-15,this.yCoordinate-15,20,20);
    }
    public double getyCoordinate(){
        return this.ballBody.getLayoutY();
    }
    public void setInsideObstacle(boolean val){
        this.isInsideObstacle = val;
    }
}
