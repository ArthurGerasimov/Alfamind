package id.meteor.alfamind.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Activity.Forgot_PasswordActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatePasswordFragment extends Fragment {

    private EditText et_old;
    private EditText et_new;
    private EditText et_new2;
    private Forgot_PasswordActivity forgot_passwordActivity;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    public void setActivity(Forgot_PasswordActivity forgot_passwordActivity) {
        this.forgot_passwordActivity = forgot_passwordActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        et_old = view.findViewById(R.id.et_old);
        et_new = view.findViewById(R.id.et_new);
        et_new2 = view.findViewById(R.id.et_new2);
        
        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        view.findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordFirstFragment fragment = new ForgotPasswordFirstFragment();
                fragment.setActivity(forgot_passwordActivity);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragment).commit();
            }
        });

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if (MyApplication.isNetworkAvailable(getActivity())) {
                        resetpassword();
                    } else {
                        ((Forgot_PasswordActivity) getActivity()).showSnackBar();
                    }
                }
            }
        });

        return view;
    }

    private boolean validation() {

        if (TextUtils.isEmpty(et_old.getText().toString())) {
            forgot_passwordActivity.getErrorDialog("Masukkan kata sandi lama");
            return false;
        }
        if (TextUtils.isEmpty(et_new.getText().toString())) {
            forgot_passwordActivity.getErrorDialog("Masukan kata sandi baru");
            return false;
        }
        if (TextUtils.isEmpty(et_new2.getText().toString())) {
            forgot_passwordActivity.getErrorDialog("kembali masukkan kata sandi baru");
            return false;
        } else {
            String et = et_new.getText().toString();
            if (!et.equals(et_new2.getEditableText().toString())) {
                forgot_passwordActivity.getErrorDialog("kedua kata sandi baru tidak cocok");
                return false;
            }
        }
        return true;
    }

    private void resetpassword() {
        forgot_passwordActivity.showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.UPDATE_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ForgotPassword", response + "");
                try {
                    forgot_passwordActivity.closeProgress();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("11") || jsonObject.getString("status").equalsIgnoreCase("success")) {
                            MyApplication.getInstance().getPrefManager().setPassword(et_old.getText().toString());
                            showAlertDialog("Password berhasil diubah", true);
                        } else {
                            showAlertDialog(jsonObject.getString("message"), false);
                        }
                    }
                } catch (Exception e) {
                    forgot_passwordActivity.closeProgress();
                    e.printStackTrace();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forgot_passwordActivity.closeProgress();
                if (!MyApplication.isNetworkAvailable(getActivity())) {
                    assert getActivity() != null;
                    ((Forgot_PasswordActivity) getActivity()).showSnackBar();
                }
                Log.d("VollyError", " something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                MyPreferenceManager manager = MyApplication.getInstance().getPrefManager();

                String access_token = manager.getAccessToken();
                Log.d("UPDATEPARAM", access_token + "");
                if (access_token != null && !access_token.equals("null") && !access_token.equals("")) {
                    param.put("access_token", access_token);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("access_token", "");
                }

                String store_name = manager.getShopName();
                Log.d("UPDATEPARAM", store_name + "");
                if (store_name != null && !store_name.equals("null") && !store_name.equals("")) {
                    param.put("store_name", store_name);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("store_name", "Ab");
                }

                String email = manager.getEmail();
                Log.d("UPDATEPARAM", email + "");
                if (email != null && !email.equals("null") && !email.equals("")) {
                    param.put("email", email);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("email", "");
                }

                String address1 = manager.getAddress();
                Log.d("UPDATEPARAM", address1 + "");
                if (address1 != null && !address1.equals("null") && !address1.equals("")) {
                    param.put("address1", address1);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("address1", "");
                }

                String city = manager.getCity();
                Log.d("UPDATEPARAM", city + "");
                if (city != null && !city.equals("null") && !city.equals("")) {
                    param.put("city", city);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("city", "");
                }

                String postcode = manager.getZipCode();
                Log.d("UPDATEPARAM", postcode + "");
                if (postcode != null && !postcode.equals("null") && !postcode.equals("")) {
                    param.put("postcode", postcode);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("postcode", "");
                }

                String phone_mobile = manager.getPhoneNumber();
                Log.d("UPDATEPARAM", phone_mobile + "");
                if (phone_mobile != null && !phone_mobile.equals("null") && !phone_mobile.equals("")) {
                    param.put("phone_mobile", phone_mobile);
                } else {
                    Log.d("UPDATEPARAM", " els");
                    param.put("phone_mobile", "");
                }

                String passwd = et_new.getEditableText().toString();
                param.put("old_passwd", et_old.getEditableText().toString() + "");
                param.put("confirm_passwd", et_new.getEditableText().toString() + "");
                param.put("passwd", passwd);
                param.put("avatar", "");
                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @SuppressLint("StaticFieldLeak")
    private void showAlertDialog(final String msg, final boolean value) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Dialog", "Alert");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setMessage(msg);
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!value)
                            dialog.cancel();
                        else {
                            dialog.cancel();
                            MyApplication.getInstance().getPrefManager().setIsLogged(false);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        }
                    }
                });
                AlertDialog alert = dialogBuilder.create();
                alert.show();
            }
        });
    }
}
