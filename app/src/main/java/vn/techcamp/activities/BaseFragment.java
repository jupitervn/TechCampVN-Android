/**
*/
package vn.techcamp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import vn.techcamp.android.R;

/**
 * Base fragment class.
 * @author Cao Duy Vu (vu.cao.duy@gmail.com)
 *
 */
public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private boolean isShowingProgressDialog = false;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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

    public void showLoadingDialog() {
        showLoadingDialog(R.string.loading_text);
    }

    public void showLoadingDialog(int resId) {
        showLoadingDialog(resId, false);
    }

    public void showLoadingDialog(int resId, boolean isCancelable) {
        showLoadingDialog(getString(resId), isCancelable);
    }

    public void showLoadingDialog(String loadingText, boolean isCancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage(loadingText);
        mProgressDialog.setCancelable(isCancelable);
        isShowingProgressDialog = true;
        mProgressDialog.show();
    }

    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        isShowingProgressDialog = false;
    }

    public void showGeneralErrorToast() {
        showErrorToast(R.string.general_error_text);
    }

    public void showErrorToast(int resId) {
        if (getActivity() != null && !isAdded()) {
            Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
        }
    }

}
