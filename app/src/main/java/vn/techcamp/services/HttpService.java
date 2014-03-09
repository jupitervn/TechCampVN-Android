/**
 */
package vn.techcamp.services;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vn.techcamp.models.Announcement;
import vn.techcamp.models.Topic;
import vn.techcamp.models.VoteResponse;
import vn.techcamp.utils.JSONUtils;
import vn.techcamp.utils.Logging;


/**
 * @author Cao Duy Vu (vu.caod@skunkworks.vn)
 */
public class HttpService {
    private static final String BASE_URL = "http://techcamp.vn/";
    private static final String VOTE_PATH = BASE_URL + "voting/topic/vote";
    private static final String TOPICS_PATH = BASE_URL + "voting/topic/api";
    private static final String REGISTER_DEVICE_PATH = BASE_URL + "voting/notification/register";
    private static final String ANNOUNCEMENT_PATH = BASE_URL + "voting/notification/list";
    private static AsyncHttpClient httpClient = new AsyncHttpClient();
    private static int MAX_TIMEOUT = 35000;
    static {
        httpClient.setTimeout(MAX_TIMEOUT);
    }

    /**
     * Get list of topics from techcamp server.
     *
     * @param context
     * @param deviceId
     * @param handler
     */
    public static void getTopics(final Context context, String deviceId, final GsonHttpResponseHandler<Topic[]> handler) {
        RequestParams params = new RequestParams();
        params.add("device_id", deviceId);
        httpClient.get(TOPICS_PATH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(response);
                if (response != null) {
                    Iterator<String> keys = response.keys();
                    List<Topic> topics = new ArrayList<Topic>();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        try {
                            JSONObject topicJson = response.getJSONObject(key);
                            Topic topic = JSONUtils.parseJson(Topic.class, topicJson.toString());
                            topic.setId(Long.valueOf(key));
                            topics.add(topic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (context != null) {
                        Logging.debug("Insert topics " + topics.size());
                        TechCampSqlHelper.getInstance(context).insertTopics(topics);
                    }
                    if (handler != null) {
                        handler.onSuccess(statusCode, headers, null, topics.toArray(new Topic[topics.size()]));
                    }
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
                if (handler != null) {
                    handler.onFailure(e, errorResponse != null ? errorResponse.toString() : null);
                }
            }
        });
    }

    /**
     * Vote topic with topicId.
     * @param context
     * @param deviceId
     * @param topicId
     * @param handler
     */
    public static void voteTopic(Context context, String deviceId, long topicId, GsonHttpResponseHandler<VoteResponse> handler) {
        RequestParams params = new RequestParams();
        params.add("id", deviceId);
        params.add("topic_id", String.valueOf(topicId));
        httpClient.post(VOTE_PATH, params, handler);
    }

    /**
     * Register device token for notification purpose.
     * @param context
     * @param deviceToken
     */
    public static void registerDeviceToken(Context context, String deviceToken) {
        RequestParams params = new RequestParams();
        params.add("device_id", deviceToken);
        params.add("platform", "Android");

        httpClient.post(REGISTER_DEVICE_PATH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Logging.debug("Response " + response);
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                super.onFailure(e, errorResponse);
            }
        });
    }

    /**
     * Get announcements from server.
     * @param context
     * @param handler
     */
    public static void getAnnouncements(final Context context, final GsonHttpResponseHandler<Announcement[]> handler) {
        httpClient.get(ANNOUNCEMENT_PATH, null, new GsonHttpResponseHandler<Announcement[]>(Announcement[].class) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String s, Announcement[] announcements) {
                Logging.debug("Get announcements " + statusCode);
                if (statusCode == 200) {
                    if (handler != null) {
                        handler.onSuccess(statusCode, headers, s, announcements);
                    }
                    if (context != null) {
                        TechCampSqlHelper.getInstance(context).insertAnnouncements(announcements);
                    }
                } else {
                    if (handler != null) {
                        handler.onFailure(statusCode, headers, null, s, null);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String s, Announcement[] announcements) {
                if (handler != null) {
                    handler.onFailure(statusCode, headers, throwable, s, null);
                }
            }
        });
    }



}
