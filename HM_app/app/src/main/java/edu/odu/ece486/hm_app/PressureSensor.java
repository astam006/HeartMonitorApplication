package edu.odu.ece486.hm_app;

import java.util.ArrayList;


public class PressureSensor {


    private int mmHg;
    private int maxPressure;
    private int count;

    public boolean checkIfTestFail() {


        if (mmHg < 20) {
            count++;
            if (count == 100) {
                return true;
            }else{
                return false;
            }

        }
        else {
            count = 0;
            return false;
        }
    }

    public PressureSensor() {
        mmHg = 0;
        count = 0;
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
