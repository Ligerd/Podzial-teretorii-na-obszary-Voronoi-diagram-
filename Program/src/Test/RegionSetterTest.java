import Data.Contours;
import Data.KeyPoint;
import Data.Point;
import Data.RegionSetter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RegionSetterTest {
    private RegionSetter regionSetter;
    private ArrayList<KeyPoint> keyPoints;
    private KeyPoint test1;
    private KeyPoint test2;
    private Contours contours;


    @Before
    public void setUp() throws Exception {
        test1 = new KeyPoint(2, 5, "test1");
        test2 = new KeyPoint(5, 2, "test2");
        keyPoints = new ArrayList<>();
        keyPoints.add(test1);
        keyPoints.add(test2);
        regionSetter = new RegionSetter(keyPoints, 8, 8);
        contours= new Contours();
        contours.addPointToContour(new Point(0,0));
        contours.addPointToContour(new Point(10,0));
        contours.addPointToContour(new Point(10,10));
        contours.addPointToContour(new Point(0,10));
    }

    @After
    public void tearDown() throws Exception {
        keyPoints = null;
        regionSetter = null;
        test1 = null;
        test2 = null;
        contours=null;
    }

    @Test
    public void assignPoints() {
        //Given
        KeyPoint expected = test1;
        KeyPoint expected2 = test2;
        //When
        regionSetter.assignPoints(contours);
        //Then
        Point[][] points = regionSetter.getPoints();
        assertEquals(expected2, points[6][2].getKeyPoint());
        assertEquals(expected, points[1][5].getKeyPoint());

    }
}