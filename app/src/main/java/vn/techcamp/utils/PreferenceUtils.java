package vn.techcamp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/4/14.
 */
public class PreferenceUtils {
    private static final String SHARED_PREFS_KEY = "BOOMCHAT_PREF";
    private static final String UUID = "UUID";
    private static final String APP_VERSION_KEY = "APP_VERSION";
    private static final String GCM_REG_ID = "GCM_REG_ID";
    public static final String getUUID(Context context) {
        String uuidStr = null;
        if (context != null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            uuidStr = sharedPrefs.getString(UUID, null);
        }
        return uuidStr;
    }

    public static final void storeUUID(Context context, String uuid) {
        if (context != null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            sharedPrefs.edit().putString(UUID, uuid).commit();
        }
    }
    public static final int getAppVersion(Context context) {
        int appVersion = -1;
        if (context != null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            appVersion = sharedPrefs.getInt(APP_VERSION_KEY, -1);
        }
        return appVersion;
    }

    public static final void storeAppVersion(Context context, int appVersion) {
        if (context != null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            sharedPrefs.edit().putInt(APP_VERSION_KEY, appVersion).commit();
        }
    }

    public static final String getGcmRegId(Context context) {
        String regId = null;
        if (context != null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            regId = sharedPrefs.getString(GCM_REG_ID, null);
        }
        return regId;
    }

    public static final void storeGcmRegId(Context context, String regId) {
        if (context != null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            sharedPrefs.edit().putString(GCM_REG_ID, regId).commit();
        }
    }
}
