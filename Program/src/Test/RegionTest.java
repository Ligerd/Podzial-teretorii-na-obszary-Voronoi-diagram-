import Data.MapObject;
import Data.Region;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegionTest {
    private Region regionTest;
    private MapObject mapObjectTest;

    @Before
    public void setUp() throws Exception {
        regionTest = new Region("Test");
        MapObject mapObject = new MapObject();
        mapObject.setY(1);
        mapObject.setX(1);
    }

    @After
    public void tearDown() throws Exception {
        mapObjectTest = null;
        regionTest = null;
    }

    @Test
    public void addObject() {
        //Given
        MapObject expected2 = new MapObject();
        MapObject expected= mapObjectTest;
        //When
        regionTest.addObject(mapObjectTest);
        //Then
        assertEquals(expected, regionTest.getMapObject(0));
        assertNotEquals(expected2, regionTest.getMapObject(0));
    }
}