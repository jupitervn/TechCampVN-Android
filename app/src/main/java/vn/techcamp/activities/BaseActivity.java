/**
 */
package vn.techcamp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import vn.techcamp.android.R;

/**
 * Every new activities should extend from this base class.
 *
 * @author Cao Duy Vu (vu.cao.duy@gmail.com)
 */
public class BaseActivity extends ActionBarActivity {
    private ProgressDialog mProgressDialog;
    private boolean isShowingProgressDialog = false;

    @Override
    public void onResume() {
        super.onResume();
        if (isShowingProgressDialog && mProgressDialog != null) {
            mProgressDialog.show();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void showLoadingDialog() {
        showLoadingDialog(R.string.loading_text);
    }

    protected void showLoadingDialog(int resId) {
        showLoadingDialog(resId, false);
    }

    protected void showLoadingDialog(int resId, boolean isCancelable) {
        showLoadingDialog(getString(resId), isCancelable);
    }

    protected void showLoadingDialog(String loadingText, boolean isCancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new android.app.ProgressDialog(this);
        }
        mProgressDialog.setMessage(loadingText);
        mProgressDialog.setCancelable(isCancelable);
        isShowingProgressDialog = true;
        mProgressDialog.show();
    }

    protected void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        isShowingProgressDialog = false;
    }

    protected void showGeneralErrorToast() {
        showErrorToast(R.string.general_error_text);
    }

    protected void showErrorToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
