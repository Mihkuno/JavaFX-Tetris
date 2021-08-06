package proj.tetris;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public interface TetrisInterface {
    int   ROW = 20;
    int   COL = 10;
    int   AREA = 27;

    int   GRID_XOFFSET = 215;
    int   GRID_YOFFSET = 30;
    int   GRID_STROKE_WIDTH = 1;
    int   FOCUS_STROKE_WIDTH = 1;

    Color GRID_FILL = Color.valueOf("#d1ccc0");
    Color GHOST_FILL = Color.valueOf("#84817a");
    Color GRID_STROKE_COLOR = Color.valueOf("#f7f1e3");
    Color FOCUS_STROKE_COLOR = Color.valueOf("#f7f1e3");   
    

    ArrayList<Rectangle> blockCollection = new ArrayList<Rectangle>();
    
    Rectangle[][] MESH = new Rectangle[ROW][COL]; 



    

         
}
