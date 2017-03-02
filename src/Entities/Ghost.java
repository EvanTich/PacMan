package Entities;

import Internals.AnimationFactory;
import Internals.Point;
import Internals.Rectangle;
import Main.PacManGame;

import java.util.Arrays;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/1/2017
 */
public abstract class Ghost extends Animatable {

    public enum Mode {
        Chase,
        Scatter, // head to respective home areas
        Frightened // aimlessly wander when in this mode, turn dark blue
    }

    public enum Move {
        Up,
        Down,
        Left,
        Right,
        None;

        public static Move getOpposite(Move move) {
            switch (move) {
                case Up:
                    return Down;
                case Down:
                    return Up;
                case Left:
                    return Right;
                case Right:
                    return Left;
            }

            return None;
        }

        public static Point toPoint(Move move) {
            switch (move) {
                case Up:
                    return Point.Up;
                case Down:
                    return Point.Down;
                case Left:
                    return Point.Left;
                case Right:
                    return Point.Right;
            }

            return Point.Zero;
        }

        public static Move getRandom() {
            return Move.values()[(int)(Math.random() * 4)];
        }
    }

    private Point monsterHome;

    protected PacMan pacman;

    protected Point pos, dir, home;
    protected Mode mode;

    private Move nextMove;

    protected boolean dead;

    public Ghost(int r, int g, int b) {
        super(AnimationFactory.getGhost(r, g, b), AnimationFactory.getDeadGhost());

        monsterHome = PacManGame.mapMiddle();

        pacman = PacManGame.getPacman();
        home = new Point();

        pos = monsterHome.clone();
        dir = Point.Zero;
        nextMove = Move.Up;

        dead = false;
        mode = Mode.Chase;
    }

    public abstract void update();

    protected final Move dead() {
        Point middle = PacManGame.mapMiddle();

        if(pos.equals(middle))
            dead = false;

        // go back to middle, respawn
        return pathFind(middle);
    }

    /**
     * Every ghost actually use this
     * @param target the point to go to
     * @return returns nextMove
     */
    private Move pathFind(Point target) {
        if(pos.equals(target))
            return (nextMove = Move.None);

        // calculate distance from tiles attached to the one in front of this ghost
        Point[] points = PacManGame.getConnectingTiles(pos.plus(dir));

        double[] distances = new double[points.length];

        for(int i = 0; i < points.length; i++) {
            if (points[i] != null)
                distances[i] = target.fastDistance(points[i]);
            else distances[i] = 1000;
        }

        // compute the fastest path
        int[] fastest = {0, 1, 2, 3}; // shortest to longest path
        for(int i = 0; i < distances.length; i++) {
            for(int j = 0; j < fastest.length; j++) {
                // if the distance at i is less than the shortest distance now
                if(distances[i] < distances[fastest[j]]){
                    fastest[j] = i;
                    break;
                }
            }
        }

        // actual moving section
        move();

        // compute next move
        Move[] moves = Move.values();
        for(int i : fastest) {
            if( !isOpposite(moves[i]) && canMove(moves[i])) {
                nextMove = moves[i];
                break;
            }
        }

        return nextMove;
    }

    protected final Move chase(Point target) {
        return pathFind(target);
    }

    protected final Move scatter(Point home) {
        return pathFind(home);
    }

    protected final Move frightened() {
        // randomly move through the map
        move();

        Move move = Move.getRandom();

        if(!isOpposite(move))
            nextMove = move;

        return nextMove;
    }

    protected final Move getOut() {
        return pathFind( monsterHome.plus(0, -3) );
    }

    protected final void move() {
        if(getAnimDirection() != nextMove.ordinal())
            changeAnim(nextMove.ordinal());

        dir = Move.toPoint(nextMove);

        if(!PacManGame.isWall(pos.plus(dir)))
            pos.add(dir);

        if(PacManGame.inTeleArea(pos)) {
            if (pos.equals(0, 14))
                pos.setXY(27, 14);
            else pos.setXY(0, 14);
        }
    }

    public final boolean inMonsterHouse() {
        Rectangle rect = new Rectangle( new Point(10, 12), new Point(17, 16) );
        return rect.isInside(pos);
    }

    public final boolean isOpposite(Move move) {
        return Move.getOpposite(move) == nextMove;
    }

    public final boolean canMove(Move move) {
        return !PacManGame.isWall(pos.plus(Move.toPoint(move)));
    }

    public final void setMode(Mode mode) {
        this.mode = mode;
    }

    public final Mode getMode() {
        return mode;
    }

    public void setPos(int x, int y) {
        pos.setXY(x, y);
    }

    public final Point getPosition() {
        return pos.clone();
    }

    public final Point getDirection() {
        return dir.clone();
    }
}
