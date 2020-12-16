package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import java.io.IOException;

public class LoadController {
    @FXML
    private Circle backCircle;
    @FXML
    private ImageView background, loadsBanner, loadsHeading, backIcon, starImage, difficultyImage;
    @FXML
    private ListView<GameState> loadsList;
    @FXML
    private AnchorPane loadBG;

    @FXML
    public void initialize()    {
        // loads and displays the saved games info
//        loadsList.getItems().add("    5                         medium");
//        loadsList.getItems().add("    10                        easy");
//        loadsList.getItems().add("    8                         easy");
//        loadsList.getItems().add("    9                         easy");
//        loadsList.getItems().add("    6                         medium");
//        loadsList.getItems().add("    3                         hard");
//        loadsList.getItems().add("    5                         medium");

        if(Main.getCurrentPlayer().getSavedGames().isEmpty())   {
            System.out.println("empty loads");
        }
        else    {
            for(GameState state: Main.getCurrentPlayer().getSavedGames())   {
                loadsList.getItems().add(state);
            }
        }

//        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent t) {
//                        background.setOpacity(background.getOpacity()+0.0015);
//                        loadsBanner.setOpacity(loadsBanner.getOpacity()+0.01);
//                        loadsBanner.setLayoutY(loadsBanner.getLayoutY()+1);
//                        loadsHeading.setOpacity(loadsHeading.getOpacity()+0.01);
//                        loadsHeading.setLayoutY(loadsHeading.getLayoutY()+1);
//                        loadsList.setOpacity(loadsList.getOpacity()+0.01);
//                        difficultyImage.setOpacity(difficultyImage.getOpacity()+0.01);
//                        starImage.setOpacity(starImage.getOpacity()+0.01);
//                        backIcon.setOpacity(backIcon.getOpacity()+0.01);
//                        backIcon.setLayoutX(backIcon.getLayoutX()-2);
//                        backCircle.setOpacity(backIcon.getOpacity()+0.01);
//                        backCircle.setLayoutX(backCircle.getLayoutX()-2);
//                        backIcon.setRotate(backIcon.getRotate()+3.6);
//                    }
//                }));
//        enterTimeline.setCycleCount(50);
//        enterTimeline.play();
    }

    @FXML
    public void loadSave() {
        // loads the selected game
        if(!loadsList.getItems().isEmpty()) {
            GameState state = loadsList.getSelectionModel().getSelectedItem().deepClone();
            try {
                Main.getCurrentPlayer().setCurrentState(state);
                loadBG.getChildren().clear();
                state.loadGame(loadBG);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void goBack() {
                AnchorPane pane = null;
                try {
                    pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadBG.getChildren().setAll(pane);
    }
}
