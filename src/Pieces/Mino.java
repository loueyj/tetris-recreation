package Pieces;

import GUI.GamePanel;
import GUI.GameplaySettings;
import GUI.KeyHandler;

import java.awt.*;

public class Mino {

    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    public int direction = 1; // Four total directions (1/2/3/4) determining the orientation of the Mino
    public boolean active = true;
    public boolean deactivating;
    int autoDropCounter = 0;
    int deactivateCounter = 0;
    boolean leftCollision, rightCollision, bottomCollision;

    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);

        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {

    }

    public void updateXY(int direction) {

        checkRotationCollision();

        if (!leftCollision && !rightCollision && !bottomCollision) {
            this.direction = direction;
            // Store values in temporary array to handle restoration in the case of collisions
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }

    // Overridden in individual mino classes
    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}

    public void checkMovementCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // Check Mino collision with the static blocks
        checkStaticBlockCollision();

        // Check Mino collision with the frame walls
        // Left Wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == GameplaySettings.left_x) {
                leftCollision = true;
                break;
            }
        }
        // Right Wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x + Block.SIZE == GameplaySettings.right_x) { // Block origin point is top left of the block
                rightCollision = true;
                break;
            }
        }
        // Bottom wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].y + Block.SIZE == GameplaySettings.bottom_y) {
                bottomCollision = true;
                break;
            }
        }
    }

    public void checkRotationCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // Check Mino collision with the static blocks
        checkStaticBlockCollision();

        // Check Mino collision with the frame walls
        // Left Wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < GameplaySettings.left_x) {
                leftCollision = true;
                break;
            }
        }
        // Right Wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > GameplaySettings.right_x) { // Block origin point is top left of the block
                rightCollision = true;
                break;
            }
        }
        // Bottom wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > GameplaySettings.bottom_y) {
                bottomCollision = true;
                break;
            }
        }
    }

    private void checkStaticBlockCollision() {

        for (Block i : GameplaySettings.staticBlocks) {

            int targetX = i.x;
            int targetY = i.y;

            // Check static blocks to the left
            for (Block j : b) {
                if (j.x - Block.SIZE == targetX && j.y == targetY) {
                    leftCollision = true;
                    break;
                }
            }
            // Check static blocks to the right
            for (Block j : b) {
                if (j.x + Block.SIZE == targetX && j.y == targetY) {
                    rightCollision = true;
                    break;
                }
            }
            // Check static blocks under
            for (Block j : b) {
                if (j.x == targetX && j.y + Block.SIZE == targetY) {
                    bottomCollision = true;
                    break;
                }
            }
        }
    }

    public void update() {

        if (deactivating) {
            deactivating();
        }

        // Move the Mino
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1: getDirection2(); break;
                case 2: getDirection3(); break;
                case 3: getDirection4(); break;
                case 4: getDirection1(); break;
                default: break;
            }

            KeyHandler.upPressed = false;
            GamePanel.se.play(3, false);
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {
            // If the Mino is not touching the bottom wall, then it can move downward
            if (!bottomCollision) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                // When moving down, need to reset the autoDropCounter to reset drop timing
                autoDropCounter = 0;
            }

            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            // If the Mino is not touching the left wall, then it can move left
            if (!leftCollision) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            // If the Mino is not touching the right wall, then it can move right
            if (!rightCollision) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }

            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            if (!deactivating) {
                GamePanel.se.play(4, false);
            }
            deactivating = true;
        }
        else {
            autoDropCounter++; // Increases counter for every frame that passes
            if (autoDropCounter == GameplaySettings.dropInterval) {
                // Move Mino down
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating() {

        deactivateCounter++;

        // Wait 45 frames until the piece is fully deactivated
        if (deactivateCounter == 45) {

            deactivateCounter = 0;
            checkMovementCollision(); // Check if the Mino is still hitting the bottom or a static block under

            // If the bottom is still hitting something after 45 frames, deactivate the Mino
            if (bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2) {

        int outline = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + outline, b[0].y + outline, Block.SIZE - (outline * 2), Block.SIZE - (outline * 2));
        g2.fillRect(b[1].x + outline, b[1].y + outline, Block.SIZE - (outline * 2), Block.SIZE - (outline * 2));
        g2.fillRect(b[2].x + outline, b[2].y + outline, Block.SIZE - (outline * 2), Block.SIZE - (outline * 2));
        g2.fillRect(b[3].x + outline, b[3].y + outline, Block.SIZE - (outline * 2), Block.SIZE - (outline * 2));
    }
}
