package edu.odu.ece486.hm_app;

import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

/**
 * Created by Larry on 1/26/2015. This class will record all necessary data from the valsalva test.
 */
public class ValsalvaDataHolder {
    //Sensor Information
    private static PressureSensor pressureSensor;
    private static List<Double> irSignal;
    private static List<Double> redSignal;
    private static List<Integer> lungPressureSignal;
    private Integer testStartIndex;
    private Integer testEndIndex;

    public static void initHolder(){
        pressureSensor = new PressureSensor();
        irSignal = new ArrayList<Double>();
        redSignal = new ArrayList<Double>();
        lungPressureSignal = new ArrayList<Integer>();
    }

    public Integer getPressure() { return pressureSensor.getPressure(); }
    public void updatePressure(int newPressure ) { pressureSensor.update(newPressure);}
    public List<Double> getIrSignal() { return irSignal; }
    public List<Double> getRedSignal() { return redSignal; }
    public List<Integer> getLungPressureSignal() { return lungPressureSignal; }
    public int getNumberOfPacketReceived() { return irSignal.size(); }
    public Integer getTestStartIndex(){ return testStartIndex; }
    public Integer getTestEndIndex(){ return testEndIndex; }

    public void SetTestStartedIndex(){
        testStartIndex = getNumberOfPacketReceived();
    }

    public void SetTestEndedIndex() {
        testEndIndex = getNumberOfPacketReceived();
    }

    public void updateFromPeripheral(int newPressure, int irPoint, int redPoint)
    {
        updatePressure(newPressure);
        irSignal.add(new Double(new Double(irPoint)/65536));
        redSignal.add(new Double(new Double(redPoint)/65536));
        lungPressureSignal.add(newPressure);
    }

    public void save() throws IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "ValsalvaData.csv";
        String filePath = baseDir + File.separator + fileName;
        File file = new File(filePath);

        try {
            CSVWriter writer;
            if (file.exists() && !file.isDirectory()) {
                file.delete();
                FileWriter fileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(fileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
            }
            String[] redStringArray = getStringArrayFromDoubles(redSignal);
            String[] irStringArray = getStringArrayFromDoubles(irSignal);
            String[] lungPressureStringArray = getStringArrayFromInts(lungPressureSignal);

            for (int i = 0; i < redStringArray.length; i++) {
                String[] nextLine = new String[3];
                nextLine[0] = irStringArray[i];
                nextLine[1] = redStringArray[i];
                nextLine[2] = lungPressureStringArray[i];
                writer.writeNext(nextLine);
            }
            writer.close();
        }
        catch(IOException e)
        {
            Log.e("SaveCSV", e.getMessage());
        }
    }

    public void saveWithCalculatedData(List<Double> pathLength, List<Integer> minimas, List<Double> amplitudes) throws IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "ValsalvaData.csv";
        String filePath = baseDir + File.separator + fileName;
        File file = new File(filePath);

        try {
            CSVWriter writer;
            if (file.exists() && !file.isDirectory()) {
                file.delete();
                FileWriter fileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(fileWriter);
            } else {
                writer = new CSVWriter(new FileWriter(filePath));
            }
            String[] redStringArray = getStringArrayFromDoubles(redSignal);
            String[] irStringArray = getStringArrayFromDoubles(irSignal);
            String[] lungPressureStringArray = getStringArrayFromInts(lungPressureSignal);
            String[] pathLengthArray = getStringArrayFromDoubles(pathLength);
            String[] amplitudeArray = getStringArrayFromDoubles(amplitudes);

            int ampCount = 0;
            for (int i = 0; i < redStringArray.length; i++) {
                String[] nextLine = new String[6];
                nextLine[0] = irStringArray[i];
                nextLine[1] = redStringArray[i];
                nextLine[2] = lungPressureStringArray[i];
                nextLine[3] = pathLengthArray[i];
                if(minimas.contains(i)) {
                    nextLine[4] = "1";
                    if (ampCount < amplitudes.size()) {
                        nextLine[5] = amplitudeArray[ampCount];
                        ampCount++;
                    } else {
                        nextLine[5] = "0";
                    }
                }
                else
                {
                    nextLine[4] = "0";
                    nextLine[5] = "0";
                }
                writer.writeNext(nextLine);
            }
            writer.close();
        }
        catch(IOException e)
        {
            Log.e("SaveCSV", e.getMessage());
        }
    }



    public String[] getStringArrayFromInts(List<Integer> intArray)
    {
        List<Integer> oldList = intArray;
        List<String> newList = new ArrayList<String>(oldList.size());
        for (Integer myInt : oldList) {
            newList.add(String.valueOf(myInt));
        }
        String[] stringArray = newList.toArray(new String[newList.size()]);
        return stringArray;
    }

    public String[] getStringArrayFromDoubles(List<Double> doubleArray)
    {
        List<Double> oldList = doubleArray;
        List<String> newList = new ArrayList<String>(oldList.size());
        for (Double myDouble : oldList) {
            newList.add(String.valueOf(myDouble));
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
        lungPressureSignal.clear();
        lungPressureSignal = null;
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


    /*
     * This function is intended to be used for importing test data rather than
     * performing the actual test.
     */
    public void ImportCSV(String filePath) throws IOException
    {
        try{
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
                irSignal.add(Double.parseDouble(nextLine[0]));
                redSignal.add(Double.parseDouble(nextLine[1]));
                lungPressureSignal.add(Integer.parseInt(nextLine[2]));
            }
        }
        catch(IOException e)
        {
            Log.e("ImportCSV", e.getMessage());
        }
    }

    public void ImportCSV(InputStream stream) throws IOException
    {
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(stream,"UTF-8"));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
                irSignal.add(Double.parseDouble(nextLine[0]));
                redSignal.add(Double.parseDouble(nextLine[1]));
                lungPressureSignal.add(Integer.parseInt(nextLine[2]));
            }
        }
        catch(IOException e)
        {
            Log.e("ImportCSV", e.getMessage());
        }
    }
}
