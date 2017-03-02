package Internals;

import java.awt.image.BufferedImage;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/4/2017
 */
public class AnimationFactory {

    private static final int
            UP = 0,
            DOWN = 1,
            LEFT = 2,
            RIGHT = 3;

    private static final Animation[]
            pacman,
            deadGhost;

    private static final Animation
            deadPacman,
            frightGhost,
            frightEndGhost;

    static {
        pacman = new Animation[] {
                toAnimation(SpriteFactory.getPacman(UP)),
                toAnimation(SpriteFactory.getPacman(DOWN)),
                toAnimation(SpriteFactory.getPacman(LEFT)),
                toAnimation(SpriteFactory.getPacman(RIGHT))
        };

        deadGhost = new Animation[] {
                toAnimation(SpriteFactory.getDeadGhost(UP)),
                toAnimation(SpriteFactory.getDeadGhost(DOWN)),
                toAnimation(SpriteFactory.getDeadGhost(LEFT)),
                toAnimation(SpriteFactory.getDeadGhost(RIGHT))
        };

        deadPacman = new Animation(SpriteFactory.getDeadPacman(), 3);

        frightGhost = toAnimation(SpriteFactory.getFrightGhost());

        frightEndGhost = toAnimation(SpriteFactory.getFrightEndGhost());
    }

    private static Animation toAnimation(BufferedImage... frames) {
        return new Animation(frames, 8);
    }

    public static Animation[] getGhost(int r, int g, int b) {
        return new Animation[] {
                toAnimation(SpriteFactory.getGhost(UP, r, g, b)),
                toAnimation(SpriteFactory.getGhost(DOWN, r, g, b)),
                toAnimation(SpriteFactory.getGhost(LEFT, r, g, b)),
                toAnimation(SpriteFactory.getGhost(RIGHT, r, g, b))
        };
    }

    public static Animation[] getPacman() {
        return pacman;
    }

    public static Animation[] getDeadGhost() {
        return deadGhost;
    }

    public static Animation getDeadPacman() {
        return deadPacman;
    }

    public static Animation getFrightGhost() {
        return frightGhost;
    }

    public static Animation getFrightEndGhost() {
        return frightEndGhost;
    }
}
