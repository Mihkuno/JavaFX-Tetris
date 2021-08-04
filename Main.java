package proj;

import javafx.application.Application;
import javafx.stage.Stage;
import proj.frags.Menu;
import proj.tetris.Tetris;


public class Main extends Application implements MainInterface {
    
    public static void main(String[] args) { launch(args); }    
    @Override
    public void start(Stage WINDOW) throws Exception {
        
        new Menu();  
        // new Tetris();

        WINDOW.setScene(DOCUMENT);
        WINDOW.setTitle("Tetris");
        WINDOW.show(); 
        
    }      
}

