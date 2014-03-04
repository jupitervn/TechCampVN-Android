package vn.techcamp.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Editable;

import java.util.UUID;

import static android.provider.Settings.Secure;

/**
 * Common utility methods.
 *
 * Created by jupiter on 22/2/14.
 */
public class MiscUtils {
    /**
     * Check if a text is empty or not.
     *
     * @param editableText
     * @return
     */
    public static boolean isNotEmpty(Editable editableText) {
        return (editableText != null) && isNotEmpty(editableText.toString());
    }

    /**
     * Check if a text is empty or not.
     *
     * @param editableText
     * @return
     */
    public static boolean isNotEmpty(CharSequence editableText) {
        return (editableText != null) && (editableText.toString() != null)
                && (!editableText.toString().trim().equals(""));
    }

    public static String getUUID(Context context) {
        String uuidStr = null;
        if (context != null) {
            uuidStr = PreferenceUtils.getUUID(context);
            if (!MiscUtils.isNotEmpty(uuidStr)) {
                TelephonyManager telephoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephoneMgr != null) {
                    uuidStr = telephoneMgr.getDeviceId();
                }
                Logging.debug("DeviceId: " + uuidStr);
                if (!MiscUtils.isNotEmpty(uuidStr)) {
                    uuidStr = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                    if (Configs.FROYO_BUG_ANDROID_ID.equals(uuidStr) || !MiscUtils.isNotEmpty(uuidStr)) {
                        uuidStr = UUID.randomUUID().toString();
                    }
                }
                PreferenceUtils.storeUUID(context, uuidStr);
            }
        }
        Logging.debug("DeviceId: " + uuidStr);
        return uuidStr;
    }

}
