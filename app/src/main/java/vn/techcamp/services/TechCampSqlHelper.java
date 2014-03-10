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

/**
 * @author Cao Duy Vu (vu.caod@skunkworks.vn)
 */
public class TechCampSqlHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "techcamp_events.db";
    private static final int DATABASE_VERSION = 3;
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
    public Cursor queryTopics() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TechCampSqlStructure.TABLE_TOPIC_NAME, null, null, null, null, null, TechCampSqlStructure.TABLE_TOPIC.VOTE_COUNT + " desc");
    }

    /**
     * Query announcement from local database.
     * @return
     */
    public Cursor queryAnnouncements() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TechCampSqlStructure.TABLE_ANNOUNCEMENT_NAME, null, null, null, null, null, TechCampSqlStructure.TABLE_ANNOUNCEMENT.DATE + " desc");
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
