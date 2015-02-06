package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TestActivityV2 extends Activity {

    private cBaseApplication app;

    int progressValue = 1;
    int count = 15;
    private boolean testOver = false;
    private PressureProgressBar pressureProgressBar;
    private TextView pressureValueText;
    private TextView timerView;
    private AlertDialog lowPressureAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_v2);

        app = (cBaseApplication)getApplication();
        app.sendBeginDataTransferCommand();

        lowPressureAlert = createAlertDialog();
        timerView = (TextView) findViewById(R.id.timerTextView);
        pressureValueText = (TextView) findViewById(R.id.pressureValueTxt);
        pressureProgressBar = (PressureProgressBar) findViewById(R.id.pressureProgressBar);
        pressureProgressBar.setProgress(0);

        ProgressBarTask pbTask = new ProgressBarTask();
        pbTask.execute();

        new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millsUntilFinished) {
                timerView.setTextColor(Color.RED);
                timerView.setText(Long.toString(millsUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                testOver = true;
                timerView.setTextColor(Color.GREEN);
                timerView.setText("Done!");
            }
        }.start();
    }

    /**
     * Method used to update the PressureProgressBar's current reading, as well as
     * determine which color the bar should be based upon it's current pressure value.
     * @param newPressure: Pressure value received from background thread.
     */
    private void updateProgress(PressureProgressBar pBar, int newPressure) {
        pressureProgressBar = pBar;
        pressureProgressBar.setProgress(newPressure);
        pressureValueText.setText(Long.toString(newPressure) + " mmHg");
        if(newPressure < 20)
            lowPressureAlert.show();
        else
            lowPressureAlert.dismiss();
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Keep pressure above 20mmHg!")
                .setTitle("Low Pressure Warning!")
                .setIcon(R.drawable.ic_warning)
                .setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();
        return  alertDialog;
    }

    /**
     * Method called at the end of the countdown to allow the user to
     * see the 'Done!' message and prepare to look at results.
     */
    private void gracefulResultsTransition() {
        try {
            Thread.sleep(1500);
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            startActivity(new Intent(TestActivityV2.this, ResultsActivity.class));
            TestActivityV2.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_activity_v2, menu);
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

    /**
    * ProgressBarTask extends the AsyncTask class that allows for the progressbar to receive
    * it's new data from a background thread as to not tie up the main UI thread resources.
    */
    class ProgressBarTask extends AsyncTask <Void, Integer, Void> {

        // Variable used to store the next pressure value found from ValsalvaDataHolder.
        int newPressure;

        protected PressureProgressBar changePressureColor(PressureProgressBar pBar, int newPressure) {
            if(newPressure >= 20)
                pBar.setProgressDrawable(getResources()
                        .getDrawable(R.drawable.custom_green_progressbar));
            else
                pBar.setProgressDrawable(getResources()
                        .getDrawable(R.drawable.custom_red_progressbar));
            return pBar;
        }

        /**
         * Retrieve the next pressure value from the sensor.
         * Send the new value to the updateProgress method to display
         * the newly updated progressBar in the UI.
         * @param arguments: No arguments are passed to this method.
         */
        @Override
        protected Void doInBackground(Void... arguments) {
            while(!testOver) {
                newPressure = ValsalvaDataHolder.getInstance().getPressure();
                publishProgress(newPressure);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            // Before returning, dismiss any dialogs and initiate transition to
            // the Results Activity.
            lowPressureAlert.dismiss();
            gracefulResultsTransition();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... newPressureValue) {
            PressureProgressBar newProgBar = changePressureColor(pressureProgressBar, newPressure);
            updateProgress(newProgBar, newPressureValue[0]);
        }

    }
}
