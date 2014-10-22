package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;


public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Implement Buttons.
        implementTestButton();
        implementSettingsButton();
        implementHelpButton();
        implementExitButton();
    }

//==================================================================================================

    // Implement Test Button functionality.
    private void implementTestButton() {
        Button testButton = (Button) findViewById(R.id.testButton);

        // Open the Valsalva test activity.
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to Test Activity
                startActivity(new Intent(MainMenuActivity.this, TestActivity.class));
            }
        });
    }

    // Implement Settings Button functionality.
    private void implementSettingsButton() {
        Button settingsButton = (Button) findViewById(R.id.settingsButton);

        // Open the settings activity
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to Settings Activity
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
            }
        });
    }

    // Implement Help Button functionality.
    private void implementHelpButton() {
        Button helpButton = (Button) findViewById(R.id.helpButton);

        // Open the Help activity.
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to help activity.
                startActivity(new Intent(MainMenuActivity.this, HelpActivity.class));
            }
        });
    }

    // Implement Exit Button functionality.
    private void implementExitButton() {
        Button exitButton = (Button) findViewById(R.id.exitButton);

        // Exit the app when the user clicks the Quit button.
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//==================================================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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