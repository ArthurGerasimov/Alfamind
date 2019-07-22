package id.meteor.alfamind.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.meteor.alfamind.Activity.Forgot_PasswordActivity;
import id.meteor.alfamind.R;
import id.meteor.alfamind.helper.Constant;
import id.meteor.alfamind.helper.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFirstFragment extends Fragment {


    private Forgot_PasswordActivity forgot_passwordActivity;
    private EditText et_edit;

    public ForgotPasswordFirstFragment() {
        // Required empty public constructor
    }

    public void setActivity(Forgot_PasswordActivity forgot_passwordActivity){
        this.forgot_passwordActivity = forgot_passwordActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgot_password_first, container, false);

        ImageView back_btn = view.findViewById(R.id.back_btn);
        TextView next = view.findViewById(R.id.next);
        et_edit = view.findViewById(R.id.et_edit);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if (MyApplication.isNetworkAvailable(getActivity())) {
                        nextProcess();
                    } else {
                        ((Forgot_PasswordActivity) getActivity()).showSnackBar();
                    }
                }
            }
        });
        return view;
    }

    private boolean validation() {
        if (TextUtils.isEmpty(et_edit.getText().toString())) {
            forgot_passwordActivity.getErrorDialog("Masukkan id dompet/email/Nomor Hp Alfamind Anda");
            return false;
        }
        return true;
    }


    private void nextProcess() {
        forgot_passwordActivity.showProgress("Please wait...", false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.RESET_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ForgotPassword", response + "");
                try {
                    forgot_passwordActivity.closeProgress();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equals("11") || jsonObject.getString("status").equalsIgnoreCase("success")) {
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame, new ForgotPasswordSecondFragment()).commit();
                        } else {
                            forgot_passwordActivity.showPopUp(jsonObject.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    forgot_passwordActivity.closeProgress();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forgot_passwordActivity.closeProgress();
                if (MyApplication.isNetworkAvailable(getActivity())) {
                    forgot_passwordActivity.getErrorDialog("Something went wrong");
                } else {
                    ((Forgot_PasswordActivity) getActivity()).showSnackBar();
                }
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("q", et_edit.getEditableText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}