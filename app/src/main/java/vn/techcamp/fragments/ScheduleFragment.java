package vn.techcamp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;

import java.util.regex.Matcher;

import vn.techcamp.activities.BaseFragment;
import vn.techcamp.activities.TalkDetailActivity;
import vn.techcamp.android.R;
import vn.techcamp.utils.Configs;
import vn.techcamp.utils.Logging;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/7/14.
 */
public class ScheduleFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<WebView> {
    private PullToRefreshWebView2 wvSchedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wvSchedule = (PullToRefreshWebView2) view.findViewById(R.id.wv_schedule);
        wvSchedule.getRefreshableView().setWebViewClient(new CustomWebViewClient());
        wvSchedule.getRefreshableView().loadUrl(Configs.AGENDA_URL);
        wvSchedule.setOnRefreshListener(this);
        WebSettings webSettings = wvSchedule.getRefreshableView().getSettings();
//        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
        wvSchedule.getRefreshableView().loadUrl(Configs.AGENDA_URL);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {

    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Matcher urlMatcher = Configs.INTERNAL_LINK_PATTERN.matcher(url);
            while (urlMatcher.find()) {
                try {
                    long topicId = Long.valueOf(urlMatcher.group(1));
                    Intent showTopicDetailIntent = new Intent(getActivity(), TalkDetailActivity.class);
                    showTopicDetailIntent.putExtra(TalkDetailActivity.EXTRA_TOPIC, topicId);
                    startActivity(showTopicDetailIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoadingDialog();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            hideLoadingDialog();
            Logging.debug("On Receive error");
            showGeneralErrorToast();
        }
    }
}
