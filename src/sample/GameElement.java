package sample;

import java.io.Serializable;

public abstract class GameElement implements Serializable {
    protected double xCoordinate, yCoordinate;

    public double getxCoordinate()  {return this.xCoordinate;}
    public double getyCoordinate()  {return this.yCoordinate;}
    public void setxCoordinate(double val)  {this.xCoordinate += val;}
    public void setyCoordinate(double val)  {this.yCoordinate += val;}
}
