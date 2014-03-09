package vn.techcamp.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import vn.techcamp.services.TechCampSqlStructure;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/9/14.
 */
public class Announcement extends BaseModel{
    private long id;
    private String subject;
    private String message;
    private Date sendAt;

    public Announcement(Cursor c) {
        super(c);
        this.id = c.getLong(c.getColumnIndex(TechCampSqlStructure.TABLE_ANNOUNCEMENT.ID));
        this.subject = c.getString(c.getColumnIndex(TechCampSqlStructure.TABLE_ANNOUNCEMENT.SUBJECT));
        this.message = c.getString(c.getColumnIndex(TechCampSqlStructure.TABLE_ANNOUNCEMENT.MESSAGE));
        long sendTime = c.getLong(c.getColumnIndex(TechCampSqlStructure.TABLE_ANNOUNCEMENT.DATE));
        this.sendAt = new Date(sendTime);

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TechCampSqlStructure.TABLE_ANNOUNCEMENT.ID, this.id);
        contentValues.put(TechCampSqlStructure.TABLE_ANNOUNCEMENT.SUBJECT, this.subject);
        contentValues.put(TechCampSqlStructure.TABLE_ANNOUNCEMENT.MESSAGE, this.message);
        long announcementTime;
        if (this.sendAt != null) {
            announcementTime = this.sendAt.getTime();
        } else {
            announcementTime = (new Date()).getTime();
        }
        contentValues.put(TechCampSqlStructure.TABLE_ANNOUNCEMENT.DATE, announcementTime);
        return contentValues;
    }
}
