package edu.odu.ece486.hm_app.tests;

import android.test.AndroidTestCase;

import java.util.ArrayList;
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
        for(int i = 0; i < 700; i++){  //Add 700 random integers to the list
            int j = generator.nextInt(50) + 4; //random number between 4 and 54
            testSignal.add(j);
        }
        testSignal.add(2);  //Minimum number in testSignal is 2 at position 700
        testSignal.add(60); //Maximum number in testSignal is 60 at position 701
        for(int i = 0; i < 300; i++){  //Add 300 random integers to the list
            int j = generator.nextInt(50) + 4; //random number between 4 and 54
            testSignal.add(j);
        }

        analyzer = new ValsalvaAnalyzer();
    }

    public void testGetMaxInRange() throws Exception {

        Integer maxInRange = analyzer.getMaxInRange(testSignal,675,725);
        assertEquals(60,maxInRange.intValue());
    }
}
