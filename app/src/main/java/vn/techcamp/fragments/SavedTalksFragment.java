package vn.techcamp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import vn.techcamp.adapters.TopicAdapter;
import vn.techcamp.android.R;
import vn.techcamp.loaders.SavedTopicsCursorLoader;
import vn.techcamp.utils.Configs;

/**
 * Saved talk fragment.
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/5/14.
 */
public class SavedTalksFragment extends BrowseTalksFragment {
    private ListView lvTopics;
    private TopicAdapter topicAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_talks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvTopics = (ListView) view.findViewById(R.id.lv_topics);
        topicAdapter = new TopicAdapter(getActivity(), null, 0, this, Configs.isEventDay());
        lvTopics.setAdapter(topicAdapter);
        lvTopics.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SavedTopicsCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            topicAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        topicAdapter.swapCursor(null);
    }
}
