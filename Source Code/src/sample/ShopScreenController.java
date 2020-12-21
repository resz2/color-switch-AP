package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Set;

public class ShopScreenController {
    @FXML
    private Button buyButton1, buyButton2, buyButton3, buyButton4,
            selectButton1, selectButton2, selectButton3, selectButton4;
    @FXML
    private Text ball1Cost, ball2Cost, ball3Cost, ball4Cost;
    @FXML
    private AnchorPane shopBG;

    @FXML
    public void initialize()    {
        boolean[] balls = Main.getCurrentPlayer().getBallTypes();
        if(balls[1])   {
            buyButton1.setVisible(false);
            buyButton1.setDisable(true);
            selectButton1.setVisible(true);
            selectButton1.setDisable(false);
        }
        if(balls[2])   {
            buyButton2.setVisible(false);
            buyButton2.setDisable(true);
            selectButton2.setVisible(true);
            selectButton2.setDisable(false);
        }
        if(balls[3])   {
            buyButton3.setVisible(false);
            buyButton3.setDisable(true);
            selectButton3.setVisible(true);
            selectButton3.setDisable(false);
        }
        if(balls[4])   {
            buyButton4.setVisible(false);
            buyButton4.setDisable(true);
            selectButton4.setVisible(true);
            selectButton4.setDisable(false);
        }
    }

    public void goBack() {
        AnchorPane pane=null;
        try {
            pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
        shopBG.getChildren().setAll(pane);
    }

    private void buy(int ball, int cost) {
        System.out.println("bought" + ball);
        Main.getCurrentPlayer().getBallTypes()[ball] = true;
        Main.getCurrentPlayer().decreaseTotalStars(cost);
        try {
            Database.serialize(Main.getDB());
        }
        catch (IOException e)   {
            e.printStackTrace();
        }
    }

    private void select(int ball) {
        System.out.println("selected " + ball);
        Main.getCurrentPlayer().setCurrentBall(ball);
    }

    public void buyBall1() {
        int cost = Integer.parseInt(ball1Cost.getText());
        if(Main.getCurrentPlayer().getTotalStars()>=cost)  {
            buyButton1.setVisible(false);
            buyButton1.setDisable(true);
            selectButton1.setVisible(true);
            selectButton1.setDisable(false);
            buy(1, cost);
        }
    }
    public void buyBall2() {
        int cost = Integer.parseInt(ball2Cost.getText());
        if(Main.getCurrentPlayer().getTotalStars()>=cost)  {
            buyButton2.setVisible(false);
            buyButton2.setDisable(true);
            selectButton2.setVisible(true);
            selectButton2.setDisable(false);
            buy(2, cost);
        }
    }
    public void buyBall3() {
        int cost = Integer.parseInt(ball3Cost.getText());
        if(Main.getCurrentPlayer().getTotalStars()>=cost)  {
            buyButton3.setVisible(false);
            buyButton3.setDisable(true);
            selectButton3.setVisible(true);
            selectButton3.setDisable(false);
            buy(3, cost);
        }
    }
    public void buyBall4() {
        int cost = Integer.parseInt(ball4Cost.getText());
        if(Main.getCurrentPlayer().getTotalStars()>=cost)  {
            buyButton4.setVisible(false);
            buyButton4.setDisable(true);
            selectButton4.setVisible(true);
            selectButton4.setDisable(false);
            buy(4, cost);
        }
    }

    public void selectBall1() {
        selectButton1.setStyle("-fx-background-color: #00a400");
        select(1);
    }
    public void selectBall2() {
        selectButton2.setStyle("-fx-background-color: #00a400");
        select(2);
    }
    public void selectBall3() {
        selectButton3.setStyle("-fx-background-color: #00a400");
        select(3);
    }
    public void selectBall4() {
        selectButton4.setStyle("-fx-background-color: #00a400");
        select(4);
    }
}
