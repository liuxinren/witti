//ECE 573 Project
//Team: Witty
//Date: 4/17/14
//Author: Brianna Heersink

package edu.arizona.ece473573.witti.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import edu.arizona.ece473573.witti.R;

public class WittiSettings {
	
    private static final String CAT_TAG = "WITTI_WittiSettings";
    public static final String KEY_REFRESH_MODE = "refreshSetting";
	public static final String KEY_DEMO_FILE = "demoFileSetting";
	public static final String KEY_DEMO_FRAMES = "demoFramesSetting";
	public static final String KEY_SERVER_LOCATION = "serverLocationSetting";
	public static final String KEY_SERVER_FILE = "serverFileSetting";
	public static final String KEY_SERVER_FRAMES = "serverFramesSetting";
	public static final String KEY_LIVE_MODE = "liveSetting";
	
	private Context mSettingsContext;
	
	public WittiSettings(Context context) {
		this.mSettingsContext = context;
	}
	
	/**
     * Resets all settings to default values
     */
	public void resetSettings(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		sharedPrefs.edit().clear().commit();
		PreferenceManager.setDefaultValues(mSettingsContext, R.layout.preferences, true);
	}
	
	/**
     * Gets state of auto refresh setting
     * 
     * @return 		true if auto-refresh is enabled, false if manual mode is enabled
     */
	public boolean getAutoRefresh(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		boolean refreshMode = sharedPrefs.getBoolean(KEY_REFRESH_MODE, false);
		Log.d(CAT_TAG, "auto refresh mode is "+Boolean.toString(refreshMode));
		return refreshMode;
	}
	
	/**
     * Sets the state of auto refresh setting
     * 
     * @param 		true for auto-refresh enabled, false for manual mode enabled
     */
	public void setAutoRefresh(boolean bool){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean(KEY_REFRESH_MODE, bool);
    	editor.apply();
		Log.d(CAT_TAG, "auto refresh mode has been set to "+Boolean.toString
				(sharedPrefs.getBoolean(KEY_REFRESH_MODE, false)));
	}
	
	/**
     * Gets state of live data setting
     * 
     * @return 		true if auto-refresh is enabled, false if manual mode is enabled
     */
	public boolean getLiveMode(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		boolean liveMode = sharedPrefs.getBoolean(KEY_LIVE_MODE, false);
		Log.d(CAT_TAG, "live mode is "+Boolean.toString(liveMode));
		return liveMode;
	}
	
	/**
     * Sets the state of live data setting
     * 
     * @param 		true for auto-refresh enabled, false for manual mode enabled
     */
	public void setLiveMode(boolean bool){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean(KEY_LIVE_MODE, bool);
    	editor.apply();
		Log.d(CAT_TAG, "live mode has been set to "+Boolean.toString
				(sharedPrefs.getBoolean(KEY_LIVE_MODE, false)));
	}
	
    /**
     * Sets the server web address to be used in Launch mode.
     * 
     * @param 	webAddress	is a web address at which the server data can be accessed
     */
	public void setServerLocation(String webAddress){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
    	editor.putString(KEY_SERVER_LOCATION, webAddress);
    	editor.apply();
	}
	
    /**
     * Gets the server web address to be used in Launch mode.
     * 
     * @return 		the server web address
     */
	public String getServerLocation(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		String location = sharedPrefs.getString(KEY_SERVER_LOCATION, "");
		Log.d(CAT_TAG, "server location: "+location);
		return location;
	}
	
    /**
     * Gets the server files available on the server.
     * 
     * @return 		an array in the format "fileName frameCount".
     * @throws 	IOException
     */
	public CharSequence[] getServerFilesAvailable(){
		//Read from raw file
		InputStream is = mSettingsContext.getResources().openRawResource(R.raw.server_data_available);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        
	    CharSequence mStringLine;
		ArrayList<CharSequence> mArrayListFiles = new ArrayList<CharSequence>();
	    
		//parse into array
	    try {
			while ((mStringLine = in.readLine()) != null) {
				if(mStringLine.charAt(0) != '%') {
					mArrayListFiles.add(mStringLine);
				}
			}
		} catch (IOException e) {
			Log.e(CAT_TAG, "Couldn't read from resource file.");
		}
	    
	    // Convert to CharSequence
	    CharSequence[] mCharSequenceFiles = mArrayListFiles.toArray(new CharSequence[mArrayListFiles.size()]);
	    
	    return mCharSequenceFiles; 	
	}
	
    /**
     * Sets the base server file name to be used in Launch mode.
     * 
     * @param 	fileName	is a base file name containing Lidar data
     */	
	public void setServerFile(String fileName){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
    	editor.putString(KEY_SERVER_FILE, fileName);
    	editor.apply();
    	Log.v(CAT_TAG, "server file set to " + fileName);
	}
	
    /**
     * Gets the currently selected server file.
     * 
     * @return 		the selected server file name
     */
	public String getServerFile(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		String mFile = sharedPrefs.getString(KEY_SERVER_FILE, "");
		Log.d(CAT_TAG, "server file name: "+mFile);
		return mFile;
	}
	
    /**
     * Sets the server file frame count to be used in Launch mode.
     * 
     * @param 	frameCount	is the number of frames available for a file on the server
     */	
	public void setServerFrameCount(Integer frameCount){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
    	editor.putString(KEY_SERVER_FRAMES, frameCount.toString());
    	editor.apply();
    	Log.v(CAT_TAG, "server frames set to " + frameCount);
	}
	
    /**
     * Gets the frame count for the currently selected server file.
     * 
     * @return 		the frame count for the selected server file name
     */
	public Integer getServerFrameCount(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		String mFramesString = sharedPrefs.getString(KEY_SERVER_FRAMES, "");
		Integer mFramesInt = Integer.valueOf(mFramesString);
		Log.d(CAT_TAG, "server frames count: "+mFramesInt);
		return mFramesInt;
	}
	
    /**
     * Gets the demo files available on the mobile device.
     * 
     * @return 		an array in the format "fileName frameCount".
     * @throws 	IOException
     */
	public CharSequence[] getDemoFilesAvailable(){
		//Read from raw file
		InputStream is = mSettingsContext.getResources().openRawResource(R.raw.demo_data_available);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        
	    CharSequence mStringLine;
		ArrayList<CharSequence> mArrayListFiles = new ArrayList<CharSequence>();
	    
		//parse into array
	    try {
			while ((mStringLine = in.readLine()) != null) {
				if(mStringLine.charAt(0) != '%') {
					mArrayListFiles.add(mStringLine);
				}
			}
		} catch (IOException e) {
			Log.e(CAT_TAG, "Couldn't read from resource file.");
		}
	    
	    // Convert to CharSequence
	    CharSequence[] mCharSequenceFiles = mArrayListFiles.toArray(new CharSequence[mArrayListFiles.size()]);
	    
	    return mCharSequenceFiles; 	
	}
	
    /**
     * Sets the demo file to be displayed 
     * 
     * @param 	fileName	is a base file name containing Lidar data
     */	
	public void setDemoFile(String fileName){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
    	editor.putString(KEY_DEMO_FILE, fileName);
    	editor.apply();
	}
	
    /**
     * Gets the currently selected demo file.
     * 
     * @return 		the selected demo file name
     */
	public String getDemoFile(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		String mFile = sharedPrefs.getString(KEY_DEMO_FILE, "");
		return mFile;
	}
	
    /**
     * Sets the demo file frame count
     * 
     * @param 	frameCount	is the number of frames available for a demo file
     */	
	public void setDemoFrameCount(Integer frameCount){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		SharedPreferences.Editor editor = sharedPrefs.edit();
    	editor.putString(KEY_DEMO_FRAMES, frameCount.toString());
    	editor.apply();
	}
	
    /**
     * Gets the frame count for the currently selected demo file.
     * 
     * @return 		the frame count for the selected demo file name
     */
	public Integer getDemoFrameCount(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mSettingsContext);
		String mFrameCount = sharedPrefs.getString(KEY_DEMO_FRAMES, "0");
		return Integer.valueOf(mFrameCount);
	}

}