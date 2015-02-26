package edu.odu.ece486.hm_app.tests;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.odu.ece486.hm_app.ValsalvaAnalyzer;
import edu.odu.ece486.hm_app.ValsalvaDataHolder;

/**
 * Created by Larry on 1/17/2015. This class contains the unit tests for the ValsalvaAnalyzer class.
 */
public class ValsalvaAnalyzerTests extends AndroidTestCase {

    List<Double> testSignal;
    ValsalvaAnalyzer analyzer;

    protected void setUp() throws Exception
    {
        testSignal = new ArrayList<Double>();
        Random generator = new Random();
        for(int i = 0; i < 700; i++){  //Add 0-699 random integers to the list
            Double j = generator.nextDouble()*50 + 4.0; //random number between 4 and 54
            testSignal.add(j);
        }
        testSignal.add(2.0);  //Minimum number in testSignal is 2 at position 700
        testSignal.add(60.0); //Maximum number in testSignal is 60 at position 701
        for(int i = 0; i < 300; i++){  //Add 702-1001 random integers to the list
            Double j = generator.nextDouble()*50 + 4; //random number between 4 and 54
            testSignal.add(j);
        }
        testSignal.add(200.0); //Added for amplitude test 1002
        testSignal.add(220.0); //Added for amplitude test 1003
        testSignal.add(240.0); //Added for amplitude test 1004
        testSignal.add(210.0); //Added for amplitude test 1005

        analyzer = new ValsalvaAnalyzer();
    }

    public void testGetMaxInRange() throws Exception {
        Double maxInRange = analyzer.getMaxInRange(testSignal,675,725);
        assertEquals(60.0,maxInRange);
    }

    public void testGetMinInRange() throws Exception {
        Double minInRange = analyzer.getMinInRange(testSignal,675,725);
        assertEquals(2,minInRange.intValue());
    }

    public void testGetAmplitudeInRange() throws Exception {
        Double amplitude = analyzer.getAmplitudeInRange(testSignal,1002,1005);
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
        Double redPoint = 0.11;
        Double irPoint = 0.16;
        Double resultingPathLength = analyzer.getPathLength(redPoint,irPoint);
        Assert.assertEquals(0.00540604,resultingPathLength, 0.0001);
    }

    public void testGetPathLengthSignal() throws Exception {
        List<Double> testIrSignal = new ArrayList<Double>();
        testIrSignal.add(0.341);
        testIrSignal.add(0.332);
        testIrSignal.add(0.312);
        testIrSignal.add(0.338);
        List<Double> testRedSignal = new ArrayList<Double>();
        testRedSignal.add(0.228);
        testRedSignal.add(0.262);
        testRedSignal.add(0.248);
        testRedSignal.add(0.259);
        List<Double> testPathLengthSignal = analyzer.getPathLengthSignal(testRedSignal,testIrSignal);
        Assert.assertEquals(0.00307515, testPathLengthSignal.get(0), 0.0001);
        Assert.assertEquals(0.00324655, testPathLengthSignal.get(1), 0.0001);
        Assert.assertEquals(0.0034406, testPathLengthSignal.get(2), 0.0001);
        Assert.assertEquals(0.00317584, testPathLengthSignal.get(3), 0.0001);
    }

    public void testGetRunningPressureAverage() throws Exception {
        List<Double> testList = new ArrayList<Double>();
        testList.add(20.0);
        testList.add(21.0);
        testList.add(24.0);
        testList.add(25.0);
        testList.add(28.0);
        testList.add(30.0);
        testList.add(34.0);
        testList.add(38.0);
        testList.add(41.0);
        testList.add(42.0);
        Assert.assertEquals(30.3,analyzer.getRunningAverage(testList), 0.1);
        testList.add(46.0);
        Assert.assertEquals(32.9,analyzer.getRunningAverage(testList), 0.1);
        testList.add(50.0);
        Assert.assertEquals(35.8,analyzer.getRunningAverage(testList), 0.1);
        testList.add(60.0);
        Assert.assertEquals(39.4,analyzer.getRunningAverage(testList), 0.1);
        testList.add(74.0);
        Assert.assertEquals(44.3,analyzer.getRunningAverage(testList), 0.1);
        testList.add(75.0);
        Assert.assertEquals(49.0,analyzer.getRunningAverage(testList), 0.1);
        testList.add(77.0);
        Assert.assertEquals(53.7,analyzer.getRunningAverage(testList), 0.1);
        testList.add(81.0);
        Assert.assertEquals(58.4,analyzer.getRunningAverage(testList), 0.1);
        testList.add(88.0);
        Assert.assertEquals(63.4,analyzer.getRunningAverage(testList), 0.1);
        testList.add(91.0);
        Assert.assertEquals(68.4,analyzer.getRunningAverage(testList), 0.1);
    }
}
