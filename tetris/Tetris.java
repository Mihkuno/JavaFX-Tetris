package proj.tetris;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import proj.MainInterface;
import proj.style.Animate;
import proj.tetris.block.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Tetris implements MainInterface, EventHandler<KeyEvent>, TetrisInterface{

    AudioClip fx_drop, fx_move, fx_rotate, fx_hold, fx_lock, fx_clear, fx_combo1, fx_combo2, fx_combo3;

    Rectangle nextPanel, holdPanel, scorePanel;
    GridPane panelContainer, blockContainer, scoreContainer, titleContainer, nextContainer, holdContainer;
    Text title_score, title_combo, title_count, title_lines, title_level, title_gain, title_hold, title_next, title_speed;

    private Text val_gain = new Text(Integer.toString(GAINLN) +" / " + Integer.toString(GAINUP));
    private Text val_score = new Text(Double.toString(SCORE));
    private Text val_count = new Text(Integer.toString(COUNT));
    private Text val_combo = new Text(Integer.toString(COMBO));
    private Text val_lines = new Text(Integer.toString(LINES));
    private Text val_level = new Text(Integer.toString(LEVEL));
    private Text val_next = new Text(Integer.toString(NEXT));
    private Text val_speed = new Text(Double.toString(SPEED));
    
    private int variant;
    private Block focus, ghost;
    private AnimationTimer gameLoop;
    private int[] nextIndex = new int[5]; 
    private Pane[] next = new Pane[nextIndex.length];
    private Block[] nextBlock = new Block[nextIndex.length]; 

    private boolean showGhost = true;
    private boolean startGame = false;
    private boolean delayGravity;

    static double SPEEDMS = 1000;
    static double SCORE = 0;
    static double SPEED = 0;
    static int GAINLN = 0;
    static int GAINUP = 5;
    static int PAREA = 17;
    static int COUNT = 0;
    static int COMBO = 0;
    static int LEVEL = 0;
    static int LINES = 0;
    static int NEXT = 0;

    private Block HOLD = null;
    private boolean hasSwitched = false;

    public Tetris() {
       startGame(true);
    }

    public void startGame(boolean isready) {
        startGame = isready;

        gameLoop = new AnimationTimer(){

            long prevTime = 0;   
            
            @Override
            public void handle(long now) {


                double sleepMs = delayGravity ? (SPEEDMS + 350) : SPEEDMS;
                double sleepNs = sleepMs * 1_000_000;

                SPEED = SPEEDMS/1000;
                                
                // some delay
                if ((now - prevTime) < sleepNs) {
                    return;
                }
                
                prevTime = now;

                initGravity();
            }
        };

        if (startGame == true) {
            
            this.generateGrid(false);
            this.generateNext();
            this.generatePanel();
            this.generateFocus();
            this.generateGhost();
            
            DOCUMENT.setOnKeyPressed(this);
            gameLoop.start();  
        }
        else {
            // LOADING ANIMATION
            Image bg_anim = new Image("proj/image/bg_anim.gif");
            ImageView view_bg_anim = new ImageView(bg_anim);
            LAYOUT.getChildren().add(view_bg_anim);

        }
    }


    @Override
    public void handle(KeyEvent key) {

        if (key.getText().equals("w")) { 
            fx_rotate = new AudioClip(this.getClass().getResource("../sound/fx_rotate.mp3").toExternalForm());
            fx_rotate.play();
            focus.rotate(true); 
            if (showGhost && !focus.isRotateCollided()) {
                ghost.rotate(false);
            }
            if (showGhost) {
                while (!ghost.isTopPoking(focus) || ghost.isNextBlockPoked()) {
                    ghost.moveUp();
                }
        
                while ( !(ghost.isBottomPoked()) ) {
                    ghost.moveDown();
                }
                focus.redrawActive();
            }   
        }
        if (key.getText().equals("d")) { 
            if (!focus.isRightPoked()) {
                focus.moveRight();  
                fx_move = new AudioClip(this.getClass().getResource("../sound/fx_move.mp3").toExternalForm());
                fx_move.play();
                if (showGhost){
                    ghost.moveRight();
                } 
            }
            if (showGhost) {
                while (!ghost.isTopPoking(focus) || ghost.isNextBlockPoked()) {
                    ghost.moveUp();
                }
        
                while ( !(ghost.isBottomPoked()) ) {
                    ghost.moveDown();
                }
                focus.redrawActive();
            }   
        }
        if (key.getText().equals("a")) { 
            if (!focus.isLeftPoked()) {
                focus.moveLeft();
                fx_move = new AudioClip(this.getClass().getResource("../sound/fx_move.mp3").toExternalForm());
                fx_move.play();
                if (showGhost) {
                    ghost.moveLeft();
                }  
            }
            if (showGhost) {
                while (!ghost.isTopPoking(focus) || ghost.isNextBlockPoked()) {
                    ghost.moveUp();
                }
        
                while ( !(ghost.isBottomPoked()) ) {
                    ghost.moveDown();
                }
                focus.redrawActive();
            }   
        }
        if (key.getText().equals("s")) { 
            if (!focus.isBottomPoked()) {
                focus.moveDown();

                fx_move = new AudioClip(this.getClass().getResource("../sound/fx_move.mp3").toExternalForm());
                fx_move.play();
                            
                SCORE += 0.01;
                this.delayGravity = true;
                val_score.setText(Double.toString(Math.floor(SCORE * 100) / 100));
            }
        }
        if (key.getCode() == KeyCode.SPACE) {            
            while ( !(focus.isBottomPoked()) ) {
                focus.moveDown();
                SCORE += 0.01;
            }
            initGravity();
        }
        if (key.getCode() == KeyCode.SHIFT) {

            // adds right and bottom margin in order to center O Block on the next panel
            PAREA = 18; int x = nextIndex[0] == 3 ? -PAREA/2 : 0; int y = nextIndex[0] == 3 ? -PAREA : 0; 

            if (holdContainer.getChildren().isEmpty()) {

                HOLD = nextBlock[0];
    
                focus.undraw();
                ghost.undraw();
    
                // pull the next indexes
                generateNext();
    
                // redraw the generated next nodes
                updateNext();
    
                generateFocus();
            }
    
            else if (hasSwitched == true) {
                fx_lock = new AudioClip(this.getClass().getResource("../sound/fx_lock.mp3").toExternalForm());
                fx_lock.play();
            }   
    
            else if (!holdContainer.getChildren().isEmpty()) {
    
                // map class names to their index
                HashMap<String,Integer> blockIndex = new HashMap<String,Integer>();
                blockIndex.put("I_Block",0);
                blockIndex.put("J_Block",1);
                blockIndex.put("L_Block",2);
                blockIndex.put("O_Block",3);
                blockIndex.put("S_Block",4);
                blockIndex.put("T_Block",5);
                blockIndex.put("Z_Block",6);
    
                // clear the old hold
                holdContainer.setGridLinesVisible(false);
                holdContainer.getChildren().clear();
    
                // new hold << focus
                String newHold = focus.getClass().getSimpleName();
    
                // undraw active
                focus.undraw();
                ghost.undraw();
    
                // generate focus previous hold
                String oldHold = HOLD.getClass().getSimpleName();
    
                generateFocus(blockIndex.get(oldHold));
    
                // switch the previous hold to new hold
                HOLD = Block.select(blockIndex.get(newHold), true);
    
            }       
    
            if (hasSwitched == false) {
    
                hasSwitched = true;
    
                fx_hold = new AudioClip(this.getClass().getResource("../sound/m_close.mp3").toExternalForm());
                fx_hold.play();
    
                next[0] = new Pane();
                next[0].setMinWidth(PAREA * 3);
                next[0].getChildren().addAll(HOLD.drawOpen(x, y, PAREA));
    
                holdContainer.setGridLinesVisible(false);
                holdContainer.add(next[0], 0, 0);
                holdContainer.setAlignment(Pos.CENTER);
                holdContainer.setGridLinesVisible(true);
    
                generateGhost();
            }
        }
    }


    private void updateScore() {
        val_gain.setText(Integer.toString(GAINLN) +" / " + Integer.toString(GAINUP));
        val_score.setText(Double.toString(Math.floor(SCORE * 100) / 100));
        val_combo.setText(Integer.toString(COMBO));
        val_count.setText(Integer.toString(COUNT));
        val_level.setText(Integer.toString(LEVEL));
        val_lines.setText(Integer.toString(LINES));
        val_next.setText(Integer.toString(NEXT));
        val_speed.setText("x"+Double.toString(SPEED));
    }

    private void updateNext() {
        // validates a (not empty) start
        if (nextIndex != null) {;

            // clears the next panel
            nextContainer.setGridLinesVisible(false);
            nextContainer.getChildren().clear();


            // loop through the next panel grid
            for (int i = 1; i < nextIndex.length; i++) {
                
                // adds right and bottom margin in order to center O Block on the next panel
                PAREA = 18; int x = nextIndex[i] == 3 ? -PAREA/2 : 0; int y = nextIndex[i] == 3 ? -PAREA : 0; 

                next[i] = new Pane();
                next[i].setMinWidth(PAREA * 3);
                next[i].getChildren().addAll(nextBlock[i].drawOpen(x,y,PAREA));

                nextContainer.add(next[i], 0, i-1);
            }

            nextContainer.setGridLinesVisible(true);
        }
    }
    
    
    public void generatePanel() {
        Font title_font = Font.loadFont( Tetris.class.getClassLoader().getResourceAsStream( "proj/font/TrulyMadlyDpad-a72o.ttf"), 17);
        Font val_font = Font.loadFont( Tetris.class.getClassLoader().getResourceAsStream( "proj/font/TrulyMadlyDpad-a72o.ttf"), 13);

        panelContainer = new GridPane();
        blockContainer = new GridPane();
        scoreContainer = new GridPane();
        titleContainer = new GridPane();
        nextContainer = new GridPane();
        holdContainer = new GridPane();

        nextPanel = new Rectangle();
        holdPanel = new Rectangle();
        scorePanel = new Rectangle();

        title_score = new Text("Score");
        title_combo = new Text("Combo");
        title_count = new Text("Count");
        title_lines = new Text("Lines");
        title_level = new Text("Level");
        title_gain = new Text("Gain");
        title_hold = new Text("Hold");
        title_next = new Text("Next");
        title_speed = new Text("Speed");      

        val_score.setFont(val_font);
        val_combo.setFont(val_font);
        val_count.setFont(val_font);
        val_lines.setFont(val_font);
        val_level.setFont(val_font);
        val_gain.setFont(val_font);
        val_next.setFont(val_font);
        val_speed.setFont(val_font);

        title_score.setFont(title_font);
        title_combo.setFont(title_font);
        title_count.setFont(title_font);
        title_level.setFont(title_font);
        title_gain.setFont(title_font);
        title_lines.setFont(title_font);
        title_speed.setFont(title_font);

        title_hold.setFont(val_font);
        title_next.setFont(val_font);

        title_hold.setTranslateX(6);
        title_hold.setTranslateY(-52);

        title_next.setTranslateX(6);
        title_next.setTranslateY(-162);
        
        scorePanel.setWidth(PANEL_WIDTH);
        scorePanel.setHeight(420);
        scorePanel.setArcWidth(PANEL_ARC);
        scorePanel.setArcHeight(PANEL_ARC);
        scorePanel.setFill(GRID_FILL);

        nextPanel.setWidth(PANEL_WIDTH);
        nextPanel.setHeight(350);
        nextPanel.setArcWidth(PANEL_ARC);
        nextPanel.setArcHeight(PANEL_ARC);
        nextPanel.setFill(GRID_FILL);

        holdPanel.setWidth(PANEL_WIDTH);
        holdPanel.setHeight(125);
        holdPanel.setArcWidth(PANEL_ARC);
        holdPanel.setArcHeight(PANEL_ARC);
        holdPanel.setFill(GRID_FILL);

        titleContainer.setMinWidth(scoreContainer.getWidth());
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.add(title_score,   0, 0);
        titleContainer.add(val_score,     0, 1);
        titleContainer.add(title_combo,   0, 2);
        titleContainer.add(val_combo,     0, 3);
        titleContainer.add(title_count,   0, 4);
        titleContainer.add(val_count,     0, 5);
        titleContainer.add(title_lines,   0, 6);
        titleContainer.add(val_lines,     0 ,7);
        titleContainer.add(title_level,   0, 8);
        titleContainer.add(val_level,     0, 9);
        titleContainer.add(title_gain,    0, 10);
        titleContainer.add(val_gain,      0, 11);
        titleContainer.add(title_speed,   0, 12);
        titleContainer.add(val_speed,     0, 13);

        val_score.setTranslateY(-8);
        val_combo.setTranslateY(-8);
        val_count.setTranslateY(-8);
        val_level.setTranslateY(-8);
        val_gain.setTranslateY(-8);
        val_lines.setTranslateY(-8);
        val_speed.setTranslateY(-8);

        titleContainer.setVgap(11);
        titleContainer.setHgap(10);

        scoreContainer.add(scorePanel,     0, 0);
        scoreContainer.add(titleContainer, 0, 0);

        blockContainer.add(holdPanel,     0, 0);
        blockContainer.add(title_hold,    0, 0);
        blockContainer.add(holdContainer, 0, 0);
        blockContainer.add(nextPanel,     0, 1);
        blockContainer.add(title_next,    0 ,1);
        blockContainer.add(nextContainer, 0, 1);
        blockContainer.setVgap(20);     

        nextContainer.setVgap(20);
        nextContainer.setMaxHeight(nextPanel.getHeight());
        nextContainer.setAlignment(Pos.CENTER);
        
        /* block generator here for nextContainer */
        this.updateNext();

        panelContainer.setAlignment(Pos.CENTER);
        panelContainer.setMinHeight(DOCUMENT_HEIGHT);
        panelContainer.setMinWidth(DOCUMENT_WIDTH);
        panelContainer.setVgap(0); 
        panelContainer.setHgap(330);  

        panelContainer.add(scoreContainer, 0, 0);
        panelContainer.add(blockContainer, 1, 0);

        LAYOUT.getChildren().addAll(panelContainer);
    }
    
    public void generateNext() {
        Random random = new Random();        

        // generates four integer variants on start game
        if (COUNT == 0 && HOLD == null) {
            for (int i = 0; i < nextIndex.length; i++) {
                nextIndex[i] = random.nextInt(Block.length);
            }
        } 
        else {
            // pull the first three index up
            for (int i = 0; i < nextIndex.length-1; i++) {
                nextIndex[i] = nextIndex[i+1];
            }     

            // generate a new index for the last array
            nextIndex[nextIndex.length-1] = random.nextInt(Block.length);
        }

        // generate the blocks accoridingly to the indexes
        for (int i = 0; i < nextIndex.length; i++) {
            nextBlock[i] = Block.select(nextIndex[i], true);
        }
    }


    public void generateFocus() {
        variant = nextIndex[0];
        focus = Block.select(variant, true);
        focus.drawActive();
        System.out.println("A new block has been created"); 
        System.out.printf("%d | %d | %d | %d\n",nextIndex[0],nextIndex[1],nextIndex[2],nextIndex[3]);
    }


    public void generateGhost() {    
        ghost = Block.select(variant, false);
        if (showGhost) {
            ghost.drawActive();
            do { ghost.moveDown();
            } while ( !ghost.isBottomPoked() );
        }
    }


    public void generateFocus(int variant) {
        this.variant = variant;
        focus = Block.select(variant, true);
        focus.drawActive();
        System.out.println("A new block has been created"); 
        System.out.printf("%d | %d | %d | %d\n",nextIndex[0],nextIndex[1],nextIndex[2],nextIndex[3]);
    }


    public void generateGrid(boolean enableAnimation) {
        for ( int r = 0; r < ROW; r++){
            for( int c = 0; c < COL; c++){
                Rectangle square = new Rectangle();
                square.setFill(GRID_FILL);
                square.setX((c * AREA)  + GRID_XOFFSET );
                square.setY((r * AREA)  + GRID_YOFFSET);
                square.setWidth(AREA);
                square.setHeight(AREA);
                square.setStroke(GRID_STROKE_COLOR);
                square.setStrokeWidth(GRID_STROKE_WIDTH);
                square.setCache(true);
                square.setCacheHint(CacheHint.SPEED);

                MESH[r][c] = square;
                LAYOUT.getChildren().add(MESH[r][c]);

                if (enableAnimation == true) {new Animate().start_swipe(MESH[r][c], false, 800);}
                
            }
        } 
    }


    public void initGravity() {
        System.out.println("---------------------------------------------------");
        if (focus.isBottomPoked()) { 

            fx_drop = new AudioClip(this.getClass().getResource("../sound/fx_lnd01.mp3").toExternalForm()); fx_drop.play();

            COUNT++; SCORE += 0.5; hasSwitched = false;

            ghost.undraw(); focus.collect();
            
            validateLine();
            generateNext();
            generateFocus();
            generateGhost(); 

            this.updateScore();
            this.updateNext();
        } 
        else {
            focus.moveDown();
            delayGravity = false;        
        }
    }


    public void validateLine() throws IndexOutOfBoundsException {

        /* CLEARS FULL ROW LINES
            if counter of row (y) of block's squares equal 9 (full row), then...
            remove all the squares that correspond to the row layout and collection 
            compared through their y attribute
        */
        
        // make a clone of the original blockCollection for (full row) valdation
        // produces ConcurrentModification exception if the original was updated during validation
        @SuppressWarnings("unchecked") 
        ArrayList<Rectangle> blockCollectionClone = (ArrayList<Rectangle>) blockCollection.clone();

        // make array and add of all y values from the clone
        ArrayList<Integer> CLEAR_blockY = new ArrayList<Integer>();

        for (Rectangle item : blockCollectionClone) {
            CLEAR_blockY.add((int)item.getY());
        }

        // create a set with values from the y values array to prevent duplicates
        Set<Integer> CLEAR_set = new HashSet<>(CLEAR_blockY);

        // clear the original y values array then change its values to that of the set
        CLEAR_blockY.clear(); CLEAR_blockY.addAll(CLEAR_set);

        // sort non-duplicate y values and the collection clone for loop mapping
        Collections.sort(CLEAR_blockY);
        blockCollectionClone.sort(Comparator.comparing(item -> item.getY()));

        int dupleCounter = 0;
        int removedCounter = 0;
        float removedLastRow = 0;
        boolean clearLine = false;
        
        for (Integer y : CLEAR_blockY) {
            for (Rectangle linerow : blockCollectionClone) {
                if (y == linerow.getY()) {
                    if ((COL-1) == dupleCounter) {
                        System.out.println("LINE FULL! MUST CLEAR");
                        for (Rectangle squarerow : blockCollectionClone) {
                            if (y == squarerow.getY()) {
                                try {
                                    LAYOUT.getChildren().remove(
                                    blockCollection.get(blockCollection.indexOf(squarerow)));

                                    blockCollection.remove(
                                    blockCollection.indexOf(squarerow));
                                    removedCounter++;

                                    
                                    if ( ((removedCounter%10) == 0)) {
                                        clearLine = true;
                                        removedLastRow = y;
                                    }
                                    
                                } catch(IndexOutOfBoundsException e) {}
                            }
                        }
                    }      
                    else { dupleCounter++; }
                } 
                else {dupleCounter = 0;}
            }
        }

        /* RECURSIVE BLOCK GRAVITY ON CLEAR CALL
            if a line clear was detected, starting from the bottom, filter all the rows above the removed lastrow 
            then pull down the blocks of the first occurence of a filled last-row (above the removed lastrow) until they have equal values
            if the blocks has equal value as the removed lastrow, then raise the removed lastrow on row above it
            which acts as a recursive pull while maintaining row holes
            loop, then proceed to filter the nextIndex row until theres no more rows above removed lastrow
        */

        if (clearLine == true) {

            fx_clear = new AudioClip(this.getClass().getResource("../sound/fx_lne01.mp3").toExternalForm());
            fx_combo1 = new AudioClip(this.getClass().getResource("../sound/fx_lne02.mp3").toExternalForm());
            fx_combo2 = new AudioClip(this.getClass().getResource("../sound/fx_lne03.mp3").toExternalForm());
            fx_combo3 = new AudioClip(this.getClass().getResource("../sound/fx_lne04.mp3").toExternalForm());

            switch(COMBO) {
                case (0) -> {fx_clear.play();   COMBO++;}
                case (1) -> {fx_combo1.play();  COMBO++;}
                case (2) -> {fx_combo2.play();  COMBO++;}
                default  -> {fx_combo3.play();  COMBO++;}
            }
            SCORE += ((3 * LEVEL) * COMBO);
            
            LINES += (removedCounter/10);
            GAINLN += (removedCounter/10);

            if (GAINLN >= GAINUP) {
                LEVEL++;
                GAINLN = 0;
                GAINUP += (5 + LEVEL);
                SPEEDMS -= 100;
            }

            System.out.println("removedCounter: "+removedCounter/10);
            System.out.println("removed lastRow: "+removedLastRow);

            // make array and add of all y values from the original
            ArrayList<Integer> PULL_blockY = new ArrayList<Integer>();

            for (Rectangle item : blockCollection) {
                PULL_blockY.add((int)item.getY());
            }

            // create a set with values from the y values array to prevent duplicates
            Set<Integer> PULL_set = new HashSet<>(PULL_blockY);

            // clear the original y values array then change its values to that of the set
            PULL_blockY.clear(); PULL_blockY.addAll(PULL_set);

            // reversely sort all non-duplicate y values for loop filtering
            Collections.sort(PULL_blockY); Collections.reverse(PULL_blockY);


            System.out.println("---------------------------------------------------");
            for (Rectangle item : blockCollection) {
                System.out.println(item);
            }
            for (Integer item : PULL_blockY) {
                System.out.print(item + " | ");
            }
            System.out.println("\npulling floating blocks..");          

            boolean decreaseLastRow = false;

            // contains non-duplicate y values in the blockCollection
            for (int i = 0; i < PULL_blockY.size(); i++) {
                // contains all the output blocks except for the focus
                for (Rectangle floatingSquare : blockCollection) {
                    // filter the squares above the removed lastrow
                    if (floatingSquare.getY() < removedLastRow) {
                        // filter the rows of each square from bottom to top
                        if (floatingSquare.getY() == PULL_blockY.get(i)) {
                            // while square is above removed lastrow, go towards it (down)
                            while(floatingSquare.getY() < removedLastRow) {
                                floatingSquare.setY(floatingSquare.getY() + AREA);
                                // stops when the floating square is equal to the removed lastrow
                            }
                            // raise the removed lastrow to recursively pull down the remaining squares above it
                            decreaseLastRow = true;
                        }                       
                    }
                }
                // get out of the row filter loop 
                // if previous row went towards the removed last row
                // then raise the removed lastrow above the previous row
                if (decreaseLastRow == true) {
                    removedLastRow = removedLastRow - AREA;
                    decreaseLastRow = false; 
                }
                // proceed to the nextIndex row filter group
            }
        } 
        else {COMBO = 0;}
        
    }


    @Deprecated
    public void showFocusDetails() {
        // System.out.println(

        //     "ActCol: "   + focus.getFocusColumn(0) + " " 
        //                  + focus.getFocusColumn(1) + " " 
        //                  + focus.getFocusColumn(2) + " " 
        //                  + focus.getFocusColumn(3) + " | " +
                            
        //     "GridCol: "  + GRID[ROW-1][COL-1].getX() + "   " +

        //     "ActRow: "   + focus.getFocusRow(0) + " " 
        //                  + focus.getFocusRow(1) + " " 
        //                  + focus.getFocusRow(2) + " " 
        //                  + focus.getFocusRow(3) + " | " +

        //     "GridRow: "  + GRID[ROW-1][COL-1].getY() + "   " +
        //     "SidePoke: " + sidePoke + " | " +
        //     "EndPoke: "  + endPoke + "   " +
        //     "VarCount: " + focus.getVariationCounter()

        // );

        // System.out.println(Block.blockCounter);
      
        // System.out.println("---------------------------------------------------");
        // for (Rectangle item : blockCollection) {
        //     System.out.println(item);
        // }
        
    }


    
}