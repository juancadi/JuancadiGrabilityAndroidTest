package com.grability.test.juancadi.util;

//class-name:     	ITunesRssTranslator
//class-overview: 	Implementa lógica necesaria para interpretar la informacion recibida en
//                  formato JSON desde iTunes RSS y organizarla de acuerdo con el modelo definido
//                  para el procesamiento de la información
//class-autor:    	Juancadi
//class-date:     	2015-11-14

import android.util.DisplayMetrics;

import com.grability.test.juancadi.model.ITunesEntry;
import com.grability.test.juancadi.model.ITunesFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ITunesRssTranslator {


    public ITunesFeed getRssFeedProperties(JSONObject iTunesJsonRsp) throws JSONException {

        ITunesFeed iTunesFeed = new ITunesFeed();

        JSONObject feedJsonObject =  iTunesJsonRsp.getJSONObject("feed");

        iTunesFeed.setAuthor(feedJsonObject.getJSONObject("author").getJSONObject("name").getString("label"));
        iTunesFeed.setRights(feedJsonObject.getJSONObject("rights").getString("label"));
        iTunesFeed.setLastUpdate(feedJsonObject.getJSONObject("updated").getString("label"));

        return iTunesFeed;
    }

    public ArrayList<ITunesEntry> getTopAppsFromRss(JSONObject iTunesJsonRsp, int screenDensity) throws JSONException {


        JSONObject feedJsonObject =  iTunesJsonRsp.getJSONObject("feed");
        JSONArray entriesJsonArray =  feedJsonObject.getJSONArray("entry");

        ArrayList<ITunesEntry> iTunesListEntries = new ArrayList<ITunesEntry>();

        for(int i = 0; i < entriesJsonArray.length(); i++){

            iTunesListEntries.add(this.getITunesEntryFromRss(entriesJsonArray.getJSONObject(i), i, screenDensity));

        }

        return iTunesListEntries;
    }


    private ITunesEntry getITunesEntryFromRss(JSONObject entryJsonObject, int index, int screenDensityDpi) throws JSONException {

        double amountPrice;
        String currencyPrice;
        ITunesEntry iTunesEntry = new ITunesEntry();

        iTunesEntry.setIdEntry(index);
        iTunesEntry.setName(entryJsonObject.getJSONObject("im:name").getString("label"));

        switch(screenDensityDpi){

            case DisplayMetrics.DENSITY_MEDIUM: //MDPI

                iTunesEntry.setImageLink(entryJsonObject.getJSONArray("im:image").getJSONObject(0).getString("label"));

                break;

            case DisplayMetrics.DENSITY_HIGH: //HDPI

                iTunesEntry.setImageLink(entryJsonObject.getJSONArray("im:image").getJSONObject(1).getString("label"));

                break;

            case DisplayMetrics.DENSITY_XHIGH: //XHDPI, XXHDPI y XXXHDPI
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH:

                iTunesEntry.setImageLink(entryJsonObject.getJSONArray("im:image").getJSONObject(2).getString("label"));

                break;
        }

        iTunesEntry.setSummary(entryJsonObject.getJSONObject("summary").getString("label"));

        amountPrice = entryJsonObject.getJSONObject("im:price").getJSONObject("attributes").getDouble("amount");
        currencyPrice = entryJsonObject.getJSONObject("im:price").getJSONObject("attributes").getString("currency");

        if(amountPrice != 0){
            iTunesEntry.setPrice("Free");
        }else{
            iTunesEntry.setPrice(amountPrice +" "+ currencyPrice);
        }

        iTunesEntry.setRights(entryJsonObject.getJSONObject("rights").getString("label"));
        iTunesEntry.setAppDownloadLink(entryJsonObject.getJSONObject("link").getJSONObject("attributes").getString("href"));
        iTunesEntry.setIdApp(entryJsonObject.getJSONObject("id").getJSONObject("attributes").getString("im:id"));
        iTunesEntry.setArtist(entryJsonObject.getJSONObject("im:artist").getString("label"));
        iTunesEntry.setCategory(entryJsonObject.getJSONObject("category").getJSONObject("attributes").getString("label"));
        iTunesEntry.setReleaseDate(entryJsonObject.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label"));


        return iTunesEntry;

    }

}
