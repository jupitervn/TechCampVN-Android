package vn.techcamp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import vn.techcamp.android.R;
import vn.techcamp.loaders.TopicDetailCursorLoader;
import vn.techcamp.models.Topic;
import vn.techcamp.models.VoteResponse;
import vn.techcamp.services.GsonHttpResponseHandler;
import vn.techcamp.services.HttpService;
import vn.techcamp.services.TechCampSqlHelper;
import vn.techcamp.utils.Configs;
import vn.techcamp.utils.Logging;
import vn.techcamp.utils.MiscUtils;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/5/14.
 */

public class TalkDetailActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    public static final String EXTRA_TOPIC = "extra-topic-id";
    private long currentTopicId = -1;
    private Topic currentTopic;
    private Menu menu;
    private boolean isMenuInflated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        initActionBar();
        currentTopicId = getIntent().getLongExtra(EXTRA_TOPIC, -1);
        getSupportLoaderManager().initLoader(0, null, this);

    }

    private void initView(Topic topic) {
        TextView tvTopicName = (TextView) findViewById(R.id.tv_topic_name);
        TextView tvTopicRoom = (TextView) findViewById(R.id.tv_topic_room);
        TextView tvTopicSpeaker = (TextView) findViewById(R.id.tv_topic_speaker);
        TextView tvTopicVotes = (TextView) findViewById(R.id.tv_topic_votes);
        TextView tvTopicDescription = (TextView) findViewById(R.id.tv_topic_description);
        TextView tvTopicAuthorDescription = (TextView) findViewById(R.id.tv_topic_speaker_info);
        tvTopicName.setText(topic.getTitle());
        tvTopicSpeaker.setText(topic.getSpeakerName());
        if (Configs.isEventDay()) {
            tvTopicVotes.setText(getResources().getQuantityString(R.plurals.topic_vote_count, (int) topic.getFavCount(), topic.getFavCount()));
        } else {
            tvTopicVotes.setText(getResources().getQuantityString(R.plurals.topic_vote_count, (int) topic.getVoteCount(), topic.getVoteCount()));
        }
        tvTopicDescription.setText(topic.getDescription());
        tvTopicAuthorDescription.setText(topic.getSpeakerDescription());
        if (MiscUtils.isNotEmpty(topic.getRoomName())) {
            tvTopicRoom.setText(topic.getRoomName());
        } else {
            tvTopicRoom.setText(R.string.room_not_available);
        }
        if (isMenuInflated) {
            initStateActionBar();
        }
    }

    private void initStateActionBar() {
        if (menu != null && currentTopic != null) {
            toggleVoteState(Configs.isEventDay() ? currentTopic.isFaved() : currentTopic.isVoted());
            toogleBookmarkState(currentTopic.isBookmarked());
        }

    }

    private void toggleVoteState(boolean isVoted) {
        MenuItem item = menu.findItem(R.id.menu_vote);
        View actionView = MenuItemCompat.getActionView(item);
        if (actionView != null) {
            actionView.setEnabled(!isVoted);
            actionView.setOnClickListener(this);
        }
        item.setEnabled(!isVoted);
    }

    private void toogleBookmarkState(boolean isBookmarked) {
        Logging.debug("Toogle bookmark state " + isBookmarked);
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.menu_bookmark);
            View actionView = MenuItemCompat.getActionView(item);
            if (actionView != null) {
                actionView.setSelected(!isBookmarked);
                actionView.setOnClickListener(this);
            }
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.talk_detail_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logging.debug("On create options menu");
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        this.menu = menu;
        isMenuInflated = true;
        if (currentTopic != null) {
            initStateActionBar();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_vote:
                break;
            case R.id.menu_bookmark:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        Logging.debug("On navigation up ");
        onBackPressed();
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TopicDetailCursorLoader(this, currentTopicId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            Topic topic = new Topic(data);
            initView(topic);
            currentTopic = topic;
        } else {
            finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu_bookmark:
                handleBookmark(currentTopic);
                break;
            case R.id.iv_menu_vote:
                handleVote(currentTopic);
                break;
        }
    }

    private void handleBookmark(Topic topic) {
        if (topic != null) {
            topic.setBookmarked(!topic.isBookmarked());
            toogleBookmarkState(topic.isBookmarked());
            TechCampSqlHelper.getInstance(this).udpateBookmarkState(topic.getId(), topic.isBookmarked());
        }
    }

    private void getTopics() {
        HttpService.getTopics(getApplicationContext(), MiscUtils.getUUID(getApplicationContext()), null);
    }

    private void handleVote(final Topic topic) {
        if (topic != null) {
            if (Configs.isEventDay() && !topic.isFaved()) {
                showLoadingDialog();
                HttpService.favorTopic(getApplicationContext(), MiscUtils.getUUID(getApplicationContext()), topic.getId(), new GsonHttpResponseHandler<VoteResponse>(VoteResponse.class) {
                    @Override
                    public void onSuccess(int i, Header[] headers, String s, VoteResponse voteResponse) {
                        if (!isFinishing()) {
                            Logging.debug("Favor topic success");
                            hideLoadingDialog();
                            TechCampSqlHelper.getInstance(TalkDetailActivity.this).updateFavouriteState(topic.getId(), true);
                            topic.setFavCount(topic.getFavCount() + 1);
                            topic.setFaved(true);
                            initView(topic);
                            getTopics();
                            toggleVoteState(true);
                        }

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, String s, VoteResponse voteResponse) {
                        Logging.debug("Vote topic failure");
                        if (!isFinishing()) {
                            hideLoadingDialog();
                            showErrorToast(R.string.topic_vote_error);
                        }
                    }
                });
            } else if (!topic.isVoted()) {
                showLoadingDialog();
                HttpService.voteTopic(getApplicationContext(), MiscUtils.getUUID(getApplicationContext()), topic.getId(), new GsonHttpResponseHandler<VoteResponse>(VoteResponse.class) {
                    @Override
                    public void onSuccess(int i, Header[] headers, String s, VoteResponse voteResponse) {
                        if (!isFinishing()) {
                            hideLoadingDialog();
                            TechCampSqlHelper.getInstance(TalkDetailActivity.this).updateVoteState(topic.getId(), true);
                            topic.setVoteCount(topic.getVoteCount() + 1);
                            topic.setVoted(true);
                            initView(topic);
                            getTopics();
                            toggleVoteState(true);
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, String s, VoteResponse voteResponse) {
                        if (!isFinishing()) {
                            hideLoadingDialog();
                            Toast.makeText(TalkDetailActivity.this, R.string.topic_vote_error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}
