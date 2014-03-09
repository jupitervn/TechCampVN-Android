package vn.techcamp.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import vn.techcamp.services.TechCampSqlStructure;

/**
 * Created by jupiter on 22/2/14.
 */
public class Topic extends BaseModel{
    private long id;
    private String title;
    private String description;
    private String slideUrl;
    private int duration;
    private String speakerName;
    private String speakerDescription;
    private String speakerUrl;
    private long voteCount;
    private boolean voted;
    private Date createdAt;

    public Topic(Cursor cursor) {
        super(cursor);
        setId(cursor.getLong(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.ID)));
        setTitle(cursor.getString(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.TITLE)));
        setDescription(cursor.getString(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.DESCRIPTION)));
        setSpeakerName(cursor.getString(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.SPEAKER_NAME)));
        setSpeakerDescription(cursor.getString(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.SPEAKER_DESCRIPTION)));
//        setSpeakerUrl(cursor.getString(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.SPEAKER_URL)));
        setSlideUrl(cursor.getString(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.SLIDE_URL)));
        setDuration(cursor.getInt(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.DURATION)));
        setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.CREATED_AT))));
//        setUpdatedAt(new Date(cursor.getLong(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.UPDATED_AT))));
        setVoteCount(cursor.getLong(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.VOTE_COUNT)));
        setVoted(cursor.getInt(cursor.getColumnIndex(TechCampSqlStructure.TABLE_TOPIC.VOTED)) > 0);
    }

    public ContentValues toContentValues() {
        ContentValues value = new ContentValues();
        value.put(TechCampSqlStructure.TABLE_TOPIC.ID, getId());
        value.put(TechCampSqlStructure.TABLE_TOPIC.TITLE, getTitle());
        value.put(TechCampSqlStructure.TABLE_TOPIC.DESCRIPTION, getDescription());
        value.put(TechCampSqlStructure.TABLE_TOPIC.SPEAKER_NAME, getSpeakerName());
        value.put(TechCampSqlStructure.TABLE_TOPIC.SPEAKER_DESCRIPTION, getSpeakerDescription());
//        value.put(TechCampSqlStructure.TABLE_TOPIC.SPEAKER_URL, getSpeakerUrl());
        value.put(TechCampSqlStructure.TABLE_TOPIC.SLIDE_URL, getSlideUrl());
        value.put(TechCampSqlStructure.TABLE_TOPIC.DURATION, getDuration());
        if (getCreatedAt() != null) {
            value.put(TechCampSqlStructure.TABLE_TOPIC.CREATED_AT, getCreatedAt().getTime());
        }
        value.put(TechCampSqlStructure.TABLE_TOPIC.VOTE_COUNT, getVoteCount());
        value.put(TechCampSqlStructure.TABLE_TOPIC.VOTED, isVoted() ? 1 : 0);
        return value;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlideUrl() {
        return slideUrl;
    }

    public void setSlideUrl(String slideUrl) {
        this.slideUrl = slideUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerDescription() {
        return speakerDescription;
    }

    public void setSpeakerDescription(String speakerDescription) {
        this.speakerDescription = speakerDescription;
    }

    public String getSpeakerUrl() {
        return speakerUrl;
    }

    public void setSpeakerUrl(String speakerUrl) {
        this.speakerUrl = speakerUrl;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }
}
