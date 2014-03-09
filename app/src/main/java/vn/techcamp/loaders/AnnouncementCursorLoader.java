package vn.techcamp.loaders;

import android.content.Context;
import android.database.Cursor;

import vn.techcamp.services.TechCampSqlHelper;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/9/14.
 */
public class AnnouncementCursorLoader extends SimpleCursorLoader {
    public AnnouncementCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return TechCampSqlHelper.getInstance(getContext()).queryAnnouncements();
    }
}
