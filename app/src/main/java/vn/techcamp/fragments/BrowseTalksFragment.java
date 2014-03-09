package vn.techcamp.fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.Header;

import vn.techcamp.activities.BaseFragment;
import vn.techcamp.android.R;
import vn.techcamp.loaders.TopicsCursorLoader;
import vn.techcamp.models.Topic;
import vn.techcamp.models.VoteResponse;
import vn.techcamp.services.GsonHttpResponseHandler;
import vn.techcamp.services.HttpService;
import vn.techcamp.utils.Logging;
import vn.techcamp.utils.MiscUtils;

/**
 * Created by jupiter on 22/2/14.
 */
public class BrowseTalksFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView lvTopics;
    private CursorAdapter topicAdapter;
    private boolean isFirstTime = true;

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
        return new TopicsCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (topicAdapter == null) {
            topicAdapter = new TopicAdapter(getActivity(), cursor, 0, this);
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

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private void getTopics() {
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
        //TODO (D.Vu): Open topic detail page.
    }

    @Override
    public void onClick(View v) {
        Long topicId = (Long) v.getTag();
        if (topicId != null) {
            Logging.debug("Vote topic " + topicId);
            showLoadingDialog();
            String deviceId = MiscUtils.getUUID(getActivity().getApplicationContext());
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

    private static class TopicAdapter extends CursorAdapter {
        private View.OnClickListener btVoteClickListener;

        public TopicAdapter(Context context, Cursor c, int flags, View.OnClickListener voteClickListener) {
            super(context, c, flags);
            this.btVoteClickListener = voteClickListener;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View convertView = LayoutInflater.from(context).inflate(R.layout.item_browse_topic, viewGroup, false);
            return convertView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvTopicName = (TextView) view.findViewById(R.id.tv_topic_name);
            TextView tvSpeakerName = (TextView) view.findViewById(R.id.tv_topic_speaker);
            TextView btVote = (TextView) view.findViewById(R.id.bt_vote);
            Topic topic = new Topic(cursor);
            tvTopicName.setText(topic.getTitle());
            tvSpeakerName.setText(topic.getSpeakerName());
            btVote.setText(context.getResources().getQuantityString(R.plurals.topic_vote_count, (int) topic.getVoteCount(), topic.getVoteCount()));
            btVote.setEnabled(!topic.isVoted());
            btVote.setTag(topic.getId());
            btVote.setOnClickListener(btVoteClickListener);
        }
    }
}
