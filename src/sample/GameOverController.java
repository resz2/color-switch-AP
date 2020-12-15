package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class GameOverController {
    @FXML
    private AnchorPane gameOverBG;

    @FXML
    public void initialize() {

//        Timeline enterTimeline = new Timeline(new KeyFrame(Duration.millis(5),
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent t) {
//                        background.setOpacity(background.getOpacity()+0.0015);
//                        highScoreBanner.setOpacity(highScoreBanner.getOpacity()+0.01);
//                        highScoreBanner.setLayoutY(highScoreBanner.getLayoutY()+1);
//                        highScoreHeading.setOpacity(highScoreHeading.getOpacity()+0.01);
//                        highScoreHeading.setLayoutY(highScoreHeading.getLayoutY()+1);
//                        highScoreList.setOpacity(highScoreList.getOpacity()+0.01);
//                        difficultyImage.setOpacity(difficultyImage.getOpacity()+0.01);
//                        starImage.setOpacity(starImage.getOpacity()+0.01);
//                        backIcon.setOpacity(backIcon.getOpacity()+0.01);
//                        backIcon.setLayoutX(backIcon.getLayoutX()-2);
//                        backCircle.setOpacity(backIcon.getOpacity()+0.01);
//                        backCircle.setLayoutX(backCircle.getLayoutX()-2);
//                        backIcon.setRotate(backIcon.getRotate()+3.6);
//                    }
//                }));
//        enterTimeline.setCycleCount(100);
//        enterTimeline.play();
    }

    public void exitToMainMenu() throws Exception {
        ArrayList<int[]> hs = Main.getCurrentPlayer().getHighScores();
        GameState state = Main.getCurrentPlayer().getCurrentState();
        // storing total stars
        Main.getCurrentPlayer().increaseTotalStars(state.getNumStarsCollected());
        int[] pair = {state.getNumStarsCollected(), state.getDifficulty()};
        hs.add(pair);
        if(hs.size()!=1)    {
            hsComparator comp = new hsComparator();
            hs.sort(comp);
        }
        Database.serialize(Main.getDB());
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        gameOverBG.getChildren().setAll(pane);
    }

    public void restartGame() throws IOException {
        ArrayList<int[]> hs = Main.getCurrentPlayer().getHighScores();
        GameState state = Main.getCurrentPlayer().getCurrentState();
        // storing total stars
        Main.getCurrentPlayer().increaseTotalStars(state.getNumStarsCollected());
        int[] pair = {state.getNumStarsCollected(), state.getDifficulty()};
        hs.add(pair);
        if(hs.size()!=1)    {
            hsComparator comp = new hsComparator();
            hs.sort(comp);
        }
        Database.serialize(Main.getDB());
        AnchorPane pane = FXMLLoader.load(getClass().getResource("newGameScreen.fxml"));
        gameOverBG.getChildren().setAll(pane);
    }

    public void continueGame() throws IOException {
        System.out.println("Reviving now :)");
        // uncomment after revive implemented
        //Main.getCurrentPlayer().getCurrentState().decreaseStars();
        revive();
    }

    public void revive() {
        // not sure how
        // newGameBG.getChildren().setAll(Main.getCurrentPlayer().getCurrentState().getGameOverCanvas());
    }

    class hsComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            if(o1[1]!=o2[1])    {
                return o2[1] - o1[1];
            }
            return o2[0] - o1[0];
        }
    }

}
