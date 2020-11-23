package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class exitController {
    @FXML
    private AnchorPane popupBG;

    @FXML
    public void closePopup() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        popupBG.getChildren().setAll(pane);
    }

    public void closeGame() {
        System.exit(0);
    }
}
