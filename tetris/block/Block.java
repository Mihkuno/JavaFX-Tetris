package proj.tetris.block;

import java.util.ArrayList;

import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import proj.MainInterface;
import proj.tetris.TetrisInterface;

public abstract class Block implements MainInterface, TetrisInterface {
    private float focusStartColumn = 3 * AREA;
    private float focusStartRow = -1 * AREA;
    private int rotateLoopCounter = 0;
    private int variantCounter = 0;
    private float column, row;
    private int counterX = 0;
    private int counterY = 0;   
    private boolean isRotateCollided;
    
    private int[][] pickedVariant = block()[this.variantCounter];    

    private Rectangle[][] SQUARE = new Rectangle[ROW][COL];  


    private static Block[] TETRONIMO = new Block[7];

    public static final int length = TETRONIMO.length;
    
    protected Color color; 

    public Block(Color color) {
        this.color = color;
    }

    protected abstract int[][][] block();

    @Deprecated
    public static Block create(Block block) {
        return block;
    }

    public static Block select(int index, boolean color) {

        TETRONIMO[0] = new I_Block(Color.valueOf("#3498db"));
        TETRONIMO[1] = new J_Block(Color.valueOf("#f39c12")); 
        TETRONIMO[2] = new L_Block(Color.valueOf("#16a085")); 
        TETRONIMO[3] = new O_Block(Color.valueOf("#f1c40f")); 
        TETRONIMO[4] = new S_Block(Color.valueOf("#2ecc71"));
        TETRONIMO[5] = new T_Block(Color.valueOf("#9b59b6")); 
        TETRONIMO[6] = new Z_Block(Color.valueOf("#e74c3c")); 

        if (color == false) {
            TETRONIMO[index].setColor(GHOST_FILL);
        }

        return TETRONIMO[index];
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void moveRight() {
        this.undraw();
        counterX += 1;
        this.drawActive(counterX, counterY);
    }
    public void moveLeft()  {
        this.undraw();
        counterX -= 1;
        this.drawActive(counterX, counterY); 
    }
    public void moveDown()  {
        this.undraw();
        counterY += 1;
        this.drawActive(counterX, counterY); 
    }
    public void moveUp()    {
        this.undraw(); 
        this.drawActive(counterX, --counterY); 
    }
    
    public void rotate(boolean focusRestriction) {

        int previousVariationCount;
        previousVariationCount = this.variantCounter;

        this.undraw();
        this.variantCounter = (this.variantCounter + 1) % block().length;
        this.pickedVariant = this.block()[this.variantCounter];
        this.drawActive(counterX, counterY);

        if (this.isNextBlockPoked() && focusRestriction == true) {
            System.out.println("block rotation is cancelled");
            this.undraw();
            this.variantCounter = previousVariationCount;
            this.pickedVariant = this.block()[this.variantCounter];
            this.drawActive(counterX, counterY);
            isRotateCollided = true;
        } 
        else if (this.isNextGridRightPoked()) {
            while(this.isNextGridRightPoked()) {
                System.out.println("right rotation kick");
                this.undraw();
                this.drawActive(--counterX, counterY);
                if (this.isNextBlockPoked() && focusRestriction == true) {
                    System.out.println("block rotation is cancelled");
                    this.undraw();
                    this.variantCounter = previousVariationCount;
                    this.pickedVariant = this.block()[this.variantCounter];
                    this.drawActive(counterX, counterY);
                    isRotateCollided = true;
                } 
            }
        }
        else if (this.isNextGridLeftPoked()) {
            while(this.isNextGridLeftPoked()) {
                System.out.println("left rotation kick");
                this.undraw();
                this.drawActive(++counterX, counterY);
                if (this.isNextBlockPoked() && focusRestriction == true) {
                    System.out.println("block rotation is cancelled");
                    this.undraw();
                    this.variantCounter = previousVariationCount;
                    this.pickedVariant = this.block()[this.variantCounter];
                    this.drawActive(counterX, counterY);
                    isRotateCollided = true;
                } 
            }
        }
        else if(this.isNextGridBottomPoked()) {

            rotateLoopCounter++; // prevents bottom grid kick hack

            if (rotateLoopCounter >= 2 && focusRestriction == true) {
                System.out.println("block rotation is cancelled");
                this.undraw();
                this.variantCounter = previousVariationCount;
                this.pickedVariant = this.block()[this.variantCounter];
                this.drawActive(counterX, counterY);
                isRotateCollided = true;
            } 
            else {
                while (this.isNextGridBottomPoked()) {
                    if (focusRestriction == true) {System.out.println("bottom rotation kick");}
                    this.undraw();
                    this.drawActive(counterX, --counterY);
                }
            }     
        }
        else {isRotateCollided = false;}
    }

    public boolean isRotateCollided() {
        return isRotateCollided;
    }


    public void collect() {
        for ( int r = 0; r < this.getPickedVariant().length; r++) {
            for ( int c = 0; c < this.getPickedVariant().length; c++) {
                if (this.getPickedVariant()[r][c] == 1) {
                    blockCollection.add(SQUARE[r][c]);
                }
            }
        }
    }
    private Rectangle createSquare(float column, float row){
        
        Rectangle square = new Rectangle();

        square.setFill(color);
        square.setX(column);
        square.setY(row);
        square.setWidth(AREA);
        square.setHeight(AREA);
        square.setStroke(FOCUS_STROKE_COLOR);
        square.setStrokeWidth(FOCUS_STROKE_WIDTH);
        square.setSmooth(true);
        square.setCache(true);
        square.setCacheHint(CacheHint.SPEED);

        DropShadow drop = new DropShadow();  
        drop.setBlurType(BlurType.GAUSSIAN);
        drop.setColor(Color.BLACK);  
        drop.setHeight(1);  
        drop.setWidth(1);  
        drop.setSpread(0.5);  

        square.setEffect(drop);
        
        return square;
    }
    



    // uses the default and current values of the class instance (default starts in the middle|top <- 0|0 )
    public void drawActive() {
        for ( int r = 0; r < this.pickedVariant.length; r++){
            for ( int c = 0; c < this.pickedVariant.length; c++){

                if (this.pickedVariant[r][c] == 1) { 
                    column = (focusStartColumn + (counterX + c) * AREA) + GRID_XOFFSET;
                    row    = (focusStartRow + (counterY + r) * AREA) + GRID_YOFFSET;
                    
                    SQUARE[r][c] = createSquare(column, row);
                    LAYOUT.getChildren().add(SQUARE[r][c]);
                }
            }
        }
    }
    // Overload.. used only in the first creation... arguments determine the location of where it spawns on the grid
    public void drawActive(int counterX, int counterY) {
        this.counterX = counterX; this.counterY = counterY;
        for ( int r = 0; r < this.pickedVariant.length; r++){
            for ( int c = 0; c < this.pickedVariant.length; c++){

                if (this.pickedVariant[r][c] == 1) { 
                    column = (focusStartColumn + (counterX + c) * AREA) + GRID_XOFFSET;
                    row    = (focusStartRow + (counterY + r) * AREA) + GRID_YOFFSET;
                    
                    SQUARE[r][c] = createSquare(column, row);
                    LAYOUT.getChildren().add(SQUARE[r][c]);
                }
            }
        }
    }

    // absolute position, used only for hold and next panels, must instantiate its own parent
    public ArrayList<Rectangle> drawOpen() {
        ArrayList<Rectangle> block = new ArrayList<Rectangle>();
        for ( int r = 0; r < this.pickedVariant.length; r++){
            for ( int c = 0; c < this.pickedVariant.length; c++){
                if (this.pickedVariant[r][c] == 1) { 
                    /*optional*/
                    column = ((c) * AREA);
                    row    = ((r) * AREA);
                    block.add(createSquare(column, row));
                }
            }
        }
        return block;
    }

    public void undraw() {
        for ( int r = 0; r < this.pickedVariant.length; r++){
            for ( int c = 0; c < this.pickedVariant.length; c++){
                if (this.pickedVariant[r][c] == 1) { 
                    LAYOUT.getChildren().remove(SQUARE[r][c]);
                }
            }
        }
    }




    public boolean isTopPoking(Block block) {
        for (int i = 0; i < 4; i++) {
            if (     block.getActiveRow(i) >= this.getActiveRow(i)   ) {
                return true;
            }
        }
        return false;
    }
    public boolean isPoking(Block block) {
        for (int i = 0; i < 4; i++) {
            if (     block.getActiveRow(i) == this.getActiveRow(i)   ) {
                return true;
            }
        }
        return false;
    }
    public boolean isRightPoked() {
        for (int i = 0; i < 4; i++) {
            for (int j =0; j < blockCollection.size(); j++) {
                if (     ((blockCollection.get(j).getX() ) == (getActiveColumn(i) + AREA )) &&  (blockCollection.get(j).getY() == getActiveRow(i))   ) {
                    return true;
                }
            }

            if ((getActiveColumn(i) ) >= MESH[ROW-1][COL-1].getX() ) {
                return true;
            }
        }
        
        return false;
    }
    public boolean isLeftPoked() {
        for (int i = 0; i < 4; i++) {
            for (int j =0; j < blockCollection.size(); j++) {
                if (     ((blockCollection.get(j).getX() ) == (getActiveColumn(i) - AREA )) &&  (blockCollection.get(j).getY() == getActiveRow(i))   ) {
                    return true;
                }
            }

            if (getActiveColumn(i) <= MESH[ROW-1][0].getX()) {
                return true;
            }
        }
        return false;
    }
    public boolean isBottomPoked() {
        for (int i = 0; i < 4; i++) {
            for (int j =0; j < blockCollection.size(); j++) {
                if (     ((blockCollection.get(j).getY() ) == (getActiveRow(i) + AREA )) &&  (blockCollection.get(j).getX() == getActiveColumn(i))   ) {
                    return true;
                }
            }

            if (MESH[ROW-1][COL-1].getY() <= (getActiveRow(i))) {
                return true;
            }
        }
        return false;
    }
    public boolean isNextBlockPoked() {
        for (int i = 0; i < 4; i++) {
            for (int j =0; j < blockCollection.size(); j++) {

                // X | Y
                if (     ((blockCollection.get(j).getY() ) == (getActiveRow(i) )) &&  (blockCollection.get(j).getX() == getActiveColumn(i))   ) {
                    return true;
                }

            }
        }
        return false;
    }
    public boolean isNextGridRightPoked() {
        for (int i = 0; i < 4; i++) {
            if ((getActiveColumn(i) - AREA) >= MESH[ROW-1][COL-1].getX() ) {
                return true;
            }
        }
        return false;
    }
    public boolean isNextGridLeftPoked() {
        for (int i = 0; i < 4; i++) {
            if ((getActiveColumn(i) + AREA) <= MESH[ROW-1][0].getX()) {
                return true;
            }
        }
        return false;
    }
    public boolean isNextGridBottomPoked() {
        for (int i = 0; i < 4; i++) {
            if ((MESH[ROW-1][COL-1].getY() + AREA) <= (getActiveRow(i))) {
                return true;
            }
        }
        return false;
    }
    
    public int getActiveColumn(int value) { 
        int counter = 0;
        for ( int r = 0; r < this.pickedVariant.length; r++) {
            for ( int c = 0; c < this.pickedVariant.length; c++) {
                if (SQUARE[r][c] == null || this.pickedVariant[r][c] == 0 ) {continue; }
                    if (counter == value) {
                        return (int)SQUARE[r][c].getX();
                    }
                    counter++;
            }
        }

        return 0;
    }
    public int getActiveRow(int value) { 
        int counter = 0;
        for ( int r = 0; r < this.pickedVariant.length; r++) {
            for ( int c = 0; c < this.pickedVariant.length; c++) {
                if (SQUARE[r][c] == null || this.pickedVariant[r][c] == 0 ) {continue; }
                    if (counter == value) {
                        return (int)SQUARE[r][c].getY();
                    }
                    counter++;
            }
        }
        return 0;
    }
    public int[][] getPickedVariant() {
        return pickedVariant;
    }
}