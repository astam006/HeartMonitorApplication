package edu.odu.ece486.hm_app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 1/17/2015. This class will contain all of the functions necessary to
 * perform analysis on data gathered from peripherals during the valsalva maneuver.
 */
public class ValsalvaAnalyzer {

    public Double getMaxInRange(List<Double> signal, int lower, int upper)
    {
        Double max = new Double(0);
        for(int i = lower; i < upper+1; i++){
            if (max < signal.get(i)){
                max = signal.get(i);
            }
        }
        return max;
    }

    public Double getMinInRange(List<Double> signal, int lower, int upper)
    {
        Double min = new Double(256); //TODO: Update this value (256) to contain the maximum possible value.
        for(int i = lower; i < upper+1; i++){
            if (min > signal.get(i)){
                min = signal.get(i);
            }
        }
        return min;
    }

    public Double getAmplitudeInRange(List<Double> signal, int lower, int upper)
    {
        Double max = getMaxInRange(signal, lower, upper);
        Double min = getMinInRange(signal, lower, upper);
        return new Double(max-min);
    }

    public int getIntFromThreeBytes(byte[] b)
    {
        return   b[2] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[0] & 0xFF) << 16;
    }

    public Double getPathLength(Double red, Double ir)
    {
        return ((Math.log(red)-6.6649*Math.log(ir))/1851.0225);
    }

    public List<Double> getPathLengthSignal(List<Double> redSignal, List<Double> irSignal) throws Exception {
        if(redSignal.size() != irSignal.size())
            throw new Exception("GetPathLengthSignal Method: list sizes are not equal");
        Log.d("ValsavaAnalyzer", "Calculating path length signal.");
        List<Double> pathLengthSignal = new ArrayList<Double>();
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
            List<Double> pathLength = getPathLengthSignal(data.getRedSignal(), data.getIrSignal());
            Double restingAmplitude = getAmplitudeInRange(pathLength, 200, 280);
            Double strainedAmplitude = getAmplitudeInRange(pathLength, 700, 750);
            int percentMagnitude = (int)(100*strainedAmplitude/restingAmplitude);
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
    public Double getRunningAverage(List<Double> signal) {
        int size = signal.size();
        List<Double> last10 = signal.subList(Math.max(signal.size() - 10, 0), signal.size());
        Double sum = new Double(0);
        for (Double p : last10) {
            sum += p.intValue();
        }
        return (sum /10);

     }
    /*
     * This function will be called to calculate the root mean square of aa signal.
     */
    public Double RMS(ArrayList<Double> signal){
        double ms = 0;
        for (int i = 0; i < signal.size(); i++)
            ms += signal.get(i)*signal.get(i);
        ms /= signal.size();
        return Math.sqrt(ms);
    }

    /*
     * This function will be used to calculate the mean of a signal.
     */
    public Double Mean(ArrayList<Double> signal){
        double mean = 0;
        for (int i = 0; i < signal.size(); i++)
            mean += signal.get(i);
        mean /= signal.size();
        return mean;
    }

    /*
     * This function will calculate the R constant for oxygen saturation.
     */
    public Double RConstant(ArrayList<Double> ir, ArrayList<Double> red){
        return ((RMS(red)/Mean(red)) / (RMS(ir)/Mean(ir)));
    }

    /*
     * This function will calculate the oxygen saturation of a signal.
     */
    public Double OxygenSaturation(ArrayList<Double> red, ArrayList<Double> ir){
        return (110 - 25*RConstant(ir, red));
    }

   }
