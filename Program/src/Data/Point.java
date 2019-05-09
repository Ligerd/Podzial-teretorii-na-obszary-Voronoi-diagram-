package Data;


public class Point {
    private double x;
    private double y;
    private KeyPoint keyPoint;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.keyPoint = null;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setKeyPoint(KeyPoint keyPoint) {
        this.keyPoint = keyPoint;
    }

    public KeyPoint getKeyPoint() {
        return keyPoint;
    }



}
