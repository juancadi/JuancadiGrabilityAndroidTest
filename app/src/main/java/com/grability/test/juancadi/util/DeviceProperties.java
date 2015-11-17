package com.grability.test.juancadi.util;

//class-name:     	DeviceProperties
//class-overview: 	Implementa lógica util relacionada con las propiedades del dispositivo.
//class-autor:    	Juancadi
//class-date:     	2015-11-16

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class DeviceProperties {




	public static boolean checkConnectivity(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isConnected = false;

		if (cm != null) {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {

						isConnected = true;
					}
				}
			}
		}
		return isConnected;

	}
	
	public static String getDeviceID(Context context){
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
		
	}

	

}
