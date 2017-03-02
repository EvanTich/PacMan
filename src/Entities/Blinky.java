package Entities;

import Internals.Point;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/1/2017
 */
public class Blinky extends Ghost {

    protected static Blinky instance;

    public Blinky() {
        super(255, 0, 0);

        home = new Point(30, 0);

        instance = this;
    }

    public void update() {
        // get the distance from immediately in front of this to pacman

        // starts initially a bit slower than pacman,
        // and speeds up when there is only 20 left, (as fast as pacman)
        // and speeds up again when only 10 left. (a bit faster than pacman)
        // gets removed for rest of game when pacman dies

        // eh do later...

        if(dead)
            dead();
        else switch(mode) {
            case Chase:
                chase(pacman.getPosition());
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
