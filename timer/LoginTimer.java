package id.meteor.alfamind.timer;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import id.meteor.alfamind.helper.MyApplication;
import id.meteor.alfamind.helper.MyPreferenceManager;

/**
 * Created by bodacious on 18/12/17.
 */

public class LoginTimer extends CountDownTimer {


    Context context ;
    public LoginTimer(long millisInFuture, long countDownInterval,Context context) {
        super(millisInFuture, countDownInterval);
        this.context =context ;
    }

    @Override
    public void onTick(long l) {

        Log.d("TIMER_AUTO_LOGIN","tick");
    }

    @Override
    public void onFinish() {

        loginToUser();
        start();
    }

    private void loginToUser() {

        //progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, id.meteor.alfamind.helper.Constant.USER_AUTHENTICATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGIN_TO_USER_TIMER", response + "");
                try {
                   // progressDialog.dismiss();

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        MyApplication.getInstance().getPrefManager().setIsLogged(true);
                        //   mainActivity.enterAccount();

                        if (jsonObject.has("access_token")) {
                            MyApplication.getInstance().getPrefManager().setAccessToken(jsonObject.getString("access_token"));
                        }
                        //getUserInfo();
                        //    access_token

                    } else {
                        //showAlertDialog(jsonObject.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                   // showAlertDialog("server error");
                   // progressDialog.dismiss();
                    Log.d("VollyError", "Server error " + e.getMessage() + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                //  getErrorDialog("Something went wrong");
               // showAlertDialog("something went wrong");
                Log.d("VollyError", " something went wrong" + error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                MyPreferenceManager manager =MyApplication.getInstance().getPrefManager();

                String email = manager.getEmail();
                String password =manager.getPassword();
                Log.d("LOGINPARAM",email+"  -  "+password+"");

                param.put("email", email+"");
                param.put("passwd", password + "");
                param.put("device_id", "na");

                return param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    /*

    it's API release and production
    https://mndsvr.net/api/

    it's API dev
    http://www.wir-fit.com/app_alfamind/

    */

}
