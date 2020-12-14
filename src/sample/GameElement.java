package sample;

import java.io.Serializable;

public abstract class GameElement implements Serializable {
    protected double xCoordinate, yCoordinate;

    public double getStaticX()  {return this.xCoordinate;}
    public double getStaticY()  {return this.yCoordinate;}
    public void setStaticX(double val)  {this.xCoordinate = val;}
    public void setStaticY(double val)  {this.yCoordinate = val;}
}
