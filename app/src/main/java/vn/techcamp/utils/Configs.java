package vn.techcamp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/4/14.
 */
public class Configs {
    public static boolean IS_LOGGABLE = true;
    public static final String FROYO_BUG_ANDROID_ID = "9774d56d682e549c";
    public static final String AGENDA_URL = "https://docs.google.com/spreadsheet/pub?key=0ApUi9j7RVQB_dG9QbWdTd2d0d3lWb1JsUUk4SU9qZUE&output=html";
    public static final Pattern INTERNAL_LINK_PATTERN = Pattern.compile("/view/id/(\\d+)/");
    private static final String EVENT_DATE = "23-03-2014 00:00:00";
    private static final SimpleDateFormat sTimeSdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final long DATE_INTERVAL = 24*3600*1000;
    public static boolean isEventDay() {
        long currentTime = System.currentTimeMillis();
        return compareWithEventDay(currentTime);
    }

    private static boolean compareWithEventDay(long currentTime) {
        Date eventDate = null;
        try {
            eventDate = sTimeSdf.parse(EVENT_DATE);
            long eventTime = eventDate.getTime();
            return currentTime >= eventTime && currentTime <= (eventTime + DATE_INTERVAL);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
