package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

class MainMenuControlller {
    @FXML
    private AnchorPane newGameBG;

    public void chooseDifficulty(MouseEvent mouseEvent) {
        int difficulty;
        double y = mouseEvent.getSceneY();
        if(150<y && y<225)  {
            difficulty = 1;
        }
        else if(270<y && y<345) {
            difficulty = 2;
        }
        else if(390<y && y<465) {
            difficulty = 3;
        }
        //start game with the chosen difficulty level
    }

    public void goBack() throws  Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        newGameBG.getChildren().setAll(pane);
    }
}
