package Entities;

import Internals.Point;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/1/2017
 */
public class Clyde extends Ghost {

    protected static Clyde instance;

    private double distance;

    public Clyde() {
        super(255, 206, 49);

        home = new Point(0, 27);

        distance = pos.fastDistance(pacman.getPosition());

        instance = this;
    }

    public void update() {
        // blinky ai if >8 blocks away from pacman
        // scatter if <8 blocks away from pacman

        if(dead)
            dead();
        else switch(mode) {
            case Chase:
                // get the fast distance from clyde to pacman
                distance = pos.fastDistance(pacman.getPosition());

                if(distance <= 8)
                    scatter(home);
                else if(distance > 8)
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
