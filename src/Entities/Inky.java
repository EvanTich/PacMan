package Entities;

import Internals.Point;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/1/2017
 */
public class Inky extends Ghost {

    protected static Inky instance;

    private Ghost blinky;

    public Inky() {
        super(49, 255, 255);

        home = new Point(31, 27);

        this.blinky = Blinky.instance;

        instance = this;
    }

    public void update() {
        // uses 2 points ahead of pacman to blinkys position x 2 to get position

        if(dead)
            dead();
        else switch(mode) {
            case Chase:
                Point tPos = pacman.getPosition(),
                        tDir = pacman.getDirection();

                // point two tiles ahead of pacman
                tPos.add(tDir.getX() * 2, tDir.getY() * 2);

                // difference of x, of y
                int dx = tPos.getX() - blinky.pos.getX(),
                        dy = tPos.getY() - blinky.pos.getY();

                dx *= 2;
                dy *= 2;

                tPos = new Point(dx + blinky.pos.getX(), dy + blinky.pos.getY());

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
