/**
 * 
 */
package vn.techcamp.utils;

import android.util.Log;

/**
 * Log wrapper class for Boomchat application.
 * 
 * @author jupiter
 * 
 */
public class Logging {
    private static final String LOG_TAG = "TechcampVN";

    /**
     * Log debug message.
     * 
     * @param message
     */
    public static void debug(String message) {
        if (Configs.IS_LOGGABLE) {
            Log.d(LOG_TAG, message);
        }
    }

    /**
     * Log info message.
     * 
     * @param message
     */
    public static void info(String message) {
        if (Configs.IS_LOGGABLE) {
            Log.i(LOG_TAG, message);
        }
    }

    /**
     * Log warn message.
     * 
     * @param message
     */
    public static void warn(String message) {
        if (Configs.IS_LOGGABLE) {
            Log.w(LOG_TAG, message);
        }
    }

    /**
     * Log error messages.
     * 
     * @param message
     */
    public static void error(String message) {
        if (Configs.IS_LOGGABLE) {
            Log.e(LOG_TAG, message);
        }
    }

    /**
     * Log error messages with exception.
     * 
     * @param message
     * @param ex
     */
    public static void error(String message, Throwable ex) {
        if (Configs.IS_LOGGABLE) {
            Log.e(LOG_TAG, message, ex);
        }
    }

}
