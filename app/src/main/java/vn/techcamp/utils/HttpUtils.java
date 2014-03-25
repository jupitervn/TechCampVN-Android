package vn.techcamp.utils;

import com.handmark.pulltorefresh.library.internal.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/11/14.
 */
public class HttpUtils {
    public static final String GDATA_VERSION_HTTP_HEADER = "GData-Version";
    public static final int HTTP_TIMEOUT = 60000;
    public static final int HTTP_READ_TIMEOUT = 60000;

    public static String addParameters(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        int questionMarkIndex = url.indexOf('?');

        for (String param : params.keySet()) {
            if (MiscUtils.isNotEmpty(param)) {
                String value = params.get(param);
                if (MiscUtils.isNotEmpty(value) && !value.equals("null")) {
                    if (questionMarkIndex > 0) {
                        sb.append("&");
                    } else {
                        sb.append("?");
                        questionMarkIndex = sb.length() - 1;
                    }
                    try {
                        sb.append(param).append("=").append(URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.toString();
    }
}
