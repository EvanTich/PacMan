package Internals;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/16/2017
 */
public class Rectangle {

    private Point p1, p2;

    public Rectangle(int x1, int y1, int x2, int y2) {
        this( new Point(x1, y1), new Point(x2, y2) );
    }

    /**
     *
     * @param p1 has to come numerically before p2
     * @param p2
     */
    public Rectangle(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean isInside(Point p) {
        return p.getX() >= p1.getX() && p.getX() <= p2.getX() /* if in bounds x */
                && p.getY() >= p1.getY() && p.getY() <= p2.getY(); /* if in bounds y */
    }

    public Point middle() {
        return new Point(p1.getX() + p2.getX() / 2, p1.getY() + p2.getY() / 2);
    }

    public Point getPoint1() {
        return p1;
    }

    public Point getPoint2() {
        return p2;
    }
}
