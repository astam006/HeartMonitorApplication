package edu.odu.ece486.hm_app;

import java.util.List;

/**
 * Created by Larry on 1/17/2015. This class will contain all of the required functions to perform
 * analysis on data gathered from the peripheral devices during the valsalva test.
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
}
