package Internals;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/10/2016
 */
public class Point {

    public static final Point
            Up = new Point(0, -1),
            Down = new Point(0, 1),
            Left = new Point(-1, 0),
            Right = new Point(1, 0),
            Zero = new Point();

    private int x, y;

    public Point() {
        this(0, 0);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double distance(Point o) {
        return distance(o.x, o.y);
    }

    public double distance(int x2, int y2) {
        return Math.sqrt( Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2) );
    }

    public double fastDistance(Point o) {
        return fastDistance(o.x, o.y);
    }

    public double fastDistance(int x2, int y2) {
        return Math.abs(x2 - x) + Math.abs(y2 - y);
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Point o) {
        add(o.x, o.y);
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * returns entirely new point based on the two
     * @param o
     * @return
     */
    public Point plus(Point o) {
        Point ret = clone();
        ret.add(o);
        return ret;
    }

    public Point plus(int x, int y) {
        return plus(new Point(x, y));
    }

    /**
     * Returns a random point where x = [0, xSize - 1], and y = [0, ySize - 1]
     * @param xSize the number of columns
     * @param ySize the number of rows
     * @return random point where x = [0, xSize - 1], and y = [0, ySize - 1]
     */
    public static Point randomPoint(int xSize, int ySize) {
        return new Point( (int)(Math.random() * xSize), (int)(Math.random() * ySize) );
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equals(Point o) {
        return equals(o.x, o.y);
    }

    @Override
    public Point clone() {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%1d, %2d)", x, y);
    }
}
