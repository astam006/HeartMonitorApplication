package edu.odu.ece486.hm_app.tests;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


import java.util.ArrayList;
import java.lang.Math;

import android.test.AndroidTestCase;
import android.util.Log;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.io.FileWriter;

/**
 * Created by Russell on 1/26/2015.
 */

public class Testdata extends AndroidTestCase{

    private int SAMPLES_PER_SECOND = 50;
    private int INTENSITY = 20;
    private int BASELINE = 512;
    private int SLOPE = -5;



    public void testExportFakeSignal() throws Exception {

        ArrayList<String> list = new ArrayList<String>();
        String csv = "output.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));

        for (int i=0; i< 45* SAMPLES_PER_SECOND; i++) {
            int data = FakeData(i);
            String data2 = Integer.toString(data);

            list.add(data2);
            writer.writeNext(new String[]{data2});

            Log.d("Fake Amplitude Values ", data2);
        }
        //return list;
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

