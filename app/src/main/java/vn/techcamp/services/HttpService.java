/**
*/
package vn.techcamp.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;

import vn.techcamp.models.Topic;

/**
 * @author Cao Duy Vu (vu.caod@skunkworks.vn)
 *
 */
public class HttpService {
    private static AsyncHttpClient httpClient = new AsyncHttpClient();
    private static final String BASE_URL = "http://techcamp.vn/";
    private static final String TOPICS_PATH = BASE_URL + "voting/topic/api";
    private static final String VOTE_PATH = BASE_URL + "voting/topic/vote";

    public static void getTopics(String deviceId) {
        httpClient.get(TOPICS_PATH,  new BaseJsonHttpResponseHandler<Topic[]>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String s, Topic[] topics) {

            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Topic[] topics) {

            }

            @Override
            protected Topic[] parseResponse(String s) throws Throwable {
                return new Topic[0];
            }
        });
    }

}
