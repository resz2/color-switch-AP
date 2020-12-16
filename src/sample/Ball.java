package sample;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends GameElement {
    private double distanceTravelled;
    private int color, numRoundsTravelled = 0;
    private transient Color ballColor;
    private transient Circle ballBody;
    public Ball(double xCoordinate, double yCoordinate, int color){

        this.color = color;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.create();
    }

    public void create() {
        switch (color) {
            case 1-> ballColor = Color.web("fae100");
            case 2-> ballColor = Color.web("ff0181");
            case 3-> ballColor = Color.web("32dbf0");
            case 4-> ballColor = Color.web("900dff");
        }
        ballBody = new Circle(10, ballColor);
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
            this.yCoordinate +=val;
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

    public Circle getBallBody() { return this.ballBody; }
    public int getColor()   { return this.color; }

    public void changeColor(int color){
        this.color = color;
        switch (color) {
            case 0: ballColor = Color.web("32dbf0");
                    break;
            case 1: ballColor = Color.web("fae100");
                    break;
            case 2: ballColor = Color.web("ff0181");
                    break;
            case 3: ballColor = Color.web("900dff");
                    break;
        }
        this.ballBody.setFill(ballColor);
    }


    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(int distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public int getNumRoundsTravelled() {
        return numRoundsTravelled;
    }

    public void setNumRoundsTravelled(int numRoundsTravelled) {
        this.numRoundsTravelled = numRoundsTravelled;
    }
}