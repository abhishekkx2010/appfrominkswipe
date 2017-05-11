package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.inkswipe.SocialSociety.R;

import org.json.JSONObject;




public class AppPreferences {
	private static AppPreferences appPreferences;

	private Context applicationContext;

	private SharedPreferences sharedPreferences;

	private AppPreferences(Context applicationContext) {
		this.applicationContext = applicationContext;

		Resources res = this.applicationContext.getResources();
		String preferencesName = res.getString(R.string.app_name);
		sharedPreferences = this.applicationContext.getSharedPreferences(
				preferencesName, Context.MODE_PRIVATE);
	}

	public static AppPreferences getAppPreferences(Context applicationContext) {
		if (appPreferences != null) {
			return appPreferences;
		}

		appPreferences = new AppPreferences(applicationContext);
		return appPreferences;
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public Boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public int putInt(String key, int value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
		return value;
	}

	public int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	public String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	public void remove(String key) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public void putStringjson(String key, JSONObject json) {
		// TODO Auto-generated method stub

		String jjson = ""+json;
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, jjson);
		editor.commit();
	}


}
