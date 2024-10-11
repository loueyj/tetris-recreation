package Pieces;

import java.awt.*;

// T
public class Mino_T extends Mino {

    public Mino_T() {
        create(Color.magenta);
    }

    public void setXY(int x, int y) {
        //    o       3
        //  o o o   1 0 2
        //

        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x - Block.SIZE;
        b[1].y = b[0].y;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x;
        b[3].y = b[0].y - Block.SIZE;
    }

    public void getDirection1() {
        //    o       3
        //  o o o   1 0 2
        //

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(1);
    }

    public void getDirection2() {
        //  o     1
        //  o o   0 3
        //  o     2

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y;

        updateXY(2);
    }

    public void getDirection3() {
        //
        //  o o o   2 0 1
        //    o       3

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x - Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(3);
    }

    public void getDirection4() {
        //    o     2
        //  o o   3 0
        //    o     1

        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y + Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y;

        updateXY(4);
    }

}
