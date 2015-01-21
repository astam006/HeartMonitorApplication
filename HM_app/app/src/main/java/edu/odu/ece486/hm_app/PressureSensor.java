package edu.odu.ece486.hm_app;

/**
 * Created by Larry on 10/8/2014.
 */
public class PressureSensor {

    private int mmHg;

    public PressureSensor() {
        mmHg = 0;
    }

    public void update(int newPressure) {
        mmHg = newPressure;
    }

    public int getPressure() {
        return mmHg;
    }

}
