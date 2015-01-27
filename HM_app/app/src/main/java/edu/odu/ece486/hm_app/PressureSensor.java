package edu.odu.ece486.hm_app;

import java.util.ArrayList;


public class PressureSensor {


    private int mmHg;
    private int maxPressure;

    //public static void main(Integer args[]){
    // creating empty arraylist
    //  ArrayList<Integer> list = new ArrayList<Integer>();
    //  }

    public PressureSensor() {
        mmHg = 0;
    }



    public void update(int newPressure) {
        adjustMax(newPressure);
        mmHg = newPressure;
    }

    public int getPressure() {
        return mmHg;
    }

    /**
     * Check the highest received input and store it in maxPressure.
     * The highest received input will be used for the Test Results.
     * @param newPressure
     */
    public void adjustMax(int newPressure) {
        if(newPressure > mmHg)
            maxPressure = newPressure;
    }

}
