package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button testHelpButton, settingsHelpButton, resultsHelpButton, backButton;
        testHelpButton     = (Button) findViewById(R.id.test_help_button);
        settingsHelpButton = (Button) findViewById(R.id.settings_help_button);
        resultsHelpButton  = (Button) findViewById(R.id.results_help_button);
        backButton         = (Button) findViewById(R.id.return_menu_button);

        implementBackButton(backButton);
        implementTestHelpButton(testHelpButton);
        implementSettingsHelpButton(settingsHelpButton);
        implementResultsHelpButton(resultsHelpButton);
    }

    // Return to the Main Menu when the back button is pressed.
    // Terminate the Help Activity.
    public void implementBackButton(Button backButton) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, MainMenuActivity.class));
                HelpActivity.this.finish();
            }
        });
    }

    public void implementTestHelpButton(Button testButton) {
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, MainMenuActivity.class));
                HelpActivity.this.finish();
            }
        });
    }

    public void implementSettingsHelpButton(Button settingsButton) {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, MainMenuActivity.class));
                HelpActivity.this.finish();
            }
        });
    }

    public void implementResultsHelpButton(Button resultsButton) {
        resultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, MainMenuActivity.class));
                HelpActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
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
