package com.grability.test.juancadi.view;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grability.test.juancadi.R;
import com.grability.test.juancadi.model.ITunesEntry;
import com.grability.test.juancadi.persistence.ITunesRssDBAdapter;
import com.grability.test.juancadi.util.ImageProcessing;

import java.util.ArrayList;


public class AppsListFragment extends ListFragment {

    private static final String TAG = "AppsListFragment";
    private ListSelectionListener mListener = null;
    ITunesEntriesAdapter iTunesEntriesAdapter;

    ITunesRssDBAdapter dbITunesRss;
    ArrayList<ITunesEntry> iTunesTopAppsEntries = new ArrayList<ITunesEntry>();


    public interface ListSelectionListener {
        public void onListSelection(int index);
    }

    // Called when the user selects an item from the List
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {

        // Indicates the selected item has been checked
        getListView().setItemChecked(pos, true);

        // Inform the TopAppsActivity that the item in position pos has been selected
        mListener.onListSelection(pos);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            // Set the ListSelectionListener for communicating with the TopAppsActivity
            mListener = (ListSelectionListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        iTunesEntriesAdapter = new ITunesEntriesAdapter(getActivity());
        dbITunesRss = new ITunesRssDBAdapter(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");

        // Set the list choice mode to allow only one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        synchronized (dbITunesRss) {

            dbITunesRss.open();

            iTunesTopAppsEntries = dbITunesRss.getAllEntries();
            Log.i(TAG, getClass().getSimpleName() + ": iTunesTopAppsEntries Size = " + iTunesTopAppsEntries.size());

            dbITunesRss.close();
        }

        // Set the list adapter for the ListView
        setListAdapter(iTunesEntriesAdapter);
        iTunesEntriesAdapter.setData(iTunesTopAppsEntries);
        iTunesEntriesAdapter.notifyDataSetChanged();
    }

    public class ITunesEntriesAdapter extends BaseAdapter {

        Context mContext;
        private ArrayList<ITunesEntry> mArrayList = new ArrayList<ITunesEntry>();


        public ITunesEntriesAdapter(Context context) {

            mContext = context;
        }

        public void setData(ArrayList<ITunesEntry> list){

            mArrayList = list;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public ITunesEntry getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View listItem = inflater.inflate(R.layout.itunes_entry_item, null);

            ImageView imgApp = (ImageView) listItem.findViewById(R.id.imageViewApp);
            imgApp.setImageBitmap(ImageProcessing.decodeBitmapFromBase64(mArrayList.get(position).getImageB64()));

            TextView txtAppName = (TextView) listItem.findViewById(R.id.textViewName);
            txtAppName.setText(mArrayList.get(position).getName());

            TextView txtAppPrice = (TextView) listItem.findViewById(R.id.textViewPrice);
            txtAppPrice.setText(mArrayList.get(position).getPrice());

            return listItem;
        }



    }
}
