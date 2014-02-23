/**
 */
package vn.techcamp.services;


import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;

import vn.techcamp.models.Topic;


/**
 * @author Cao Duy Vu (vu.caod@skunkworks.vn)
 */
public class HttpService {
    private static final String BASE_URL = "http://techcamp.vn/";
    private static final String VOTE_PATH = BASE_URL + "voting/topic/vote";
    private static final String TOPICS_PATH = BASE_URL + "voting/topic/api";
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    public static void getTopics(String deviceId) {
        httpClient.get(TOPICS_PATH, new GsonHttpResponseHandler<Topic[]>(Topic[].class) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String s, Topic[] topics) {

            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Topic[] topics) {

            }

        });
    }

}

