package GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Add GamePanel to window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); // Size of GamePanel becomes the size of the window

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
}
