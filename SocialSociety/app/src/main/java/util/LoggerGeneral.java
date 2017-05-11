package util;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Ajinkya,Nitin
 * @reminder please make enable
 *
 */
public class LoggerGeneral {

	public static final boolean enable = true;
	public static final String TAG = "News";

	/**
	 * Used for Logging with className as one of paramaters
	 * 
	 * @param msg
	 * @param classname
	 */
	public static void info(String msg, Class<?> classname) {
		if (enable) {
			Log.i(TAG, classname.getName() + ":" + msg);
		}
	}

	/**
	 * Used for Logging with only message
	 * 
	 * @param msg
	 */
	public static void info(String msg) {
		if (enable) {
			Log.i(TAG, msg);
		}
	}

	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		return dateFormat.format(date);
	}

	public static int getOSVersionNumber() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}

	/**
	 * This method will help in retrieving the Device unique ID sample output ID
	 * :: 83a3265d5b9e1211
	 * 
	 * @param context
	 * @return
	 * @throws NullPointerException
	 * @throws Exception
	 */
	public static String getDeviceID(Context context)
			throws NullPointerException, Exception {
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		LoggerGeneral.info("ID :: " + android_id);
		return android_id;
	}

}
