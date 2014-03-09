package vn.techcamp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/9/14.
 */
public class UIUtils {
    private static SimpleDateFormat sTimeSdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    /**
     * Format announcement time to dd.MM.yyyy HH:mm
     * @param createdDate
     * @return
     */
    public static String formatAnnouncementDate(Date createdDate) {
        String result = null;
        if (createdDate == null) {
            createdDate = new Date();
        }
        result = sTimeSdf.format(createdDate);
        return result;
    }
}
