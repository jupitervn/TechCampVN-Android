package vn.techcamp.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/9/14.
 */
public abstract class BaseModel {
    public BaseModel(Cursor c) {

    }
    public abstract ContentValues toContentValues();

}
