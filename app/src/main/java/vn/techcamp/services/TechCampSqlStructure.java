package vn.techcamp.services;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/4/14.
 */
public class TechCampSqlStructure {
    public static final String TABLE_TOPIC_NAME = "TOPICS";

    public static final String ID_COL = "_id";

    public static class TABLE_TOPIC {
        public static final String ID = ID_COL;
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String SLIDE_URL = "slide_url";
        public static final String SPEAKER_NAME = "speaker_name";
        public static final String SPEAKER_DESCRIPTION = "speaker_description";
        public static final String SPEAKER_URL = "speakder_url";
        public static final String DURATION = "duration";
        public static final String VOTED = "voted";
        public static final String VOTE_COUNT = "vote_count";
        public static final String UPDATED_AT = "udpated_at";
        public static final String CREATED_AT = "created_at";
    }

}
