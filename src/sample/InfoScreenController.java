package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class InfoScreenController {
    @FXML
    private AnchorPane infoBG;

    @FXML
    public void goBack() throws Exception {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("titleScreen.fxml"));
        infoBG.getChildren().setAll(pane);
    }

    private void openBrowser(String url)  {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    public void linkedinOne() {
        openBrowser("https://www.linkedin.com/in/saatvik-bhatnagar-345838196");
    }

    public void linkedinTwo() {
        openBrowser("https://www.linkedin.com/in/aryangdsingh");
    }

    public void githubOne() {
        openBrowser("https://github.com/Saatvik07");
    }

    public void githubTwo() {
        openBrowser("https://github.com/resz2");
    }
}
