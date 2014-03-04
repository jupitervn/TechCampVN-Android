package vn.techcamp.services;

import com.loopj.android.http.BaseJsonHttpResponseHandler;

import vn.techcamp.utils.JSONUtils;
import vn.techcamp.utils.MiscUtils;


/**
 * Created by jupiter on 22/2/14.
 */
public abstract class GsonHttpResponseHandler<T> extends BaseJsonHttpResponseHandler<T> {
    private Class<T> clazz;

    public GsonHttpResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    protected T parseResponse(String s) throws Throwable {
        if (MiscUtils.isNotEmpty(s)) {
            return JSONUtils.parseJson(clazz, s);
        }
        return null;
    }
}
