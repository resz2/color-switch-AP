package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class GameOverController {
    @FXML
    private AnchorPane gameOverBG;

    public void exitToMainMenu() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        gameOverBG.getChildren().setAll(pane);
    }

    public void restartGame() {
    }

    public void continueGame() {
    }
}
