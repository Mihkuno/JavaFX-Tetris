package src;
import javafx.scene.paint.Color;

public class Z_Block extends Block {


    public Z_Block(Color color) {
        super(color);
    }
    @Override
    protected int[][][] block() {
        int[][][] coordinates = {
            {
                {1,1,0},
                {0,1,1},
                {0,0,0},
            },
            {
                {0,0,1},
                {0,1,1},
                {0,1,0},
            },
            {
                {0,0,0},
                {1,1,0},
                {0,1,1},
            },
            {
                {0,1,0},
                {1,1,0},
                {1,0,0},
            }
        };
        return coordinates;
    }
}
