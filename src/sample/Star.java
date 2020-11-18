package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Star {
    private double angle = Math.PI / 5;
    protected double xCoordinate,yCoordinate;
    protected Rectangle Bbox;
    protected ImageView starBody;
    public Star(double xCoordinate, double yCoordinate) throws FileNotFoundException {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.Bbox = new Rectangle(this.xCoordinate-25,this.yCoordinate-25,50,50);
        this.create();
    }
    public void create() throws FileNotFoundException {
        InputStream stream = new FileInputStream("C:\\Users\\SAATVIK\\Downloads\\star.png");
        Image image = new Image(stream);
        starBody = new ImageView();
        starBody.setImage(image);
        starBody.setX(this.xCoordinate-25);
        starBody.setY(this.yCoordinate-25);
        starBody.setFitWidth(50);
        starBody.setPreserveRatio(true);
    }
    public void setyCoordinate(double val){
        this.Bbox = new Rectangle(this.xCoordinate-25,this.Bbox.getLayoutY()+val,50,50);
        this.starBody.setLayoutY(this.starBody.getLayoutY()+val);
        this.yCoordinate = this.starBody.getLayoutY();

    }
    public boolean checkCollision(Rectangle b){
//        System.out.println(this.Bbox.getBoundsInParent());
        return b.getBoundsInParent().intersects(this.Bbox.getBoundsInParent());
    }
    public void showAnimation(){
        this.starBody.setOpacity(0);
//        this.starBody = null;
//        Path smallStar = new Path();
//        ArrayList<Path> smallStars = new ArrayList<>();
//        for(int j=0;j<10;j++){
//            for (int i = 0; i < 10; i++) {
//                double r = (i & 1) == 0 ? 10 : 5;
//                Point2D p = new Point2D(
//                        this.xCoordinate-20 + Math.cos(i * angle) * r,
//                        this.yCoordinate-(j-2)*15 + Math.sin(i * angle) * r);
//                if (i == 0) {
//                    smallStar.getElements().add(new MoveTo(p.getX(), p.getY()));
//                }
//                else {
//                    smallStar.getElements().add(new LineTo(p.getX(), p.getY()));
//                }
//                smallStars.add(smallStar);
//            }
//        }
//        return smallStars;
    }
}
