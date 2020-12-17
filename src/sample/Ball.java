package sample;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

public class Ball extends GameElement {
    private double distanceTravelled;
    private int color, numRoundsTravelled = 0,type;
    private transient Color ballColor;
    private transient SVGPath ballBody;
    public Ball(double xCoordinate, double yCoordinate, int color, int type){
        this.color = color;
        this.type=type;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.create();
    }
    public int getType(){
        return this.type;
    }
    public void setType(int type){
        this.type = type;
    }
    public void create() {
        System.out.println(color);
        String path="";
        switch(type){
            case 0 -> path ="M 15 0 C 6.761719 0 0 6.761719 0 15 C 0 23.238281 6.761719 30 15 30 C 23.238281 30 30 23.238281 30 15 C 30 6.761719 23.238281 0 15 0 Z M 15 0 ";
            case 1 -> path = "M 24.828125 30 L 5.171875 30 C 2.316406 30 0 27.683594 0 24.828125 L 0 5.171875 C 0 2.316406 2.316406 0 5.171875 0 L 24.828125 0 C 27.683594 0 30 2.316406 30 5.171875 L 30 24.828125 C 30 27.683594 27.683594 30 24.828125 30 Z M 24.828125 30 ";
            case 2 -> path = "M 0 15 L 7.5 2.007812 L 22.5 2.007812 L 30 15 L 22.5 27.992188 L 7.5 27.992188 Z M 0 15";
            case 3 -> path = "M 29.660156 11.5 L 21.910156 11.5 C 21.515625 11.5 21.207031 11.828125 21.089844 12.203125 C 20.789062 13.167969 19.890625 13.863281 18.832031 13.863281 C 17.527344 13.863281 16.46875 12.804688 16.46875 11.5 C 16.46875 10.773438 16.796875 10.125 17.3125 9.691406 C 17.613281 9.4375 17.824219 9.015625 17.699219 8.640625 L 15.21875 1.011719 C 15.097656 0.636719 14.902344 0.636719 14.777344 1.011719 L 12.324219 8.570312 C 12.199219 8.945312 12.386719 9.414062 12.644531 9.707031 C 13.011719 10.121094 13.234375 10.664062 13.234375 11.257812 C 13.234375 12.5625 12.175781 13.621094 10.871094 13.621094 C 9.894531 13.621094 9.066406 13.03125 8.707031 12.1875 C 8.554688 11.824219 8.210938 11.5 7.816406 11.5 L 0.339844 11.5 C -0.0546875 11.5 -0.113281 11.6875 0.203125 11.921875 L 6.832031 16.734375 C 7.152344 16.964844 7.640625 16.925781 7.996094 16.753906 C 8.300781 16.605469 8.644531 16.523438 9.007812 16.523438 C 10.3125 16.523438 11.371094 17.582031 11.371094 18.886719 C 11.371094 20.191406 10.3125 21.25 9.007812 21.25 C 8.875 21.25 8.746094 21.238281 8.621094 21.214844 C 8.410156 21.171875 8.148438 21.417969 8.027344 21.792969 L 5.71875 28.890625 C 5.597656 29.265625 5.757812 29.382812 6.078125 29.148438 L 12.429688 24.535156 C 12.75 24.304688 12.835938 23.863281 12.730469 23.511719 C 12.667969 23.304688 12.636719 23.082031 12.636719 22.855469 C 12.636719 21.550781 13.695312 20.492188 15 20.492188 C 16.304688 20.492188 17.363281 21.550781 17.363281 22.855469 C 17.363281 23.082031 17.332031 23.304688 17.269531 23.511719 C 17.164062 23.863281 17.25 24.300781 17.570312 24.535156 L 23.921875 29.148438 C 24.242188 29.382812 24.402344 29.265625 24.28125 28.890625 L 21.992188 21.851562 C 21.871094 21.476562 21.644531 21.207031 21.484375 21.230469 C 21.390625 21.242188 21.296875 21.25 21.195312 21.25 C 19.890625 21.25 18.835938 20.191406 18.835938 18.886719 C 18.835938 17.582031 19.890625 16.523438 21.195312 16.523438 C 21.515625 16.523438 21.816406 16.589844 22.09375 16.707031 C 22.457031 16.859375 22.957031 16.886719 23.277344 16.65625 L 29.796875 11.921875 C 30.113281 11.6875 30.054688 11.5 29.660156 11.5 Z M 15 17.972656 C 13.859375 17.972656 12.9375 17.046875 12.9375 15.910156 C 12.9375 14.769531 13.859375 13.84375 15 13.84375 C 16.136719 13.84375 17.0625 14.769531 17.0625 15.910156 C 17.0625 17.046875 16.136719 17.972656 15 17.972656 Z M 15 17.972656";
            case 4 -> path = "M 6.710938 29.382812 C 6.382812 29.382812 6.058594 29.28125 5.777344 29.078125 C 5.261719 28.703125 5.019531 28.054688 5.160156 27.433594 L 7.089844 18.933594 L 0.546875 13.195312 C 0.0664062 12.777344 -0.121094 12.113281 0.078125 11.503906 C 0.277344 10.894531 0.8125 10.464844 1.449219 10.40625 L 10.105469 9.621094 L 13.527344 1.613281 C 13.78125 1.023438 14.355469 0.644531 14.992188 0.644531 C 15.632812 0.644531 16.207031 1.023438 16.460938 1.609375 L 19.882812 9.621094 L 28.539062 10.40625 C 29.175781 10.464844 29.710938 10.894531 29.910156 11.503906 C 30.105469 12.109375 29.925781 12.777344 29.445312 13.195312 L 22.902344 18.933594 L 24.832031 27.429688 C 24.972656 28.054688 24.730469 28.703125 24.210938 29.078125 C 23.695312 29.453125 23.003906 29.480469 22.457031 29.152344 L 14.992188 24.691406 L 7.527344 29.15625 C 7.277344 29.304688 6.996094 29.382812 6.710938 29.382812 Z M 14.992188 22.769531 C 15.277344 22.769531 15.558594 22.847656 15.8125 22.996094 L 22.855469 27.210938 L 21.035156 19.1875 C 20.90625 18.617188 21.097656 18.023438 21.539062 17.636719 L 27.71875 12.21875 L 19.542969 11.476562 C 18.957031 11.421875 18.449219 11.050781 18.21875 10.511719 L 14.992188 2.953125 L 11.765625 10.511719 C 11.535156 11.050781 11.03125 11.417969 10.441406 11.472656 L 2.269531 12.214844 L 8.445312 17.632812 C 8.890625 18.019531 9.082031 18.617188 8.953125 19.1875 L 7.132812 27.207031 L 14.175781 22.996094 C 14.429688 22.847656 14.710938 22.769531 14.992188 22.769531 Z M 10.039062 9.777344 Z M 19.945312 9.773438 L 19.949219 9.773438 C 19.949219 9.773438 19.949219 9.773438 19.945312 9.773438 Z M 19.945312 9.773438 ";
        }
        switch (color) {
            case 0-> ballColor = Color.web("32dbf0");
            case 1-> ballColor = Color.web("fae100");
            case 2-> ballColor = Color.web("ff0181");
            case 3-> ballColor = Color.web("900dff");}
        ballBody = new SVGPath();
        ballBody.setContent(path);
        ballBody.setFill(ballColor);
        if(type==4){
            ballBody.setStrokeWidth(10);
        }
        this.ballBody.setLayoutY(yCoordinate-15);
        this.ballBody.setLayoutX(xCoordinate-15);
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

    public SVGPath getBallBody() { return this.ballBody; }
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