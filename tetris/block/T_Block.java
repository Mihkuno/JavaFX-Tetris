package proj.tetris.block;

import javafx.scene.paint.Color;

public class T_Block extends Block {
    public T_Block(Color color) {
        super(color);
    }
    @Override
    protected int[][][] block() {
        int[][][] coordinates = {
            {
                {0,1,0},
                {1,1,1},
                {0,0,0},
            },
            {
                {0,1,0},
                {0,1,1},
                {0,1,0},
            },
            {
                {0,0,0},
                {1,1,1},
                {0,1,0},
            },
            {
                {0,1,0},
                {1,1,0},
                {0,1,0},
            }
        };
        return coordinates;
    }
    
}
