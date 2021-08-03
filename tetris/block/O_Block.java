package proj.tetris.block;

import javafx.scene.paint.Color;

public class O_Block extends Block {

    public O_Block(Color color) {
        super(color);
    }
    @Override
    protected int[][][] block() {
        int[][][] coordinates = {
            {
                {0,0,0,0},
                {0,1,1,0},
                {0,1,1,0},
                {0,0,0,0}
            },
        };
        return coordinates;
    }

}
