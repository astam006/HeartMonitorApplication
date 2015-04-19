package edu.odu.ece486.hm_app.tests;

import android.test.AndroidTestCase;
import android.util.Log;

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

    /*
     * This test can be used to display information about the signals calculated from input data.
     * 1. Select a file from the assets folder to import and run calculations
     *         upon and insert the file name into the 4th line of this test.
     * 2. Run the test.
     * 3. Click on the Android tab at the bottom.
     * 4. Search for TestFindMinimas in the logcat search window.
     * 5. View information about the data.
     */
    public void testFindMinimas() throws Exception {
        Log.d("TestFindMinimas", "Getting data holder.");
        ValsalvaDataHolder data = ValsalvaDataHolder.getInstance();
        Log.d("TestFindMinimas", "Importing csv file.");
        data.ImportCSV(this.getContext().getAssets().open("jenTestDataCsv1.csv"));
        Log.d("TestFindMinimas", "Number of data points imported: " + data.getRedSignal().size());
        Log.d("TestFindMinimas", "Calculating Path Length Signal.");
        List<Double> pathLength = analyzer.getPathLengthSignal(data.getRedSignal(), data.getIrSignal());
        Log.d("TestFindMinimas", "Verifying path length size.");
        //Assert.assertTrue("Pathlength size: " + pathLength.size(),pathLength.size() == 1368);
        Log.d("TestFindMinimas", "Finding minimas.");
        List<Integer> minimas = analyzer.findMinimas(pathLength);
        Log.d("TestFindMinimas", "Number of minimas: " + minimas.size());
        List<Double> amplitudes = analyzer.calculateAmplitudeWithMinimas(pathLength, minimas);
        Log.d("TestFindMinimas", "Number of amplitudes: " + amplitudes.size());
        Integer ratio = analyzer.getRatio(amplitudes,data.getTestStartIndex(), data.getTestEndIndex());
        Log.d("TestFindMinimas", "Amplitude ratio: " + ratio);
    }

    public void testFullWithFindMaximas() throws Exception {
        Log.d("TestFindMaximas", "Getting data holder.");
        ValsalvaDataHolder data = ValsalvaDataHolder.getInstance();
        Log.d("TestFindMaximas", "Importing csv file.");
        data.ImportCSV(this.getContext().getAssets().open("LB1.csv"));
        Log.d("TestFindMaximas", "Number of data points imported: " + data.getRedSignal().size());
        Log.d("TestFindMaximas", "Calculating Path Length Signal.");
        List<Double> pathLength = analyzer.getPathLengthSignal(data.getRedSignal(), data.getIrSignal());
        Log.d("TestFindMaximas", "Verifying path length size.");
        //Assert.assertTrue("Pathlength size: " + pathLength.size(),pathLength.size() == 1368);
        Log.d("TestFindMaximas", "Finding maximas.");
        List<Integer> maximas = analyzer.findMaximas(pathLength);
        Log.d("TestFindMaximas", "Number of maximas: " + maximas.size());
        List<Double> amplitudes = analyzer.calculateAmplitudeWithMaximas(pathLength, maximas);
        Log.d("TestFindMaximas", "Number of amplitudes: " + amplitudes.size());
        Integer ratio = analyzer.getRatio(amplitudes, 550, 1400);
        Log.d("TestFindMaximas", "Amplitude ratio: " + ratio);
    }

    /*
     * This test can be used to display information about the signals calculated from input data.
     * and save the data to a file.
     * 1. Select a file from the assets folder to import and run calculations
     *         upon and insert the file name into the 4th line of this test.
     * 2. Run the test.
     * 3. Click on the Android tab at the bottom.
     * 4. Search for TestFindMinimas in the logcat search window.
     * 5. View information about the data.
     * 6. A csv is saved into the base directory of android device.
     * The csv contains the regular saved application information (light and pressure signals)
     * along with the calculated pathlength signal, 0 or 1 indication for whether a point is a
     * minima, along with the amplitude for that range next to the points that are minimas.
     */
    public void testMinimaChooseAFileFromAssetsAndItWillSaveData() throws Exception
    {
        Log.d("TestChooseAndSave", "Getting data holder.");
        ValsalvaDataHolder data = ValsalvaDataHolder.getInstance();
        Log.d("TestChooseAndSave", "Importing csv file.");
        data.ImportCSV(this.getContext().getAssets().open("LB6.csv"));
        Log.d("TestChooseAndSave", "Number of data points imported: " + data.getRedSignal().size());
        Log.d("TestChooseAndSave", "Calculating Path Length Signal.");
        List<Double> pathLength = analyzer.getPathLengthSignal(data.getRedSignal(), data.getIrSignal());
        Log.d("TestChooseAndSave", "Verifying path length size.");
        Log.d("TestChooseAndSave", "Finding minimas.");
        List<Integer> minimas = analyzer.findMinimas(pathLength);
        Log.d("TestChooseAndSave", "Number of minimas: " + minimas.size());
        List<Double> amplitudes = analyzer.calculateAmplitudeWithMinimas(pathLength, minimas);
        Log.d("TestChooseAndSave", "Number of amplitudes: " + amplitudes.size());
        data.saveWithCalculatedData(pathLength, minimas, amplitudes);
        Log.d("TestChooseAndSave", "Save complete.");
    }

    public void testMaximaChooseAFileFromAssetsAndItWillSaveData() throws Exception
    {
        Log.d("TestChooseAndSave", "Getting data holder.");
        ValsalvaDataHolder data = ValsalvaDataHolder.getInstance();
        Log.d("TestChooseAndSave", "Importing csv file.");
        data.ImportCSV(this.getContext().getAssets().open("Russ5.csv"));
        Log.d("TestChooseAndSave", "Number of data points imported: " + data.getRedSignal().size());
        Log.d("TestChooseAndSave", "Calculating Path Length Signal.");
        List<Double> pathLength = analyzer.getPathLengthSignal(data.getRedSignal(), data.getIrSignal());
        Log.d("TestChooseAndSave", "Verifying path length size.");
        Log.d("TestChooseAndSave", "Finding maximas.");
        List<Integer> maximas = analyzer.findMaximas(pathLength);
        Log.d("TestChooseAndSave", "Number of maximas: " + maximas.size());
        List<Double> amplitudes = analyzer.calculateAmplitudeWithMaximas(pathLength, maximas);
        Log.d("TestChooseAndSave", "Number of amplitudes: " + amplitudes.size());
        data.saveWithCalculatedData(pathLength, maximas, amplitudes);
        Log.d("TestChooseAndSave", "Save complete.");
    }

    public void testGetTestResultsAfterImport() throws Exception {
        Log.d("TestGetTestResults", "Getting data holder.");
        ValsalvaDataHolder data = ValsalvaDataHolder.getInstance();
        Log.d("TestGetTestResults", "Importing csv file.");
        data.ImportCSV(this.getContext().getAssets().open("Cody1.csv"));
        Log.d("TestGetTestResults", "Number of data points imported: " + data.getRedSignal().size());
        int results = analyzer.getTestResults(data);
        Log.d("TestGetTestResults", "Ratio: " + results);
    }
}
