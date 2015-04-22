package edu.odu.ece486.hm_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;


public class SettingsActivity extends PreferenceActivity {

    private cBaseApplication app;

    int progressValue = 1;
    int count = 0;
    private boolean testOver = false;
    private PressureProgressBar pressureProgressBar;
    private TextView pressureValueText, transferStatus;
    private Button toggleButton;
    //private AlertDialog lowPressureAlert;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Loading preferences from the resource file.
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}


