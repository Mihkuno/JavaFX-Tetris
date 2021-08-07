package proj.tetris.block;

import javafx.scene.CacheHint;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
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
    
    
    protected Color color; 

    public Block(Color color) {
        this.color = color;
    }

    protected abstract int[][][] block();

    public static Block create(Block block) {
        return block;
    }

    public void show() {
        drawActive(counterX, counterY);
    }
    public void remove() {
        undrawActive();
    }

    public void moveRight() {
        this.undrawActive();
        counterX += 1;
        this.drawActive(counterX, counterY);
    }
    public void moveLeft()  {
        this.undrawActive();
        counterX -= 1;
        this.drawActive(counterX, counterY); 
    }
    public void moveDown()  {
        this.undrawActive();
        counterY += 1;
        this.drawActive(counterX, counterY); 
    }
    public void moveUp()    {
        this.undrawActive(); 
        this.drawActive(counterX, --counterY); 
    }
    
    public void rotate(boolean focusRestriction) {

        int previousVariationCount;
        previousVariationCount = this.variantCounter;

        this.undrawActive();
        this.variantCounter = (this.variantCounter + 1) % block().length;
        this.pickedVariant = this.block()[this.variantCounter];
        this.drawActive(counterX, counterY);

        if (this.isNextBlockPoked() && focusRestriction == true) {
            System.out.println("block rotation is cancelled");
            this.undrawActive();
            this.variantCounter = previousVariationCount;
            this.pickedVariant = this.block()[this.variantCounter];
            this.drawActive(counterX, counterY);
            isRotateCollided = true;
        } 
        else if (this.isNextGridRightPoked()) {
            while(this.isNextGridRightPoked()) {
                System.out.println("right rotation kick");
                this.undrawActive();
                this.drawActive(--counterX, counterY);
                if (this.isNextBlockPoked() && focusRestriction == true) {
                    System.out.println("block rotation is cancelled");
                    this.undrawActive();
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
                this.undrawActive();
                this.drawActive(++counterX, counterY);
                if (this.isNextBlockPoked() && focusRestriction == true) {
                    System.out.println("block rotation is cancelled");
                    this.undrawActive();
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
                this.undrawActive();
                this.variantCounter = previousVariationCount;
                this.pickedVariant = this.block()[this.variantCounter];
                this.drawActive(counterX, counterY);
                isRotateCollided = true;
            } 
            else {
                while (this.isNextGridBottomPoked()) {
                    if (focusRestriction == true) {System.out.println("bottom rotation kick");}
                    this.undrawActive();
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
    
    private void drawActive(int counterX, int counterY) {
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
    private void undrawActive() {
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
