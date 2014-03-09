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
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.Header;

import vn.techcamp.activities.BaseFragment;
import vn.techcamp.android.R;
import vn.techcamp.loaders.AnnouncementCursorLoader;
import vn.techcamp.models.Announcement;
import vn.techcamp.services.GsonHttpResponseHandler;
import vn.techcamp.services.HttpService;
import vn.techcamp.utils.Logging;
import vn.techcamp.utils.UIUtils;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/5/14.
 */
public class AnnouncementFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, LoaderManager.LoaderCallbacks<Cursor> {
    private PullToRefreshListView lvAnnouncements;
    private AnnouncementAdapter announcementAdapter;
    private boolean isFirstTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvAnnouncements = (PullToRefreshListView) view.findViewById(R.id.lv_announcements);
        lvAnnouncements.setOnRefreshListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        doGetAnnouncements();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    private void doGetAnnouncements() {
        HttpService.getAnnouncements(getActivity().getApplicationContext(), new GsonHttpResponseHandler<Announcement[]>(Announcement[].class) {
            @Override
            public void onSuccess(int i, Header[] headers, String s, Announcement[] announcements) {
                Logging.debug("Get announcements success");
                if (getActivity() != null && isAdded()) {
                    hideLoadingDialog();
                    lvAnnouncements.onRefreshComplete();
                    getLoaderManager().restartLoader(0, null, AnnouncementFragment.this);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Announcement[] announcements) {
                Logging.debug("Get announcements error " + s);
                if (getActivity() != null && isAdded()) {
                    hideLoadingDialog();
                    lvAnnouncements.onRefreshComplete();
                    showGeneralErrorToast();
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AnnouncementCursorLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor cursor) {
        if (announcementAdapter == null) {
            announcementAdapter = new AnnouncementAdapter(getActivity(), cursor, 0);
        } else {
            announcementAdapter.swapCursor(cursor);
        }
        if (lvAnnouncements.getRefreshableView().getAdapter() == null) {
            lvAnnouncements.setAdapter(announcementAdapter);
        }
        if (isFirstTime) {
            showLoadingDialog();
            doGetAnnouncements();
            isFirstTime = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> objectLoader) {

    }

    private static class AnnouncementAdapter extends CursorAdapter {

        public AnnouncementAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.item_announcement, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvTime = (TextView) view.findViewById(R.id.tv_announce_time);
            TextView tvMessage = (TextView) view.findViewById(R.id.tv_announce_message);
            Announcement announcement = new Announcement(cursor);
            tvTime.setText(UIUtils.formatAnnouncementDate(announcement.getSendAt()));
            tvMessage.setText(announcement.getMessage());


        }
    }
}
