package edu.odu.ece486.hm_app;

/**
 * Created by Larry on 10/8/2014.
 */
public class PressureSensor {
    public int mmHg;

    public void update(byte newPressure) {
        mmHg = (int)newPressure;
    }

    public int getPressure() {
        return mmHg;
    }
}
