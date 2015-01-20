package edu.odu.ece486.hm_app.tests;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.odu.ece486.hm_app.PressureSensor;

/**
 * Created by Larry on 1/20/2015.
 */
public class PressureSensorTests extends AndroidTestCase {
    List<Integer> pressureSignal;
    PressureSensor pSensor;

    protected void setUp() throws Exception {
        pressureSignal = new ArrayList<Integer>();
        pSensor = new PressureSensor();
        
        Random generator = new Random();
        for(int i = 0; i < 700; i++){  //Add 0-699 random integers to the list
            int j = generator.nextInt(5) + 21; //random number between 21 and 26
            pressureSignal.add(j);
        }
    }

    public void testUpdate() throws Exception {
        for(Integer i: pressureSignal)
        {
            pSensor.update(i.intValue());
            assertEquals(i.intValue(),pSensor.getPressure());
        }
    }
}
