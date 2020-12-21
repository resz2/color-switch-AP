package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class CannotContinueController {
    @FXML
    private AnchorPane cantBG;

    @FXML
    public void goBack() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("gameOverScreen.fxml"));
        cantBG.getChildren().setAll(pane);
    }
}
