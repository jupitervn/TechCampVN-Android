package vn.techcamp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import vn.techcamp.android.R;

/**
 * Created by Jupiter on 2/15/14.
 */
public class MainActivity extends BaseActivity implements ActionBar.TabListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(R.string.action_bar_title);
        actionBar.addTab(actionBar.newTab().setText(R.string.browse_talks).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.saved_talks).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.announcements).setTabListener(this));
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
