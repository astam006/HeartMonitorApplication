package edu.odu.ece486.hm_app;

/**
 * Created by Larry on 10/8/2014.
 */
public class PressureSensor {

    private int mmHg;
    private int maxPressure;

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
