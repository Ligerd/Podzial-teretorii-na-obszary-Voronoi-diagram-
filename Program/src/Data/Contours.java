package Data;

import java.util.ArrayList;

public class Contours {

    private class Vector {
        private Point start;
        private Point end;

        public Vector(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

    }

    public ArrayList<Point> getListOfContourPoints() {
        return listOfContourPoints;
    }

    private ArrayList<Point> listOfContourPoints;
    private int mapWidth;
    private int mapHeight;
    private boolean mapWidthNeedUpdate = false;
    private boolean mapHeightNeedUpdate = false;

    public Contours() {
        this.listOfContourPoints = new ArrayList<>();
        mapHeight = 0;
        mapWidth = 0;
    }

    public void addPointToContour(Point point) {
        if (Math.round(point.getY()) > mapWidth) {
            mapWidth = (int) Math.round(point.getY()) + 1;
        }

        if (Math.round(point.getX()) > mapHeight) {
            mapHeight = (int) Math.round(point.getX()) + 1;
        }

        listOfContourPoints.add(point);
    }

    public void addPointToContourAtPlace(Point point, int index)
    {
        if (Math.round(point.getY()) > mapWidth) {
            mapWidth = (int) Math.round(point.getY()) + 1;
        }

        if (Math.round(point.getX()) > mapHeight) {
            mapHeight = (int) Math.round(point.getX()) + 1;
        }
        if (index < listOfContourPoints.size())
        {
            listOfContourPoints.add(index, point);
        } else {
            listOfContourPoints.add(point);
        }

    }

    public int getSize() {
        return listOfContourPoints.size();
    }

    public Point getPointFromContour(int index) {
        return listOfContourPoints.get(index);
    }

    public Point removeContour(int index) {
        if (mapWidth == (int) Math.round(listOfContourPoints.get(index).getY()) + 1) {
            mapWidthNeedUpdate = true;
        }

        if (mapHeight == (int) Math.round(listOfContourPoints.get(index).getX()) + 1) {
            mapHeightNeedUpdate = true;
        }

       return listOfContourPoints.remove(index);

    }

    public boolean update() {
        boolean isUpdated = false;
        if (mapHeightNeedUpdate && mapWidthNeedUpdate) {
            mapHeight = 0;
            mapWidth = 0;

            for (Point p : listOfContourPoints) {
                if (Math.round(p.getY()) > mapWidth) {
                    mapWidth = (int) Math.round(p.getY()) + 1;
                }

                if (Math.round(p.getX()) > mapHeight) {
                    mapHeight = (int) Math.round(p.getX()) + 1;
                }

                mapHeightNeedUpdate = false;
                mapWidthNeedUpdate = false;
                isUpdated = true;
            }

        } else if (mapWidthNeedUpdate) {
            mapWidth = 0;

            for (Point p : listOfContourPoints) {
                if (Math.round(p.getY()) > mapWidth) {
                    mapWidth = (int) Math.round(p.getY()) + 1;
                }
            }
            mapWidthNeedUpdate = false;
            isUpdated = true;

        } else if (mapHeightNeedUpdate) {
            mapHeight = 0;
            for (Point p : listOfContourPoints) {
                if (Math.round(p.getX()) > mapHeight) {
                    mapHeight = (int) Math.round(p.getX()) + 1;
                }
            }
            mapHeightNeedUpdate = false;
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean contourСheck() {
        ArrayList<Vector> vectors = new ArrayList<>();
        Point tmp = null;
        Vector vector;
        for (int i = 0; i < listOfContourPoints.size(); i++) {
            if (i == 0) {
                tmp = listOfContourPoints.get(i);
                continue;
            } else {
                Point nextPoint = listOfContourPoints.get(i);
                vector = new Vector(tmp, nextPoint);
                vectors.add(vector);
                tmp = nextPoint;
            }
            if (vectors.size() > 2) {
                for (int index = 0; index < vectors.size() - 1; index++) {
                    if (checkVectors(vector, vectors.get(index))) {
                        return true;
                    }

                }
            }
            if (i == listOfContourPoints.size() - 1) {
                vector = new Vector(tmp, listOfContourPoints.get(0));
                vectors.add(vector);
                for (int index = 0; index < vectors.size() - 1; index++) {
                    if (checkVectors(vector, vectors.get(index))) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean checkVectors(Vector vector, Vector vector1) {
        double v1 = crossProduct(vector1, vector.start);
        double v2 = crossProduct(vector1, vector.end);
        double v3 = crossProduct(vector, vector1.start);
        double v4 = crossProduct(vector, vector1.end);
        if ((v1 > 0 && v2 < 0 || v1 < 0 && v2 > 0) && (v3 > 0 && v4 < 0 || v3 < 0 && v4 > 0))
            return true;
        if (v1 * v2 < 0 && v3 * v4 < 0)
            return true;
        return false;
    }

    private double crossProduct(Vector vector, Point point) {
        double xVector = vector.end.getX() - vector.start.getX();
        double yVector = vector.end.getY() - vector.start.getY();
        double x = point.getX() - vector.start.getX();
        double y = point.getY() - vector.start.getY();
        return xVector * y - x * yVector;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public boolean checkPointInContur(Point point) {
        int size = listOfContourPoints.size();
        int j = size - 1;
        boolean result = false;

        double XOfContourPoint1;
        double YOfContourPoint1;
        double XOfContourPoint2;
        double YOfContourPoint2;

        for (int i = 0; i < size; i++) {
            XOfContourPoint1 = listOfContourPoints.get(i).getX();
            XOfContourPoint2 = listOfContourPoints.get(j).getX();
            YOfContourPoint1 = listOfContourPoints.get(i).getY();
            YOfContourPoint2 = listOfContourPoints.get(j).getY();

            if ((((YOfContourPoint1 <= point.getY()) && (point.getY() < YOfContourPoint2)) || ((YOfContourPoint2 <= point.getY()) && (point.getY() < YOfContourPoint1)))
                    && (point.getX() > (XOfContourPoint2 - XOfContourPoint1) * (point.getY() - YOfContourPoint1) / (YOfContourPoint2 - YOfContourPoint1) + XOfContourPoint1)) {
                result = !result;
            }
            j = i;
        }

        return result;
    }

    public KeyPoint checkKeyPoints (ArrayList<KeyPoint> keyPoints)
    {
        for (KeyPoint keyPoint : keyPoints){
            if(!checkPointInContur(keyPoint))
            {
                return keyPoint;
            }
        }
        return null;
    }


}
