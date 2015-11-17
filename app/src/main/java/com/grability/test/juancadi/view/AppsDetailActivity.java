package com.grability.test.juancadi.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.grability.test.juancadi.R;
import com.grability.test.juancadi.control.PreferencesManager;

public class AppsDetailActivity extends AppCompatActivity {

    public static final String ID_ITUNES_ENTRY = "idITunesEntry";

    SharedPreferences preferences;
    PreferencesManager mngrPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);

        //Get A reference to the Preference Manager
        preferences = this.getSharedPreferences(PreferencesManager.PREFERENCES_FILE, Context.MODE_PRIVATE);
        mngrPreferences = new PreferencesManager(preferences);

        //ToolBar config
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("iTunes - Top Apps");
        toolbar.setSubtitle("Last Update: " + mngrPreferences.getRssUpdate().substring(0,10));

        //Reference to AppsDetailFragment
        AppsDetailFragment detailFragment =
                (AppsDetailFragment)getSupportFragmentManager().findFragmentById(R.id.apps_detail_fragment);

        detailFragment.showAppDetail(getIntent().getIntExtra(ID_ITUNES_ENTRY, -1));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //It's necessary to capture the KEYCODE_BACK event to animate the screen transition
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            AppsDetailActivity.this.finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
