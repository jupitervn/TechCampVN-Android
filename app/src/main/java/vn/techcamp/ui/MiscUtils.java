package vn.techcamp.ui;

import android.text.Editable;

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

}
