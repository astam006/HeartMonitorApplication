package edu.odu.ece486.hm_app.tests;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.odu.ece486.hm_app.ValsalvaAnalyzer;

/**
 * Created by Larry on 1/17/2015. This class contains the unit tests for the ValsalvaAnalyzer class.
 */
public class ValsalvaAnalyzerTests extends AndroidTestCase {

    List<Integer> testSignal;
    ValsalvaAnalyzer analyzer;

    protected void setUp() throws Exception
    {
        testSignal = new ArrayList<Integer>();
        Random generator = new Random();
        for(int i = 0; i < 700; i++){  //Add 0-699 random integers to the list
            int j = generator.nextInt(50) + 4; //random number between 4 and 54
            testSignal.add(j);
        }
        testSignal.add(2);  //Minimum number in testSignal is 2 at position 700
        testSignal.add(60); //Maximum number in testSignal is 60 at position 701
        for(int i = 0; i < 300; i++){  //Add 702-1001 random integers to the list
            int j = generator.nextInt(50) + 4; //random number between 4 and 54
            testSignal.add(j);
        }
        testSignal.add(200); //Added for amplitude test 1002
        testSignal.add(220); //Added for amplitude test 1003
        testSignal.add(240); //Added for amplitude test 1004
        testSignal.add(210); //Added for amplitude test 1005

        analyzer = new ValsalvaAnalyzer();
    }

    public void testGetMaxInRange() throws Exception {
        Integer maxInRange = analyzer.getMaxInRange(testSignal,675,725);
        assertEquals(60,maxInRange.intValue());
    }

    public void testGetMinInRange() throws Exception {
        Integer minInRange = analyzer.getMinInRange(testSignal,675,725);
        assertEquals(2,minInRange.intValue());
    }

    public void testGetAmplitudeInRange() throws Exception {
        Integer amplitude = analyzer.getAmplitudeInRange(testSignal,1002,1005);
        assertEquals(40, amplitude.intValue());
    }

    public void testGetIntFromThreeBytes() throws Exception {
        byte[] b = {0, 0, 1, 0, 0, 1, 0};
        Integer firstThree = analyzer.getIntFromThreeBytes(Arrays.copyOfRange(b,0,3));
        Assert.assertEquals(firstThree, new Integer(1));
        Integer lastThree = analyzer.getIntFromThreeBytes(Arrays.copyOfRange(b,4, b.length));
        Assert.assertEquals(lastThree, new Integer(256));
    }

    public void testGetPathLength() throws Exception {
        int redPoint = 2000;
        int irPoint = 30000;
        int resultingPathLength = analyzer.getPathLength(redPoint,irPoint);


    }
}
