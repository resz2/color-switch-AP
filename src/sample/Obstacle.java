package sample;

import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

abstract class Obstacle {
    protected Group obstacle;
    protected int angleOfRotation;
    protected BoundingBox BboxOuter, BboxInner;
    protected double distInner, distOuter, yCoordinate, xCoordinate;
    public Obstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        this.distInner = distInner;
        this.distOuter = distOuter;
        this.yCoordinate = yCoordinate;
        this.xCoordinate = xCoordinate;
        this.BboxInner = new BoundingBox(this.distInner,this.yCoordinate-this.distInner,2*this.distInner,2*this.distInner);
        this.BboxOuter = new BoundingBox(this.distOuter,this.yCoordinate-this.distOuter,2*this.distOuter,2*this.distOuter);
    }
    public boolean collidesInner(BoundingBox b){
        return this.BboxInner.intersects(b.getMinX(),b.getMinY(),b.getWidth(),b.getHeight());
    }
    public boolean collidesOuter(BoundingBox b) {
        System.out.println(b.getMinY());
        return this.BboxOuter.intersects(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    }
    public abstract void create();
    public int getAngleOfRotation(){
        return this.angleOfRotation;
    }
    public void setAngleOfRotation(double rotateBy){
        this.obstacle.setRotate(this.obstacle.getRotate()+rotateBy);
    }
    public void setyCoordinate(double val){
        this.obstacle.setLayoutY(this.obstacle.getLayoutY()+val);

    }
}

class CircularObstacle extends Obstacle{
    public CircularObstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        super(distInner,distOuter,yCoordinate, xCoordinate);
        this.BboxInner = new BoundingBox(this.distInner,this.yCoordinate-this.distInner,2*this.distInner,2*this.distInner);
        this.BboxInner = new BoundingBox(this.distOuter,this.yCoordinate-this.distOuter,2*this.distOuter,2*this.distOuter);
    }
    @Override
    public boolean collidesInner(BoundingBox b) {
        return super.collidesInner(b);
    }

    @Override
    public boolean collidesOuter(BoundingBox b) {
        return super.collidesOuter(b);
    }
    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.RED);
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.yCoordinate + this.distInner);
        moveTo.setY(this.yCoordinate);
        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(this.yCoordinate);
        arcToInner.setY(this.yCoordinate-this.distInner);
        arcToInner.setRadiusX(this.distInner);
        arcToInner.setRadiusY(this.distInner);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(this.yCoordinate + this.distInner);
        moveTo2.setY(this.yCoordinate);

        HLineTo hLineToRightLeg = new HLineTo();
        hLineToRightLeg.setX(this.yCoordinate + this.distOuter);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(this.yCoordinate);
        arcTo.setY(this.yCoordinate-this.distOuter);
        arcTo.setRadiusX(this.distOuter);
        arcTo.setRadiusY(this.distOuter);

        VLineTo hLineToLeftLeg = new VLineTo();
        hLineToLeftLeg.setY(this.yCoordinate-this.distInner);
        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);
        path.getElements().add(moveTo2);
        path.getElements().add(hLineToRightLeg);
        path.getElements().add(arcTo);
        path.getElements().add(hLineToLeftLeg);

        obstacle.getChildren().add(path);
        Path path2 = new Path();
        path2.setFill(Color.GREEN);
        path2.setFillRule(FillRule.EVEN_ODD);
        path2.setLayoutY(path.getLayoutY()+this.distOuter);
        path2.getElements().add(moveTo);
        path2.getElements().add(arcToInner);
        path2.getElements().add(moveTo2);
        path2.getElements().add(hLineToRightLeg);
        path2.getElements().add(arcTo);
        path2.getElements().add(hLineToLeftLeg);
        path2.setScaleX(1);
        path2.setScaleY(-1);
        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.YELLOW);
        path3.setFillRule(FillRule.EVEN_ODD);
        path3.setLayoutX(path.getLayoutY()-this.distOuter);
        path3.getElements().add(moveTo);
        path3.getElements().add(arcToInner);
        path3.getElements().add(moveTo2);
        path3.getElements().add(hLineToRightLeg);
        path3.getElements().add(arcTo);
        path3.getElements().add(hLineToLeftLeg);
        path3.setScaleX(-1);
        path3.setScaleY(1);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.BLUE);
        path4.setFillRule(FillRule.EVEN_ODD);
        path4.setLayoutX(path.getLayoutX()-this.distOuter);
        path4.setLayoutY(path.getLayoutY()+this.distOuter);
        path4.getElements().add(moveTo);
        path4.getElements().add(arcToInner);
        path4.getElements().add(moveTo2);
        path4.getElements().add(hLineToRightLeg);
        path4.getElements().add(arcTo);
        path4.getElements().add(hLineToLeftLeg);
        path4.setScaleX(-1);
        path4.setScaleY(-1);
        obstacle.getChildren().add(path4);
        this.obstacle = obstacle;
    }
}