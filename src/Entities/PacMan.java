package Entities;

import Internals.*;
import Main.PacManGame;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/29/2016
 */
public class PacMan extends Animatable {

    private Point pos, dir;

    private int powerUpFrames;
    private boolean powerUp;

    private int score;

    private boolean dead;

    // protected static PacMan instance;

    public PacMan() {
        this(new Point(1, 1));
    }

    public PacMan(Point pos) {
        super(AnimationFactory.getPacman(), new Animation[]{ AnimationFactory.getDeadPacman() });

        this.pos = pos;
        dir = new Point(0, 0);
        powerUp = false;
        dead = false;

        score = 0;
    }

    public void update() {
        int direction = toDir(dir);
        if(getAnimDirection() != direction)
            changeAnim(direction);

        // if pacman has the power up, count down until he doesn't
        if(powerUp)
            if(--powerUpFrames <= 0)
                powerUp = false;

        System.out.println(pos);

        // move
        if(!PacManGame.isWall(pos.plus(dir)))
            pos.add(dir);

        if(PacManGame.inTeleArea(pos)) {
            if (pos.equals(0, 14))
                pos.setXY(27, 14);
            else pos.setXY(0, 14);
        }

        collectPellet();

        if(dead) changeDeadAnim(0);
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public void setPos(int x, int y) {
        pos.setXY(x, y);
    }

    public synchronized void setDir(Point dir) {
        this.dir = dir;
    }

    public Point getPosition() {
        return pos.clone();
    }

    public Point getDirection() {
        return dir.clone();
    }

    public boolean collectPellet() {
        if(PacManGame.isPowerPellet(pos)) {
            PacManGame.removePellet(pos);
            score += 50;

            powerUp();

            return true;
        } else if(PacManGame.isPellet(pos)) {
            PacManGame.removePellet(pos);
            score += 10;
            return true;
        }
        return false;
    }

    public static int toDir(Point p) {
        if(p.equals(Point.Up))
            return 0;
        else if(p.equals(Point.Down))
            return 1;
        else if(p.equals(Point.Left))
            return 2;
        else if(p.equals(Point.Right))
            return 3;
        return -1;
    }

    public boolean isPoweredUp() {
        return powerUp;
    }

    public void dead() {
        changeDeadAnim(0);
    }

    public void powerUp() {
        powerUp = true;
        powerUpFrames = 120; // 15 fps, last for 8 seconds


    }

    public int score() {
        return score;
    }
}
