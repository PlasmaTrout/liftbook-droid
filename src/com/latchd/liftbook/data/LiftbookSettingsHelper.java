package com.latchd.liftbook.data;

import android.app.Activity;
import android.content.SharedPreferences;

public class LiftbookSettingsHelper {

	public static final String METRIC_KEY = "USE_METRIC_SYSTEM";
	public static final String WENDLER_KEY = "USE_WENDLER_MAX";
	
	public static final String FILE_KEY = "LiftbookSettingsFile";
	
	public static boolean UseMetricSystem(Activity a){
		SharedPreferences sp = a.getSharedPreferences(FILE_KEY, Activity.MODE_PRIVATE);
		return sp.getBoolean(METRIC_KEY,false);
	}
	public static boolean UseWendlerMax(Activity a){
		SharedPreferences sp = a.getSharedPreferences(FILE_KEY, Activity.MODE_PRIVATE);
		return sp.getBoolean(WENDLER_KEY,false);
	}
	
	public static void SetMetricSystem(Activity a, boolean value){
		SharedPreferences sp = a.getSharedPreferences(FILE_KEY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putBoolean(METRIC_KEY, value);
		ed.commit();
	}
	
	public static void SetWendlerMax(Activity a, boolean value){
		SharedPreferences sp = a.getSharedPreferences(FILE_KEY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putBoolean(WENDLER_KEY, value);
		ed.commit();
	}
	
	public static float GetDefaultWeight(Activity a){
		if(UseMetricSystem(a)){
			return 20;
		}else{
			return 45;
		}
	}
}
