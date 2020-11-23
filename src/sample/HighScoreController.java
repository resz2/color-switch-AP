package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class HighScoreController {
    @FXML
    private ListView<String> highScoreList;
    @FXML
    private AnchorPane highScoreBG;

    @FXML
    public void initialize()    {
        // loads and displays the high scores
        highScoreList.getItems().add("    10                        easy");
        highScoreList.getItems().add("    9                         medium");
        highScoreList.getItems().add("    8                         easy");
        highScoreList.getItems().add("    7                         easy");
        highScoreList.getItems().add("    6                         medium");
        highScoreList.getItems().add("    5                         medium");
        highScoreList.getItems().add("    4                         hard");
        highScoreList.getItems().add("    3                         hard");
        highScoreList.getItems().add("    2                         hard");
    }

    public void goBack() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        highScoreBG.getChildren().setAll(pane);
    }
}
