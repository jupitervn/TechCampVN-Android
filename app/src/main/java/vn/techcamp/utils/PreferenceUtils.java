package vn.techcamp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/4/14.
 */
public class PreferenceUtils {
    private static final String SHARED_PREFS_KEY = "BOOMCHAT_PREF";
    private static final String UUID = "UUID";
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
}
