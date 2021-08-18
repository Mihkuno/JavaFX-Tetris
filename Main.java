package src;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application implements MainInterface {
    
    public static void main(String[] args) { launch(args); }    
    @Override
    public void start(Stage WINDOW) throws Exception {

        new Menu();

        Tetris.init();

        WINDOW.getIcons().add(new Image("src/tetris.png"));
        WINDOW.setScene(DOCUMENT);
        WINDOW.setTitle("A Game of Tetris");
        WINDOW.show();
        
    }      
}

