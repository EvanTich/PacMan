package Main;

import Entities.*;
import Internals.Tile;
import Internals.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/29/2016
 */
public class PacManGame extends JPanel {

    private static PacMan pacman;
    private Ghost[] ghosts;

    private static Tile[][] map;

    private int lives;

    public static boolean paused;
    public static PacManGame instance;

    public PacManGame() {
        startGame();

        instance = this;
    }

    public void update() {
        // get keys
        keys();

        if(!paused) {
            if(pelletsLeft() <= 0)
                reset();

            // do the stuff
            pacman.update();

            for(Ghost g : ghosts) {
                g.update();

                if(pacman.getPosition().equals(g.getPosition()) && !pacman.isPoweredUp()) {
                    pacman.dead();
                    lives--;
                    reset();
                }
            }
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        setOpaque(true);
        setBackground(Color.BLACK);

        g2d.setColor(Color.WHITE);

        // draw world, pellets, power pellets (blinking) [read all from file]
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                g2d.drawImage(map[i][j].getSprite(), j * 16, i * 16, null);

        // draw pac-man, probably need to
        g2d.drawImage(pacman.getSprite(), pacman.getX() * 16, pacman.getY() * 16, null);
        pacman.updateAnim();

        // draw ghosts
        for(Ghost ghost : ghosts) {
            Point pos = ghost.getPosition();

            g2d.drawImage(ghost.getSprite(), pos.getX() * 16, pos.getY() * 16, null);
            ghost.updateAnim();
        }

    }

    public synchronized void keys() {
        if(!paused) {
            Point dir = pacman.getDirection();
            int animType = pacman.getAnimDirection();

            switch (EasyKey.getKey()) {
                case 'w':
                case 38: // up arrow
                    dir = Point.Up;
                    animType = PacMan.UP;
                    break;
                case 'a':
                case 37: // left arrow
                    dir = Point.Left;
                    animType = PacMan.LEFT;
                    break;
                case 's':
                case 40: // down arrow
                    dir = Point.Down;
                    animType = PacMan.DOWN;
                    break;
                case 'd':
                case 39: // right arrow
                    dir = Point.Right;
                    animType = PacMan.RIGHT;
                    break;
            }

            if(!isWall(pacman.getPosition().plus(dir))) {
                pacman.setDir(dir);
                pacman.changeAnim(animType);
            }

            EasyKey.emptyKey();
        } else pacman.setDir(Point.Zero);
    }

    private void reset() { // wah wah game over
        paused = true;

        if(lives > 0)
            notOverButDied();
        else gameOver(score());
    }

    public int score() {
        return pacman.score();
    }

    public int lives() {
        return lives;
    }

    public static PacMan getPacman() {
        return pacman;
    }

    // MAP STUFF

    public static int getScreenSizeX() {
        return map[0].length * 16;
    }

    public static int getScreenSizeY() {
        return map.length * 16;
    }

    public static Point mapMiddle() {
        return new Point(map[0].length / 2, map.length / 2);
    }

    public static boolean isWall(Point p) {
        return outOfBounds(p) ||
                map[p.getY()][p.getX()].getType() == Tile.Type.Wall;
    }

    public static boolean isPellet(Point p) {
        return !outOfBounds(p) &&
                map[p.getY()][p.getX()].getType() == Tile.Type.Pellet;
    }

    public static boolean isPowerPellet(Point p) {
        return !outOfBounds(p) &&
                map[p.getY()][p.getX()].getType() == Tile.Type.PowerPellet;
    }

    public static void removePellet(Point p) {
        if(isPellet(p) || isPowerPellet(p))
            map[p.getY()][p.getX()] = new Tile(' ');
    }

    public static boolean inTeleArea(Point p) {
        return p.equals(0, 14) || p.equals(27, 14);
    }

    public static boolean outOfBounds(Point p) {
        try {
            // just try to get the point, do nothing with it
            map[p.getY()][p.getX()].getType();
        } catch(IndexOutOfBoundsException e) {
            return true;
        }

        return false;
    }

    public static int pelletsLeft() {
        int left = 0;
        for(Tile[] cells : map)
            for(Tile cell : cells)
                if(cell.getType() == Tile.Type.Pellet ||
                        cell.getType() == Tile.Type.PowerPellet)
                    left++;

        return left;
    }

    public static Tile getTile(Point p) {
        return map[p.getY()][p.getX()];
    }

    public static Point screenToMap(Point p) {
        return new Point(p.getX() / 8, p.getY() / 8);
    }

    /**
     * returns an array of 4 points with the order of UP, DOWN, LEFT, RIGHT
     * @param pos the point to get the connected tiles from
     * @return the connected tiles
     */
    public static Point[] getConnectingTiles(Point pos) {
        Point[] tiles = {
                Point.Up,
                Point.Down,
                Point.Left,
                Point.Right
        };

        for(int i = 0; i < tiles.length; i++) {
            Point p = pos.plus(tiles[i]);
            tiles[i] = isWall(p) ? null : p;
        }

        return tiles;
    }

    // END OF MAP STUFF

    public void notOverButDied() {
        // reset all positions
        pacman.setPos(1, 1);

        Point middle = mapMiddle();
        for (Ghost g : ghosts)
            g.setPos(middle.getX(), middle.getY());

        // un-pause after 2 seconds...
        long time = System.currentTimeMillis() + 2000;
        while(time > System.currentTimeMillis());

        paused = false;

        // go
    }

    public void gameOver(int score) {
        // press any key to start
        final JDialog dialog = new JDialog(Main.frame, "You died.");

        JLabel label = new JLabel(
                String.format("<html><center>Final score: %1d<br>Close to start again</center></html>", score)
        );

        label.setPreferredSize(new Dimension(210, 75));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton exit = new JButton("Close");
        exit.setPreferredSize(new Dimension(100, 25));

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(label, BorderLayout.NORTH);
        panel.add(exit, BorderLayout.SOUTH);

        dialog.add(panel);

        ActionListener action = e -> {
            dialog.dispose();
            startGame();
        };

        exit.addActionListener(action);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                startGame();
            }
        });

        dialog.pack();
        dialog.setResizable(false);

        dialog.setFocusable(true);
        dialog.setLocationRelativeTo(Main.frame);
        dialog.setVisible(true);
        dialog.requestFocus();
    }

    private void loadMap() {
        try(BufferedReader reader = new BufferedReader( new FileReader("resources/map.txt") )) {
            // get x and y size of map
            int x = Integer.parseInt(reader.readLine());
            int y = Integer.parseInt(reader.readLine());

            map = new Tile[y][x];

            for(int i = 0; i < y; i++) {
                String line = reader.readLine();
                for(int j = 0; j < x; j++)
                    map[i][j] = new Tile(line.charAt(j));
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        // make world from file, includes blocks(#), pellets(p), and power pellets(o) (+ ' ' empty spots)
        loadMap();

        paused = false;
        pacman = new PacMan();
        lives = 3;

        ghosts = new Ghost[]{
                new Blinky(),
                new Pinky(),
                new Inky(),
                new Clyde()
        };

        EasyKey.emptyKey();
    }
}
