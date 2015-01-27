package edu.odu.ece486.hm_app.tests;

import java.util.ArrayList;
import java.lang.Math;
import android.util.Log;


/**
 * Created by Russell on 1/26/2015.
 */

public class Testdata {

    private int SAMPLES_PER_SECOND = 50;
    private int INTENSITY = 20;
    private int BASELINE = 512;
    private int SLOPE = -5;


    public void main(Integer[] args){

        ArrayList<Double> list = new ArrayList<Double>();
        for (int i=0; i< 45* SAMPLES_PER_SECOND; i++) {
            double data = FakeData(i);
            list.add(data);
            Log.d("Pressure", "New Pressure = "+ data);
        }
       }

    public int FakeData(int t){

        int sine = (int)(INTENSITY * Math.sin(2 * (int) t / 50));
        if (t < 15 * SAMPLES_PER_SECOND){
            return BASELINE + sine;
        } else if(t < 30 * SAMPLES_PER_SECOND) {
            return BASELINE + ((int)t/SAMPLES_PER_SECOND) * SLOPE + 75 + sine;
        } else {
            return BASELINE + sine;
        }
        }
    }

