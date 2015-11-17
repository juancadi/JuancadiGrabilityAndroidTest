package com.grability.test.juancadi.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grability.test.juancadi.R;
import com.grability.test.juancadi.connection.HttpConnection;
import com.grability.test.juancadi.control.PreferencesManager;
import com.grability.test.juancadi.model.ITunesEntry;
import com.grability.test.juancadi.model.ITunesFeed;
import com.grability.test.juancadi.persistence.ITunesRssDBAdapter;
import com.grability.test.juancadi.util.DeviceProperties;
import com.grability.test.juancadi.util.ITunesRssTranslator;
import com.grability.test.juancadi.util.ImageProcessing;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private static final String URL_ITUNES_RSS = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    Timer timerSplash;
    Context context;

    HttpConnection httpConnection;
    SharedPreferences preferences;
    PreferencesManager mngrPreferences;
    ITunesRssDBAdapter dbITunesRss;
    ITunesRssAsyncTask iTunesAsyncTask;

    ITunesRssTranslator iTunesRssTranslator;
    ArrayList<ITunesEntry> iTunesTopApplications = new ArrayList<ITunesEntry>();
    ITunesFeed iTunesFeedProperties;

    DisplayMetrics displayMetrics;
    ProgressBar splashProgressBar;
    TextView splashTextView;

	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_splash);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        splashProgressBar = (ProgressBar) findViewById(R.id.splashProgressBar);
        splashTextView = (TextView) findViewById(R.id.splashTextView);

        //Get  a Preference Manager reference
        preferences = this.getSharedPreferences(PreferencesManager.PREFERENCES_FILE, Context.MODE_PRIVATE);
        mngrPreferences = new PreferencesManager(preferences);
		
		this.timerSplash = new Timer();
		this.context = this;

        //Get  a Http`Connection reference
        httpConnection = new HttpConnection();
    }

	@Override
	public void onStart(){
		super.onStart();

        //Verify device internet connection
        if(DeviceProperties.checkConnectivity(this)){

            dbITunesRss = new ITunesRssDBAdapter(this);
            iTunesAsyncTask = new ITunesRssAsyncTask();
            iTunesAsyncTask.execute(URL_ITUNES_RSS);

        }else {

            String lastRssUpdate = mngrPreferences.getRssUpdate();


            if(lastRssUpdate.equalsIgnoreCase(PreferencesManager.PREFERENCE_NOT_FOUND)){

                //Without connection or entries stored in DB, then... Bye bye!
                new AlertDialog.Builder(this)
                        .setTitle("iTUNES TOP APPS")
                        .setMessage("To synchronize de iTunes Apps List, the internet connection must be available. Please check and try again!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                                SplashActivity.this.finish();
                                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);

                            }
                        })
                        .show();

            }else{

                //Show RSS saved in DB if user wants it
                new AlertDialog.Builder(this)
                        .setTitle("iTUNES APP OFFLINE")
                        .setMessage("Your device does not have internet access!\n" +
                                "Last Update: "+lastRssUpdate+"\n" +"Do you want to continue?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                                Intent topAppsIntent = new Intent(SplashActivity.this, TopAppsActivity.class);
                                startActivity(topAppsIntent);
                                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                                SplashActivity.this.finish();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                                SplashActivity.this.finish();
                                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                            }
                        })
                        .show();

            }


        }


	}

    //Async Task used to synchronize the DB with the RSS feed and show the porgress in UI.
	class ITunesRssAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {

            splashTextView.setVisibility(View.VISIBLE);
            splashProgressBar.setVisibility(View.VISIBLE);
            splashProgressBar.setProgress(0);

		}

		@Override
		protected Boolean doInBackground(String... urlRequest) {

			JSONObject iTunesRssJsonResponse;
            int progressBarValue = 0;
            double progressRate = 0;
            boolean processResponse = false;

			try {

                iTunesRssJsonResponse = httpConnection.getJsonObjectFromUrl(urlRequest[0]);

				//Log.i(TAG+">>doBack", "iTunes RSS Response: " + iTunesRssJsonResponse);

                iTunesRssTranslator = new ITunesRssTranslator();

                iTunesFeedProperties = iTunesRssTranslator.getRssFeedProperties(iTunesRssJsonResponse);
                //Log.i(TAG+">>doBack", "iTunes RSS Feed Properties: " + iTunesFeedProperties.toString());

                if(!iTunesFeedProperties.getLastUpdate().equalsIgnoreCase(mngrPreferences.getRssUpdate())){

                    mngrPreferences.setRssAuthor(iTunesFeedProperties.getAuthor());
                    mngrPreferences.setRssRights(iTunesFeedProperties.getRights());
                    mngrPreferences.setRssUpdate(iTunesFeedProperties.getLastUpdate());

                    iTunesTopApplications = iTunesRssTranslator.getTopAppsFromRss(iTunesRssJsonResponse, displayMetrics.densityDpi);
                    //Log.i(TAG+">>doBack", "iTunes RSS Entry[0]: " + iTunesTopApplications.get(0).toString());

                    //The progress showed in the progress bar must be dynamic,
                    // depending of the amount of entries contained in the RSS Feed
                    progressRate = 100/iTunesTopApplications.size();

                    synchronized (dbITunesRss) {

                        dbITunesRss.open();
                        dbITunesRss.deleteAll();

                        for(ITunesEntry iTunesEntry : iTunesTopApplications){


                            //The image downloaded is saved in the DB like a encoded String Base64
                            Bitmap entryBitmapImage = httpConnection.downloadImage(iTunesEntry.getImageLink());
                            iTunesEntry.setImageB64(ImageProcessing.encodeBitmapToBase64(entryBitmapImage, 100));

                            //Log.i(TAG+">>doBack", "iTunes RSS Entry: " + iTunesEntry.toString());

                            long countTemp = dbITunesRss.insertEntry(iTunesEntry);
                            //Log.i(TAG+">>doBack", "iTunes row insert: " + countTemp);

                            progressBarValue += (int) progressRate;
                            publishProgress(progressBarValue);

                        }

                    }

                    if (dbITunesRss != null) {
                        dbITunesRss.close();
                    }

                    processResponse = true;

                }else{

                    processResponse = false;

                }


			} catch (Exception e) {
                //Log.e(TAG+">>doBack", ">>> Error procesando respuesta JSON");
				e.printStackTrace();
                processResponse = false;
			}

            return processResponse;

		}

        @Override
        protected void onProgressUpdate(Integer... values) {

            //Updating progress bar...
            splashProgressBar.setProgress(values[0].intValue());
        }

		@Override
		protected void onPostExecute(Boolean result) {

            int time = 1000;

            if(!result) {

                splashTextView.setVisibility(View.INVISIBLE);
                splashProgressBar.setVisibility(View.INVISIBLE);
                time = 2000;
            }

            timerSplash.schedule(new TimerTask() {

                public void run() {

                    Intent topAppsIntent = new Intent(SplashActivity.this, TopAppsActivity.class);
                    startActivity(topAppsIntent);
                    overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);

                    try {
                        SplashActivity.this.finish();
                        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                    } catch (Throwable e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, time);

		}



	}






}




