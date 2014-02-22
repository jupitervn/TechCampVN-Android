package vn.techcamp.ui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by jupiter on 22/2/14.
 */
public class JSONUtils {
    public static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

    public static <T> T parseJson(Class<T> cls, String json) throws JsonSyntaxException {
        return gson.fromJson(json, cls);
    }

    public static String toJson(Object src) throws JsonSyntaxException {
        return gson.toJson(src);
    }

}
