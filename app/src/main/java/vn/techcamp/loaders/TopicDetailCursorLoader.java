package vn.techcamp.loaders;

import android.content.Context;
import android.database.Cursor;

import vn.techcamp.services.TechCampSqlHelper;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/16/14.
 */
public class TopicDetailCursorLoader extends SimpleCursorLoader {
    private long topicId = -1;
    public TopicDetailCursorLoader(Context context, long topicId) {
        super(context);
        this.topicId = topicId;
    }

    @Override
    public Cursor loadInBackground() {
        if (topicId != -1) {
            return TechCampSqlHelper.getInstance(getContext()).queryTopicDetail(topicId);
        } else {
            return null;
        }
    }
}
