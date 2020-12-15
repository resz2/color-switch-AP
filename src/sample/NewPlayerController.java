package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NewPlayerController {
    @FXML
    private TextField nameField;
    @FXML
    private AnchorPane playerBG;

    public void confirmAdd() {
        String name = nameField.getText();
        if(name!=null)  {
            Player p = new Player(name);
            Main.getDB().getPlayers().add(p);
            Main.setCurrentPlayer(p);
            Main.getDB().setLastPlayer(Main.getDB().getPlayers().indexOf(p));
            try {
                Database.serialize(Main.getDB());
            }
            catch (IOException e)   {
                e.printStackTrace();
                System.out.println("could not save");
            }
            goBack();
        }
    }

    public void goBack() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("playerListScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerBG.getChildren().setAll(pane);
    }
}
