package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class exitController {
    @FXML
    private AnchorPane popupBG;

    @FXML
    public void closePopup() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        popupBG.getChildren().setAll(pane);
    }

    public void closeGame() {
        try {
            Database.serialize(Main.getDB());
        }
        catch (IOException e)   {
            System.out.println("could not save progress");
        }
        System.exit(0);
    }
}
