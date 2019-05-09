import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import Logic.FileParser;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileParserTest {
    private static FileParser fileParser;
    private static File correctFile;
    private static File incompleteData;
    private static File wrongContourPoint;
    private static File wrongKeyPoint;
    private static File wrongMapObject;
    private static File testFile6;

    @Before
    public void setUp() throws Exception {
        fileParser = new FileParser();
        correctFile = new File("CorrectFileTest.txt");
        incompleteData = new File("IncompleteData.txt");
        wrongContourPoint = new File("WrongContourPoint.txt");
        wrongKeyPoint = new File("WrongKeyPoint.txt");
        wrongMapObject = new File("WrongMapObject.txt");
        testFile6 = new File("Test6.txt");

    }

    @After
    public void tearDown() throws Exception {
        fileParser = null;
        correctFile = null;
        incompleteData = null;
        wrongContourPoint = null;
        wrongKeyPoint = null;
        wrongMapObject = null;
    }

    @Test
    public void readFile() throws IOException {
        //Then
        assertTrue(fileParser.readFile(correctFile));
        assertFalse(fileParser.readFile(incompleteData));
        assertFalse(fileParser.readFile(wrongContourPoint));
        assertFalse(fileParser.readFile(wrongKeyPoint));
        assertFalse(fileParser.readFile(wrongMapObject));
        //assertFalse(fileParser.readFile(testFile6));
    }


    @Test
    public void checkKeyPointAndMapObject() {
    }
}