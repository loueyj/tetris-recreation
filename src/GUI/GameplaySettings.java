package GUI;

import Pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameplaySettings {

    // Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // Others
    public static int dropInterval = 60; // Mino drops once every second (every 60 frames)
    boolean gameOver;

    // Effects
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    // Score Keeping
    int level = 1;
    int lines;
    int score;

    public GameplaySettings() {

        // Main Play Area Frame
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        // Mino starting position at the top-middle of the play area
        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        // Set the starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private Mino pickMino() {

        // Pick random Mino
        Mino mino = null;
        int rand = new Random().nextInt(7);

        switch(rand) {
            case 0: mino = new Mino_I(); break;
            case 1: mino = new Mino_J(); break;
            case 2: mino = new Mino_L(); break;
            case 3: mino = new Mino_O(); break;
            case 4: mino = new Mino_S(); break;
            case 5: mino = new Mino_T(); break;
            case 6: mino = new Mino_Z(); break;
            default: break;
        }

        return mino;
    }

    public void update() {

        // Check if the currentMino is active
        if (!currentMino.active) {
            // If the currentMino is no longer active, then put it into the staticBlocks list
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // Check to see if the game is over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) { // Mino was not able to move from the starting position
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }

            // Reset mino deactivating state
            currentMino.deactivating = false;

            // Replace the currentMino with the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // When a Mino becomes inactive, check the staticBlocks list to see if any lines can be deleted
            checkDelete();
        }
        else {
            currentMino.update();
        }
    }

    private void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (Block i : staticBlocks) {
                if (i.x == x & i.y == y) {
                    // Increase the blockCount if there is a static block on the line
                    blockCount++;
                }
            }
            x += Block.SIZE;

            if (x == right_x) {
                // Row is completely filled with staticBlocks so the line can be deleted
                if (blockCount == 12) {

                    effectCounterOn = true;
                    effectY.add(y); // Add the line where the line was completed to the effect array to display effect on the line

                    for (int i = staticBlocks.size()-1; i >= 0; i--) {
                        // Remove the staticBlocks in the line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    // Increase the level and the piece drop speed every time 10 lines are created
                    if (lines % 10 == 0 && dropInterval > 1) {

                        level++;
                        if (dropInterval > 10) { // dropInterval starting value of 60
                            dropInterval -= 10;
                        }
                        else {
                            dropInterval -= 1;
                        }
                    }

                    // After a line has been deleted, all the blocks above the line need to move down
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // If the block is above the deleted line, then the block is moved down by one block size
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        // Add Score
        if (lineCount > 0) {
            GamePanel.se.play(1, false);
            score += 10 * level * lineCount;
        }
    }

    public void draw(Graphics2D g2) {

        // Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        // Draw Next Piece Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);

        // Draw Score Frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y);
        g2.drawString("LINES: " + lines, x, y + 70);
        g2.drawString("SCORE: " + score, x, y + 140);

        // Draw the current Mino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // Draw the next Mino
        nextMino.draw(g2);

        // Draw Static Blocks
        for (Block i : staticBlocks) {
            i.draw(g2);
        }

        // Draw Effects
        if (effectCounterOn) {

            effectCounter++;
            g2.setColor(Color.white);
            for (int i : effectY) {
                g2.fillRect(left_x, i, WIDTH, Block.SIZE);
            }

            // Reset the effect
            if (effectCounter == 10) { // Display effect for 10 frames
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // Draw Text Screens
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameOver) { // Game Over Screen
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }
        if (KeyHandler.pausePressed) { // Pause Screen
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        // Draw Left Side
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 80));
        x = 140;
        y = 80;
        g2.drawString("Tetris", x, y); // Title
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        x = 40;
        y = 200;
        g2.drawString("W: Rotate Piece", x, y);
        y = 300;
        g2.drawString("A: Move Left", x, y);
        y = 400;
        g2.drawString("S: Move Down", x, y);
        y = 500;
        g2.drawString("D: Move Right", x, y);
        y = 600;
        g2.drawString("SPACE: Pause", x, y);
    }
}
