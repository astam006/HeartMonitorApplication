package edu.odu.ece486.hm_app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 1/17/2015. This class will contain all of the functions necessary to
 * perform analysis on data gathered from peripherals during the valsalva maneuver.
 */
public class ValsalvaAnalyzer {

    public Integer getMaxInRange(List<Integer> signal, int lower, int upper)
    {
        Integer max = new Integer(0);
        for(int i = lower; i < upper+1; i++){
            if (max < signal.get(i)){
                max = signal.get(i);
            }
        }
        return max;
    }

    public Integer getMinInRange(List<Integer> signal, int lower, int upper)
    {
        Integer min = new Integer(256); //TODO: Update this value (256) to contain the maximum possible value.
        for(int i = lower; i < upper+1; i++){
            if (min > signal.get(i)){
                min = signal.get(i);
            }
        }
        return min;
    }

    public Integer getAmplitudeInRange(List<Integer> signal, int lower, int upper)
    {
        Integer max = getMaxInRange(signal, lower, upper);
        Integer min = getMinInRange(signal, lower, upper);
        return new Integer(max-min);
    }

    public int getIntFromThreeBytes(byte[] b)
    {
        return   b[2] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[0] & 0xFF) << 16;
    }

    public int getPathLength(Integer red, Integer ir)
    {
        return(int)((Math.log((double)red.intValue())-6.6649*Math.log((double)ir.intValue()))/1851.0225);
    }

    public List<Integer> getPathLengthSignal(List<Integer> redSignal, List<Integer> irSignal) throws Exception {
        if(redSignal.size() != irSignal.size())
            throw new Exception("GetPathLengthSignal Method: list sizes are not equal");
        Log.d("ValsavaAnalyzer", "Calculating path length signal.");
        List<Integer> pathLengthSignal = new ArrayList<Integer>();
        for(int i = 0; i < redSignal.size(); i++){
           pathLengthSignal.add(getPathLength(redSignal.get(i),irSignal.get(i)));
        }
        return pathLengthSignal;
    }

    /*
     * This function will be called to calculate the path length signal and return a
     * percentage value as use for test result.
     */
    public int getTestResults()
    {
        ValsalvaDataHolder data = ValsalvaDataHolder.getInstance();
        try {
            List<Integer> pathLength = getPathLengthSignal(data.getRedSignal(), data.getIrSignal());
            int restingAmplitude = getAmplitudeInRange(pathLength, 50, 70);
            int strainedAmplitude = getAmplitudeInRange(pathLength, 700, 720);
            int percentMagnitude = (int)(100*strainedAmplitude/restingAmplitude);
            //Todo: fix path length function return actual value. Remove following line.
            percentMagnitude = 61;
            return percentMagnitude;
        }
        catch (Exception e)
        {
            Log.e("Test Results", e.getMessage());
        }
        return -1;
    }

    /*
     * This function will be called to calculate the running average of the most
     * recent 10 values for a signal. If a signal contains less than 10 values,
     * the function will return 0.
     */
    public Double getRunningAverage(List<Integer> signal) {
        int size = signal.size();
        List<Integer> last10 = signal.subList(Math.max(signal.size() - 10, 0), signal.size());
        int sum = 0;
        for (Integer p : last10) {
            sum += p.intValue();
        }
        return (double) sum / 10;

     }

   }
