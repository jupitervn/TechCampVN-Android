package vn.techcamp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.techcamp.activities.BaseFragment;
import vn.techcamp.android.R;
import vn.techcamp.utils.Logging;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/11/14.
 */
public class BrowseContainerFragment extends BaseFragment {
    private static final String BROWSE_TAG = "browse-topic-tag";
    private static final String SCHEDULE_TAG = "schedule-topic-tag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container, container, false);
    }

    public void switchFragment(int position) {
        String tag = BROWSE_TAG;
        String className = BrowseTalksFragment.class.getName();
        if (position == 1) {
            tag = SCHEDULE_TAG;
            className = ScheduleFragment.class.getName();
        }
        Logging.debug("On navigation " + position + " " + tag + " " + className);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment mFragment = getChildFragmentManager().findFragmentByTag(tag);
        Logging.debug("On navigation " + (mFragment != null));
        if (mFragment == null) {
            mFragment = Fragment.instantiate(getActivity(), className, null);
        }
        ft.replace(android.R.id.content, mFragment, tag);
        ft.commit();
    }
    public void filterTopics(String queryStr) {
        BrowseTalksFragment mFragment = (BrowseTalksFragment) getChildFragmentManager().findFragmentByTag(BROWSE_TAG);
        if (mFragment != null) {
            mFragment.filterTopic(queryStr);
        }
    }
    public String getCurrentQueryStr() {
        BrowseTalksFragment mFragment = (BrowseTalksFragment) getChildFragmentManager().findFragmentByTag(BROWSE_TAG);
        if (mFragment != null) {
            return mFragment.getFilterString();
        }
        return null;
    }
}
