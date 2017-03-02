package Entities;

import Internals.Animation;
import java.awt.image.BufferedImage;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/9/2017
 */
public abstract class Animatable {

    public static final int
            UP = 0,
            DOWN = 1,
            LEFT = 2,
            RIGHT = 3;

    private Animation currentAnim;
    private int animDirection;

    private final Animation[] anims;
    private final Animation[] deadAnim;

    public Animatable(Animation[] anims, Animation[] deadAnim) {
        this.anims = anims;
        this.deadAnim = deadAnim;

        currentAnim = anims[0];
        animDirection = 0;
    }

    public final void changeDeadAnim(int dir) {
        if(dir >= 0 && dir < deadAnim.length)
            currentAnim = deadAnim[ animDirection = dir ];
    }

    public final void changeAnim(int dir) {
        if(dir >= 0 && dir < anims.length)
            currentAnim = anims[ animDirection = dir ];
    }

    public final void updateAnim() {
        currentAnim.update();
    }

    public final BufferedImage getSprite() {
        return currentAnim.getSprite();
    }

    public final int getAnimDirection() {
        return animDirection;
    }
}
