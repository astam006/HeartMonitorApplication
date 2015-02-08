package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
