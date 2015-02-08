package edu.odu.ece486.hm_app;

import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

/**
 * Created by Larry on 1/26/2015. This class will record all necessary data from the valsalva test.
 */
public class ValsalvaDataHolder {
    private static PressureSensor pressureSensor;
    private static List<Integer> irSignal;
    private static List<Integer> redSignal;

    public static void initHolder(){
        pressureSensor = new PressureSensor();
        irSignal = new ArrayList<Integer>();
        redSignal = new ArrayList<Integer>();
    }

    public Integer getPressure() {return pressureSensor.getPressure();}
    public void updatePressure(int newPressure ) { pressureSensor.update(newPressure);}
    public List<Integer> getIrSignal() { return irSignal; }
    public List<Integer> getRedSignal() { return redSignal; }

    public void updateFromPeripheral(int newPressure, int irPoint, int redPoint)
    {
        updatePressure(newPressure);
        irSignal.add(irPoint);
        redSignal.add(redPoint);
    }

    public void save() throws IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String redFileName = "RedValsalvaData.csv";
        String redFilePath = baseDir + File.separator + redFileName;
        File redFile = new File(redFilePath);

        CSVWriter redWriter;
        if(redFile.exists() && !redFile.isDirectory()){
            redFile.delete();
            FileWriter mRedFileWriter = new FileWriter(redFilePath , true);
            redWriter = new CSVWriter(mRedFileWriter);
        }
        else {
            redWriter = new CSVWriter(new FileWriter(redFilePath));
        }
        redWriter.writeNext(getStringArray(redSignal));
        redWriter.close();

        String irFileName = "IRValsalvaData.csv";
        String irFilePath = baseDir + File.separator + irFileName;
        File irFile = new File(irFilePath);

        CSVWriter irWriter;
        if(irFile.exists() && !irFile.isDirectory()){
            irFile.delete();
            FileWriter mIrFileWriter = new FileWriter(irFilePath , true);
            irWriter = new CSVWriter(mIrFileWriter);
        }
        else {
            irWriter = new CSVWriter(new FileWriter(irFilePath));
        }
        irWriter.writeNext(getStringArray(irSignal));
        irWriter.close();
    }

    public String[] getStringArray(List<Integer> intArray)
    {
        List<Integer> oldList = intArray;
        List<String> newList = new ArrayList<String>(oldList.size());
        for (Integer myInt : oldList) {
            newList.add(String.valueOf(myInt));
        }
        String[] stringArray = newList.toArray(new String[newList.size()]);
        return stringArray;
    }

    public void cleanUp()
    {
        pressureSensor = null;
        irSignal.clear();
        irSignal = null;
        redSignal.clear();
        redSignal = null;
        holder = null;
    }

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


    //Todo: This is an e
    public static void main(String[] args) throws Exception
    {
        String csv = "data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

        String [] record = "3,AJ, Larry, Russeell" .split(",");

        writer.writeNext(record);

        writer.close();
    }


}
