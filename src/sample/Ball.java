package sample;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
    protected double xCoordinate,yCoordinate;
    protected Color ballColor;
    int color;
    double distanceTravelled;
    int numRoundsTravelled=0;
    protected Circle ballBody;
    public  Ball(double xCoordinate, double yCoordinate, int color){

        this.color = color;
        switch (color) {
            case 1 -> ballColor = Color.web("fae100");
            case 2 -> ballColor = Color.web("ff0181");
            case 3 -> ballColor = Color.web("32dbf0");
            case 4 -> ballColor = Color.web("900dff");
            default -> ballColor = Color.WHITE;
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
            distanceTravelled+=-1*val;
            //System.out.println(distanceTravelled);
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
        switch (color) {
            case 0 -> ballColor = Color.web("32dbf0");
            case 1 -> ballColor = Color.web("fae100");
            case 2 -> ballColor = Color.web("ff0181");
            case 3 -> ballColor = Color.web("900dff");
        }
        this.ballBody.setFill(ballColor);
    }


}
