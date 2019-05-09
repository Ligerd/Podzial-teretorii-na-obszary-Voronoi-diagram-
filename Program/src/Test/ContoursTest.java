import Data.Contours;
import Data.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContoursTest {
    private static Contours testСorrectСontour;
    private static Contours testWrongContour;
    private static Point testPointInContour;
    private static Point testPointOutContour;

    @Before
    public void setUp() throws Exception {
        testСorrectСontour = new Contours();
        testСorrectСontour.addPointToContour(new Point(1, 1));
        testСorrectСontour.addPointToContour(new Point(5, 1));
        testСorrectСontour.addPointToContour(new Point(5, 4));
        testСorrectСontour.addPointToContour(new Point(1, 4));
        testWrongContour = new Contours();
        testWrongContour.addPointToContour(new Point(1, 1));
        testWrongContour.addPointToContour(new Point(5, 1));
        testWrongContour.addPointToContour(new Point(1, 4));
        testWrongContour.addPointToContour(new Point(5, 4));
        testPointInContour = new Point(3, 2);
        testPointOutContour = new Point(3, 5);

    }

    @After
    public void tearDown() throws Exception {
        testСorrectСontour = null;
        testWrongContour = null;
        testPointInContour = null;
        testPointOutContour = null;
    }

    @Test
    public void contourСheck() {
        //When
        boolean result = testСorrectСontour.contourСheck();
        boolean result2 = testWrongContour.contourСheck();
        //Then
        assertFalse(result);
        assertTrue(result2);
    }

    @Test
    public void checkPointInContur() {
        //When
        boolean result = testСorrectСontour.checkPointInContur(testPointInContour);
        boolean result2 = testСorrectСontour.checkPointInContur(testPointOutContour);
        //Then
        assertTrue(result);
        assertFalse(result2);
    }
}