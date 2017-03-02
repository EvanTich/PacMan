package Internals;

import java.awt.image.BufferedImage;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/31/2016
 */
public class Tile {

    public enum Type {
        None,
        Wall,
        Pellet,
        PowerPellet
    }

    private static final char
            none = ' ',
            ghostArea = 'x',
            ghostAreaExit = '-',
            block = '#',
            pellet = 'p',
            powerPellet = 'o';

    private BufferedImage sprite;
    private Type type;

    public Tile(char c) {
        switch(c) {
            case block:
            case ghostArea:
                sprite = SpriteFactory.getSprite(0, 3);
                type = Type.Wall;
                break;
            case pellet:
                sprite = SpriteFactory.getPellet();
                type = Type.Pellet;
                break;
            case powerPellet:
                sprite = SpriteFactory.getPowerPellet();
                type = Type.PowerPellet;
                break;
            case none:
            case ghostAreaExit:
                sprite = SpriteFactory.getSprite(2, 2);
                type = Type.None;
                break;
            default:
                throw new RuntimeException("Could not make cell: Invalid char[" + c + "]");
        }
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public Type getType() {
        return type;
    }
}
