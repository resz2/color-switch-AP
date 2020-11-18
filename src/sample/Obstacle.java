package sample;

import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

abstract class Obstacle {
    protected Group obstacle;
    protected int angleOfRotation;
    protected Rectangle BboxOuter, BboxInner;
    int rotationsCount=0;
    protected double distInner, distOuter, yCoordinate, xCoordinate;
    public Obstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        this.distInner = distInner;
        this.distOuter = distOuter;
        this.yCoordinate = yCoordinate;
        this.xCoordinate = xCoordinate;
        this.BboxInner = new Rectangle(this.xCoordinate-this.distInner,this.yCoordinate-this.distInner,this.distInner*2,this.distInner*2);
        this.BboxOuter = new Rectangle(this.xCoordinate-this.distOuter,this.yCoordinate-this.distOuter,this.distOuter*2,this.distOuter*2);
    }
    public boolean collidesInner(Rectangle b){
        return b.getBoundsInParent().intersects(this.BboxInner.getBoundsInParent());

    }
    public boolean collidesOuter(Rectangle b) {

        return b.getBoundsInParent().intersects(this.BboxOuter.getBoundsInParent());
    }
    public boolean colorMatch (int color, boolean inside){
        int flip = inside ? 180:0;
        boolean match=false;
        switch(color){
            case 0:
                if(this.getAngleOfRotation()>(0-flip)+(this.rotationsCount)*360 && this.getAngleOfRotation()<(90-flip)+(this.rotationsCount)*360){
                    match=true;
                }
                break;
            case 1:
                if(this.getAngleOfRotation()>(90-flip)+(this.rotationsCount)*360 && this.getAngleOfRotation()<(180-flip)+(this.rotationsCount)*360){
                    match=true;
                }
                break;
            case 2:
                if(this.getAngleOfRotation()>(180-flip)+(this.rotationsCount)*360 && this.getAngleOfRotation()<(270-flip)+(this.rotationsCount)*360){
                    match=true;
                }
                break;
            case 3:
                if(this.getAngleOfRotation()>(270-flip)+(this.rotationsCount)*360 && this.getAngleOfRotation()<(360-flip)+(this.rotationsCount)*360){
                    match=true;
                }
                break;
            default:
                break;
        }
        return match;
    }
    public abstract void create();
    public double getAngleOfRotation(){
        return this.obstacle.getRotate();
    }
    public void setAngleOfRotation(double rotateBy){
        this.obstacle.setRotate(this.obstacle.getRotate()+rotateBy);
        if(this.getAngleOfRotation()%360==0){
            this.rotationsCount+=1;
        }
    }
    public void setyCoordinate(double val){
        this.obstacle.setLayoutY(this.obstacle.getLayoutY()+val);
        this.yCoordinate = this.obstacle.getLayoutY();
        this.BboxInner = new Rectangle(this.xCoordinate-this.distInner,this.yCoordinate-this.distInner,this.distInner*2,this.distInner*2);
        this.BboxOuter = new Rectangle(this.xCoordinate-this.distOuter,this.yCoordinate-this.distOuter,this.distOuter*2,this.distOuter*2);
    }
}

class CircularObstacle extends Obstacle{
    public CircularObstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        super(distInner,distOuter,yCoordinate, xCoordinate);
    }
    @Override
    public boolean collidesInner(Rectangle b) {
        return super.collidesInner(b);
    }

    @Override
    public boolean collidesOuter(Rectangle b) {
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