package Entities;

import Internals.Point;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/1/2017
 */
public class Pinky extends Ghost {

    protected static Pinky instance;

    public Pinky() {
        super(255, 156, 206);

        home = new Point(2, 0);

        instance = this;
    }

    public void update() {
        // target four tiles ahead of pacman

        if(dead)
            dead();
        else switch(mode) {
            case Chase:
                Point tPos = pacman.getPosition(),
                        tDir = pacman.getDirection();

                // point four tiles ahead of pacman
                tPos.add(tDir.getX() * 4, tDir.getY() * 4);

                chase(tPos);
                break;
            case Scatter:
                scatter(home);
                break;
            case Frightened:
                frightened();
                break;
        }
    }
}
