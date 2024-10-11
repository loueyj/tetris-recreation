package GUI;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    GameplaySettings gs;
    public static Sound music = new Sound(); // Background Music
    public static Sound se = new Sound(); // Sound Effects

    public GamePanel() {

        // Panel Specifications
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);

        // Implement KeyListner
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        gs = new GameplaySettings();
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); // Calls the run() method

        music.play(0, true); // Play background music
        music.loop();
    }

    @Override
    public void run() {

        // Game Loop
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {

        if (!KeyHandler.pausePressed && !gs.gameOver) {
            gs.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        gs.draw(g2);
    }
}
