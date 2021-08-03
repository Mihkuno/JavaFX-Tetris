package proj.tetris.block;

import javafx.scene.paint.Color;

public class S_Block extends Block {

    public S_Block(Color color) {
        super(color);
    }
    @Override
    protected int[][][] block() {
        int[][][] coordinates = {
            {
                {0,1,1},
                {1,1,0},
                {0,0,0},
            },
            {
                {1,0,0},
                {1,1,0},
                {0,1,0},
            },
            {
                {0,0,0},
                {0,1,1},
                {1,1,0},
            },
            {
                {0,1,0},
                {0,1,1},
                {0,0,1},
            }
        };
        return coordinates;
    }
}
