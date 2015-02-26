package edu.odu.ece486.hm_app;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ResultsActivity extends Activity {

    private cBaseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        app = (cBaseApplication)getApplication();
        app.sendEndDataTransferCommand();

        // Variables used to modify the results screen.
        int calculatedPercentage = 0;
        int numberRedValues = 0;
        int numberIRValues = 0;
        TextView percentageTextView = (TextView)findViewById(R.id.percentageView);
        TextView numRedValues = (TextView)findViewById(R.id.numRedValues);
        TextView numIRValues = (TextView)findViewById(R.id.numIRValues);
        ValsalvaAnalyzer analyzer = new ValsalvaAnalyzer();

        // Implement the Graph buttons
        implementRedGraphButton();
        implementIRGraphButton();
        implementPressureGraphButton();

        // Implement the return to main menu button
        implementMainMenuButton();
        implementSaveButton();

        // Pull data from the ValsalvaAnalyzer
        calculatedPercentage = analyzer.getTestResults();
        numberRedValues = ValsalvaDataHolder.getInstance().getRedSignal().size();
        numberIRValues = ValsalvaDataHolder.getInstance().getIrSignal().size();

        numRedValues.setText(Long.toString(numberRedValues) + " received Red Values");
        numIRValues.setText(Long.toString(numberIRValues) + " received IR Values");

        if(calculatedPercentage == -1)
            percentageTextView.setText("Error during calculation.");
        else
            percentageTextView.setText(Long.toString(calculatedPercentage) + "%");
    }

    /**
     * Implement the save button functionality
     */
    private void implementSaveButton() {
        Button saveButton = (Button) findViewById(R.id.saveButton);

        // Save the test results
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ValsalvaDataHolder.getInstance().save();
                    Toast.makeText(getApplicationContext(),
                            "Test Results have been saved.", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e("Save Data", e.getMessage());
                }
            }
        });
    }

    /**
     * Return to the main menu when button is pressed on the results screen.
     * The testing activity is destroyed when they return to the main menu.
     */
    private void implementMainMenuButton() {
        Button returnButton = (Button) findViewById(R.id.returnToMainMenuButton);

        // Open the Valsalva test activity.
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to Test Activity
                startActivity(new Intent(ResultsActivity.this, MainMenuActivity.class));
                ValsalvaDataHolder.getInstance().cleanUp();
                ResultsActivity.this.finish();
            }
        });
    }

    /*
     * Display the Red Values Graph when the respective button is pressed.
     */
    private void implementRedGraphButton() {
        Button redGraphBtn = (Button) findViewById(R.id.redGraphBtn);

        redGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and open the Red Values Graph.
                openRedGraph();
            }
        });
    }

    /*
     * Display the IR Values Graph when the respective button is pressed.
     */
    private void implementIRGraphButton() {
        Button irGraphBtn = (Button) findViewById(R.id.irGraphBtn);

        irGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and open the IR Values Graph.
                openIRGraph();
            }
        });
    }

    /*
     * Display the Air Pressure Graph when the respective button is pressed.
     */
    private void implementPressureGraphButton() {
        Button pressGraphBtn = (Button) findViewById(R.id.pressureGraphBtn);

        pressGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and open the Pressure Graph.
                openPressureGraph();
            }
        });
    }

    /*
     * This method will generate and then open a graph of the Red Values received from the last
     * test taken by the user.
     */
    private void openRedGraph() {
        // Total number of data points to plot.
        int numPoints = ValsalvaDataHolder.getInstance().getRedSignal().size();
        // Get the Red Values list
        List<Double> redData = ValsalvaDataHolder.getInstance().getRedSignal();
        //Creating an XY Series for the Red Data
        XYSeries redSeries = new XYSeries("Red Signal");
        // Adding data to the series.
        for(int i=0;i<numPoints;i++)
            redSeries.add(i, redData.get(i));

        // Creating a dataSet to hold the series
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        // Adding Red Series to the dataSet
        dataSet.addSeries(redSeries);

        // Creating XYSeriesRenderer to customize redSeries
        XYSeriesRenderer redRenderer = new XYSeriesRenderer();
        redRenderer.setColor(Color.RED);
        redRenderer.setPointStyle(PointStyle.CIRCLE);
        redRenderer.setFillPoints(true);
        redRenderer.setLineWidth(2);
        //redRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setAxisTitleTextSize(35);
        multiRenderer.setLegendTextSize(25);
        multiRenderer.setChartTitleTextSize(35);
        multiRenderer.setLabelsTextSize(20);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setMargins(new int[] {60, 70, 15, 0});
        multiRenderer.setYAxisMin(0);
        multiRenderer.setChartTitle("Red Value Chart");
        multiRenderer.setXTitle("Test Duration");
        multiRenderer.setYTitle("Red Signal Value");
        multiRenderer.setZoomButtonsVisible(true);

        // Add redRenderer to the overall graph
        multiRenderer.addSeriesRenderer(redRenderer);

        // Creating an intent to plot line chart using dataSet and multipleRenderer
        Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataSet, multiRenderer);

        // Start Activity
        startActivity(intent);
    }

    /*
     * This method will generate and then open a graph of the IR Values received from the last
     * test taken by the user.
     */
    private void openIRGraph() {
        // Total number of data points to plot.
        int numPoints = ValsalvaDataHolder.getInstance().getIrSignal().size();
        // Get the IR Values list.
        List<Double> irData = ValsalvaDataHolder.getInstance().getIrSignal();
        // Creating an XY Series for the IR Data.
        XYSeries irSeries = new XYSeries("IR Signal");
        // Adding data to the series.
        for(int i = 0; i < numPoints; i++)
            irSeries.add(i, irData.get(i));

        // Creating a dataSet to hold the series
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        // Adding IR Series to the dataSet
        dataSet.addSeries(irSeries);

        // Creating XYSeriesRenderer to customize irSeries
        XYSeriesRenderer irRenderer = new XYSeriesRenderer();
        irRenderer.setColor(Color.WHITE);
        irRenderer.setPointStyle(PointStyle.CIRCLE);
        irRenderer.setFillPoints(true);
        irRenderer.setLineWidth(2);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setAxisTitleTextSize(35);
        multiRenderer.setLegendTextSize(25);
        multiRenderer.setChartTitleTextSize(35);
        multiRenderer.setLabelsTextSize(20);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setMargins(new int[] {60, 70, 15, 0});
        multiRenderer.setYAxisMin(0);
        multiRenderer.setChartTitle("IR Value Chart");
        multiRenderer.setXTitle("Test Duration");
        multiRenderer.setYTitle("IR Signal Value");
        multiRenderer.setZoomButtonsVisible(true);

        // Add irRenderer to the overall graph
        multiRenderer.addSeriesRenderer(irRenderer);

        // Creating an intent to plot line char using dataSet and multipleRenderer
        Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataSet, multiRenderer);

        // Start Activity
        startActivity(intent);
    }

    /*
     * Create and open a graph of the most recently collected pressure values.
     */
    private void openPressureGraph() {
        // Total number of data points to plot.
        int numPoints = ValsalvaDataHolder.getInstance().getLungPressureSignal().size();
        // Get the IR Values list.
        List<Integer> pressureData = ValsalvaDataHolder.getInstance().getLungPressureSignal();
        // Creating an XY Series for the IR Data.
        XYSeries pressureSeries = new XYSeries("Air Pressure");
        // Adding data to the series.
        for(int i = 0; i < numPoints; i++)
            pressureSeries.add(i, pressureData.get(i));

        // Creating a dataSet to hold the series
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        // Adding IR Series to the dataSet
        dataSet.addSeries(pressureSeries);

        // Creating XYSeriesRenderer to customize irSeries
        XYSeriesRenderer pressRenderer = new XYSeriesRenderer();
        pressRenderer.setColor(Color.GREEN);
        pressRenderer.setPointStyle(PointStyle.CIRCLE);
        pressRenderer.setFillPoints(true);
        pressRenderer.setLineWidth(2);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setAxisTitleTextSize(35);
        multiRenderer.setLegendTextSize(25);
        multiRenderer.setChartTitleTextSize(35);
        multiRenderer.setLabelsTextSize(20);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setMargins(new int[] {60, 60, 15, 0});
        multiRenderer.setYAxisMin(0);
        multiRenderer.setChartTitle("Air Pressure Chart");
        multiRenderer.setXTitle("Test Duration");
        multiRenderer.setYTitle("Pressure (mmHg)");
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setZoomButtonsVisible(true);

        // Add irRenderer to the overall graph
        multiRenderer.addSeriesRenderer(pressRenderer);

        // Creating an intent to plot line char using dataSet and multipleRenderer
        Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataSet, multiRenderer);

        // Start Activity
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
