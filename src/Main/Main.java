package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/29/2016
 */
public class Main extends JFrame {

    public static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("PAC-MAN");

        PacManGame game = new PacManGame();
        EasyKey key = new EasyKey();

        frame.add(game, BorderLayout.CENTER);
        frame.addKeyListener(key);
        frame.setFocusable(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Game closing...");
            }
        });

        frame.setSize(new Dimension(PacManGame.getScreenSizeX() + 16, PacManGame.getScreenSizeY() + 32));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // set in the middle of the screen
        frame.setVisible(true);

        frame.requestFocus();

        long lastTime = System.nanoTime(),
                timer = System.currentTimeMillis();
        int frames = 0;

        final double ns = 1e+9 / 15; // nano seconds per frame, 15 fps

        double delta = 0;

        boolean running = true;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                game.update();
                delta--;
                frames++;
            }

            // its been a second!
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(String.format("PAC-MAN | Score: %1d | Lives: %2d |%2d fps", game.score(), game.lives() , frames));
                frames = 0;
            }

            if(EasyKey.getKey() == 27)
                running = false;
        }

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}

