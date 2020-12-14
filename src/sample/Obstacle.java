package sample;

import javafx.geometry.BoundingBox;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

abstract class Obstacle {
    protected Group obstacle;
    protected boolean inside=false;
    protected Path pink,blue,purple,yellow;
    int rotationsCount=0;
    protected double distInner, distOuter, yCoordinate, xCoordinate;
    public Obstacle(){

    }
    public Obstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        this.distInner = distInner;
        this.distOuter = distOuter;
        this.yCoordinate = yCoordinate;
        this.xCoordinate = xCoordinate;
    }
    public int collides(Circle ball, int color){
        if(obstacle.getBoundsInParent().intersects(ball.getBoundsInParent())){
            inside=true;
        }
        if(inside && !obstacle.getBoundsInParent().intersects(ball.getBoundsInParent())){
            inside =false;
            return 3;
        }
        switch (color) {
            case 1 -> {
                if (Shape.intersect(ball, pink).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, purple).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, blue).getBoundsInLocal().getWidth() != -1) {
                    return 0;
                } else if (Shape.intersect(ball, yellow).getBoundsInLocal().getWidth() != -1) {

                    return 1;
                }
                return 2;
            }
            case 2 -> {
                if (Shape.intersect(ball, yellow).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, purple).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, blue).getBoundsInLocal().getWidth() != -1) {
                    return 0;
                } else if (Shape.intersect(ball, pink).getBoundsInLocal().getWidth() != -1) {

                    return 1;
                }
                return 2;
            }
            case 0 -> {
                if (Shape.intersect(ball, yellow).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, purple).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, pink).getBoundsInLocal().getWidth() != -1) {
                    return 0;
                } else if (Shape.intersect(ball, blue).getBoundsInLocal().getWidth() != -1) {

                    return 1;
                }
                return 2;
            }
            case 3 -> {
                if (Shape.intersect(ball, yellow).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, pink).getBoundsInLocal().getWidth() != -1 || Shape.intersect(ball, blue).getBoundsInLocal().getWidth() != -1) {
                    return 0;
                } else if (Shape.intersect(ball, purple).getBoundsInLocal().getWidth() != -1) {
                    return 1;
                }
                return 2;
            }
        }
        return 2;
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
    }
}

class CircularObstacle extends Obstacle{

    public CircularObstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        super(distInner,distOuter,yCoordinate, xCoordinate);
    }
    @Override
    public int collides(Circle ball,int color) {
       return super.collides(ball,color);
    }

    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate + this.distInner);
        moveTo.setY(this.yCoordinate);
        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(this.xCoordinate);
        arcToInner.setY(this.yCoordinate-this.distInner);
        arcToInner.setRadiusX(this.distInner);
        arcToInner.setRadiusY(this.distInner);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(this.xCoordinate + this.distInner);
        moveTo2.setY(this.yCoordinate);

        HLineTo hLineToRightLeg = new HLineTo();
        hLineToRightLeg.setX(this.xCoordinate + this.distOuter);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(this.xCoordinate);
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
        path2.setFill(Color.web("#900dff"));
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
        path3.setFill(Color.web("#fae100"));
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
        path4.setFill(Color.web("#32dbf0"));
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
        this.blue = path4;
        this.yellow = path3;
        this.pink = path;
        this.purple = path2;
        this.obstacle = obstacle;
    }
}
class SquareObstacle extends Obstacle{
    public SquareObstacle(double distInner, double distOuter, double yCoordinate, double xCoordinate){
        super(distInner,distOuter,yCoordinate, xCoordinate);
    }
    @Override
    public int  collides(Circle ball,int color) {
        return super.collides(ball,color);
    }

    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate +  this.distInner);
        moveTo.setY(this.yCoordinate);
        VLineTo line1 = new VLineTo();
        line1.setY(this.yCoordinate-this.distInner);

        HLineTo line2 = new HLineTo();
        line2.setX(this.xCoordinate);

        VLineTo line3 = new VLineTo();
        line3.setY(this.yCoordinate-this.distOuter);

        HLineTo line4 = new HLineTo();
        line4.setX(this.xCoordinate + this.distOuter);

        VLineTo line5 = new VLineTo();
        line5.setY(this.yCoordinate);

        HLineTo line6 = new HLineTo();
        line6.setX(this.xCoordinate +this.distInner);


        path.getElements().add(moveTo);
        path.getElements().add(line1);
        path.getElements().add(line2);
        path.getElements().add(line3);
        path.getElements().add(line4);
        path.getElements().add(line5);
        path.getElements().add(line6);


        obstacle.getChildren().add(path);
        Path path2 = new Path();
        path2.setFill(Color.web("#900dff"));
        path2.setFillRule(FillRule.EVEN_ODD);
        path2.setLayoutY(path.getLayoutY()+this.distOuter);
        path2.getElements().add(moveTo);
        path2.getElements().add(line1);
        path2.getElements().add(line2);
        path2.getElements().add(line3);
        path2.getElements().add(line4);
        path2.getElements().add(line5);
        path2.getElements().add(line6);
        path2.setScaleX(1);
        path2.setScaleY(-1);
        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.web("#fae100"));
        path3.setFillRule(FillRule.EVEN_ODD);
        path3.setLayoutX(path.getLayoutY()-this.distOuter);
        path3.getElements().add(moveTo);
        path3.getElements().add(line1);
        path3.getElements().add(line2);
        path3.getElements().add(line3);
        path3.getElements().add(line4);
        path3.getElements().add(line5);
        path3.getElements().add(line6);
        path3.setScaleX(-1);
        path3.setScaleY(1);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.web("32dbf0"));
        path4.setFillRule(FillRule.EVEN_ODD);
        path4.setLayoutX(path.getLayoutX()-this.distOuter);
        path4.setLayoutY(path.getLayoutY()+this.distOuter);
        path4.getElements().add(moveTo);
        path4.getElements().add(line1);
        path4.getElements().add(line2);
        path4.getElements().add(line3);
        path4.getElements().add(line4);
        path4.getElements().add(line5);
        path4.getElements().add(line6);
        path4.setScaleX(-1);
        path4.setScaleY(-1);
        obstacle.getChildren().add(path4);
        this.blue = path4;
        this.yellow = path3;
        this.pink = path;
        this.purple = path2;
        this.obstacle = obstacle;
    }
}

class CrossObstacle extends Obstacle{
    public CrossObstacle(double yCoordinate, double xCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    @Override
    public int collides(Circle ball,int color) {
        return super.collides(ball,color);
    }
    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate);
        moveTo.setY(this.yCoordinate);

        LineTo line1 = new LineTo();
        line1.setY(this.yCoordinate-11.25);
        line1.setX(this.xCoordinate-11.25);

        HLineTo line2 = new HLineTo();
        line2.setX(this.xCoordinate-112.5);

        VLineTo line3 = new VLineTo();
        line3.setY(this.yCoordinate+11.25);

        HLineTo line4 = new HLineTo();
        line4.setX(this.xCoordinate-11.25);

        LineTo line5 = new LineTo();
        line5.setY(this.yCoordinate);
        line5.setX(this.xCoordinate);
        path.getElements().add(moveTo);
        path.getElements().add(line1);
        path.getElements().add(line2);
        path.getElements().add(line3);
        path.getElements().add(line4);
        path.getElements().add(line5);


        obstacle.getChildren().add(path);
        Path path2 = new Path();
        path2.setFill(Color.web("#900dff"));
        path2.setFillRule(FillRule.EVEN_ODD);
        path2.setLayoutX(path.getLayoutX()+112.5);
        path2.getElements().add(moveTo);
        path2.getElements().add(line1);
        path2.getElements().add(line2);
        path2.getElements().add(line3);
        path2.getElements().add(line4);
        path2.getElements().add(line5);
        path2.setScaleX(-1);
        path2.setScaleY(1);
        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.web("#fae100"));
        path3.setFillRule(FillRule.EVEN_ODD);
        path3.setLayoutY(path.getLayoutY()-56.25);
        path3.setLayoutX(path.getLayoutX()+56.25);
        path3.getElements().add(moveTo);
        path3.getElements().add(line1);
        path3.getElements().add(line2);
        path3.getElements().add(line3);
        path3.getElements().add(line4);
        path3.getElements().add(line5);
        path3.setScaleX(-1);
        path3.setScaleY(1);
        path3.setRotate(-90);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.web("#32dbf0"));
        path4.setFillRule(FillRule.EVEN_ODD);
        path4.setLayoutX(path.getLayoutX()+56.25);
        path4.setLayoutY(path.getLayoutY()+56.25);
        path4.getElements().add(moveTo);
        path4.getElements().add(line1);
        path4.getElements().add(line2);
        path4.getElements().add(line3);
        path4.getElements().add(line4);
        path4.getElements().add(line5);
        path4.setScaleX(-1);
        path4.setScaleY(-1);
        path4.setRotate(90);
        obstacle.getChildren().add(path4);
        this.blue = path4;
        this.yellow = path3;
        this.pink = path;
        this.purple = path2;
        this.obstacle = obstacle;
    }
}
class BowObstacle extends Obstacle{
    protected double height;
    public BowObstacle(double yCoordinate, double xCoordinate, double distInner, double distOuter, double height){
        super(distInner,distOuter,yCoordinate, xCoordinate);
        this.height = height;
    }
    @Override
    public int collides(Circle ball,int color) {
        return super.collides(ball,color);
    }
    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate+this.distOuter);
        moveTo.setY(this.yCoordinate);

        LineTo line1 = new LineTo();
        line1.setY(this.yCoordinate-this.height);
        line1.setX(this.xCoordinate+this.distOuter+50);

        LineTo line2 = new LineTo();
        line2.setX(this.xCoordinate+this.distInner+ 50 );
        line2.setY(this.yCoordinate-this.height);

        LineTo line3 = new LineTo();
        line3.setX(this.xCoordinate+this.distInner);
        line3.setY(this.yCoordinate);

        LineTo line4 = new LineTo();
        line4.setX(this.xCoordinate+this.distInner +50);
        line4.setY(this.yCoordinate+this.height);

        LineTo line5 = new LineTo();
        line5.setY(this.yCoordinate + this.height);
        line5.setX(this.xCoordinate + this.distOuter +50);

        LineTo line6 = new LineTo();
        line6.setY(this.yCoordinate );
        line6.setX(this.xCoordinate + this.distOuter);
        path.getElements().add(moveTo);
        path.getElements().add(line1);
        path.getElements().add(line2);
        path.getElements().add(line3);
        path.getElements().add(line4);
        path.getElements().add(line5);
        path.getElements().add(line6);

        obstacle.getChildren().add(path);
        Path path2 = new Path();
        path2.setFill(Color.web("#900dff"));
        path2.setFillRule(FillRule.EVEN_ODD);
        moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate-this.distOuter);
        moveTo.setY(this.yCoordinate);

        line1 = new LineTo();
        line1.setY(this.yCoordinate-this.height);
        line1.setX(this.xCoordinate-this.distOuter-50);

        line2 = new LineTo();
        line2.setX(this.xCoordinate-this.distInner-50 );
        line2.setY(this.yCoordinate-this.height);

        line3 = new LineTo();
        line3.setX(this.xCoordinate-this.distInner);
        line3.setY(this.yCoordinate);

        line4 = new LineTo();
        line4.setX(this.xCoordinate-this.distInner -50);
        line4.setY(this.yCoordinate+this.height);

        line5 = new LineTo();
        line5.setY(this.yCoordinate + this.height);
        line5.setX(this.xCoordinate - this.distOuter -50);

        line6 = new LineTo();
        line6.setY(this.yCoordinate );
        line6.setX(this.xCoordinate - this.distOuter);
        path2.getElements().add(moveTo);
        path2.getElements().add(line1);
        path2.getElements().add(line2);
        path2.getElements().add(line3);
        path2.getElements().add(line4);
        path2.getElements().add(line5);
        path2.getElements().add(line6);

        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.web("#fae100"));
        path3.setFillRule(FillRule.EVEN_ODD);
        moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate+this.distInner+ 50 );
        moveTo.setY(this.yCoordinate-this.height);

        line1 = new LineTo();
        line1.setY(this.yCoordinate-this.height);
        line1.setX(this.xCoordinate-this.distInner-50);

        line2 = new LineTo();
        line2.setX(this.xCoordinate-this.distInner-50 + this.distOuter - this.distInner - (this.distOuter-this.distInner)/2 );
        line2.setY(this.yCoordinate-this.height + this.distOuter - this.distInner);

        line3 = new LineTo();
        line3.setX(this.xCoordinate+this.distInner+50-this.distOuter +this.distInner + (this.distOuter-this.distInner)/2 );
        line3.setY(this.yCoordinate-this.height + this.distOuter - this.distInner );

        line4 = new LineTo();
        line4.setX(this.xCoordinate+this.distInner+ 50);
        line4.setY(this.yCoordinate-this.height);


        path3.getElements().add(moveTo);
        path3.getElements().add(line1);
        path3.getElements().add(line2);
        path3.getElements().add(line3);
        path3.getElements().add(line4);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.web("#32dbf0"));
        path4.setFillRule(FillRule.EVEN_ODD);
        moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate+this.distInner+ 50 );
        moveTo.setY(this.yCoordinate+this.height);

        line1 = new LineTo();
        line1.setY(this.yCoordinate+this.height);
        line1.setX(this.xCoordinate-this.distInner-50);

        line2 = new LineTo();
        line2.setX(this.xCoordinate-this.distInner-50 + this.distOuter - this.distInner - (this.distOuter-this.distInner)/2 );
        line2.setY(this.yCoordinate+this.height - this.distOuter + this.distInner);

        line3 = new LineTo();
        line3.setX(this.xCoordinate+this.distInner+50-this.distOuter +this.distInner + (this.distOuter-this.distInner)/2 );
        line3.setY(this.yCoordinate+this.height - this.distOuter + this.distInner );

        line4 = new LineTo();
        line4.setX(this.xCoordinate+this.distInner+ 50);
        line4.setY(this.yCoordinate+this.height);


        path4.getElements().add(moveTo);
        path4.getElements().add(line1);
        path4.getElements().add(line2);
        path4.getElements().add(line3);
        path4.getElements().add(line4);
        obstacle.getChildren().add(path4);
        this.blue = path4;
        this.yellow = path3;
        this.pink = path;
        this.purple = path2;
        this.obstacle = obstacle;
    }
}
class HalfBowObstacle extends Obstacle{
    protected double height;
    public HalfBowObstacle(double yCoordinate, double xCoordinate, double distInner, double distOuter, double height){
        super(distInner,distOuter,yCoordinate, xCoordinate);
        this.height = height;
    }
    @Override
    public int collides(Circle ball,int color) {
        return super.collides(ball,color);
    }
    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate+this.distOuter);
        moveTo.setY(this.yCoordinate);

        LineTo line1 = new LineTo();
        line1.setY(this.yCoordinate-this.height);
        line1.setX(this.xCoordinate+this.distOuter+50);

        LineTo line2 = new LineTo();
        line2.setX(this.xCoordinate+this.distInner+ 50 );
        line2.setY(this.yCoordinate-this.height);

        LineTo line3 = new LineTo();
        line3.setX(this.xCoordinate+this.distInner);
        line3.setY(this.yCoordinate);

        LineTo line4 = new LineTo();
        line4.setX(this.xCoordinate+this.distOuter);
        line4.setY(this.yCoordinate);

        path.getElements().add(moveTo);
        path.getElements().add(line1);
        path.getElements().add(line2);
        path.getElements().add(line3);
        path.getElements().add(line4);


        obstacle.getChildren().add(path);
        Path path2 = new Path();
        path.setStroke(Color.RED);
        path2.setFill(Color.web("#900dff"));
        path2.setFillRule(FillRule.EVEN_ODD);
        moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate+this.distInner);
        moveTo.setY(this.yCoordinate);

        line1 = new LineTo();
        line1.setX(this.xCoordinate+this.distInner +50);
        line1.setY(this.yCoordinate+this.height);

        line2 = new LineTo();
        line2.setY(this.yCoordinate + this.height);
        line2.setX(this.xCoordinate + this.distOuter +50);

        line3 = new LineTo();
        line3.setY(this.yCoordinate );
        line3.setX(this.xCoordinate + this.distOuter);

        line4 = new LineTo();
        line4.setX(this.xCoordinate+this.distInner);
        line4.setY(this.yCoordinate);

        path2.getElements().add(moveTo);
        path2.getElements().add(line1);
        path2.getElements().add(line2);
        path2.getElements().add(line3);
        path2.getElements().add(line4);


        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.web("#fae100"));
        path.setStroke(Color.RED);
        path3.setFillRule(FillRule.EVEN_ODD);
        moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate-this.distOuter);
        moveTo.setY(this.yCoordinate);

        line1 = new LineTo();
        line1.setY(this.yCoordinate-this.height);
        line1.setX(this.xCoordinate-this.distOuter-50);

        line2 = new LineTo();
        line2.setX(this.xCoordinate-this.distInner-50 );
        line2.setY(this.yCoordinate-this.height);

        line3 = new LineTo();
        line3.setX(this.xCoordinate-this.distInner);
        line3.setY(this.yCoordinate);

        line4 = new LineTo();
        line4.setX(this.xCoordinate+this.distOuter);
        line4.setY(this.yCoordinate);


        path3.getElements().add(moveTo);
        path3.getElements().add(line1);
        path3.getElements().add(line2);
        path3.getElements().add(line3);
        path3.getElements().add(line4);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.web("#32dbf0"));
        path.setStroke(Color.RED);
        path4.setFillRule(FillRule.EVEN_ODD);
        moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate-this.distInner);
        moveTo.setY(this.yCoordinate);

        line1 = new LineTo();
        line1.setX(this.xCoordinate-this.distInner -50);
        line1.setY(this.yCoordinate+this.height);

        line2 = new LineTo();
        line2.setY(this.yCoordinate + this.height);
        line2.setX(this.xCoordinate - this.distOuter -50);

        line3 = new LineTo();
        line3.setY(this.yCoordinate );
        line3.setX(this.xCoordinate - this.distOuter);

        line4 = new LineTo();
        line4.setX(this.xCoordinate-this.distInner);
        line4.setY(this.yCoordinate);

        path4.getElements().add(moveTo);
        path4.getElements().add(line1);
        path4.getElements().add(line2);
        path4.getElements().add(line3);
        path4.getElements().add(line4);
        obstacle.getChildren().add(path4);
        this.blue = path4;
        this.yellow = path3;
        this.pink = path;
        this.purple = path2;
        this.obstacle = obstacle;
    }
}
class ThornObstacle extends Obstacle{
    protected double thornLength;
    public ThornObstacle(double distInner ,double yCoordinate, double xCoordinate, double thornLength){
        this.distInner = distInner;
        this.thornLength = thornLength;
        this.yCoordinate = yCoordinate;
        this.xCoordinate = xCoordinate;
    }
    @Override
    public int collides(Circle ball,int color) {
        return super.collides(ball,color);
    }

    public Group getObstacle(){
        return this.obstacle;
    }
    @Override
    public void create() {
        Group obstacle = new Group();
        Path path = new Path();
        path.setFill(Color.web("#ff0181"));
        path.setStroke(Color.RED);
        path.setFillRule(FillRule.EVEN_ODD);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(this.xCoordinate + this.distInner);
        moveTo.setY(this.yCoordinate);
        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(this.xCoordinate);
        arcToInner.setY(this.yCoordinate-this.distInner);
        arcToInner.setRadiusX(this.distInner);
        arcToInner.setRadiusY(this.distInner);

        LineTo line1 = new LineTo();
        line1.setY(this.yCoordinate-this.thornLength*this.distInner);
        line1.setX(this.xCoordinate+this.thornLength*this.distInner);

        LineTo line2 = new LineTo();
        line2.setX(this.xCoordinate+this.distInner);
        line2.setY(this.yCoordinate);

        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);
        path.getElements().add(line1);
        path.getElements().add(line2);
        obstacle.getChildren().add(path);

        Path path2 = new Path();
        path2.setFill(Color.web("#900dff"));
        path2.setFillRule(FillRule.EVEN_ODD);
        path2.setLayoutY(path.getLayoutY()+this.thornLength*this.distInner);
        path2.getElements().add(moveTo);
        path2.getElements().add(arcToInner);
        path2.getElements().add(line1);
        path2.getElements().add(line2);
        path2.setScaleX(1);
        path2.setScaleY(-1);
        obstacle.getChildren().add(path2);

        Path path3 = new Path();
        path3.setFill(Color.web("#fae100"));
        path3.setFillRule(FillRule.EVEN_ODD);
        path3.setLayoutX(path.getLayoutY()-this.thornLength*this.distInner);
        path3.getElements().add(moveTo);
        path3.getElements().add(arcToInner);
        path3.getElements().add(line1);
        path3.getElements().add(line2);
        path3.setScaleX(-1);
        path3.setScaleY(1);
        obstacle.getChildren().add(path3);

        Path path4 = new Path();
        path4.setFill(Color.web("#32dbf0"));
        path4.setFillRule(FillRule.EVEN_ODD);
        path4.setLayoutX(path.getLayoutX()-this.thornLength*this.distInner);
        path4.setLayoutY(path.getLayoutY()+this.thornLength*this.distInner);
        path4.getElements().add(moveTo);
        path4.getElements().add(arcToInner);
        path4.getElements().add(line1);
        path4.getElements().add(line2);
        path4.setScaleX(-1);
        path4.setScaleY(-1);
        obstacle.getChildren().add(path4);
        this.blue = path4;
        this.yellow = path3;
        this.pink = path;
        this.purple = path2;
        this.obstacle = obstacle;
    }
}