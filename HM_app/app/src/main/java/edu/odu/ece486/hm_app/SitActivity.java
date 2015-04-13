package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class SitActivity extends Activity {

    final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sit_instr);

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd("welcomeAudio.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button nextButton = (Button) findViewById(R.id.sitNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the next instruction
                mp.stop();
                startActivity(new Intent(SitActivity.this, PulseActivity.class));
                SitActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SitActivity.this, MainMenuActivity.class));
        SitActivity.this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sit, menu);
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
