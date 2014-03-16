package vn.techcamp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.Header;

import vn.techcamp.activities.BaseFragment;
import vn.techcamp.activities.TalkDetailActivity;
import vn.techcamp.adapters.TopicAdapter;
import vn.techcamp.android.R;
import vn.techcamp.loaders.TopicsCursorLoader;
import vn.techcamp.models.Topic;
import vn.techcamp.models.VoteResponse;
import vn.techcamp.services.GsonHttpResponseHandler;
import vn.techcamp.services.HttpService;
import vn.techcamp.utils.Configs;
import vn.techcamp.utils.Logging;
import vn.techcamp.utils.MiscUtils;

/**
 * Created by jupiter on 22/2/14.
 */
public class BrowseTalksFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView lvTopics;
    private CursorAdapter topicAdapter;
    private boolean isFirstTime = true;
    private String currentFilterStr = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_talks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvTopics = (PullToRefreshListView) view.findViewById(R.id.lv_topics);
        lvTopics.setOnRefreshListener(this);
        lvTopics.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new TopicsCursorLoader(getActivity(), currentFilterStr);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (topicAdapter == null) {
            topicAdapter = new TopicAdapter(getActivity(), cursor, 0, this, Configs.isEventDay());
        } else {
            topicAdapter.swapCursor(cursor);
        }
        if (lvTopics.getRefreshableView().getAdapter() == null) {
            lvTopics.setAdapter(topicAdapter);
        }
        if (isFirstTime) {
            if (cursor.getCount() == 0) {
                showLoadingDialog();
            }
            getTopics();
            isFirstTime = false;
        }
    }

    public void filterTopic(String queryString) {
        currentFilterStr = queryString;
        getLoaderManager().restartLoader(0, null, this);
    }

    public String getFilterString() {
        return currentFilterStr;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    protected void getTopics() {
        Logging.debug("Load new topics");
        String deviceId = MiscUtils.getUUID(getActivity().getApplicationContext());
        HttpService.getTopics(getActivity().getApplicationContext(), deviceId, new GsonHttpResponseHandler<Topic[]>(Topic[].class) {

            @Override
            public void onSuccess(int i, Header[] headers, String s, Topic[] topics) {
                Logging.debug("Load topics success");
                if (getActivity() != null && isAdded()) {
                    hideLoadingDialog();
                    lvTopics.onRefreshComplete();
                    getLoaderManager().restartLoader(0, null, BrowseTalksFragment.this);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Topic[] topics) {
                Logging.debug("Load topics failed");
                if (getActivity() != null && isAdded()) {
                    hideLoadingDialog();
                    lvTopics.onRefreshComplete();
                    showErrorToast(R.string.topic_fetch_error);
                }
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getTopics();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long topicId = (Long) view.getTag();
        Logging.debug("On Item click event " + topicId);
        if (topicId != null) {
            Intent showTopicDetailIntent = new Intent(getActivity(), TalkDetailActivity.class);
            showTopicDetailIntent.putExtra(TalkDetailActivity.EXTRA_TOPIC, topicId);
            startActivity(showTopicDetailIntent);
        }
    }

    @Override
    public void onClick(View v) {
        Long topicId = (Long) v.getTag();
        if (topicId != null) {
            Logging.debug("Vote topic " + topicId);
            showLoadingDialog();
            String deviceId = MiscUtils.getUUID(getActivity().getApplicationContext());
            if (Configs.isEventDay()) {
                HttpService.favorTopic(getActivity().getApplicationContext(), deviceId, topicId, new GsonHttpResponseHandler<VoteResponse>(VoteResponse.class) {
                    @Override
                    public void onSuccess(int i, Header[] headers, String s, VoteResponse voteResponse) {
                        Logging.debug("Favor topic success");
                        if (getActivity() != null && isAdded()) {
                            getTopics();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, String s, VoteResponse voteResponse) {
                        Logging.debug("Vote topic failure");
                        if (getActivity() != null && isAdded()) {
                            hideLoadingDialog();
                            showErrorToast(R.string.topic_vote_error);
                        }
                    }
                });
            } else {
                HttpService.voteTopic(getActivity().getApplicationContext(), deviceId, topicId, new GsonHttpResponseHandler<VoteResponse>(VoteResponse.class) {
                    @Override
                    public void onSuccess(int i, Header[] headers, String s, VoteResponse voteResponse) {
                        Logging.debug("Vote topic success");
                        if (getActivity() != null && isAdded()) {
                            getTopics();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, String s, VoteResponse voteResponse) {
                        Logging.debug("Vote topic failure");
                        if (getActivity() != null && isAdded()) {
                            hideLoadingDialog();
                            showErrorToast(R.string.topic_vote_error);
                        }
                    }
                });
            }
        }
    }

}
