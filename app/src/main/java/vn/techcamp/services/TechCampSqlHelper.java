/**
 */
package vn.techcamp.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

import vn.techcamp.models.Announcement;
import vn.techcamp.models.BaseModel;
import vn.techcamp.models.Topic;
import vn.techcamp.utils.MiscUtils;

/**
 * 4: update room_name, room_time, bookmarked columns for topic.
 * @author Cao Duy Vu (vu.caod@skunkworks.vn)
 */
public class TechCampSqlHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "techcamp_events.db";
    private static final int DATABASE_VERSION = 4;
    private static TechCampSqlHelper _instance;

    private TechCampSqlHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public static TechCampSqlHelper getInstance(Context context) {
        if (_instance == null) {
            _instance = new TechCampSqlHelper(context.getApplicationContext());
        }
        return _instance;
    }

    /**
     * Query topics from local database.
     * @return
     */
    public Cursor queryTopics(String queryStr) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = null;
        if (MiscUtils.isNotEmpty(queryStr)) {
            selection = TechCampSqlStructure.TABLE_TOPIC.TITLE + " LIKE '%" +queryStr + "%'";
        }
        return db.query(TechCampSqlStructure.TABLE_TOPIC_NAME, null, selection, null, null, null, TechCampSqlStructure.TABLE_TOPIC.VOTE_COUNT + " desc");
    }

    /**
     * Query announcement from local database.
     * @return
     */
    public Cursor queryAnnouncements() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TechCampSqlStructure.TABLE_ANNOUNCEMENT_NAME, null, null, null, null, null, TechCampSqlStructure.TABLE_ANNOUNCEMENT.DATE + " desc");
    }

    /**
     * Query topic detail by id.
     * @param topicId
     * @return
     */
    public Cursor queryTopicDetail(long topicId) {
        SQLiteDatabase db = getReadableDatabase();
        return  db.query(TechCampSqlStructure.TABLE_TOPIC_NAME, null, TechCampSqlStructure.TABLE_TOPIC.ID + " = ?", new String[] {String.valueOf(topicId)}, null, null, null, null);
    }

    public void udpateBookmarkState(long topicId, boolean isBookmark) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(TechCampSqlStructure.TABLE_TOPIC.BOOKMARKED, isBookmark ? 1 : 0);
        db.update(TechCampSqlStructure.TABLE_TOPIC_NAME, values, TechCampSqlStructure.TABLE_TOPIC.ID + " = ?", new String[] {String.valueOf(topicId)});
    }

    public void updateVoteState(long topicId, boolean isVote) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(TechCampSqlStructure.TABLE_TOPIC.VOTED, isVote ? 1 : 0);
        db.update(TechCampSqlStructure.TABLE_TOPIC_NAME, values, TechCampSqlStructure.TABLE_TOPIC.ID + " = ?", new String[] {String.valueOf(topicId)});
    }

    public void updateFavouriteState(long topicId, boolean isFavorite) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(TechCampSqlStructure.TABLE_TOPIC.FAVOURITED, isFavorite ? 1 : 0);
        db.update(TechCampSqlStructure.TABLE_TOPIC_NAME, values, TechCampSqlStructure.TABLE_TOPIC.ID + " = ?", new String[] {String.valueOf(topicId)});
    }

    public Cursor getSavedTopics() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TechCampSqlStructure.TABLE_TOPIC_NAME, null, TechCampSqlStructure.TABLE_TOPIC.BOOKMARKED + " = ?", new String[] {"1"}, null, null, TechCampSqlStructure.TABLE_TOPIC.TITLE + " asc");
    }


    public void insertTopics(List<Topic> topics) {
        if (topics != null) {
            bulkInsert(TechCampSqlStructure.TABLE_TOPIC_NAME, topics.toArray(new Topic[topics.size()]));
        }
    }

    public void insertAnnouncements(Announcement[] announcements) {
        bulkInsert(TechCampSqlStructure.TABLE_ANNOUNCEMENT_NAME, announcements);
    }

    private void bulkInsert(String tableName, BaseModel[] models) {
        if (models!= null && models.length > 0) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            int count = 0;
            try {
                for (BaseModel model : models) {
                    if (model != null) {
                        ContentValues value = model.toContentValues();
                        long rowId = insertRecord(db, tableName, value);
                        if (rowId >= 0) {
                            count++;
                        }
                    }
                }
                if (count == models.length) {
                    db.setTransactionSuccessful();
                }
            } catch (Exception ex) {
                count = 0;
            } finally {
                db.endTransaction();
            }
        }
    }

    private long insertRecord(SQLiteDatabase db, String tableName, ContentValues value) {
        long idValue = value.getAsLong(TechCampSqlStructure.ID_COL);
        Cursor cursor = db.query(tableName, null, TechCampSqlStructure.ID_COL + " = ?",
                new String[]{String.valueOf(idValue)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            db.update(tableName, value, TechCampSqlStructure.ID_COL + " = ?", new String[]{String.valueOf(idValue)});
            cursor.close();
            return idValue;
        } else {
            return db.insert(tableName, null, value);
        }

    }
}
