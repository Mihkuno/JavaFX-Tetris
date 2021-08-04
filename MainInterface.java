package proj;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public interface MainInterface {
    int   DOCUMENT_WIDTH = 900;
    // int   DOCUMENT_HEIGHT = 550;
    int   DOCUMENT_HEIGHT = 600;
    
    Color BACKGROUND = Color.valueOf("#f7f1e3");

    
    VBox INTRO = new VBox();
    HBox INTRO_MISC = new HBox();
    HBox SETTINGS_MISC = new HBox();
    VBox INTRO_CONTAINER = new VBox();
    GridPane SETTINGS = new GridPane();
    VBox SETTINGS_CONTAINER = new VBox();

    Pane LAYOUT = new Pane();
    Scene DOCUMENT = new Scene(LAYOUT, DOCUMENT_WIDTH, DOCUMENT_HEIGHT, BACKGROUND);
    
    ArrayList<MediaPlayer> MUSIC = new ArrayList<MediaPlayer>();
}