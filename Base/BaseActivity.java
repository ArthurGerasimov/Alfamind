package id.meteor.alfamind.Base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import id.meteor.alfamind.R;
import id.meteor.alfamind.customDialog.ActionSheetErrorDialog;


public class BaseActivity extends AppCompatActivity {

    public Context _context = null;

    private ProgressDialog _progressDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        _context = this;
    }

    public void showProgress(String strMsg, boolean cancelable) {
        if (_progressDlg != null)
            return;

        try {
            _progressDlg = new ProgressDialog(_context);
            _progressDlg.setIndeterminate(true);
            _progressDlg.setMessage(strMsg);
            _progressDlg.setCancelable(cancelable);
            _progressDlg
                    .setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            _progressDlg.show();

        } catch (Exception ignored) {
        }
    }

    public void showProgress() {
        showProgress("", false);
    }

    public void closeProgress() {

        if (_progressDlg == null) {
            return;
        }

        _progressDlg.dismiss();
        _progressDlg = null;
    }

    public void showAlertDialog(String msg, boolean value) {

        final AlertDialog alertDialog = new AlertDialog.Builder(_context).create();

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);
        if (!value) {
            alertDialog.setCancelable(value);
        } else {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, _context.getString(R.string.OK),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
        }
        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    public void showPopUp(final String msg) {
        Log.d("Dialog", "Alert");
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alert = dialogBuilder.create();

        if (!isFinishing()) {
            alert.show();
        }
    }

    public void getErrorDialog(String str) {
        final String[] list = {str};
        final ActionSheetErrorDialog dialog = new ActionSheetErrorDialog(this, list, null);
        dialog.title("");
        dialog.cancelText("Ok");
        if (!isFinishing()) {
            dialog.show();
        }

    }

    public void showSnackBar() {
        RelativeLayout main_parent_layout = findViewById(R.id.main_layout);
        Snackbar snackbar = Snackbar.make(main_parent_layout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showToast(String toast_string) {
        Toast.makeText(_context, toast_string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
