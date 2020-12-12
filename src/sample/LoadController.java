package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LoadController {
    @FXML
    private ListView<String> savelist;
    @FXML
    private AnchorPane loadBG;

    @FXML
    public void initialize()    {
        // loads and displays the saved games info
        savelist.getItems().add("    5                         medium");
        savelist.getItems().add("    10                        easy");
        savelist.getItems().add("    8                         easy");
        savelist.getItems().add("    9                         easy");
        savelist.getItems().add("    6                         medium");
        savelist.getItems().add("    3                         hard");
        savelist.getItems().add("    5                         medium");
    }

    public void loadSave(MouseEvent mouseEvent) {
        // loads the selected game
        // for now loads game 0
        Main.getCurrentPlayer().setCurrentState(Main.getCurrentPlayer().getSavedGames().get(0));
        Main.getCurrentPlayer().getCurrentState().loadGame(loadBG);
    }

    public void goBack() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        loadBG.getChildren().setAll(pane);
    }
}
