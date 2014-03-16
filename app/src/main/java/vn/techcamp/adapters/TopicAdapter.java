package vn.techcamp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.techcamp.android.R;
import vn.techcamp.models.Topic;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/16/14.
 */
public class TopicAdapter extends CursorAdapter{
    private View.OnClickListener btVoteClickListener;
    private boolean isEventDay = false;

    public TopicAdapter(Context context, Cursor c, int flags, View.OnClickListener voteClickListener, boolean isEventDay) {
        super(context, c, flags);
        this.btVoteClickListener = voteClickListener;
        this.isEventDay = isEventDay;
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
        if (!isEventDay) {
            btVote.setText(context.getResources().getQuantityString(R.plurals.topic_vote_count, (int) topic.getVoteCount(), topic.getVoteCount()));
            btVote.setEnabled(!topic.isVoted());
        } else {
            btVote.setText(context.getResources().getQuantityString(R.plurals.topic_vote_count, (int) topic.getFavCount(), topic.getFavCount()));
            btVote.setEnabled(!topic.isFaved());
        }
        btVote.setTag(topic.getId());
        view.setTag(topic.getId());
        btVote.setOnClickListener(btVoteClickListener);
    }
}
