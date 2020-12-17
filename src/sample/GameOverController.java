package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class GameOverController {
    @FXML
    private Label continueLabel;
    @FXML
    private ImageView continueText, continueStar;
    @FXML
    private Text scoreText, continueCost;
    @FXML
    private AnchorPane gameOverBG;

    @FXML
    public void initialize() {
        GameState state = Main.getCurrentPlayer().getCurrentState();
        scoreText.setText(String.valueOf(state.getNumStarsCollected()));
        if(state.getNumRetry()<0)   {
            continueCost.setDisable(true);
            continueLabel.setDisable(true);
            continueStar.setDisable(true);
            continueStar.setOpacity(0.5);
            continueText.setDisable(true);
            continueText.setOpacity(0.5);
        }
        else    {
            continueCost.setText(String.valueOf(-(state.getNumRetry()+1)*4));
        }

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
        Player p = Main.getCurrentPlayer();
        ArrayList<int[]> hs = p.getHighScores();
        GameState state = p.getCurrentState();
        // storing total stars
        p.increaseTotalStars(state.getNumStarsCollected());
        //storing highscore
        if(state.getMode()==0 && state.getNumStarsCollected()>0){
            if(state.getStateID()==0  || !p.getGameStateIDs().contains(state.getStateID())) {
                if(state.getStateID()!=0)   {
                    p.getGameStateIDs().add(state.getStateID());
                }
                int[] triplet = {state.getStateID(),  state.getNumStarsCollected(), state.getDifficulty()};
                hs.add(triplet);
                if(hs.size()!=1)    {
                    hsComparator comp = new hsComparator();
                    hs.sort(comp);
                }
            }
            else    {
                for(int[] triplet: p.getHighScores())   {
                    if(state.getStateID()==triplet[0])  {
                        if(state.getNumStarsCollected()>triplet[1]) {
                            triplet[1] = state.getNumStarsCollected();
                        }
                        break;
                    }
                }
            }
        }
        Database.serialize(Main.getDB());
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        gameOverBG.getChildren().clear();
        gameOverBG.getChildren().setAll(pane);
    }

    public void restartGame() throws IOException {
        Player p = Main.getCurrentPlayer();
        ArrayList<int[]> hs = p.getHighScores();
        GameState state = p.getCurrentState();
        // storing total stars
        p.increaseTotalStars(state.getNumStarsCollected());
        // storing highscore
        if(state.getMode()==0 && state.getNumStarsCollected()>0){
            if(state.getStateID()==0  || !p.getGameStateIDs().contains(state.getStateID())) {
                if(state.getStateID()!=0)   {
                    p.getGameStateIDs().add(state.getStateID());
                }
                int[] triplet = {state.getStateID(),  state.getNumStarsCollected(), state.getDifficulty()};
                hs.add(triplet);
                if(hs.size()!=1)    {
                    hsComparator comp = new hsComparator();
                    hs.sort(comp);
                }
            }
            else    {
                for(int[] triplet: p.getHighScores())   {
                    if(state.getStateID()==triplet[0])  {
                        if(state.getNumStarsCollected()>triplet[1]) {
                            triplet[1] = state.getNumStarsCollected();
                        }
                        break;
                    }
                }
            }
        }
        Database.serialize(Main.getDB());
        AnchorPane pane = FXMLLoader.load(getClass().getResource("newGameScreen.fxml"));
        gameOverBG.getChildren().clear();
        gameOverBG.getChildren().setAll(pane);
    }

    public void continueGame() throws Exception {
        GameState state;
        try {
            state = revive();
        }
        catch (CannotContinueException e) {
            // cannot continue popup
            AnchorPane pane = FXMLLoader.load(getClass().getResource("cannotContinueScreen.fxml"));
            gameOverBG.getChildren().setAll(pane);
            return;
        }
        if(state!=null) {
            try {
                System.out.println("Reviving now :)");
                Main.getCurrentPlayer().setCurrentState(state);
                gameOverBG.getChildren().clear();
                // setting background to black
                Rectangle background = new Rectangle(0, 0, 450, 600);
                background.setFill(Color.BLACK);
                gameOverBG.getChildren().add(background);
                state.loadGame(gameOverBG);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public GameState revive() throws CannotContinueException {
        GameState state = Main.getCurrentPlayer().getCurrentState().getGameOverState();
        if(state.getNumStarsCollected()<(state.getNumRetry()+1)*4)  {
            throw new CannotContinueException("not enough stars");
        }
        else    {
            state.incrementNumRetry();
            state.decreaseStars();
            return state;
        }
    }

    static class hsComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            if(o1[1]!=o2[1])    {
                return o2[1] - o1[1];
            }
            return o2[0] - o1[0];
        }
    }

}
