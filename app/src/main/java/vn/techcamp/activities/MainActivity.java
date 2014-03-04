package vn.techcamp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import vn.techcamp.android.R;
import vn.techcamp.fragments.AnnouncementFragment;
import vn.techcamp.fragments.BrowseTalksFragment;
import vn.techcamp.fragments.SavedTalksFragment;

/**
 * Created by Jupiter on 2/15/14.
 */
public class MainActivity extends BaseActivity {

    private static final String BROWSE_TAG = "browse";
    private static final String SAVED_TAG = "saved";
    private static final String ANNOUNCEMENT_TAG = "announcement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(R.string.action_bar_title);
        actionBar.addTab(actionBar.newTab().setText(R.string.browse_talks).setTabListener(new TabListener<BrowseTalksFragment>(this, BROWSE_TAG, BrowseTalksFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(R.string.saved_talks).setTabListener(new TabListener<SavedTalksFragment>(this, SAVED_TAG, SavedTalksFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(R.string.announcements).setTabListener(new TabListener<AnnouncementFragment>(this, ANNOUNCEMENT_TAG, AnnouncementFragment.class)));
    }

    private static class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private final FragmentActivity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private Fragment mFragment;
        private Bundle mArgs;


        public TabListener(FragmentActivity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }
        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         * @param args     Arguments pass to fragment.
         */
        public TabListener(FragmentActivity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }


        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }
}
