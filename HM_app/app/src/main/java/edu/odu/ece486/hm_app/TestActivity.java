package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DecimalFormat;

public class TestActivity extends Activity {

    private cBaseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        app = (cBaseApplication)getApplication();

        /**
         * Initialize widgets for manipulation.
         */
        final TextView timerView = (TextView) findViewById(R.id.timerValueView);

        /**
         * Use a countdown timer for the valsalva test.
         */
        /**-----------------------------------------------------------------*/
        new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millsUntilFinished) {
                timerView.setTextColor(Color.RED);
                timerView.setText(Long.toString(millsUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timerView.setTextColor(Color.GREEN);
                timerView.setText("Done!");
                startActivity(new Intent(TestActivity.this, ResultsActivity.class));
            }
        }.start();
        /**-----------------------------------------------------------------*/
        app.sendBeginDataTransferCommand();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
