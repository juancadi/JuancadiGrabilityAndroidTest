package com.grability.test.juancadi.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.grability.test.juancadi.R;
import com.grability.test.juancadi.control.PreferencesManager;

public class TopAppsActivity extends AppCompatActivity implements AppsListFragment.ListSelectionListener {

    private static final String TAG = "TopAppsActivity";

    private Toolbar toolbar;
    private AppsListFragment appsListFragment;
    private AppsDetailFragment appsDetailFragment;

    SharedPreferences preferences;
    PreferencesManager mngrPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_apps);
        toolbar = (Toolbar) findViewById(R.id.toolbarList);
        setSupportActionBar(toolbar);

        // Get a reference to the AppsListFragment
        appsListFragment = (AppsListFragment) getFragmentManager()
                .findFragmentById(R.id.apps_list_fragment);

        // Get a reference to the appsDetailFragment
        appsDetailFragment = (AppsDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.apps_detail_fragment);

        preferences = this.getSharedPreferences(PreferencesManager.PREFERENCES_FILE, Context.MODE_PRIVATE);
        mngrPreferences = new PreferencesManager(preferences);

        //ToolBar Config
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("iTunes - Top Apps");
        toolbar.setSubtitle("Last Update: " + mngrPreferences.getRssUpdate().substring(0,10));

        if( appsDetailFragment != null) {

            if(appsDetailFragment.getLastSelectedIndex() != -1){

                appsDetailFragment.showAppDetail(appsDetailFragment.getLastSelectedIndex());

            }else{

                appsDetailFragment.showAppDetail(0);
            }

        }



    }

    @Override
    public void onListSelection(int index) {

        //Detect device (smartphone or tablet) according to the layout config
        boolean existAppDetail =
                (appsDetailFragment != null);

        //Log.i(TAG, "Hay Detalle = " + existAppDetail);

        if(existAppDetail && index != appsDetailFragment.getLastSelectedIndex()) {

            appsDetailFragment.showAppDetail(index);

        } else {

            Intent detailIntent = new Intent(this, AppsDetailActivity.class);
            detailIntent.putExtra(AppsDetailActivity.ID_ITUNES_ENTRY, index);
            startActivity(detailIntent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_apps, menu);
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
