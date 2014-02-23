package vn.techcamp.services;

import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;

import vn.techcamp.ui.JSONUtils;
import vn.techcamp.ui.MiscUtils;

/**
 * Created by jupiter on 22/2/14.
 */
public class GsonHttpResponseHandler<T> extends BaseJsonHttpResponseHandler<T> {
    private Class<T> clazz;
    public GsonHttpResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onSuccess(int i, Header[] headers, String s, T t) {

    }

    @Override
    public void onFailure(int i, Header[] headers, Throwable throwable, String s, T t) {

    }

    @Override
    protected T parseResponse(String s) throws Throwable {
        if (MiscUtils.isNotEmpty(s)) {
            return JSONUtils.parseJson(clazz, s);
        }
        return null;
    }
}
