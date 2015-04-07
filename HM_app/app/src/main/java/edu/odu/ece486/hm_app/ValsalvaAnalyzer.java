package edu.odu.ece486.hm_app;

import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Iterator;
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
        return Math.abs((Math.log(red)-6.6649*Math.log(ir))/1851.0225);
    }

    public List<Double> getPathLengthSignal(List<Double> redSignal, List<Double> irSignal) throws Exception {
        if(redSignal.size() != irSignal.size())
            throw new Exception("GetPathLengthSignal Method: list sizes are not equal");
        Log.d("ValsavaAnalyzer", "Calculating path length signal.");
        List<Double> pathLengthSignal = new ArrayList<Double>();
        for(int i = 0; i < redSignal.size(); i++){
           pathLengthSignal.add(getPathLength(redSignal.get(i),irSignal.get(i)));
        }
        Log.d("ValsavaAnalyzer", "Size of path length signal: " + pathLengthSignal.size());
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
            List<Integer> minimas = findMinimas(pathLength);
            List<Double> amplitudes = calculateAmplitude(pathLength, minimas);
            int percentMagnitude = getRatio(amplitudes);
            return percentMagnitude;
        }
        catch (Exception e)
        {
            Log.e("Test Results", "Error while calculating test results.");
        }
        return -1;
    }

    public int getRatio(List<Double> amplitudes)
    {
        return (int)(100*amplitudeRatio(averageRestAmplitude(splitRestAmplitude(amplitudes)),splitTestAmplitude(amplitudes)));
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

    public int SEARCH_RANGE =  15;

    public List<Double> calculateAmplitude(List<Double> pathlength, List<Integer> minima)
    {
        Log.d("ValsavaAnalyzer", "Calculating amplitude signal.");
        List<Double> amplitude = new ArrayList<Double>();

        for (int i=0; i<minima.size()-1; i++) {

            double value = (pathlength.get(i) + pathlength.get(i+1))/2;
            amplitude.add(Math.abs(maxValueBetweenPoints(pathlength, minima.get(i), minima.get(i + 1)) - (value)));

        }

        return amplitude;
    }

    public List<Integer> findMinimas(List<Double> pathlength)
    {
        Log.d("ValsavaAnalyzer", "Creating minimas list from " + pathlength.size() + " elements.");
        List<Integer> min = new ArrayList<Integer>();
        for (int i = 0; i < pathlength.size(); i++) {
            boolean noMin;
            noMin = false;

            for (int back = i; back > 0 && i - back < SEARCH_RANGE; back--) {
                if (pathlength.get(back) < pathlength.get(i)) {
                    Log.d("ValsavaAnalyzer", "Back is less than current.");
                    noMin = true;
                    break;
                }
            }

            if(! noMin) {
                for (int front = i; front < pathlength.size() && front-i < SEARCH_RANGE; front++) {
                    if (pathlength.get(front) < pathlength.get(i)) {
                        Log.d("ValsavaAnalyzer", "Front is less than current.");
                        noMin = true;
                        break;
                    }
                }
            }

            if (! noMin) {
                Log.d("ValsavaAnalyzer", "Adding index" + i + "to minimas list");
                min.add(new Integer(i));
            }
        }
        Log.d("ValsavaAnalyzer", "Size of minimas list: " + min.size());
        return min;
    }

    public List<Integer> groomMinimas(List<Integer> minimas)
    {
        for(int i = 0; i < minimas.size()-1; i++)
        {
            if(minimas.get(i)+1 == minimas.get(i+1))
            {
                minimas.remove(i);
            }
        }
        return minimas;
    }


    public Double maxValueBetweenPoints(final List<Double> pathlength, int begin, int end)
    {
        double max = 0;

        for (int i = begin+1; i < end; i++) {
            if(max < pathlength.get(i)) {
                max = pathlength.get(i);
            }
        }
        return max;
    }

    //Next two functions split the list of amplitude values into two list, before and after the valsalva
    //maneuver

    public List<Double> splitRestAmplitude(final List<Double> amplitude)
    {
        Log.d("ValsavaAnalyzer", "Grabbing rest amplitude values.");
        Log.d("ValsavaAnalyzer", "Size of amplitude signal: " + amplitude.size());
        List<Double> restAmplitude = new ArrayList<Double>(amplitude.subList(8,15));

        return restAmplitude;
    }

    public List<Double> splitTestAmplitude(final List<Double> amplitude) {
        Log.d("ValsavaAnalyzer", "Grabbing test amplitude values.");
        List<Double> testAmplitude = new ArrayList<Double>(amplitude.subList(5,amplitude.size()));

        return testAmplitude;
    }

    // Will determine the average amplitude during the second half of the rest period
    public Double averageRestAmplitude(final List<Double> restAmplitude)
    {
        Log.d("ValsavaAnalyzer", "Calculating average rest amplitude.");
        int length = restAmplitude.size();
        double averageAmplitude = 0;
        for (int i = 0; i < length; i++){
            averageAmplitude += restAmplitude.get(i);
        }
        return averageAmplitude/restAmplitude.size();
    }

    // Will take the averageRestAmplitude and create a ratio for every amplitude value during the valsalva
    // maneuver and out put a list of values
    public Double amplitudeRatio(final Double averageAmplitude,final List<Double> testAmplitude)
    {
        Log.d("ValsavaAnalyzer", "Calculating amplitude ratio.");
        double lowTestAmplitude1 = Collections.min(testAmplitude);
        testAmplitude.remove(testAmplitude.indexOf(lowTestAmplitude1));
        double lowTestAmplitude2 = Collections.min(testAmplitude);

        double lowTestAmplitude = (lowTestAmplitude1 + lowTestAmplitude2/2);
        double ratio = 0;

        ratio = lowTestAmplitude/averageAmplitude;

        return ratio;
    }
}
