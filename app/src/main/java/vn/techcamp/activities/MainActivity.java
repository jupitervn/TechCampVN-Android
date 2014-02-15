package vn.techcamp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

/**
 * Created by Jupiter on 2/15/14.
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    private void initActionBar() {
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setTitle("TechCamp Saigon");
    }


}
