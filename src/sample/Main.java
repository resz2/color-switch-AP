package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage titleScreen) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        titleScreen.setTitle("Color Switch");
        titleScreen.setScene(new Scene(root, 450, 600));
        titleScreen.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
