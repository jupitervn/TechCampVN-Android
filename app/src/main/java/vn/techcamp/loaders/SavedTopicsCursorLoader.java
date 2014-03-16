package vn.techcamp.loaders;

import android.content.Context;
import android.database.Cursor;

import vn.techcamp.services.TechCampSqlHelper;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/4/14.
 */
public class SavedTopicsCursorLoader extends SimpleCursorLoader {

    public SavedTopicsCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return TechCampSqlHelper.getInstance(getContext()).getSavedTopics();
    }
}
