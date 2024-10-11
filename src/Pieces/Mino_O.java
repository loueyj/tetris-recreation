package Pieces;

import java.awt.*;

// Square
public class Mino_O extends Mino {

    public Mino_O() {
        create(Color.yellow);
    }

    public void setXY(int x, int y) {
        //  o o  0 1
        //  o o  2 3
        //

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x + Block.SIZE;
        b[1].y = b[0].y;
        b[2].x = b[0].x;
        b[2].y = b[0].y + Block.SIZE;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }

    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}

}
