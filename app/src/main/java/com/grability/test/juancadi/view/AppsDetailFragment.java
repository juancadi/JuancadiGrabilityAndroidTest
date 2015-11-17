package com.grability.test.juancadi.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grability.test.juancadi.R;
import com.grability.test.juancadi.model.ITunesEntry;
import com.grability.test.juancadi.persistence.ITunesRssDBAdapter;


public class AppsDetailFragment extends Fragment {

    TextView txtNameApp;
    TextView txtPriceApp;
    TextView txtCategoryApp;
    TextView txtRightsApp;
    TextView txtArtistApp;
    TextView txtReleaseApp;
    TextView txtSummaryApp;
    ImageButton btnDownloadApp;

    ITunesEntry iTunesSelectedApp = null;
    ITunesRssDBAdapter dbITunesRss;

    private int currentIndex = -1;


    public AppsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        dbITunesRss = new ITunesRssDBAdapter(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apps_detail, container, false);


    }

    public int getLastSelectedIndex() {
        return currentIndex;
    }


    public void showAppDetail(int idEntry) {

        Log.i("AppsDetailFragment", "idEntry: " + idEntry);

        if(idEntry != -1) {

        txtNameApp = (TextView)getView().findViewById(R.id.textViewNameApp);
        txtPriceApp = (TextView)getView().findViewById(R.id.textViewPriceApp);
        txtCategoryApp = (TextView)getView().findViewById(R.id.textViewCategoryApp);
        txtRightsApp = (TextView)getView().findViewById(R.id.textViewRightsApp);
        txtArtistApp = (TextView)getView().findViewById(R.id.textViewArtistApp);
        txtReleaseApp = (TextView)getView().findViewById(R.id.textViewReleaseDateApp);
        txtSummaryApp = (TextView)getView().findViewById(R.id.textViewSummaryApp);

        btnDownloadApp = (ImageButton) getView().findViewById(R.id.buttonDownloadApp);
        btnDownloadApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iTunesSelectedApp != null) {

                    //Intent for show the web page to download the selected app
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(iTunesSelectedApp.getAppDownloadLink()));
                    startActivity(webIntent);
                    getActivity().overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

                }

            }
        });

            //This variable is used to track the current screen and responds to changes in the screen orientation
            currentIndex = idEntry;

            synchronized (dbITunesRss) {

                dbITunesRss.open();
                iTunesSelectedApp = dbITunesRss.getEntry(idEntry);
                dbITunesRss.close();

            }

            if (iTunesSelectedApp != null) {

                txtNameApp.setText(iTunesSelectedApp.getName());
                txtPriceApp.setText(iTunesSelectedApp.getPrice());
                txtCategoryApp.setText(iTunesSelectedApp.getCategory());
                txtRightsApp.setText(iTunesSelectedApp.getRights());
                txtArtistApp.setText(iTunesSelectedApp.getArtist());
                txtReleaseApp.setText(iTunesSelectedApp.getReleaseDate());
                txtSummaryApp.setText(iTunesSelectedApp.getSummary());
            }

        }
    }




}
