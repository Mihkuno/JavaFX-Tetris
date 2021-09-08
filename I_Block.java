package src;

import javafx.scene.paint.Color;

public class I_Block extends Block {

    public I_Block(Color color) {
        super(color);
    }

    @Override
    protected int[][][] block() {
        int[][][] coordinates = {
            {
                {0,1,0,0},
                {0,1,0,0},
                {0,1,0,0},
                {0,1,0,0}
            },

            {
                {0,0,0,0},
                {1,1,1,1},
                {0,0,0,0},
                {0,0,0,0}
            },

            {
                {0,0,1,0},
                {0,0,1,0},
                {0,0,1,0},
                {0,0,1,0}
            },

            {
                {0,0,0,0},
                {0,0,0,0},
                {1,1,1,1},
                {0,0,0,0}
            },
            
            
            

            

            
            
            

            
            
        };
        return coordinates;
    }

}
