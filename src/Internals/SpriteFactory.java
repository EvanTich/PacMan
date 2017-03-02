package Internals;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/29/2016
 */
public class SpriteFactory {

    public static final int
            UP = 0,
            DOWN = 1,
            LEFT = 2,
            RIGHT = 3;

    private static final String spritePath;
    private static final int TILE_SIZE;
    private static BufferedImage spriteSheet;

    private static final BufferedImage[][]
            pacman,
            ghost,
            deadGhost;

    private static final BufferedImage[]
            deadPacman,
            frightGhost,
            frightEndGhost;

    private static final BufferedImage
            pellet,
            powerPellet;

    static {
        spritePath = "resources/Sprites/sheet.png";
        TILE_SIZE = 16;

        pacman = new BufferedImage[][] {
                { getSprite(0, 1), getSprite(1, 1), getSprite(0, 1), getSprite(8, 1) }, // UP
                { getSprite(2, 1), getSprite(3, 1), getSprite(2, 1), getSprite(8, 1) }, // DOWN
                { getSprite(4, 1), getSprite(5, 1), getSprite(4, 1), getSprite(8, 1) }, // LEFT
                { getSprite(6, 1), getSprite(7, 1), getSprite(6, 1), getSprite(8, 1) }  // RIGHT
        };

        ghost = new BufferedImage[][] {
                { getSprite(0, 0), getSprite(1, 0) }, // UP
                { getSprite(2, 0), getSprite(3, 0) }, // DOWN
                { getSprite(4, 0), getSprite(5, 0) }, // LEFT
                { getSprite(6, 0), getSprite(7, 0) }  // RIGHT
        };

        deadGhost = new BufferedImage[][] {
                { getSprite(0, 5) }, // UP
                { getSprite(1, 5) }, // DOWN
                { getSprite(2, 5) }, // LEFT
                { getSprite(3, 5) }  // RIGHT
        };

        deadPacman = new BufferedImage[] {
                getSprite(0, 6), getSprite(1, 6), getSprite(2, 6), getSprite(3, 6),
                getSprite(4, 6), getSprite(5, 6), getSprite(6, 6), getSprite(7, 6),
                getSprite(8, 6), getSprite(9, 6), getSprite(10, 6)
        };

        frightGhost = new BufferedImage[] {
                getSprite(4, 0), getSprite(4, 1)
        };

        frightEndGhost = new BufferedImage[] {
                getSprite(4, 0), getSprite(4, 2), getSprite(4, 1), getSprite(4, 3)
        };

        pellet = getSprite(1, 2);
        powerPellet = getSprite(0, 2);
    }

    public static BufferedImage loadSprite(String path) {
        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }

    public static BufferedImage getSprite(int xGrid, int yGrid) {
        if (spriteSheet == null)
            spriteSheet = loadSprite(spritePath);

        return spriteSheet.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    // REQUIRED FOR POST-COLORING GHOSTS TO PROPER COLOR

    private static BufferedImage colorGhost(BufferedImage img, int red, int green, int blue) {
        int width = img.getWidth();
        int height = img.getHeight();

        WritableRaster raster = img.getRaster();

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                int[] pixels = raster.getPixel(i, j, (int[]) null);
                System.out.println(java.util.Arrays.toString(pixels));
                if(pixels[0] == 178 && pixels[1] == 0 && pixels[2] == 255) {
                    pixels[0] = red;
                    pixels[1] = green;
                    pixels[2] = blue;

                    raster.setPixel(i, j, pixels);
                }
            }
        }

        return img;
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static BufferedImage[] colorGhost(BufferedImage[] imgs, int red, int green, int blue) {
        // copy imgs to rtn
        BufferedImage[] rtn = new BufferedImage[imgs.length];
        for(int i = 0; i < imgs.length; i++)
            rtn[i] = deepCopy(imgs[i]);

        for(BufferedImage img : rtn)
            colorGhost(img, red, green, blue);

        return rtn;
    }

    // end of REQUIRED FOR POST-COLORING

    public static BufferedImage[] getGhost(int dir, int red, int green, int blue) {
        return colorGhost(ghost[dir], red, green, blue);
    }

    // all images below require no processing to change

    public static BufferedImage[] getDeadGhost(int dir) {
        return deadGhost[dir];
    }

    public static BufferedImage[] getFrightGhost() {
        return frightGhost;
    }

    public static BufferedImage[] getFrightEndGhost() {
        return frightEndGhost;
    }

    public static BufferedImage[] getPacman(int dir) {
        return pacman[dir];
    }

    public static BufferedImage[] getDeadPacman() {
        return deadPacman;
    }

    public static BufferedImage getPellet() {
        return pellet;
    }

    public static BufferedImage getPowerPellet() {
        return powerPellet;
    }
}