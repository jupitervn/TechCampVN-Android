package vn.techcamp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import vn.techcamp.android.R;
import vn.techcamp.fragments.AnnouncementFragment;
import vn.techcamp.fragments.BrowseContainerFragment;
import vn.techcamp.fragments.SavedTalksFragment;
import vn.techcamp.services.NotificationService;
import vn.techcamp.utils.Logging;
import vn.techcamp.utils.MiscUtils;
import vn.techcamp.utils.PreferenceUtils;

/**
 * Created by Jupiter on 2/15/14.
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_TAB_INDEX = "extra-tab-index";
    public static final int BROWSE_TAB = 0;
    public static final int SAVED_TAB = 1;
    public static final int ANNOUNCEMENT_TAB = 2;
    private static final String BROWSE_TAG = "browse";
    private static final String SAVED_TAG = "saved";
    private static final String ANNOUNCEMENT_TAG = "announcement";
    private static final String SCHEDULE_TAG = "schedule";
    private static final String LAST_TAB_STATE = "last-tab-index";
    private static final String CURRENT_SPINNER_POSITION = "current-spinner-position";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 0x1;

    private Spinner navigationSpinner;
    private int currentSpinnerPosition = 0;
    private int currentTabIndex = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            currentSpinnerPosition = savedInstanceState.getInt(CURRENT_SPINNER_POSITION, 0);
            currentTabIndex = savedInstanceState.getInt(LAST_TAB_STATE, 0);
        }
        initActionBar();
        registerGcm();
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int tabIndex = intent.getIntExtra(EXTRA_TAB_INDEX, currentTabIndex);
        if (tabIndex > ANNOUNCEMENT_TAB || tabIndex < BROWSE_TAB) {
            tabIndex = 0;
        }
        getSupportActionBar().setSelectedNavigationItem(tabIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(R.string.browse_talks).setTabListener(new TabListener<BrowseContainerFragment>(this, BROWSE_TAG, BrowseContainerFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(R.string.saved_talks).setTabListener(new TabListener<SavedTalksFragment>(this, SAVED_TAG, SavedTalksFragment.class)));
        actionBar.addTab(actionBar.newTab().setText(R.string.announcements).setTabListener(new TabListener<AnnouncementFragment>(this, ANNOUNCEMENT_TAG, AnnouncementFragment.class)));
        changeActionBar(BROWSE_TAG);

    }

    private void changeActionBar(String tag) {
        ActionBar actionBar = getSupportActionBar();
        if (BROWSE_TAG.equals(tag)) {
            View customView = getLayoutInflater().inflate(R.layout.custom_action_bar, null, false);
            actionBar.setCustomView(customView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            actionBar.setDisplayShowCustomEnabled(true);
            navigationSpinner = (Spinner) customView.findViewById(R.id.sp_navigation);
            navigationSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.item_navigation_list, android.R.id.text1, getResources().getStringArray(R.array.navigation_array)));
            navigationSpinner.setSelection(currentSpinnerPosition);
            navigationSpinner.setOnItemSelectedListener(this);
        } else {
            actionBar.setTitle(R.string.action_bar_title);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_TAB_STATE, getSupportActionBar().getSelectedNavigationIndex());
        outState.putInt(CURRENT_SPINNER_POSITION, currentSpinnerPosition);
        super.onSaveInstanceState(outState);
    }

    private void registerGcm() {
        if (checkPlayServices()) {
            String regId = getRegistrationId(this);
            if (regId.isEmpty()) {
                registerInBackground();
            }
        } else {
            Logging.debug("No valid Google Play Services APK found.");
        }
    }

    private void registerInBackground() {
        Intent registerIntent = new Intent(this, NotificationService.class);
        registerIntent.setAction(NotificationService.ACTION_REGISTER_GCM);
        startService(registerIntent);
    }

    private String getRegistrationId(Context context) {
        String registrationId = PreferenceUtils.getGcmRegId(getApplicationContext());
        if (registrationId != null && !MiscUtils.isNotEmpty(registrationId)) {
            Logging.debug("Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredAppVersion = PreferenceUtils.getAppVersion(getApplicationContext());
        int currentVersion = MiscUtils.getAppVersion(context);
        if (registeredAppVersion != currentVersion) {
            Logging.debug("App version changed.");
            PreferenceUtils.storeAppVersion(getApplicationContext(), currentVersion);
            return "";
        }
        return registrationId;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Logging.debug("This device is not supported.");
                Toast.makeText(this, R.string.gcm_not_supported_error, Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BrowseContainerFragment mFragment = (BrowseContainerFragment) getSupportFragmentManager().findFragmentByTag(BROWSE_TAG);
        Logging.debug("On navigation " + (mFragment != null));
        if (mFragment != null) {
            mFragment.switchFragment(position);
        }
        currentSpinnerPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private static class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private final MainActivity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private Fragment mFragment;
        private Bundle mArgs;


        public TabListener(MainActivity activity, String tag, Class<T> clz) {
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
        public TabListener(MainActivity activity, String tag, Class<T> clz, Bundle args) {
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
//            mActivity.initActionBar(mTag);
            mActivity.changeActionBar(mTag);
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
