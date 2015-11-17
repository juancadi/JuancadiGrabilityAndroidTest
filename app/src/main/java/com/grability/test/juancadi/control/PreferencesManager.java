//class-name:     	PreferencesManager
//class-overview: 	Gestor de las preferencias definidas para el proyecto
//class-autor:    	Juancadi
//class-date:     	2015-11-12

package com.grability.test.juancadi.control;

import android.content.SharedPreferences;
import android.util.Log;


public class PreferencesManager {
	
	public static final String PREFERENCES_FILE									= "grability-itunes-test";
	
	//Preferences TAG's
	public static final String PREFERENCES_TAG_RSS_AUTHOR 						= "rssAuthor";
	public static final String PREFERENCES_TAG_RSS_RIGHTS 						= "rssRights";
	public static final String PREFERENCES_TAG_RSS_UPDATE 						= "rssLastUpdate";
	
	//Preferences Reponse
	public static final String PREFERENCE_NOT_FOUND 							= "not-found";
	
	
	SharedPreferences rssPreferences;
	SharedPreferences.Editor editor;
	
	public PreferencesManager(SharedPreferences preferences){
		
		this.rssPreferences = preferences;
		editor = this.rssPreferences.edit();
		
	}

	
	/*****/
	
	public boolean setRssAuthor(String rssAuthor){
		
		boolean editRssAuthor = false;
			
			try{
			
				editor.putString(PREFERENCES_TAG_RSS_AUTHOR, rssAuthor);
				
				editor.commit();
				editRssAuthor = true;
			
			}catch(Exception e){
				
				Log.e("PreferencesManager", "Exception: Error setting RSS Author in Preferences...");
			}
			
				
		return editRssAuthor;
		
	}

	
	public String getRssAuthor(){
		
		return this.rssPreferences.getString(PREFERENCES_TAG_RSS_AUTHOR, PREFERENCE_NOT_FOUND);
	}
	
	
	/*****/

	public boolean setRssRights(String rssRights){

		boolean editRssRights = false;

		try{

			editor.putString(PREFERENCES_TAG_RSS_RIGHTS, rssRights);

			editor.commit();
			editRssRights = true;

		}catch(Exception e){

			Log.e("PreferencesManager", "Exception: Error setting RSS Rights in Preferences...");
		}


		return editRssRights;

	}


	public String getRssRights(){

		return this.rssPreferences.getString(PREFERENCES_TAG_RSS_RIGHTS, PREFERENCE_NOT_FOUND);
	}

	/****/
	public boolean setRssUpdate(String rssUpdate){

		boolean editRssUpdate = false;

		try{

			editor.putString(PREFERENCES_TAG_RSS_UPDATE, rssUpdate);

			editor.commit();
			editRssUpdate = true;

		}catch(Exception e){

			Log.e("PreferencesManager", "Exception: Error setting RSS Update in Preferences...");
		}


		return editRssUpdate;

	}


	public String getRssUpdate(){

		return this.rssPreferences.getString(PREFERENCES_TAG_RSS_UPDATE, PREFERENCE_NOT_FOUND);
	}

}
