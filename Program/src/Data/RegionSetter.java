package Data;

import java.util.ArrayList;

public class RegionSetter {
    private Point[][] points;
    private ArrayList<KeyPoint> keyPoints;
    private int height;
    private int width;


    public RegionSetter(ArrayList<KeyPoint> keyPoints, int height, int width) {
        this.keyPoints = keyPoints;
        this.height = height;
        this.width = width;
        points = new Point[height][width];
        for (int i = 0; i < points.length; ++i)
            for (int j = 0; j < points[0].length; ++j) {
                points[i][j] = new Point(i, j);
            }
    }

    public int getHeight() {
        return height;
    }


    public int getWidth() {
        return width;
    }


    public void update(int height, int width) {
        this.height = height;
        this.width = width;
        points = new Point[height][width];
        for (int i = 0; i < points.length; ++i)
            for (int j = 0; j < points[i].length; ++j) {
                points[i][j] = new Point(i, j);
            }
    }

    public void assignPoints(Contours contours) {
        for (Point[] row : points) {
            for (Point point : row) {
                assignPointToRegion(point, contours);
            }
        }
    }

    private void assignPointToRegion(Point point, Contours contours) {
        double minDist = Double.MAX_VALUE;
        double currentValue;
        if (contours.checkPointInContur(point)) {
            for (KeyPoint keyPoint : keyPoints) {

                currentValue = calculateDistanceBetweenPoints(point, keyPoint);
                if (currentValue < minDist) {
                    minDist = currentValue;
                    point.setKeyPoint(keyPoint);
                }

            }
        } else {
            point.setKeyPoint(null);
        }
    }

    private double calculateDistanceBetweenPoints(Point point1, Point point2) {

        return Math.sqrt((point1.getX() - point2.getX()) * (point1.getX() - point2.getX()) + (point1.getY() - point2.getY()) * (point1.getY() - point2.getY()));

    }

    public Point[][] getPoints() {
        return points;
    }

    public ArrayList<KeyPoint> getKeyPoints() {
        return keyPoints;
    }

    public void removeKeyPoint(int index) {
        keyPoints.remove(index);
    }


    public boolean addKeyPointAndCheckIfIsInContour(double x, double y, String name, Contours contours) {
        KeyPoint newKeyPoint = new KeyPoint(x, y, name);
        if (contours.checkPointInContur(newKeyPoint)) {
            keyPoints.add(newKeyPoint);
            return true;
        }
        return false;

    }

    public void assignObjectToRegion(ArrayList<MapObject> objects) {
        for (KeyPoint keyPoint : keyPoints) {
            keyPoint.getRegion().clearMapObjectList();
        }
        for (int i = 0; i < objects.size(); i++) {
            int x = (int) objects.get(i).getX();
            int y = (int) objects.get(i).getY();
            if (x <= height && y <= width && points[x][y] != null) {
                if (points[x][y].getKeyPoint() != null) {
                    points[x][y].getKeyPoint().getRegion().addObject(objects.get(i));
                }
            }
        }
    }

}
