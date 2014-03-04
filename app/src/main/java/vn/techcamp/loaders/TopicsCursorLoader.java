package vn.techcamp.loaders;

import android.content.Context;
import android.database.Cursor;

import vn.techcamp.services.TechCampSqlHelper;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/4/14.
 */
public class TopicsCursorLoader extends SimpleCursorLoader {

    public TopicsCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return TechCampSqlHelper.getInstance(getContext()).queryTopics();
    }
}
