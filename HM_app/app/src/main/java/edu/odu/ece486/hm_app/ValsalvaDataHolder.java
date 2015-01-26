package edu.odu.ece486.hm_app;

/**
 * Created by Larry on 1/26/2015. This class will record all necessary data from the valsalva test.
 */
public class ValsalvaDataHolder {
    private static PressureSensor pressureSensor;

    public static void initHolder(){
        pressureSensor = new PressureSensor();
    }

    public Integer getPressure() {return pressureSensor.getPressure();}
    public void updatePressure(int newPressure ) { pressureSensor.update(newPressure);}

    private static ValsalvaDataHolder holder = null;
    private static Object mutex = new Object();
    private ValsalvaDataHolder() {}
    public static ValsalvaDataHolder getInstance() {
        if (holder == null) {
            synchronized (mutex){
                if (holder == null) {
                    holder = new ValsalvaDataHolder();
                    initHolder();
                }
            }
        }
        return holder;
    }
}
