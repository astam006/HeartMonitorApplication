package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // Default view to the splash page.

        // Create a thread that sleeps for 5 seconds before transitioning to the
        // main menu activity. An intent is basically a request for the app to
        // change activities.
        Thread splashTimer = new Thread() {
          public void run(){
              try{
                  sleep(2000);
                  Intent menuIntent = new Intent("edu.odu.ece486.hm_app.MENU");
                  startActivity(menuIntent);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              } finally {
                  finish();
              }
          }
        };
        splashTimer.start();        // Starting the thread.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
