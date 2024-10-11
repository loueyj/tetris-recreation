package Pieces;

import java.awt.*;

public class Block extends Rectangle {

    public int x, y;
    public static final int SIZE = 30; // 30 by 30 block
    public Color c;

    public Block(Color c) {
        this.c = c;
    }

    public void draw(Graphics2D g2) {
        int outline = 2;
        g2.setColor(c);
        g2.fillRect(x + outline, y + outline, SIZE - (outline * 2), SIZE - (outline * 2));
    }
}
