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
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

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
                    handler.onFailure(e, errorResponse.toString());
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


}

